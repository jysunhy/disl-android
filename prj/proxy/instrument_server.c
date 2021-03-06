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
#define SERVER_IP "10.10.6.101"
#define SOCKFILE "/dev/socket/instrument"
#define min(a,b) a>b?b:a

int map_key[300];
int map_size=0;
int map_value[300];

void * my_thread (void *arg)
{
	ALOG (LOG_INFO,"HAIYANG","IS: new client of instrument server");
	unsigned int myClient_s;    //copy socket
	char buf[BUF_SIZE]; // buffer for socket
	int retcode;       // Return code

	unsigned int dex_size=0;
	unsigned int cnt=0;
	cnt = 0;
	dex_size=0;
	myClient_s = *(unsigned int *) arg; // copy the socket
	int sock_host = -1;
	while(TRUE) {
		retcode = recv(myClient_s, &dex_size, sizeof(int), 0);
		ALOG (LOG_INFO,"HAIYANG","IS: receive %d size from client", dex_size);
		
		if(dex_size == -1)
		{
			break;
		}
		if(dex_size == -2) {
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
		int i = 0;
		for(; i< map_size;i++){
			if(map_key[i] == dex_size){
				break;
			}
		}
		if(i == map_size)
			map_key[map_size++] = dex_size;

		if(retcode != sizeof(int))
			goto release;
		while(sock_host < 0) {
			sock_host = socket_network_client(SERVER_IP, HOST_PORT, SOCK_STREAM);
			if(sock_host < 0){
				ALOG (LOG_INFO,"HAIYANG","new host sock error");
				sleep(1);
			}
			
		}
		ALOG (LOG_INFO,"HAIYANG","IS: sending %d size from IS", dex_size);
		retcode = send(sock_host, &dex_size, sizeof(int), 0);
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			send(sock_host, buf, retcode, 0);
			cnt+=retcode;
		}

		cnt = 0;
		retcode = recv(sock_host, &dex_size, sizeof(int), 0);
		if(i == map_size - 1){
			map_value[map_size-1] = dex_size;
		}
		ALOG (LOG_INFO,"HAIYANG","IS: new size %d size from HS", dex_size);
		if(retcode != sizeof(int))
			goto release;
		retcode = send(myClient_s, &dex_size, sizeof(int), 0);
		while(cnt<dex_size) {
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
 

 socket_fd = socket(PF_UNIX, SOCK_STREAM, 0);
 if(socket_fd < 0)
 {
		ALOG (LOG_INFO,"HAIYANG","create socket error");
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
		ALOG (LOG_INFO,"HAIYANG","bind socket error");
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

	while (TRUE)
	{
		ALOG (LOG_INFO,"HAIYANG","my server is ready ...");

 		connection_fd = accept(socket_fd,                     
                               (struct sockaddr *) &address,  
                               &address_length);                                                                                                                                                                                      
		ALOG (LOG_INFO,"HAIYANG","a new client arrives ...");

		if (connection_fd == FALSE)
		{
			ALOG (LOG_INFO,"HAIYANG","ERROR - Unable to create socket");
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

