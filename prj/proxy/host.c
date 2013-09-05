// Server running on the instrument server or as an daemon service on android
// S.HY 2013
// Waiting for connection from dalvik library loading process

//----- Include files ---------------------------------------------------------
#include <stdio.h>      // for printf()
#include <stdlib.h>     // for exit()
#include <string.h>     // for strcpy(),strerror() and strlen()
#include <unistd.h>
#include <fcntl.h>      // for file i/o constants
#include <sys/stat.h>       // for file i/o constants
#include <errno.h>

/* FOR BSD UNIX/LINUX  ---------------------------------------------------- */
#include <sys/types.h>      //
#include <netinet/in.h>     //
#include <sys/socket.h>     // for socket system calls
#include <arpa/inet.h>      // for socket system calls (bind)
#include <sched.h>
#include <pthread.h>        /* P-thread implementation        */
#include <signal.h>     /* for signal                     */
#include <semaphore.h>      /* for p-thread semaphores        */
/* ------------------------------------------------------------------------ */

//----- Defines -------------------------------------------------------------
#define BUF_SIZE            1024    // buffer size in bytes
#define PORT_NUM            6666    // Port number for a Web server (TCP 5080)
#define PEND_CONNECTIONS     100    // pending connections to hold
#define TRUE                   1
#define FALSE                  0
#define NTHREADS 10      /* Number of child threads        */
#define NUM_LOOPS  10       /* Number of local loops          */
#define SCHED_INTVL 5       /* thread scheduling interval     */
#define HIGHPRIORITY 10

#define MAX_DEX 20000000

#define min(a,b) a>b?b:a

//int able[300];
//int able_size = 0;
int blacklist[300];
int bl_size = 0;
int alllist[300];
int a_size = 0;

/* global variables ---------------------------------------------------- */

sem_t thread_sem[NTHREADS];
int next_thread;
int can_run;
int i_stopped[NTHREADS];
char* dexes[NTHREADS];

//unsigned int client_s;      // Client socket descriptor

/* Child thread implementation ----------------------------------------- */
	void *
my_thread (void *arg)
{
	unsigned int myClient_s;    //copy socket
	int success = 1;

	/* other local variables ------------------------------------------------ */
	char buf[BUF_SIZE]; // buffer for socket
	unsigned int retcode;       // Return code

	int index = ((int*)arg)[1]%NTHREADS;
	printf("in %d thread\n",index);
	char* dex = (char*)malloc(20000000);
	unsigned int dex_size=0;
	unsigned int cnt=0;

	myClient_s = *(unsigned int *) arg; // copy the socket
	FILE* output = NULL;

	// receive from the client the dex content
	retcode = recv(myClient_s, &dex_size, sizeof(int), 0);
	printf("receive dex size: %d\n", dex_size);
	if(dex_size>20000000) {
		goto release;
		printf("too big size %d\n", dex_size);
	}
	int i = 0;
	for(; i < bl_size; i++) {
		if(dex_size == blacklist[i]) {
			printf("this one can not be insturmented\n");
			success = 0;
			break;
		}
	}
	if(success) {
		for(i=0;i < a_size; i++) {
			if(dex_size == alllist[i]) {
				success = 0;
				blacklist[bl_size++]=dex_size;
				printf("found new in black list %d\n", dex_size);
				break;
			}
		}
		if(success)
			alllist[a_size++]=dex_size;
	}
	if(!success) {
		bl_size = 0;
	}

	if(success) {
		char filename[100];
		char filename2[100];
		int len = snprintf(filename,100,"tmp%d.dex",dex_size);
		int len2 = snprintf(filename2,100,"tmp%d.dex.output",dex_size);
		filename[len]=0;
		filename2[len2]=0;
		output = fopen(filename,"wb");
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			memcpy(dex+cnt,buf,retcode);
			fwrite(buf, sizeof(char),retcode,output);
			cnt+=retcode;
		}
		fclose(output);
		output=NULL;
		//
		char cmd[300]="java -jar /home/sunh/instr.jar ";
		snprintf(cmd+strlen(cmd),300,"%s ",filename);
		snprintf(cmd+strlen(cmd),300,"%s",filename2);
		printf("%s\n", cmd);
		system(cmd);

		FILE * input = NULL;
		input = fopen(filename2,"rb");
		if(!input) {
			printf("cannot open file");
			success = -1;
		}
		int newsize = 0;
		while( (retcode=fread(buf,sizeof(unsigned char),BUF_SIZE, input))!=0)
		{
			newsize += retcode;
		}
		fclose(input);
		printf("old size/new size: %d/%d",dex_size,newsize);
		retcode = send(myClient_s, &newsize, sizeof(int),0);
		if(retcode != sizeof(int))
			goto release;
		input = fopen(filename2,"rb");
		while( (retcode=fread(buf,sizeof(unsigned char),BUF_SIZE, input))!=0)
		{
			newsize -= retcode;
			send(myClient_s, buf, BUF_SIZE,0);
		}
		if(newsize != 0) {
			printf("err happens during sending new file\n");
		}

		fclose(input);
	}else {
		cnt = 0;
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			memcpy(dex+cnt,buf,retcode);
			cnt+=retcode;
		}
		printf("received %d bytes\n",cnt);
		retcode = send(myClient_s, &dex_size, sizeof(int), 0);
		cnt = 0;
		while(cnt < dex_size) {
			retcode = send(myClient_s, dex+cnt, min(BUF_SIZE, dex_size-cnt),0);
			if(retcode < 0)
				goto release;
			cnt+=retcode;
		}
		printf("return size returned: %d\n", cnt);
	}
	
