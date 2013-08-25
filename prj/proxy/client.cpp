#include <netinet/in.h>    // for sockaddr_in
#include <sys/types.h>    // for socket
#include <sys/socket.h>    // for socket
#include <stdio.h>        // for printf
#include <stdlib.h>        // for exit
#include <string.h>        // for bzero
#include <unistd.h>           
#include <netinet/in.h>       
#include <arpa/inet.h>

#define HELLO_WORLD_SERVER_PORT    6666 
#define BUFFER_SIZE 1024
#define SERVER_IP "10.10.6.101"
#define DEX_SIZE 1000000
#define min(a,b) a>b?b:a

int instrument(const char* in, int i_len,char* out, int &o_len){
    struct sockaddr_in client_addr;
    bzero(&client_addr,sizeof(client_addr)); 
    client_addr.sin_family = AF_INET;    
    client_addr.sin_addr.s_addr = htons(INADDR_ANY);
    client_addr.sin_port = htons(0);    
    
    int client_socket = socket(AF_INET,SOCK_STREAM,0);
    if( client_socket < 0)
    {
        printf("Create Socket Failed!\n");
    close(client_socket);
	return -1;
    }
    if( bind(client_socket,(struct sockaddr*)&client_addr,sizeof(client_addr)))
    {
        printf("Client Bind Port Failed!\n"); 
    close(client_socket);
	return -1;
    }

    struct sockaddr_in server_addr;
    bzero(&server_addr,sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    if(inet_aton(SERVER_IP,&server_addr.sin_addr) == 0) 
    {
        printf("Server IP Address Error!\n");
    close(client_socket);
	return -1;
    }
    server_addr.sin_port = htons(HELLO_WORLD_SERVER_PORT);
    socklen_t server_addr_length = sizeof(server_addr);
    if(connect(client_socket,(struct sockaddr*)&server_addr, server_addr_length) < 0)
    {
        printf("Can Not Connect To %s!\n",SERVER_IP);
    close(client_socket);
	return -1;
    }

    int length = 0;
    char buffer[BUFFER_SIZE];
	send(client_socket,&i_len,sizeof(int),0);
	int cnt = 0;
	while(cnt < i_len) {
    	length = send(client_socket,in + cnt,min(BUFFER_SIZE, i_len-cnt),0);
		if(length<=0) {
    close(client_socket);
	return -1;
		}
		cnt+=length;
	}
	length = recv(client_socket, &o_len, sizeof(int), 0);
	if(length!=sizeof(int))
	{
    close(client_socket);
	return -1;
		//goto err;
	}
	cnt = 0;
    while( cnt < o_len)
	{
		length = recv(client_socket,buffer,BUFFER_SIZE,0);
        if(length <= 0) {
    close(client_socket);
	return -1;
			//goto err;
		}
		strncpy(out + cnt,buffer,length);
		cnt+=length;
    }
    close(client_socket);
	return 0;
//err:
//    close(client_socket);
//	return -1;
}

int main(int argc, char **argv)
{

	const char* input="12345";
	char output[1000];
	int output_size;
	instrument(input,5,output,output_size);
	output[output_size]='\0';
	printf("%s",output);
/*
    struct sockaddr_in client_addr;
    bzero(&client_addr,sizeof(client_addr)); 
    client_addr.sin_family = AF_INET;    
    client_addr.sin_addr.s_addr = htons(INADDR_ANY);
    client_addr.sin_port = htons(0);    
    
    int client_socket = socket(AF_INET,SOCK_STREAM,0);
    if( client_socket < 0)
    {
        printf("Create Socket Failed!\n");
        exit(1);
    }
    if( bind(client_socket,(struct sockaddr*)&client_addr,sizeof(client_addr)))
    {
        printf("Client Bind Port Failed!\n"); 
        exit(1);
    }

    struct sockaddr_in server_addr;
    bzero(&server_addr,sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    if(inet_aton(SERVER_IP,&server_addr.sin_addr) == 0) 
    {
        printf("Server IP Address Error!\n");
        exit(1);
    }
    server_addr.sin_port = htons(HELLO_WORLD_SERVER_PORT);
    socklen_t server_addr_length = sizeof(server_addr);
    if(connect(client_socket,(struct sockaddr*)&server_addr, server_addr_length) < 0)
    {
        printf("Can Not Connect To %s!\n",SERVER_IP);
        exit(1);
    }


	char dex[DEX_SIZE]="1234567890";
	int i=0;
	for(; i < 10000; i++)
		dex[i]='0'+i%10;
	int dex_size=10000;
    int length = 0;
    
    char buffer[BUFFER_SIZE];

	send(client_socket,&dex_size,sizeof(int),0);
	int cnt = 0;
	while(cnt < dex_size) {
    	length = send(client_socket,dex+cnt,min(BUFFER_SIZE,dex_size-cnt),0);
		if(length<=0)
			goto error;
		cnt+=length;
	}
	length = recv(client_socket, &dex_size, sizeof(int), 0);
	if(length!=sizeof(int))
		goto error;
	cnt = 0;
    while( cnt < dex_size)
	{
		length = recv(client_socket,buffer,BUFFER_SIZE,0);
        if(length <= 0)
			goto error;
		strncpy(dex+cnt,buffer,length);
		cnt+=length;
    }
	dex[cnt]=0;
	printf("%s\n",dex);
	printf("size: %d\n",cnt);
	return 0;
error:
    close(client_socket);
	printf("error happens\n");
	exit(-1);
    return 0;
	*/
}
