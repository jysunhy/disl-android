// Server running on the android linux
// S.HY 2013
// Waiting for connection from dalvik library loading process

//----- Include files ---------------------------------------------------------
#include <stdio.h>      // for printf()
#include <stdlib.h>     // for exit()
#include <string.h>     // for strcpy(),strerror() and strlen()
#include <fcntl.h>      // for file i/o constants
#include <sys/stat.h>       // for file i/o constants
#include <errno.h>
#include <cutils/sockets.h>
#include <cutils/log.h>

/* FOR BSD UNIX/LINUX  ---------------------------------------------------- */
#include <sys/types.h>      //
#include <netinet/in.h>     //
#include <sys/socket.h>     // for socket system calls
#include <arpa/inet.h>      // for socket system calls (bind)
#include <sched.h>
#include <pthread.h>        /* P-thread implementation        */
#include <signal.h>     /* for signal                     */
#include <semaphore.h>      /* for p-thread semaphores        */
#include <sys/un.h>

//----- Defines -------------------------------------------------------------
#define BUF_SIZE            1024    // buffer size in bytes
#define HOST_PORT            6666    // Port number for a Web server (TCP 5080··)
#define LISTEN_PORT            6664    // Port number for a Web server (TCP 5080)
#define TRUE                   1
#define FALSE                  0
//#define SERVER_IP "10.10.6.101"
#define SERVER_IP "192.168.1.103"
//#define SERVER_IP "192.168.56.101"
//#define SERVER_IP "10.0.3.15"
#define SOCKFILE "/dev/socket/instrument"
#define min(a,b) a>b?b:a

int map_key[300];
int map_size=0;
int map_value[300];

#define MAX_PROCESS 10000
int pids[MAX_PROCESS];
int psize = 0;
char* pnames[MAX_PROCESS];
char* observedNames[MAX_PROCESS];
int observedCnt = 0;

char bufname[1024];

pthread_mutex_t gl_mtx;

