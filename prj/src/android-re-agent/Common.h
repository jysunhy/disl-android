#ifndef _COMMON_H_
#define _COMMON_H_

#include <jni.h>
#include <Dalvik.h>
#include <JniInternal.h>
#include <cutils/log.h>
#include <pthread.h>

#define MY_LOG_TAG "HAIYANG"

#define ASSERT(cond, msg) do{if(!cond) ALOG(LOG_ERROR,MY_LOG_TAG,msg);}while(0)

#define ERROR(msg) do{ALOG(LOG_ERROR,MY_LOG_TAG,msg);}while(0)

#define DEBUG(msg) do{ALOG(LOG_DEBUG,MY_LOG_TAG,msg);}while(0)

class ScopedMutex{
	public:
		ScopedMutex(pthread_mutex_t *mtx):local_mtx(mtx){
			pthread_mutex_lock(local_mtx);
		}
		~ScopedMutex(){
			pthread_mutex_unlock(local_mtx);
		}
	private:
		pthread_mutex_t *local_mtx;
		ScopedMutex(){}
};

char *myitoa(int num,char *str,int radix)
{     
	char index[]="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	unsigned unum; 
	int i=0,j,k;
	if(radix==10&&num<0) 
	{
		unum=(unsigned)-num;
		str[i++]='-';
	}
	else unum=(unsigned)num; 
	do{
		str[i++]=index[unum%(unsigned)radix];
		unum/=radix;
	}while(unum);
	str[i]='\0';
	if(str[0]=='-') k=1; 
	else k=0;
	char temp;
	for(j=k;j<=(i-1)/2;j++)
	{
		temp=str[j];
		str[j] = str[i-1+k-j];
		str[i-1+k-j] = temp;
	}
	return str;
}

typedef long ordering_id_type;

typedef ordering_id_type lock_id_type;

typedef u4 thread_id_type;

#endif
