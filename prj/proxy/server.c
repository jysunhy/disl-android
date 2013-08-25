// Server running on the android linux
// S.HY 2013
// Waiting for connection from dalvik library loading process

//----- Include files ---------------------------------------------------------
#include "instr_header.h"
//#include "ByteBuffer.h"
#include <stdio.h>		// for printf()
#include <stdlib.h>		// for exit()
#include <string.h>		// for strcpy(),strerror() and strlen()
#include <fcntl.h>		// for file i/o constants
#include <sys/stat.h>		// for file i/o constants
#include <errno.h>

/* FOR BSD UNIX/LINUX  ---------------------------------------------------- */
#include <sys/types.h>		//
#include <netinet/in.h>		//
#include <sys/socket.h>		// for socket system calls
#include <arpa/inet.h>		// for socket system calls (bind)
#include <sched.h>
#include <pthread.h>		/* P-thread implementation        */
#include <signal.h>		/* for signal                     */
#include <semaphore.h>		/* for p-thread semaphores        */
/* ------------------------------------------------------------------------ */

//----- Defines -------------------------------------------------------------
#define DEX_SIZE			1000000
#define PORT_NUM            6666	// Port number for a Web server (TCP 5080)
#define PEND_CONNECTIONS     100	// pending connections to hold
#define TRUE                   1
#define FALSE                  0
#define NTHREADS 5		/* Number of child threads        */
#define NUM_LOOPS  10		/* Number of local loops          */
#define SCHED_INTVL 5		/* thread scheduling interval     */
#define HIGHPRIORITY 10

/* global variables ---------------------------------------------------- */
sem_t thread_sem[NTHREADS];
int next_thread;
int can_run;
int i_stopped[NTHREADS];

/* Child thread implementation ----------------------------------------- */
	void *
parse_dex (void *arg)
{
	unsigned int sock;	//copy socket

	char buf[BUF_SIZE];	// buffer for socket
	unsigned int ret;		// Return code

	char dex[DEX_SIZE];
	unsigned int dex_size=0;

	sock = *(unsigned int *) arg;	// copy the socket

	// receive from the client the dex content
	ret = recv_content(sock, dex, &dex_size); 
	if(ret <= 0)
		goto error;
	
	printf("dex received size: %d\n", dex_size);


	ret = send_content(sock, dex, dex_size);
	if(ret <= 0)
		goto error;
	close (sock);	// close the client connection
	pthread_exit(NULL);
error:
	close (sock);	// close the client connection
	printf("error occurs\n");
	exit(-1);
}

//===== Main program ========================================================
	int
main (void)
{
	/* local variables for socket connection -------------------------------- */
	unsigned int server_s;	// Server socket descriptor
	struct sockaddr_in server_addr;	// Server Internet address
	unsigned int            client_s;           // Client socket descriptor
	struct sockaddr_in client_addr;	// Client Internet address
	struct in_addr client_ip_addr;	// Client IP address
	int addr_len;			// Internet address length

	unsigned int ids;		// holds thread args
	pthread_attr_t attr;		//  pthread attributes
	pthread_t threads;		// Thread ID (used by OS)

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
			pthread_create (	/* Create a child thread        */
					&threads,	/* Thread ID (system assigned)  */
					&attr,	/* Default thread attributes    */
					parse_dex,	/* Thread routine               */
					&ids);	/* Arguments to be passed       */

		}
	}

	/* To make sure this "main" returns an integer --- */
	close (server_s);		// close the primary socket
	return (TRUE);		// return code from "main"
}
