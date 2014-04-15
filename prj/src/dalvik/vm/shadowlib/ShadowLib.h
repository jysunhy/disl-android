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
typedef long ordering_id_type;

typedef ordering_id_type lock_id_type;

typedef int thread_id_type;
jint ShadowLib_OnLoad(JavaVM* vm, void* reserved);
jint ShadowLib_Zygote_OnLoad(JavaVM* vm, void* reserved);
jint ShadowLib_SystemServer_OnLoad(JavaVM* vm, void* reserved);

void _mapPID(int pid, const char* pname);
void onFork(int parent);
int registerShadowNatives(JNIEnv *env);

#endif