release:
	printf("get to release on client\n");
	if(output)
		fclose(output);
	free(dex);
	close(myClient_s);
	pthread_detach(pthread_self());
	pthread_exit(NULL);
}

//===== Main program ========================================================
	int
main (void)
{
	/* local variables for socket connection -------------------------------- */
	unsigned int server_s;  // Server socket descriptor
	struct sockaddr_in server_addr; // Server Internet address
	unsigned int            client_s;           // Client socket descriptor
	struct sockaddr_in client_addr; // Client Internet address
	struct in_addr client_ip_addr;  // Client IP address
	int addr_len;           // Internet address length

	unsigned int ids;       // holds thread args
	pthread_attr_t attr;        //  pthread attributes
	pthread_t threads;      // Thread ID (used by OS)

	/* create a new socket -------------------------------------------------- */
	server_s = socket (AF_INET, SOCK_STREAM, 0);

	/* fill-in address information, and then bind it ------------------------ */
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons (PORT_NUM);
	server_addr.sin_addr.s_addr = htonl (INADDR_ANY);
	bind (server_s, (struct sockaddr *) &server_addr, sizeof (server_addr));

	/* Listen for connections and then accept ------------------------------- */
	listen (server_s, PEND_CONNECTIONS);
	/* the web server main loop ============================================= */
	pthread_attr_init (&attr);

	int i = 0;
	////for(; i < NTHREADS; i++)
	//	dexes[i] = malloc(20000000);

	int index=0;
	//test system
	printf("\ndone message in program\n");





	while (TRUE)
	{
		printf ("my server is ready ...\n");

		/* wait for the next client to arrive -------------- */
		addr_len = sizeof (client_addr);
		client_s =
			accept (server_s, (struct sockaddr *) &client_addr, &addr_len);

		printf ("a new client arrives ...\n");

		if (client_s == FALSE)
		{
			printf ("ERROR - Unable to create socket \n");
			exit (FALSE);
		}

		else
		{
			/* Create a child thread --------------------------------------- */
			ids = client_s;
			int a[2]={client_s,index};
			pthread_create (    /* Create a child thread        */
					&threads,   /* Thread ID (system assigned)  */
					&attr,  /* Default thread attributes    */
					my_thread,  /* Thread routine               */
					a);  /* Arguments to be passed       */

		}
		index++;
	}

	/* To make sure this "main" returns an integer --- */
	close (server_s);       // close the primary socket
	return (TRUE);      // return code from "main"
}

