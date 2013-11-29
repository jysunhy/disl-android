#include <stdio.h>
#include <string.h>

#include <netdb.h>
#include <unistd.h>
#include <netinet/tcp.h>

#include <jvmti.h>
#include <jni.h>

// has to be defined for jvmtihelper.h
#define ERR_PREFIX "DiSL agent error: "

#include "jvmtihelper.h"
#include "comm.h"

#include "dislagent.h"

static const int ERR_SERVER = 10003;

// defaults - be sure that space in host_name is long enough
static const char * DEFAULT_HOST = "localhost";
static const char * DEFAULT_PORT = "11217";

typedef struct {
	jint control_size;
	jint classcode_size;
	const unsigned char * control;
	const unsigned char * classcode;
} message;

// linked list to hold socket file descriptor
// access must be protected by monitor
struct strc_connection_item {
	int sockfd;
	volatile int available;
	struct strc_connection_item *next;
};

typedef struct strc_connection_item connection_item;

// port and name of the instrumentation server
static char host_name[1024];
static char port_number[6]; // including final 0

static jvmtiEnv * jvmti_env;
static jrawMonitorID global_lock;

// we are using multiple connections for parallelization
// pros: there is no need for synchronization in this client :)
// cons: gets ugly with strong parallelization

// the first element on the list of connections
// modifications should be protected by critical section
static connection_item * conn_list = NULL;

// ******************* Helper routines *******************

static message create_message(const unsigned char * control,
		jint control_size, const unsigned char * classcode,
		jint classcode_size) {

	message result;

	// control + size
	result.control_size = control_size;

	// contract: (if control_size <= 0) pointer may be copied (stolen)
	if(control != NULL && control_size > 0) {

		// without ending 0
		unsigned char * buffcn = (unsigned char *) malloc(control_size);
		memcpy(buffcn, control, control_size);
		result.control = buffcn;
	}
	else {

		result.control = control;
		result.control_size = abs(control_size);
	}

	// class code + size
	result.classcode_size = classcode_size;

	// contract: (if classcode_size <= 0) pointer may be copied (stolen)
	if(classcode != NULL && classcode_size > 0) {

		unsigned char * buffcc = (unsigned char *) malloc(classcode_size);
		memcpy(buffcc, classcode, classcode_size);
		result.classcode = buffcc;
	}
	else {

		result.classcode = classcode;
		result.classcode_size = abs(classcode_size);
	}

	return result;
}

static void free_message(message * msg) {

	if(msg->control != NULL) {

		// cast because of const
		free((void *) msg->control);
		msg->control = NULL;
		msg->control_size = 0;
	}

	if(msg->classcode != NULL) {

		// cast because of const
		free((void *) msg->classcode);
		msg->classcode = NULL;
		msg->classcode_size = 0;
	}
}

static void parse_agent_options(char *options) {

	static const char PORT_DELIM = ':';

	// assign defaults
	strcpy(host_name, DEFAULT_HOST);
	strcpy(port_number, DEFAULT_PORT);

	// no options found
	if (options == NULL) {
		return;
	}

	char * port_start = strchr(options, PORT_DELIM);

	// process port number
	if(port_start != NULL) {

		// replace PORT_DELIM with end of the string (0)
		port_start[0] = '\0';

		// move one char forward to locate port number
		++port_start;

		// convert number
		int fitsP = strlen(port_start) < sizeof(port_number);
		check_error(! fitsP, "Port number is too long");

		strcpy(port_number, port_start);
	}

	// check if host_name is big enough
	int fitsH = strlen(options) < sizeof(host_name);
	check_error(! fitsH, "Host name is too long");

	strcpy(host_name, options);
}

// ******************* Communication routines *******************

// sends class over network
static void send_msg(connection_item * conn, message * msg) {

#ifdef DEBUG
	printf("Sending - control: %d, code: %d ... ", msg->control_size, msg->classcode_size);
#endif

	// send control and code size first and then data

	int sockfd = conn->sockfd;

	// convert to java representation
	jint nctls = htonl(msg->control_size);
	send_data(sockfd, &nctls, sizeof(jint));

	// convert to java representation
	jint nccs = htonl(msg->classcode_size);
	send_data(sockfd, &nccs, sizeof(jint));

	send_data(sockfd, msg->control, msg->control_size);

	send_data(sockfd, msg->classcode, msg->classcode_size);

#ifdef DEBUG
	printf("done\n");
#endif
}

