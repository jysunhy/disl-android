#include <stdio.h>
#include "ShadowLib.h"
#include "DalvikHeader.h"
#include "ReProtocol.h"
#include "Netref.h"
#include "stdlib.h"
#include "stdio.h"
#include "Socket.h"
#include <sched.h>
#include <cutils/sockets.h>

#include <sys/types.h>      //  
#include <netinet/in.h>     //  
#include <sys/socket.h>     // for socket system calls
#include <arpa/inet.h>      // for socket system calls (bind)
#include <sched.h>
#include <pthread.h>        /* P-thread implementation        */
#include <signal.h>     /* for signal                     */
#include <semaphore.h>      /* for p-thread semaphores        */
#include <sys/un.h>


#define SERVER_IP "127.0.0.1"

#define MAX_BUFFER 100000
#define UNIX_PATH_MAX 108

int gl_svm_socket = -1;

int get_socket(){
	if(gl_svm_socket > 0){
		return gl_svm_socket;
	}
	struct sockaddr_un address;

	int client_socket = socket(PF_UNIX, SOCK_STREAM, 0);
	if(client_socket < 0)
	{
		ALOG(LOG_INFO,"EPOLL","CL: Create Socket Failed! %d",errno);
		return -1;
	}

	/* start with a clean address structure */
	memset(&address, 0, sizeof(struct sockaddr_un));

	address.sun_family = AF_UNIX;
	snprintf(address.sun_path, UNIX_PATH_MAX, "/dev/socket/epoll_sock");

	void * tmp = &address;

	if(connect(client_socket, 
				(struct sockaddr *) tmp,  
				sizeof(struct sockaddr_un)) != 0)
	{
		ALOG(LOG_INFO,"EPOLL","CL: Connect Socket Failed! %d",errno);
		client_socket = -1;
		return -1;
	}
	gl_svm_socket = client_socket;
	return client_socket;

	/*
	   struct sockaddr_in client_addr;
	   bzero(&client_addr,sizeof(client_addr));
	   client_addr.sin_family = AF_INET;
	   client_addr.sin_addr.s_addr = htons(INADDR_ANY);
	   client_addr.sin_port = htons(0);

	   int client_socket = socket(AF_INET,SOCK_STREAM,0);
	   if( client_socket < 0)
	   {
	   if(client_socket > 0)
	   close(client_socket);
	   client_socket = -1;
	   ALOG(LOG_DEBUG, "EPOLL", "NEW EPOLL SOCKET ERR NEW SOCK ERROR, RETRY");
	   return -1;
	   }
	   void *tmp = &client_addr;
	   if( bind(client_socket,(struct sockaddr*)tmp,sizeof(client_addr)))
	   {
	   if(client_socket > 0)
	   close(client_socket);
	   client_socket = -1;
	   ALOG(LOG_DEBUG, "EPOLL", "NEW EPOLL SOCKET ERR, BIND ERROR RETRY");
	   return -1;
	//printf("Client Bind Port Failed!\n");
	//exit(1);
	}

	struct sockaddr_in server_addr;
	bzero(&server_addr,sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	if(inet_aton(SERVER_IP,&server_addr.sin_addr) == 0)
	{
	if(client_socket > 0)
	close(client_socket);
	client_socket = -1;
	ALOG(LOG_DEBUG, "EPOLL", "NEW EPOLL SOCKET ERR,XX RETRY");
	return -1;
	//printf("Server IP Address Error!\n");
	//exit(1);
	}
	server_addr.sin_port = htons(6789);
	socklen_t server_addr_length = sizeof(server_addr);
	tmp = &server_addr;
	if(connect(client_socket,(struct sockaddr*)tmp, server_addr_length) < 0)
	{
	if(client_socket > 0)
	close(client_socket);
	client_socket = -1;
	ALOG(LOG_DEBUG, "EPOLL", "NEW EPOLL SOCKET ERR, CONNECT ERR, RETRY");
	return -1;
	}
	gl_svm_socket = client_socket;
	return client_socket;
	*/
}

int forward_to_svm(char* buff, int len){
	int esock = -1;
	while(esock <=0 ) {
		esock = get_socket();
		sleep(2);
	}
	/*int pid = getpid();
	  int retcode = send(esock, &pid, sizeof(int), 0);
	  if(retcode != sizeof(int)){
	  ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND 1 : %d", errno);
	  }*/
	int pid;
	memcpy(&pid, buff, sizeof(int));
	pid = htonl(pid);
	//ALOG(LOG_DEBUG,"EPOLL","FORWARDER TO SEND %d data, for pid %d", len, pid);
	int retcode = send(esock, buff, len, 0);
	if(retcode != len){
		ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND 2 : %d", errno);
	}
	//close(esock);
	//ALOG(LOG_DEBUG,"EPOLL","FORWARDER SENT %d data, for pid %d", len, pid);
	return true;
}

