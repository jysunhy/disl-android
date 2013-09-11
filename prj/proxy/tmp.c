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

/*----- HTTP response messages ----------------------------------------------
#define OK_IMAGE    "HTTP/1.0 200 OK\nContent-Type:image/gif\n\n"
#define OK_TEXT     "HTTP/1.0 200 OK\nContent-Type:text/html\n\n"
#define NOTOK_404   "HTTP/1.0 404 Not Found\nContent-Type:text/html\n\n"
#define MESS_404    "<html><body><h1>FILE NOT FOUND</h1></body></html>"
*/

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

	/* other local variables ------------------------------------------------ */
	char buf[BUF_SIZE]; // buffer for socket
	unsigned int retcode;       // Return code

	int index = ((int*)arg)[1]%NTHREADS;
	printf("in %d thread\n",index);
	//char* dex=dexes[index];
	char* dex = (char*)malloc(20000000);


	unsigned int dex_size=0;
	unsigned int cnt=0;

	myClient_s = *(unsigned int *) arg; // copy the socket
	FILE* output = NULL;
	printf("new client\n");

	{
		// receive from the client the dex content
		retcode = recv(myClient_s, &dex_size, sizeof(int), 0);
		printf("need to receive %d bytes\n", dex_size);
		if(dex_size>20000000) {
			goto release;
			printf("too big size %d\n", dex_size);

		}
		if(retcode != sizeof(int))
			goto release;
		char filename[100];
		int len = snprintf(filename,100,"tmp%d.odex",dex_size);
		//filename[len]=0;
		output = fopen(filename,"wb");
		while(cnt<dex_size) {
			retcode = recv (myClient_s, buf, BUF_SIZE, 0);
			if (retcode < 0)
				goto release;
			memcpy(dex+cnt,buf,retcode);
			fwrite(buf, sizeof(char),retcode,output);
			//strncpy(dex+cnt,buf,retcode);

		//	fwrite(dex+cnt,sizeof(char),retcode,output);
			cnt+=retcode;
		}
		fclose(output);
		output=NULL;
		printf("dex received size: %d\n", dex_size);

		retcode = send(myClient_s, &dex_size, sizeof(int),0);
		if(retcode != sizeof(int))
			goto release;
		printf("return size returned: %d\n", dex_size);

		cnt = 0;
		while(cnt < dex_size){
			retcode = send(myClient_s, dex+cnt, min(BUF_SIZE,dex_size-cnt), 0);
			if (retcode < 0)
				goto release;
			cnt += retcode;
		}
		printf("return dex returned:\n" );
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
	i = 0;
	//for(; i < NTHREADS; i++)
	//	free(dexes[i]);

	/* To make sure this "main" returns an integer --- */
	close (server_s);       // close the primary socket
	return (TRUE);      // return code from "main"
}
