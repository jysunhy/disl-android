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

#include <cutils/log.h>

//----- Defines -------------------------------------------------------------
#define BUF_SIZE            1024    // buffer size in bytes
#define PORT_NUM            6666    // Port number for a Web server (TCP 5080··)
#define PORT_NUM2            6665    // Port number for a Web server (TCP 5080)
#define PEND_CONNECTIONS     100    // pending connections to hold
#define TRUE                   1
#define FALSE                  0
#define SERVER_IP "10.10.6.101"

#define min(a,b) a>b?b:a

//#define INPUT "/home/sunh/testapk/tmp.apk"
#define INPUT "/system/framework/core.jar"
//#define OUTPUT "/home/sunh/testapk/tmp2.apk"
#define OUTPUT "/system/framework/core2.jar"

int sock_host = -1;
int new_host_sock();

/*
void * send_file(void *arg)
{
	new_host_sock();
	printf("in send file");
	FILE* infile = fopen(INPUT,"rb");
	if(!infile)
		goto error;
	char buf[BUF_SIZE]; // buffer for socket
	int size=0;
	int rc;       // Return code
	int cnt=0;


	while( (rc = fread(buf, sizeof(unsigned char), BUF_SIZE,infile)) != 0 )
	{
		size+=rc;
	}
	fclose(infile);
	printf("input size is %d\n", size);

	send(sock_host,&size,sizeof(int),0);
	infile = fopen(INPUT,"rb");

	while( (rc = fread(buf, sizeof(unsigned char), BUF_SIZE,infile)) != 0 )
	{
		send(sock_host, buf, rc,0 );
	} 

	FILE* outfile = fopen(OUTPUT,"wb");
	if(!outfile)
		goto error;
	cnt = 0;
	rc = recv(sock_host, &size, sizeof(int), 0);
	if(rc != sizeof(int))
		goto error;
	while(cnt<size) {
		rc = recv (sock_host, buf, BUF_SIZE, 0);
		if (rc < 0)
			goto error;
		fwrite(buf, sizeof(unsigned char), rc, outfile);
		cnt+=rc;
	}
	printf("dex write back size: %d\n", size);

	fclose(infile);
	fclose(outfile);
	close(sock_host);
	//pthread_exit(NULL);
	return 0;
error:
	close(sock_host);
	if(infile)
		fclose(infile);
	if(outfile)
		fclose(outfile);
	printf("error occurs\n");
	return 0;
}
*/

void * my_thread (void *arg)
{
	printf("in new thread!\n");
	unsigned int myClient_s;    //copy socket
	char buf[BUF_SIZE]; // buffer for socket
	int retcode;       // Return code

	unsigned int dex_size=0;
	unsigned int cnt=0;
	while(1) {
	while(new_host_sock()<0) {
		printf("cannot connect to host in %s\n",__FUNCTION__);
		sleep(1);
	}
	cnt = 0;
	dex_size=0;
	myClient_s = *(unsigned int *) arg; // copy the socket
	retcode = recv(myClient_s, &dex_size, sizeof(int), 0);
	printf("receive client size %d:\n",dex_size);
	if(retcode != sizeof(int))
		goto release;
	retcode = send(sock_host, &dex_size, sizeof(int), 0);
	printf("sent size %d to host:\n", dex_size);
	while(cnt<dex_size) {
		retcode = recv (myClient_s, buf, BUF_SIZE, 0);
		if (retcode < 0)
			goto release;
		send(sock_host, buf, retcode, 0);
		cnt+=retcode;
	}
	printf("dex received from client size: %d\n", dex_size);

	//FILE *output = fopen("tmp.odex","wb");
	cnt = 0;
	retcode = recv(sock_host, &dex_size, sizeof(int), 0);
	if(retcode != sizeof(int))
		goto release;
	retcode = send(myClient_s, &dex_size, sizeof(int), 0);
	while(cnt<dex_size) {
		retcode = recv (sock_host, buf, BUF_SIZE, 0);
		if (retcode < 0)
			goto release;
		send(myClient_s, buf, retcode, 0);
		//fwrite(buf, sizeof(unsigned char), retcode, output);
		cnt+=retcode;
	}

	printf("dex sent back size: %d\n", dex_size);
	close (sock_host);
	sock_host=  -1;
	}
release:
	//fclose(output);
	close (sock_host);
	close (myClient_s); // close the client connection
	pthread_detach(pthread_self());
	pthread_exit(NULL);
	return NULL;
}