static ReProtocol remote("/dev/socket/instrument",11218);

static bool isZygote = true;
char curpname[100] ={0};
//static Socket *sock = NULL;

//static pthread_t *send_thread;
//static pthread_t send_zygote_thread;

static pthread_mutex_t gl_mtx = PTHREAD_MUTEX_INITIALIZER;
static volatile jint ot_class_id = 1;
static volatile jlong ot_object_id = 1;
static volatile jshort method_id = 1;

//Socket *zsock = NULL;
Socket *sock = NULL;
void send_once();

//bool gl_bypass=true;
static bool isDecided = false;

//jint add(JNIEnv *env, jobject thiz, jint x, jint y){
//	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in shadowvm native %s", __FUNCTION__);
//	return x + y + 1000;
//}

// ******************* REDispatch methods *******************

void vmEndHook(JavaVM* vm);
void NativeLog(JNIEnv * jni_env, jclass this_class, jstring text){
	//jsize str_len = jni_env->GetStringUTFLength(text);
	const char * str = 	jni_env->GetStringUTFChars(text, NULL);
	//if(DEBUGMODE)
	ALOG(LOG_INFO,"NATIVELOG", "LOG: %s in %d tid: %d",str, getpid(),dvmThreadSelf()->threadId );
	jni_env->ReleaseStringUTFChars(text,str);
}

void _mapPID(int pid, const char* pname){
	memcpy(curpname, pname, strlen(pname)+1);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS PID: %d NAME: %s", pid, pname);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS PID: %d NAME: %s", pid, curpname);
	Socket *tmpsock = new Socket(false);
	while(!tmpsock->Connect()){
		LOGDEBUG("Cannot connect through UDS in %d", getpid());
		sleep(2);
	}
	int signal = -4;
	tmpsock->Send((char*)&signal,sizeof(int));
	signal = strlen(pname);
	tmpsock->Send((char*)&signal,sizeof(int));
	tmpsock->Send(pname, strlen(pname));
	signal = pid;
	tmpsock->Send((char*)&signal,sizeof(int));

	int bp = -2;
	tmpsock->RecvInt(bp);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS is:%d PID: %d NAME: %s TID:%d", bp, pid, pname,dvmThreadSelf()->threadId);
	gDvm.bypass = bp==0?false:true;


	pthread_mutex_lock(&gDvm.s_mtx);
	if(gDvm.bypass) {
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","THIS PROCESS NEED NO HOOKS, SET TO NULL");
		gDvm.newObjHook = NULL;
		gDvm.freeObjHook = NULL;
		gDvm.threadEndHook = NULL;
		gDvm.vmEndHook = NULL;
	}
	//dvmDumpThread(dvmThreadSelf(),false);
	//dvmSetFieldBoolean(dvmThreadSelf()->threadObj, gDvm.offJavaLangThread_bypass, gDvm.bypass);
	pthread_mutex_unlock(&gDvm.s_mtx);
	isDecided = true;
	delete tmpsock;
	remote.MapPidPname(pid, pname);
}

void mapPID_2
(JNIEnv * jni_env, jclass this_class, jstring pname) {
	jsize str_len = jni_env->GetStringUTFLength(pname);
	const char * str = 	jni_env->GetStringUTFChars(pname, NULL);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPING PID: %d NAME: %s", getpid(), str);
	Socket *tmpsock = new Socket(true);
	while(!tmpsock->Connect()){
		LOGDEBUG("Cannot connect through UDS");
		sleep(2);
	}
	int signal = -4;
	tmpsock->Send((char*)&signal,sizeof(int));
	signal = str_len;
	tmpsock->Send((char*)&signal,sizeof(int));
	tmpsock->Send(str, str_len);
	signal = getpid();
	tmpsock->Send((char*)&signal,sizeof(int));

	int bp = -2;
	tmpsock->RecvInt(bp);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","BYPASS is:%d PID: %d NAME: %s", bp, getpid(), str);
	gDvm.bypass = bp==0?false:true;


	jni_env->ReleaseStringUTFChars(pname,str);
	pthread_mutex_lock(&gDvm.s_mtx);
	if(gDvm.bypass) {
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","SETTING THE HOOKS TO NULL");
		gDvm.newObjHook = NULL;
		gDvm.freeObjHook = NULL;
		gDvm.threadEndHook = NULL;
		gDvm.vmEndHook = NULL;
	}
	pthread_mutex_unlock(&gDvm.s_mtx);
	isDecided = true;
	delete tmpsock;
}
void mapPID
(JNIEnv * jni_env, jclass this_class, jstring pname, jint pid) {
	//remote.OpenConnection();
	//jsize str_len = jni_env->GetStringUTFLength(pname);
	//const char * str = 	jni_env->GetStringUTFChars(pname, NULL);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","PID: %d NAME: %s", pid, str);
	/*
	   Socket *tmpsock = new Socket();
	   while(!tmpsock->Connect()){
	   LOGDEBUG("Cannot connect through UDS");
	   sleep(2);
	   }
	   int signal = -4;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   signal = str_len;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   tmpsock->Send(str, str_len);
	   signal = pid;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   jni_env->ReleaseStringUTFChars(pname,str);
	   delete tmpsock;
	   */
}