// receives class from network
static message rcv_msg(connection_item * conn) {

#ifdef DEBUG
	printf("Receiving ");
#endif

	// receive control and code size first and then data

	int sockfd = conn->sockfd;

	// *** receive control size - jint
	jint nctls;
	rcv_data(sockfd, &nctls, sizeof(jint));

	// convert from java representation
	jint control_size = ntohl(nctls);

	// *** receive class code size - jint
	jint nccs;
	rcv_data(sockfd, &nccs, sizeof(jint));

	// convert from java representation
	jint classcode_size = ntohl(nccs);

	// *** receive control string
	// +1 - ending 0 - useful when printed - normally error msgs here
	unsigned char * control = (unsigned char *) malloc(control_size + 1);

	rcv_data(sockfd, control, control_size);

	// terminate string
	control[control_size] = '\0';

	// *** receive class code
	unsigned char * classcode = (unsigned char *) malloc(classcode_size);

	rcv_data(sockfd, classcode, classcode_size);

#ifdef DEBUG
	printf("- control: %d, code: %d ... done\n", control_size, classcode_size);
#endif

	// negative length - create_message adopts pointers
	return create_message(control, -control_size, classcode, -classcode_size);
}

static connection_item * open_connection() {

	// get host address
	struct addrinfo * addr;
	int gai_res = getaddrinfo(host_name, port_number, NULL, &addr);
	check_error(gai_res != 0, gai_strerror(gai_res));

	// create stream socket
	int sockfd = socket(addr->ai_family, SOCK_STREAM, 0);
	check_std_error(sockfd, -1, "Cannot create socket");

	// connect to server
	int conn_res = connect(sockfd, addr->ai_addr, addr->ai_addrlen);
	check_std_error(conn_res, -1, "Cannot connect to server");

	// disable Nagle algorithm
	// http://www.techrepublic.com/article/tcpip-options-for-high-performance-data-transmission/1050878
	int flag = 1;
	int set_res = setsockopt(sockfd, IPPROTO_TCP, TCP_NODELAY, &flag,
			sizeof(int));
	check_std_error(set_res, -1, "Cannot set TCP_NODELAY");

	// free host address info
	freeaddrinfo(addr);

	// allocate new connection information
	connection_item * conn = (connection_item *) malloc(sizeof(connection_item));
	conn->available = TRUE;
	conn->sockfd = sockfd;
	conn->next = NULL;

	return conn;
}

static void close_connection(connection_item * conn) {

	// prepare close message - could be done more efficiently (this is nicer)
	// close message has zeros as lengths
	message close_msg = create_message(NULL, 0, NULL, 0);

	// send close message
	send_msg(conn, &close_msg);

	// nothing was allocated but for completeness
	free_message(&close_msg);

	// close socket
	close(conn->sockfd);

	// free connection space
	free(conn);
}

// get an available connection, create one if no one is available
static connection_item * acquire_connection() {

#ifdef DEBUG
	printf("Acquiring connection ... ");
#endif

	connection_item * curr;

	// the connection list requires access using critical section
	enter_critical_section(jvmti_env, global_lock);
	{
		curr = conn_list;

		while (curr != NULL) {

			if (curr->available == TRUE) {
				break;
			}

			curr = curr->next;
		}

		if (curr == NULL) {

			// create new connection
			curr = open_connection();

			// add it at the beginning of the connection list
			curr->next = conn_list;
			conn_list = curr;
		}

		curr->available = FALSE;
	}
	exit_critical_section(jvmti_env, global_lock);

#ifdef DEBUG
	printf("done\n");
#endif

	return curr;
}

// make the socket available again
static void release_connection(connection_item * conn) {

#ifdef DEBUG
	printf("Releasing connection ... ");
#endif

	// the connection list requires access using critical section
	// BUT :), release can be done without it
	//enter_critical_section(jvmti_env, global_lock);
	{
		// make connection available
		conn->available = TRUE;
	}
	//exit_critical_section(jvmti_env, global_lock);

#ifdef DEBUG
	printf("done\n");
#endif
}

// instruments remotely
static message instrument_class(const char * classname,
		const unsigned char * classcode, jint classcode_size) {

	// get available connection
	connection_item * conn = acquire_connection();

	// crate class data
	message msg = create_message((const unsigned char *)classname,
			strlen(classname), classcode, classcode_size);

	send_msg(conn, &msg);

	message result = rcv_msg(conn);

	release_connection(conn);

	return result;
}

// ******************* CLASS LOAD callback *******************

