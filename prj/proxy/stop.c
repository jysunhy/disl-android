#include <netinet/in.h> // for sockaddr_in
#include <sys/types.h> // for socket
#include <sys/socket.h> // for socket
#include <stdio.h> // for printf
#include <stdlib.h> // for exit
#include <string.h> // for bzero

#define HELLO_WORLD_SERVER_PORT 6666
#define BUFFER_SIZE 1024
#define SERVER_IP "127.0.0.1"
#define min(a,b) a>b?b:a

int main(int argc, char **argv)
{

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




	int filesize = -1;
	printf("sending size:%d!\n",filesize);
	send(client_socket,&filesize,sizeof(int),0);
	close(client_socket);
	return 0;
}