jshort registerMethod
(JNIEnv * jni_env, jclass this_class, jstring analysis_method_desc) {
	jsize str_len = jni_env->GetStringUTFLength(analysis_method_desc);
	const char * str = 	jni_env->GetStringUTFChars(analysis_method_desc, NULL);
	char tmp[100];
	int tmpsize = str_len<100?str_len:99;
	memcpy(tmp, str, tmpsize);
	tmp[tmpsize] = 0;
	//pthread_mutex_lock(&gl_mtx);
	method_id++;
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, method_id, str, str_len);
	jni_env->ReleaseStringUTFChars(analysis_method_desc,str);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT:register method %s pid:%d tid:%d", tmp, getpid(),dvmThreadSelf()->threadId);
	//pthread_mutex_unlock(&gl_mtx);
	//dvmDumpThread(dvmThreadSelf(),true);
	return method_id;
}

void onFork
(int para) {
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","ONFORK happens currentid-paraid: %d-%d",getpid(),para);
	//send_events();
	if(para == 0) {
		pthread_mutex_init(&gl_mtx, NULL);//RE INIT LOCK
		remote.UpdateMutex();
		Buffer* zygote_buff;
		zygote_buff = remote.ReturnAndResetBuffer();
		gl_svm_socket = -1;
		if(DEBUGMODE)
			ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","DROPPING PACKETS FROM PARENT: %d-%d sized %d",getpid(),para, zygote_buff->q_occupied);
		delete zygote_buff;
		remote.OnForkEvent(para);
	}
	if(para != 0){
		//the parent
		remote.OnForkEvent(para);
		Buffer* zygote_buff;
		pthread_mutex_lock(&gl_mtx);
		zygote_buff = remote.ReturnAndResetBuffer();
		//ALOG(LOG_DEBUG,"SHADOWDEBUG","BEFORE FORK, BUFF OF ZYGOTE: %d", zygote_buff->q_occupied);
		/*
		   zsock = new Socket();
		   while(!zsock->Connect()){
		   LOGDEBUG("Zygote Cannot connect through UDS");
		   sleep(2);
		   }
		   int signal = -3;
		   zsock->Send((char*)&signal,sizeof(int));


		   int pid = getpid();
		   zsock->Send((char*)&pid,sizeof(int));
		   zsock->Send((char*)&(zygote_buff->q_occupied),sizeof(int));
		   zsock->Send(zygote_buff->q_data,zygote_buff->q_occupied);

		   delete zsock;
		   zsock = NULL;
		   */

		forward_to_svm(zygote_buff->q_data, zygote_buff->q_occupied);

		pthread_mutex_unlock(&gl_mtx);

		if(zygote_buff)
			delete zygote_buff;
		zygote_buff=NULL;
	}
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d, tid:%d", (int)analysis_method_id,dvmThreadSelf()->threadId);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, 0, analysis_method_id);
	//dvmDumpAllThreads(false);
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
 jbyte ordering_id) {
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d with ordering %d", (int)analysis_method_id, (int)ordering_id);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, ordering_id, analysis_method_id);
	//dvmDumpAllThreads(false);
}
/*
   void OpenConnection(){
   ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: open connection");
//remote.OpenConnection();
if(sock) {
return;
}
sock = new Socket();
while(!sock->Connect()){
LOGDEBUG("Cannot connect through UDS");
sleep(2);
}
int signal = -3;
sock->Send((char*)&signal,sizeof(int));
}
*/
void manuallyOpen
(JNIEnv * jni_env, jclass this_class) {
	//OpenConnection();
}
void manuallyClose
(JNIEnv * jni_env, jclass this_class) {
	if(DEBUGMODE)
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: close connection at %d", getpid());
	//remote.ConnectionClose();
	vmEndHook(NULL);
	/*Socket *sock = new Socket(false);
	  int pid = getpid();
	  ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: close connection at %d %d", getpid(), pid);
	  pid = htonl(pid);
	  sock->Send((char*)&pid, sizeof(int));
	  char tmp = MSG_CLOSE;
	  sock->Send(&tmp, 1);
	  delete sock;
	  sock = NULL;*/
	// exit(0);
}
void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis end for method in %d", dvmThreadSelf()->threadId);

	remote.AnalysisEndEvent(dvmThreadSelf()->threadId);
	if(remote.GetCurrentSize() > MAX_BUFFER){
		pthread_mutex_lock(&gl_mtx);
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","LOCK in %d",dvmThreadSelf()->threadId);
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","SEND ONCE %d", remote.GetCurrentSize());
		send_once();
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","UNLOCK in %d",dvmThreadSelf()->threadId);
		pthread_mutex_unlock(&gl_mtx);
	}
}

