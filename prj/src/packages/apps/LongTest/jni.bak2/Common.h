#ifndef _COMMON_H_
#define _COMMON_H_

#include <jni.h>
#include <Dalvik.h>
#include <JniInternal.h>
#include <cutils/log.h>
#include <pthread.h>

#define LOG_TAG "SHADOWVM"

#define ASSERT(cond, msg) do{if(!cond) ALOG(LOG_ERROR,LOG_TAG,msg);}while(0)

#define ERROR(msg) do{ALOG(LOG_ERROR,LOG_TAG,msg);}while(0)

#define DEBUG(msg) do{ALOG(LOG_DEBUG,LOG_TAG,msg);}while(0)

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

#endif
