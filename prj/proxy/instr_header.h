#ifndef _INSTR_HEADER_H_
#define _INSTR_HEADER_H_

#include <netinet/in.h>   
#include <sys/types.h>   
#include <sys/socket.h> 
#include <stdio.h>     
#include <stdlib.h>   
#include <string.h>  
#include <unistd.h>           
#include <netinet/in.h>       
#include <arpa/inet.h>        

#define BUF_SIZE 1024      

int send_content(int sock, const char* content, int length){
	int ret = send(sock,&length,sizeof(int),0);
	if(ret != sizeof(int))
		return -1;
	int cnt = 0;
	while(cnt < length) {
		ret = send(sock,content + cnt,(BUF_SIZE > (length-cnt))?(length-cnt):BUF_SIZE,0);
		if(ret<=0) {
			return -1;
		}
		cnt += ret;
	}
	return cnt;
}

int recv_content(int sock, char* content, int* plength){
	int ret = recv(sock, plength, sizeof(int), 0);
	if(ret!=sizeof(int))
		return -1;
	int cnt = 0;
	while(cnt < *plength)
	{
		ret = recv(sock, content+cnt,BUF_SIZE,0);
		if(ret <= 0) {
			return -1;
		}
		cnt+=ret;
	}
	return cnt;
}

#endif