void sendBoolean
(JNIEnv * jni_env, jclass this_class, jboolean to_send) {
	remote.SendJboolean(dvmThreadSelf()->threadId, to_send);
}

void sendByte
(JNIEnv * jni_env, jclass this_class, jbyte to_send) {
	remote.SendJbyte(dvmThreadSelf()->threadId, to_send);
}

void sendChar
(JNIEnv * jni_env, jclass this_class, jchar to_send) {
	remote.SendJchar(dvmThreadSelf()->threadId, to_send);
}

void sendShort
(JNIEnv * jni_env, jclass this_class, jshort to_send) {
	remote.SendJshort(dvmThreadSelf()->threadId, to_send);
}

void sendInt
(JNIEnv * jni_env, jclass this_class, jint to_send) {
	remote.SendJint(dvmThreadSelf()->threadId, to_send);
}

void sendLong
(JNIEnv * jni_env, jclass this_class, jlong to_send) {
	remote.SendJlong(dvmThreadSelf()->threadId, to_send);
}

void sendFloat
(JNIEnv * jni_env, jclass this_class, jfloat to_send) {
	remote.SendJfloat(dvmThreadSelf()->threadId, to_send);
}

void sendDouble
(JNIEnv * jni_env, jclass this_class, jdouble to_send) {
	remote.SendJdouble(dvmThreadSelf()->threadId, to_send);
}

jlong SetAndGetNetref(Object* obj);
jlong newClass(ClassObject *obj){
	if(obj == NULL)  {
		return 0;
	}
	jlong superid = SetAndGetNetref(obj->super);
	jlong loaderid = SetAndGetNetref(obj->classLoader);
	obj->tag = _set_net_reference(ot_object_id++,ot_class_id++,1,1);
	char tmp;
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW class found %s id %d, loaderid: %lld, superid: %lld",obj->descriptor, ot_class_id-1, loaderid, superid);
	remote.NewClassEvent(obj->descriptor, strlen(obj->descriptor), loaderid, 0, &tmp);
	remote.NewClassInfo(obj->tag, obj->descriptor, strlen(obj->descriptor), "", 0, loaderid, superid);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW class found %lld:%s",obj->tag, obj->descriptor);
	return obj->tag;
}

jlong SetAndGetNetref(Object* obj){
	jlong res;
	if(obj == NULL) //to_send is null or is weak reference which has already been cleared
	{
		res = 0;
	}else if(obj->tag != 0){
		res = obj->tag;
	}else if(dvmIsClassObject(obj)){
		res = newClass((ClassObject*)obj);
	}else {
		//ASSERT(obj->clazz != NULL,"object class is null");
		if(obj->clazz->tag == 0){ //its class not registered
			newClass(obj->clazz);
		}
		obj->tag = _set_net_reference(ot_object_id++,net_ref_get_class_id(obj->clazz->tag),0,0);
		res = obj->tag;
		//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW object found tag:%lld, instance of %s classid %d",obj->tag, obj->clazz->descriptor,net_ref_get_class_id(obj->tag));
	}
	return res;
}

void sendObject
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	/*
	   if(obj->clazz == gDvm.classJavaLangString){
	   int len=((StringObject*)obj)->length();
	   int utflen = ((StringObject*)obj)->utfLength();
	   const u2* tmp = ((StringObject*)obj)->chars();
	   const jchar* content = (jchar*)tmp;
	   ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send string object");
	   remote.SendStringObject(self->threadId, obj->tag, content, len);
	   }
	   if(obj->clazz == gDvm.classJavaLangThread){
	   ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
//char tmp[16]="abcd";
//myitoa(self->threadId, tmp, 10);
//remote.SendThreadObject(self->threadId, obj->tag, tmp, strlen(tmp), isDaemon);
}
*/
remote.SendJobject(self->threadId, netref);
}