void * my_thread (void *arg)
{
	//ALOG (LOG_INFO,"INSTRUMENTSERVER","IS: new client of instrument server");
	unsigned int myClient_s;    //copy socket
	char buf[BUF_SIZE]; // buffer for socket
	int retcode;       // Return code

	unsigned int sign4=0;
	unsigned int cnt=0;
	cnt = 0;
	sign4=0;
	myClient_s = *(unsigned int *) arg; // copy the socket
	int sock_host = -1;
	while(TRUE) {
		retcode = recv(myClient_s, &sign4, sizeof(int), 0);
		if(sign4 == -1)
			break;
		if(sign4 == -2) {
			int key;
			recv(myClient_s, &key, sizeof(int),0);
			int i = 0;
			for(; i < map_size; i++) {
				if(map_key[i] == key)
					break;
			}
			if(i == map_size) {
				int tmp = -1;
				send(myClient_s,&tmp,sizeof(int),0);
			}else {
				send(myClient_s,&map_value[i],sizeof(int),0);
			}
			break;
		}
		if(sign4 == -3){
			ALOG (LOG_INFO,"INSTRUMENTSERVER","receive shadow event");
			while(true) {
				sock_host = socket_network_client(SERVER_IP, 11218, SOCK_STREAM);
				if(sock_host <= 0){
					ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
					sleep(10);
				}else
					break;
			}


			while(true){
				retcode = recv(myClient_s, buf, BUF_SIZE, 0);
				if(retcode < 0){
					ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "closed from app");
					break;
				}
				///*
				retcode = send(sock_host, buf, retcode, 0);
				if(retcode < 0) {
					ALOG (LOG_INFO,"INSTRUMENTSERVER","host sock error here");
					close(myClient_s);
					break;
				}
				//*/

				/*
				   char tmp[10241];
				   int i = 0;
				   for(; i < retcode; i++){
				   snprintf(tmp+(2*sizeof(int)+2)*i, 2*sizeof(int)+2, "%d:%d ", i, (unsigned int)buf[i]);
				   }
				   tmp[retcode*(2+2*sizeof(int))] = 0;
				   ALOG(LOG_INFO, "INSTRUMENTSERVER", "SHADOW PACKET %s", tmp);
				   */
			}
			break;
		}
		if(sign4 == -4) {
			ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "GET MAPPING EVENT");
			int pname_len;
			char pname[1024];
			int pid;
			recv(myClient_s, &pname_len, sizeof(int), 0);
			if(pname_len>1023)
				pname_len = 1023;
			if(recv(myClient_s,pname,pname_len,0) < 0)
				ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "GET PNAME ERROR");
			pname[pname_len] = 0;
			recv(myClient_s, &pid, sizeof(int), 0);
			ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "GET MAPPING PID: %d to NAME: %s", pid, pname);
			int tmp = 1;
			int i = 0;
			for(; i < observedCnt; i++){
				if(!strcmp(pname, observedNames[i])){
					ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "PROCESS NAME: %s is OBSERVED", pname);
					tmp = 0;
				}
			}
			if(tmp)
				ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: FOUNDED, SENDING BACK 1", pid);
			else
				ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: FOUNDED and ISTARGET, SENDING BACK 0", pid);

			send(myClient_s,&tmp,sizeof(int),0);
			if(pid != -1) {
				if(psize <= MAX_PROCESS){
					pids[psize]=pid;
					char* tmpname = (char*)malloc(pname_len+1);
					memcpy(tmpname,pname,pname_len);
					tmpname[pname_len]='\0';
					pnames[psize]=tmpname;
					psize++;
				}else{
					ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "more than max processes");
				}
			}else{
				//memcpy(bufname,pname,pname_len);
				//bufname[pname_len]='\0';
				//ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "GET MAPPING PID: -1 BUFFERING NAME: %s", bufname);
			}
			break;		
		}
		if(sign4 == -5) {
			int key;//PID
			recv(myClient_s, &key, sizeof(int),0);
			int i = 0;
			for(; i < psize; i++) {
				if(pids[i] == key)
					break;
			}
			int tmp = 1;
			if(i == psize) { //not in process list, then return false
				//send(myClient_s,&tmp,sizeof(int),0);
				ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: NOT FOUNDED, SENDING BACK -1", key);
				tmp = -1;
				//ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: NOT FOUNDED, GUESSING %s", key, bufname);
				//if(strcmp(bufname,"com.inspur.test")){
				//	tmp = 1;
				//}
			}else {
				if(!strcmp(pnames[i],"com.inspur.test")){
					tmp = 0;
				}
				if(!strcmp(pnames[i],"com.android.contacts")){
					tmp = 0;
				}
				if(!strcmp(pnames[i],"system_server")){
					tmp = 0;
				}
				if(tmp)
					ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: FOUNDED, SENDING BACK 1", key);
				else
					ALOG(LOG_DEBUG,"INSTRUMENTSERVER", "QUERYING %d: FOUNDED and ISTARGET, SENDING BACK 0", key);
			}
			send(myClient_s,&tmp,sizeof(int),0);
			break;
		}
		int namelen = sign4;
		retcode = recv(myClient_s, &sign4, sizeof(int), 0);
		if(retcode != sizeof(int))
			goto release;
		int codelen = sign4;
		cnt = 0;
		while(cnt<namelen) {
			retcode = recv (myClient_s, buf, namelen, 0);
			if (retcode < 0)
				goto release;
			cnt+=retcode;
		}
		buf[namelen] = 0;
		//	ALOG (LOG_INFO,"INSTRUMENTSERVER","new name comes : %s ", buf);

		int i = 0;
		for(; i< map_size;i++){
			if(map_key[i] == codelen){
				break;
			}
		}
		if(i == map_size)
			map_key[map_size++] = codelen;

		while(sock_host < 0) {
			sock_host = socket_network_client(SERVER_IP, HOST_PORT, SOCK_STREAM);
			if(sock_host < 0){
				ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
				sleep(1);
			}

		}
		//	ALOG (LOG_INFO,"INSTRUMENTSERVER","IS: sending %d size from IS", sign4);
		//	int flag = 123;
		//	flag = htonl(flag);
		//	retcode = send(sock_host, &flag, sizeof(int), 0);
		int namelen2n = htonl(namelen);
		retcode = send(sock_host, &namelen2n, sizeof(int), 0);
		int codelen2n = htonl(codelen);
		retcode = send(sock_host, &codelen2n, sizeof(int), 0);
		retcode = send(sock_host, buf, namelen, 0);
		cnt = 0;
		while(cnt<codelen) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			send(sock_host, buf, retcode, 0);
			cnt+=retcode;
		}

		cnt = 0;
		int newnamelen;
		retcode = recv(sock_host, &newnamelen, sizeof(int), 0);
		newnamelen = ntohl(newnamelen);
		retcode = recv(sock_host, &sign4, sizeof(int), 0);
		sign4 = ntohl(sign4);
		if(i == map_size - 1){
			map_value[map_size-1] = sign4;
		}
		//ALOG (LOG_INFO, "INSTRUMENTSERVER","new name received %d", newnamelen);
		retcode = recv(sock_host, buf, newnamelen, 0);
		//ALOG (LOG_INFO, "INSTRUMENTSERVER","new dexsize received %d", sign4);
		//ALOG (LOG_INFO,"INSTRUMENTSERVER","IS: new size %d size from HS", sign4);
		retcode = send(myClient_s, &sign4, sizeof(int), 0);
		while(cnt<sign4) {
			retcode = recv (sock_host, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			send(myClient_s, buf, retcode, 0);
			cnt+=retcode;
		}
		close (sock_host);
		sock_host = -1;
	}
