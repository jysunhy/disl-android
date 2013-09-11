#include <netinet/in.h> // for sockaddr_in
#include <sys/types.h> // for socket
#include <sys/socket.h> // for socket
#include <stdio.h> // for printf
#include <stdlib.h> // for exit
#include <string.h> // for bzero
#include <cutils/sockets.h>

#define HELLO_WORLD_SERVER_PORT 6664
#define BUFFER_SIZE 1024
#define SERVER_IP "127.0.0.1"
#define min(a,b) a>b?b:a

int main(int argc, char **argv)
{


	int client_socket = socket_loopback_client(HELLO_WORLD_SERVER_PORT,SOCK_STREAM);
	if( client_socket < 0)
	{
		printf("Create Socket Failed!\n");
		exit(1);
	}

	FILE* input = fopen(argv[1],"rb");
	if(!input){
		printf("openerrr\n");
		exit(1);
	}
	char *content = malloc(10000000);
	int rc;
	int filesize=0;
	while( (rc=fread(content+filesize,sizeof(unsigned char),BUFFER_SIZE, input))!=0)
	{
		filesize+=rc;
	}
	fclose(input);

	char buffer[BUFFER_SIZE];
	char* dex = malloc(10000000);
	int dex_size=10000000;

	int i = 0;
	{
	int length = 0;
	printf("sending size:%d!\n",filesize);
	send(client_socket,&filesize,sizeof(int),0);
	int cnt = 0;
	while(cnt < filesize) {
		length = send(client_socket,content+cnt,min(BUFFER_SIZE,filesize-cnt),0);
		if(length<=0)
			goto error;
		cnt+=length;
	}
	printf("receiving!\n");
	length = recv(client_socket, &dex_size, sizeof(int), 0);
	if(length!=sizeof(int))
		goto error;
	cnt = 0;

	//FILE* output=fopen("tmp.dex","wb");

	while( cnt < dex_size)
	{
		length = recv(client_socket,buffer,BUFFER_SIZE,0);
		if(length <= 0)
			goto error;
	//	fwrite(buffer,sizeof(unsigned char), length, output);
		memcpy(dex+cnt,buffer,length);
		cnt+=length;
	}
	printf("receive dex size %d:\n",dex_size);
	//dex[cnt]=0;
	if(memcmp(content,dex,dex_size))
		printf("not equal\n");
	else
		printf("equal\n");

	}
	free(content);
	//fclose(output);
	
	if(dex)
		free(dex);
	close(client_socket);
	return 0;
error:
	if(dex)
		free(dex);
	close(client_socket);
	printf("error happens\n");
	return 0;
}