void sendObjectPlusData
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN %s", __FUNCTION__);
	//TODO
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	if(obj->clazz == gDvm.classJavaLangString){
		int len=((StringObject*)obj)->length();
		int utflen = ((StringObject*)obj)->utfLength();
		const u2* tmp = ((StringObject*)obj)->chars();
		//const jchar* content = (jchar*)tmp;
		char* str = new char[utflen+1];
		for(int i = 0; i < utflen; i++){
			str[i] = tmp[i];
		}
		str[utflen] = 0;
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send string object %s, %d, %d",str, len, utflen);

		if(!net_ref_get_spec(obj->tag)) {
			remote.SendStringObject(self->threadId, obj->tag, str, utflen);
			net_ref_set_spec((jlong*)&(obj->tag), 1);
		}else{
	if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","AVOID SENDING STRING");
		}
		delete []str;
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		bool isDaemon = false;
		char *name =  dvmGetThreadName_cstr(self);
		if(name!=NULL) {
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is %s", name);
			remote.SendThreadObject(self->threadId, obj->tag, name, strlen(name), isDaemon);
			free(name);
		}else{
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is null");
			if(!net_ref_get_spec(obj->tag)) {
				remote.SendThreadObject(self->threadId, obj->tag, "default", strlen("default"), isDaemon);
				net_ref_set_spec((jlong*)&(obj->tag), 1);
			}else{
	if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","AVOID SENDING THREAD");
			}

		}
	}
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","PLUS DATA HAS BEEN SENT TO BUFFER");

	remote.SendJobject(self->threadId, netref);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","__%s ENDED__", __FUNCTION__);
}

static const char *classPathName = "ch/usi/dag/dislre/AREDispatch";

static JNINativeMethod methods[]= {
	{"NativeLog", "(Ljava/lang/String;)V", (void*)NativeLog},
	{"mapPID", "(Ljava/lang/String;I)V", (void*)mapPID},
	{"mapPID", "(Ljava/lang/String;)V", (void*)mapPID_2},
	{"registerMethod", "(Ljava/lang/String;)S", (void*)registerMethod},
	{"analysisStart", "(S)V", (void*)analysisStart__S},
	{"analysisStart", "(SB)V", (void*)analysisStart__SB},
	{"analysisEnd", "()V", (void*)analysisEnd},
	{"sendBoolean", "(Z)V", (void*)sendBoolean},
	{"sendByte", "(B)V", (void*)sendByte},
	{"sendChar", "(C)V", (void*)sendChar},
	{"sendShort", "(S)V", (void*)sendShort},
	{"sendInt", "(I)V", (void*)sendInt},
	{"sendLong", "(J)V", (void*)sendLong},
	{"sendFloat", "(F)V", (void*)sendFloat},
	{"sendDouble", "(D)V", (void*)sendDouble},
	{"sendObject", "(Ljava/lang/Object;)V", (void*)sendObject},
	{"sendObjectPlusData", "(Ljava/lang/Object;)V", (void*)sendObjectPlusData},
	{"manuallyOpen", "()V", (void*)manuallyOpen},
	{"manuallyClose", "()V", (void*)manuallyClose},
	//{"onFork", "(I)V", (void*)onFork},
};


typedef union{
	JNIEnv* env;
	void* venv;
}UnionJNIEnvToVoid;

static int registerNativeMethods(JNIEnv* env, const char* className,
		JNINativeMethod* gMethods, int numMethods){

	jclass clazz;
	clazz = env->FindClass(className);

	if (clazz == NULL)
		return JNI_FALSE;
	if (env->RegisterNatives(clazz, gMethods, numMethods)<0)
		return JNI_FALSE;
	return JNI_TRUE;

}

int registerShadowNatives(JNIEnv *env){

	if (!registerNativeMethods(env, classPathName,
				methods, sizeof(methods)/sizeof(methods[0])))
	{
		return JNI_FALSE;
	}

	return JNI_TRUE;

}

void objNewHook(Object* obj){
	//return;
	SetAndGetNetref(obj);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s %s %lld", __FUNCTION__, obj->clazz->descriptor, obj->tag);
}
void threadEndHook(Thread* self){
	//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
	bool isDaemon = false;
	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s %d %s", __FUNCTION__, self->threadId, isDaemon?"daemon":"not daemon");
}
void vmEndHook(JavaVM* vm){
	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s for %s", __FUNCTION__, curpname);

	if(!gDvm.bypass) {
		//if(!strcmp(curpname, "zygote") || !strcmp(curpname, "dalvikvm")) {
		pthread_mutex_lock(&gl_mtx);
		remote.ConnectionClose();
		Buffer* tmp = remote.ReturnAndResetBuffer();

		/*
		   zsock = new Socket();
		   while(!zsock->Connect()){
		   LOGDEBUG("Zygote Cannot connect through UDS");
		   sleep(2);
		   }
		   int signal = -3;
		   zsock->Send((char*)&signal,sizeof(int));
		   int pid = getpid();
		   zsock->Send((char*)&pid,sizeof(int));
		   zsock->Send((char*)&(tmp->q_occupied),sizeof(int));
		   zsock->Send((char*)tmp->q_data, tmp->q_occupied);
		   delete zsock;
		   zsock = NULL;
		   */
		forward_to_svm(tmp->q_data, tmp->q_occupied);

		pthread_mutex_unlock(&gl_mtx);

		if(tmp)
			delete tmp;
		//}
	}

}
void objFreeHook(Object* obj, Thread* self){
	//if(self->isDaemon)
	//	return;
	if(obj == NULL)
		return;
	if(obj->tag == 0)
		return;
	SetAndGetNetref(obj);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in FREE hook %s %lld", obj->clazz->descriptor, obj->tag);
	if(!dvmIsClassObject(obj))
		remote.ObjFreeEvent(obj->tag);
}
int classfileLoadHook(const char* name, int len){
	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","LOADING CLASS %s", name);
	return 1;
}
int classInitHook(ClassObject* co){
	SetAndGetNetref(co);
	return 1;
}