release:
	if(sock_host > 0)
		close (sock_host);
	sock_host = -1;
	close (myClient_s); // close the client connection
	pthread_detach(pthread_self());
	pthread_exit(NULL);
	return NULL;
}

//===== Main program ========================================================
int main (void)
{
	struct sockaddr_un address;
	int socket_fd, connection_fd;
	socklen_t address_length;
	pid_t child;

	pthread_mutex_init(&gl_mtx, NULL);


	socket_fd = socket(PF_UNIX, SOCK_STREAM, 0);
	if(socket_fd < 0)
	{
		ALOG (LOG_INFO,"INSTRUMENTSERVER","create socket error");
		return 1;
	} 

	/* start with a clean address structure */
	memset(&address, 0, sizeof(struct sockaddr_un));                                                                                                                                                                                           
	address.sun_family = AF_UNIX;
	snprintf(address.sun_path, UNIX_PATH_MAX, SOCKFILE);
	if(bind(socket_fd, 
				(struct sockaddr *) &address,  
				sizeof(struct sockaddr_un)) != 0)                                                                                                                                                                                                  
	{
		ALOG (LOG_INFO,"INSTRUMENTSERVER","bind socket error");
		return 1;
	}
	char mod[]="0666";
	chmod(address.sun_path, strtol(mod,0,8));

	if(listen(socket_fd, 5) != 0)
	{
		printf("listen() failed\n");
		return 1;
	}



	unsigned int ids;       // holds thread args
	pthread_attr_t attr;        //  pthread attributes     
	pthread_t threads;      // Thread ID (used by OS)



	pthread_attr_init (&attr);

	int sock_host = -1;
	int retcode;
	while(true) {
		sock_host = socket_network_client(SERVER_IP, HOST_PORT, SOCK_STREAM);
		if(sock_host <= 0){
			ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
			sleep(5);
		}else
			break;
	}
	int flag = 1;
	flag = htonl(flag);
	retcode = send(sock_host, &flag, sizeof(int), 0);
	flag = 0;
	flag = htonl(flag);
	retcode = send(sock_host, &flag, sizeof(int), 0);
	char minus = '-';
	retcode = send(sock_host, &minus, sizeof(char), 0);

	int listlen;
	char * list;

	int newnamelen;
	retcode = recv(sock_host, &newnamelen, sizeof(int), 0);
	newnamelen = ntohl(newnamelen);
	retcode = recv(sock_host, &listlen, sizeof(int), 0);
	listlen = ntohl(listlen);
	char buf[10240];
	retcode = recv(sock_host, buf, newnamelen, 0);
	if(listlen < 10240){
		retcode = recv (sock_host, buf, listlen, 0);
		buf[listlen]='\0';
		ALOG (LOG_INFO,"INSTRUMENTSERVER","observed name list:%s", buf);
	}else {
		ALOG (LOG_INFO,"INSTRUMENTSERVER","observed name list length beyond 10240");
	}

	int startPos = 0;
	int endPos = 0;
	for(;endPos<=listlen;endPos++){
		if(buf[endPos]==';'){
			observedNames[observedCnt] = (char*)malloc(endPos-startPos+1);
			memcpy(observedNames[observedCnt], buf+startPos, endPos-startPos);
			observedNames[observedCnt][endPos-startPos]='\0';
			ALOG (LOG_INFO,"INSTRUMENTSERVER","new observedName %s",observedNames[observedCnt]);
			observedCnt++;
			startPos=endPos+1;
		}
	}

	close (sock_host);


	while (TRUE)
	{
		ALOG (LOG_INFO,"INSTRUMENTSERVER","my server is ready ...");

		connection_fd = accept(socket_fd,                     
				(struct sockaddr *) &address,  
				&address_length);                                                                                                                                                                                      
		ALOG (LOG_INFO,"INSTRUMENTSERVER","a new client arrives ...");

		if (connection_fd == FALSE)
		{
			ALOG (LOG_INFO,"INSTRUMENTSERVER","ERROR - Unable to create socket");
			continue;
		}

		else
		{
			/* Create a child thread --------------------------------------- */
			ids = connection_fd;
			pthread_create (    /* Create a child thread        */
					&threads,   /* Thread ID (system assigned)  */
					&attr,  /* Default thread attributes    */
					my_thread,  /* Thread routine               */
					&ids);  /* Arguments to be passed       */

		}
	}

	/* To make sure this "main" returns an integer --- */
	close (socket_fd);       // close the primary socket
	return (TRUE);      // return code from "main"
}