int new_host_sock(){
	struct sockaddr_in client_addr;
	bzero(&client_addr,sizeof(client_addr));
	client_addr.sin_family = AF_INET;
	client_addr.sin_addr.s_addr = htons(INADDR_ANY);
	client_addr.sin_port = htons(0);

	sock_host = socket(AF_INET,SOCK_STREAM,0);
	if( sock_host < 0)
	{
		printf("Create Socket Failed!\n");
		return -1;
	}
	if( bind(sock_host,(struct sockaddr*)&client_addr,sizeof(client_addr)))
	{
		printf("Client Bind Port Failed!\n");
		return -1;
	}

	struct sockaddr_in server_addr;
	bzero(&server_addr,sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	if(inet_aton(SERVER_IP,&server_addr.sin_addr) == 0)
	{
		printf("Server IP Address Error!\n");
		return -1;
	}
	server_addr.sin_port = htons(PORT_NUM);
	socklen_t server_addr_length = sizeof(server_addr);
	if(connect(sock_host,(struct sockaddr*)&server_addr, server_addr_length) < 0)
	{
		sock_host = -1;
		printf("Can Not Connect To %s!\n",SERVER_IP);
		return -1;
	}
	return 0;
}
//===== Main program ========================================================
int main (int argc, const char* argv[])
{
	
	ALOG(LOG_INFO, "HAIYANG", "in MMMMMMMMMAIN %d", argc);
	int i =0;
	for(i = 0; i < argc; i++) {
		ALOG(LOG_INFO, "HAIYANG", "in MMMMMMMMMAIN %s", argv[i]);	

	}
	/* local variables for socket connection -------------------------------- */
	unsigned int server_s;  // Server socket descriptor
	struct sockaddr_in server_addr2; // Server Internet address
	unsigned int            client_s;           // Client socket descriptor
	struct sockaddr_in client_addr2; // Client Internet address
	struct in_addr client_ip_addr;  // Client IP address
	int addr_len;           // Internet address length

	unsigned int ids;       // holds thread args
	pthread_attr_t attr;        //  pthread attributes
	pthread_t threads;      // Thread ID (used by OS)

	/* create a new socket -------------------------------------------------- */
	server_s = socket (AF_INET, SOCK_STREAM, 0);

	/* fill-in address information, and then bind it ------------------------ */
	server_addr2.sin_family = AF_INET;
	server_addr2.sin_port = htons (PORT_NUM2);
	server_addr2.sin_addr.s_addr = htonl (INADDR_ANY);
	bind (server_s, (struct sockaddr *) &server_addr2, sizeof (server_addr2));

	/* Listen for connections and then accept ------------------------------- */
	listen (server_s, PEND_CONNECTIONS);
	/* the web server main loop ============================================= */
	pthread_attr_init (&attr);
	//send_file(NULL);
	while (TRUE)
	{
		printf ("my server is ready ...\n");

		/* wait for the next client to arrive -------------- */
		addr_len = sizeof (client_addr2);
		client_s =
			accept (server_s, (struct sockaddr *) &client_addr2, &addr_len);

		printf ("a new client arrives ...\n");

		if (client_s == FALSE)
		{
			printf ("ERROR - Unable to create socket \n");
			//exit (FALSE);
			continue;
		}

		else
		{
			/* Create a child thread --------------------------------------- */
			ids = client_s;
			pthread_create (    /* Create a child thread        */
					&threads,   /* Thread ID (system assigned)  */
					&attr,  /* Default thread attributes    */
					my_thread,  /* Thread routine               */
					&ids);  /* Arguments to be passed       */

		}
	}

	/* To make sure this "main" returns an integer --- */
	close (server_s);       // close the primary socket
	return (TRUE);      // return code from "main"
}