/* static void * zygote_send_thread_loop(void * obj) {
   while(true){
   ALOG(LOG_DEBUG,"SHADOWDEBUG","IN ZYGOTE WHILE LOOOP");
   sleep(15);
//sched_yield();
//std::this_thread::yield();
}
return NULL;
}
*/
/*
   static void * send_ss_thread_loop(void * obj) {
   int sock_host = -1;
   int retcode;
   while(true) {
   sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
   if(sock_host <= 0){
   ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
   sleep(5);
   }else
   break;
   }
   Buffer *undecidedBuf = new Buffer(1024);
//int signal = -3;
//sock->Send((char*)&signal,sizeof(int));
while(true){
ALOG(LOG_DEBUG,"SHADOWDEBUG","IN WHILE LOOOP");
Buffer* tmp = NULL;
tmp = remote.ReturnAndResetBuffer();
if(!isDecided){
undecidedBuf->Enqueue(tmp->q_data, tmp->q_occupied);
ALOG(LOG_DEBUG,"SHADOWDEBUG","PUTTING UNDECIDED EVENTS INTO BUFFER SIZED: %d", undecidedBuf->q_occupied);
}else{
if(gDvm.bypass){
ALOG(LOG_DEBUG,"SHADOWDEBUG","DROP THE BUFFER FOR UNOBSERVED PROCESS SIZED %d", undecidedBuf->q_occupied);
delete tmp;
break;
}

if(undecidedBuf){
ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING UNDECIDED BUFFER SIZED: %d", undecidedBuf->q_occupied);
if(undecidedBuf->q_occupied >0 ){
retcode = send(sock_host, undecidedBuf->q_data, undecidedBuf->q_occupied, 0);
while(retcode <0)
{
while(true) {
ALOG (LOG_INFO,"INSTRUMENTSERVER","SEND ERROR RETRYING");
sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
if(sock_host <= 0){
ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
sleep(5);
}else
break;
}
retcode = send(sock_host, undecidedBuf->q_data, undecidedBuf->q_occupied, 0);
}
ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d retcode: %d", undecidedBuf->q_occupied, retcode);
}else{
ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");

}

delete undecidedBuf;
undecidedBuf = NULL;
}
ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d", tmp->q_occupied);
if(tmp->q_occupied > 0) {
retcode = send(sock_host,tmp->q_data, tmp->q_occupied, 0);
while(retcode <0){
ALOG (LOG_INFO,"INSTRUMENTSERVER","SEND ERROR RETRYING");
while(true) {
sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
if(sock_host <= 0){
ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
sleep(5);
}else
break;
}
retcode = send(sock_host,tmp->q_data, tmp->q_occupied, 0);
}
ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d retcode: %d", tmp->q_occupied, retcode);
}else{
	ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
}
}
if(tmp)
	delete tmp;
	sleep(2);
	//if(remote.IsClosed())
	//	break;
	}
if(undecidedBuf)
	delete undecidedBuf;

	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
	}
*/
static void * send_zygote_thread_loop(void * obj) {
	//Buffer *undecidedBuf = new Buffer(1024);
	Socket *sock = new Socket();
	while(!sock->Connect()){
		LOGDEBUG("Cannot connect through UDS in %s for %d",__FUNCTION__, getpid());
		sleep(2);
	}
	int signal = -3;
	sock->Send((char*)&signal,sizeof(int));
	while(true){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,"SHADOWDEBUG","IN  ZYGOTE WHILE LOOOP");
		Buffer* tmp = NULL;
		tmp = remote.ReturnAndResetBuffer();
		{
	if(DEBUGMODE)
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d", tmp->q_occupied);
			if(tmp->q_occupied >0 ){
				if(!sock->Send(tmp->q_data, tmp->q_occupied)){
	if(DEBUGMODE)
					ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND");
					delete sock;
					sock = NULL;
				}
			}else {
	if(DEBUGMODE)
				ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
			}
		}
		if(tmp)
			delete tmp;
		sleep(2);
	}

	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
}