void JNICALL jvmti_callback_class_file_load_hook( jvmtiEnv *jvmti_env,
		JNIEnv* jni_env, jclass class_being_redefined, jobject loader,
		const char* name, jobject protection_domain, jint class_data_len,
		const unsigned char* class_data, jint* new_class_data_len,
		unsigned char** new_class_data) {

#ifdef DEBUG
	if(name != NULL) {
		printf("Instrumenting class %s\n", name);
	}
	else {
		printf("Instrumenting unknown class\n");
	}
#endif

	// ask the server to instrument
	message instrclass = instrument_class(name, class_data, class_data_len);

	// error on the server
	if (instrclass.control_size > 0) {

		// classname contains the error message

		fprintf(stderr, "%sError occurred in the remote instrumentation server\n",
				ERR_PREFIX);
		fprintf(stderr, "   Reason: %s\n", instrclass.control);
		exit(ERR_SERVER);
	}

	// instrumented class recieved (0 - means no instrumentation done)
	if(instrclass.classcode_size > 0) {

		// give to JVM the instrumented class
		unsigned char *new_class_space;

		// let JVMTI to allocate the mem for the new class
		jvmtiError err = (*jvmti_env)->Allocate(jvmti_env, (jlong)instrclass.classcode_size, &new_class_space);
		check_jvmti_error(jvmti_env, err, "Cannot allocate memory for the instrumented class");

		memcpy(new_class_space, instrclass.classcode, instrclass.classcode_size);

		// set the newly instrumented class + len
		*(new_class_data_len) = instrclass.classcode_size;
		*(new_class_data) = new_class_space;

		// free memory
		free_message(&instrclass);
	}

#ifdef DEBUG
	printf("Instrumentation done\n");
#endif

}

// ******************* SHUTDOWN callback *******************

void JNICALL jvmti_callback_class_vm_death_hook(jvmtiEnv *jvmti_env, JNIEnv* jni_env) {

	enter_critical_section(jvmti_env, global_lock);
	{

		connection_item * cnode = conn_list;

		// will be deallocated in the while cycle
		conn_list = NULL;

		// close all connections
		while(cnode != NULL) {

			// prepare for closing
			connection_item * connToClose = cnode;

			// advance first - pointer will be invalid after close
			cnode = cnode->next;

			// close connection
			close_connection(connToClose);
		}
	}
	exit_critical_section(jvmti_env, global_lock);
}

// ******************* JVMTI entry method *******************

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *jvm, char *options, void *reserved) {

	jvmti_env = NULL;

	jint res = (*jvm)->GetEnv(jvm, (void **) &jvmti_env, JVMTI_VERSION_1_0);

	if (res != JNI_OK || jvmti_env == NULL) {
		/* This means that the VM was unable to obtain this version of the
		 *   JVMTI interface, this is a fatal error.
		 */
		fprintf(stderr, "%sUnable to access JVMTI Version 1 (0x%x),"
				" is your J2SE a 1.5 or newer version?"
				" JNIEnv's GetEnv() returned %d\n", ERR_PREFIX, JVMTI_VERSION_1,
				res);

		exit(ERR_JVMTI);
	}

	jvmtiError error;

	// adding hooks
	jvmtiCapabilities cap;
	memset(&cap, 0, sizeof(cap));

	// class hook
	cap.can_generate_all_class_hook_events = 1;

	error = (*jvmti_env)->AddCapabilities(jvmti_env, &cap);
	check_jvmti_error(jvmti_env, error,
			"Unable to get necessary JVMTI capabilities.");

	// adding callbacks
	jvmtiEventCallbacks callbacks;
	(void) memset(&callbacks, 0, sizeof(callbacks));

	callbacks.ClassFileLoadHook = &jvmti_callback_class_file_load_hook;
	callbacks.VMDeath = &jvmti_callback_class_vm_death_hook;

	error = (*jvmti_env)->SetEventCallbacks(jvmti_env, &callbacks, (jint) sizeof(callbacks));
	check_jvmti_error(jvmti_env, error, "Cannot set callbacks");

	error = (*jvmti_env)->SetEventNotificationMode(jvmti_env, JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
	check_jvmti_error(jvmti_env, error, "Cannot set class load hook");

	error = (*jvmti_env)->SetEventNotificationMode(jvmti_env, JVMTI_ENABLE, JVMTI_EVENT_VM_DEATH, NULL);
	check_jvmti_error(jvmti_env, error, "Cannot set jvm death hook");

	error = (*jvmti_env)->CreateRawMonitor(jvmti_env, "agent data", &global_lock);
	check_jvmti_error(jvmti_env, error, "Cannot create raw monitor");

	// read options (port/hostname)
	parse_agent_options(options);

	return 0;
}
