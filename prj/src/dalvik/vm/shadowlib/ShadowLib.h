#ifndef _SHADOWLIB_H_
#define _SHADOWLIB_H_

#include <jni.h>
#include <pthread.h>
#include <netinet/in.h> 
#include <endian.h>
#ifndef htobe64
# if __BYTE_ORDER == __LITTLE_ENDIAN
# define htobe64(x) __bswap_64 (x)
# define htole64(x) (x)
# define be64toh(x) __bswap_64 (x)
# define le64toh(x) (x)
# else
# define htobe64(x) (x)
# define htole64(x) __bswap_64 (x)
# define be64toh(x) (x)
# define le64toh(x) __bswap_64 (x)
# endif
#endif

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
/*
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
*/
typedef long ordering_id_type;

typedef ordering_id_type lock_id_type;

typedef int thread_id_type;
//typedef u4 thread_id_type;
void BeforeFork();
jint ShadowLib_OnLoad(JavaVM* vm, void* reserved);
jint ShadowLib_Zygote_OnLoad(JavaVM* vm, void* reserved);

void _mapPID(int pid, const char* pname);
void onFork(int parent);
int registerShadowNatives(JNIEnv *env);

#endif