void send_once(){
	if(remote.GetCurrentSize() <= MAX_BUFFER)
		return;
	Buffer* tmp = NULL;
	tmp = remote.ReturnAndResetBuffer();
	if(tmp->q_occupied >0 ){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d in %d:%d", tmp->q_occupied, getpid(), dvmThreadSelf()->threadId);

		/*
		   sock = new Socket();
		   while(!sock->Connect()){
		   LOGDEBUG("Cannot connect through UDS in %s for %d",__FUNCTION__, getpid());
		   sleep(2);
		   }
		   int signal = -3;
		   sock->Send((char*)&signal,sizeof(int));
		   int pid = getpid();
		   sock->Send((char*)&pid,sizeof(int));
		   sock->Send((char*)&(tmp->q_occupied), sizeof(int));
		   if(!sock->Send(tmp->q_data, tmp->q_occupied)){
		   ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND");
		   delete sock;
		   sock = NULL;
		   }
		   delete sock;
		   */


		forward_to_svm(tmp->q_data, tmp->q_occupied);

	}else {
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
	}
	if(tmp)
		delete tmp;
}
static void * send_thread_loop(void * obj) {
	while(!isDecided){
		sleep(3);
	}
	if(gDvm.bypass){
		pthread_detach(pthread_self());
		pthread_exit(NULL);
		return NULL;
	}

	while(true){
		//ALOG(LOG_DEBUG,"SHADOWDEBUG","IN WHILE LOOOP");
		Buffer* tmp = NULL;
		pthread_mutex_lock(&gl_mtx);
		pthread_mutex_lock(&remote.gl_mtx);
		tmp = remote.ReturnAndResetBufferNolock();
		if(tmp->q_occupied >0 ){
			pthread_mutex_unlock(&remote.gl_mtx);
	if(DEBUGMODE)
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d from pid %d", tmp->q_occupied, getpid());
			forward_to_svm(tmp->q_data, tmp->q_occupied);
			pthread_mutex_unlock(&gl_mtx);
			sleep(2);
		}else {
	if(DEBUGMODE)
			ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
			pthread_mutex_unlock(&gl_mtx);
			pthread_cond_wait(&remote.new_event_cond, &remote.gl_mtx);
			pthread_mutex_unlock(&remote.gl_mtx);
			sleep(2);
		}
		if(tmp)
			delete tmp;
	}

	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
}
jint ShadowLib_Zygote_OnLoad(JavaVM* vm, void* reserved){
	pthread_mutex_init(&gl_mtx, NULL);
	isZygote = true;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;
	//zygote_buff = NULL;

	//gDvm.shadowHook = &testShadowHook;
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, Zygote PID: %d", getpid());

	/* pthread_attr_t thread_attr;
	   int thread_policy;
	   struct sched_param thread_param;
	   int status, rr_min_priority, rr_max_priority;

	   pthread_attr_init(&thread_attr);
#if defined(_POSIX_THREAD_PRIORITY_SCHEDULING) && !defined(sun)
pthread_attr_getschedpolicy(&thread_attr, &thread_policy);
pthread_attr_getschedparam(&thread_attr, &thread_param);
ALOG(LOG_DEBUG,"SVM","Default policy is %s, priority is %d",
(thread_policy == SCHED_FIFO ? "FIFO"
: (thread_policy == SCHED_RR ? "RR"
: (thread_policy == SCHED_OTHER ? "OTHER"
: "unknown"))),
thread_param.sched_priority);

status = pthread_attr_setschedpolicy(&thread_attr, SCHED_RR);
if(status != 0)
ALOG(LOG_DEBUG,"SVM","Unable to set SCHED_RR policy.");
else{
rr_min_priority = sched_get_priority_min(SCHED_RR);
if(rr_min_priority == -1)
errno_abort("Get SCHED_RR min priority");
rr_max_priority = sched_get_priority_max(SCHED_RR);
if(rr_max_priority == -1)
errno_abort("Get SCHED_RR max priority");
thread_param.sched_priority = (rr_min_priority + rr_max_priority)/2;
ALOG(LOG_DEBUG,"SVM","SCHED_RR priority range is %d to %d: using %d\n",
rr_min_priority, rr_max_priority, thread_param.sched_priority);
pthread_attr_setschedparam(&thread_attr, &thread_param);
ALOG(LOG_DEBUG,"SVM","Creating thread at RR/%d\n", thread_param.sched_priority);
pthread_attr_setinheritsched(&thread_attr, PTHREAD_EXPLICIT_SCHED);
}
#else
ALOG(LOG_DEBUG,"SVM","Priority scheduling not supported\n");
#endif
*/
	//
	//if(gDvm.zygote){
	if(getpid()<100){
		_mapPID(getpid(),"zygote");
	}else{
		_mapPID(getpid(),"dalvikvm");
		if(false){
			pthread_t send_thread;
			pthread_create(&send_thread, NULL, send_zygote_thread_loop, NULL);
		}
	}
//OpenConnection();
//gDvm.newObjHook = &objNewHook;
gDvm.freeObjHook = &objFreeHook;
gDvm.threadEndHook = &threadEndHook;
gDvm.vmEndHook = &vmEndHook;
//gDvm.classInitHook = &classInitHook;
//gDvm.classfileLoadHook = &classfileLoadHook;


if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
	goto bail;
}

