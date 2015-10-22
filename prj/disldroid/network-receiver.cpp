/* A simple server in the internet domain using TCP
 *    The port number is passed as an argument */
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <iostream>
using namespace std;

void error(const char *msg)
{
	perror(msg);
//	exit(1);
}

int main(int argc, char *argv[])
{
	int sockfd, newsockfd, portno, clilen;
	struct sockaddr_in serv_addr, cli_addr;
	int n;
	if (argc < 2) {
		fprintf(stderr,"ERROR, no port provided\n");
//		exit(1);
	}
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) 
		error("ERROR opening socket");
	bzero((char *) &serv_addr, sizeof(serv_addr));
	portno = atoi(argv[1]);
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = INADDR_ANY;
	serv_addr.sin_port = htons(portno);
	if (bind(sockfd, (struct sockaddr *) &serv_addr,
				sizeof(serv_addr)) < 0) 
		error("ERROR on binding");
	listen(sockfd,5);
	clilen = sizeof(cli_addr);
	newsockfd = accept(sockfd, (sockaddr*)&cli_addr, (socklen_t*)&clilen);
	if (newsockfd < 0) 
		error("ERROR on accept");
	int bufferSize = atoi(argv[2]);
	char *buffer = new char[bufferSize];
	int sum = 0;
	while(true) {
		n = read(newsockfd,buffer,bufferSize);
		//cout<<n<<endl;
		sum+=n;
		if(sum > 10 * 1024 * 1024) {
			cout<<sum<<endl;
			return 0;
		}
	}
	if (n < 0) error("ERROR reading from socket");
	printf("Here is the message: %s\n",buffer);
//	n = write(newsockfd,"I got your message",18);
	if (n < 0) error("ERROR writing to socket");
	return 0; 
}