env = uenv.env;

env = uenv.env;

if (registerShadowNatives(env) != JNI_TRUE){

	goto bail;
}

result = JNI_VERSION_1_4;

//pthread_t send_thread;
//pthread_create(&send_thread, NULL, zygote_send_thread_loop, NULL);
//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","CAN BE PRINTED?");

bail:
return result;
}

void BeforeFork(){
	/*
	   Buffer* zygote_buff;
	   zygote_buff = remote.ReturnAndResetBuffer();
	   ALOG(LOG_DEBUG,"SHADOWDEBUG","BEFORE FORK, BUFF OF ZYGOTE: %d", zygote_buff->q_occupied);
	   if(zygote_buff->q_occupied > 0){
	   pthread_mutex_lock(&gl_mtx);
	   zsock = new Socket();
	   while(!zsock->Connect()){
	   LOGDEBUG("Zygote Cannot connect through UDS");
	   sleep(2);
	   }
	   int signal = -3;
	   zsock->Send((char*)&signal,sizeof(int));
	   zsock->Send((char*)&(zygote_buff->q_occupied),sizeof(int));
	   zsock->Send(zygote_buff->q_data,zygote_buff->q_occupied);
	   delete zsock;
	   zsock = NULL;
	   pthread_mutex_unlock(&gl_mtx);
	   }
	   if(zygote_buff)
	   delete zygote_buff;
	   zygote_buff=NULL;
	   */
}

jint ShadowLib_SystemServer_OnLoad(JavaVM* vm, void* reserved){
	//remote.UpdateMutex();
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//gDvm.shadowHook = &testShadowHook;
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, PID: %d", getpid());
	//pthread_mutex_init(&gl_mtx, NULL);
	//if(gDvm.bypass) {

	//OpenConnection();
	//pthread_mutex_lock(&gDvm.s_mtx);
	//gDvm.newObjHook = &objNewHook;
	//gDvm.freeObjHook = &objFreeHook;
	//gDvm.threadEndHook = &threadEndHook;
	//gDvm.vmEndHook = &vmEndHook;
	//gDvm.classInitHook = &classInitHook;
	//pthread_mutex_unlock(&gDvm.s_mtx);
	pthread_t send_thread;
	//pthread_create(&send_thread, NULL, send_ss_thread_loop, NULL);
	pthread_create(&send_thread, NULL, send_thread_loop, NULL);
	//gDvm.classfileLoadHook = &classfileLoadHook;

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}

	env = uenv.env;

	env = uenv.env;

	if (registerShadowNatives(env) != JNI_TRUE){
		goto bail;
	}

	result = JNI_VERSION_1_4;


bail:
	return 0;
}
jint ShadowLib_OnLoad(JavaVM* vm, void* reserved){
	//remote.UpdateMutex();
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//gDvm.shadowHook = &testShadowHook;
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, PID: %d", getpid());
	//pthread_mutex_init(&gl_mtx, NULL);
	//if(gDvm.bypass) {

	//OpenConnection();
	//pthread_mutex_lock(&gDvm.s_mtx);
	//gDvm.newObjHook = &objNewHook;
	//gDvm.freeObjHook = &objFreeHook;
	//gDvm.threadEndHook = &threadEndHook;
	//gDvm.vmEndHook = &vmEndHook;
	//gDvm.classInitHook = &classInitHook;
	//pthread_mutex_unlock(&gDvm.s_mtx);
	pthread_t send_thread;
	pthread_create(&send_thread, NULL, send_thread_loop, NULL);
	//gDvm.classfileLoadHook = &classfileLoadHook;

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}

	env = uenv.env;

	env = uenv.env;

	if (registerShadowNatives(env) != JNI_TRUE){

		goto bail;
	}

	result = JNI_VERSION_1_4;


bail:
	return 0;
}
