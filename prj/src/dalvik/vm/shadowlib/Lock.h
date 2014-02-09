#ifndef _RE_LOCK_H_
#define _RE_LOCK_H_

#include <pthread.h>
#include "ShadowLib.h"

#define DEFAULT_LOCK_BUFFER 1024

class LockBuffer{
	public:
		LockBuffer(int capacity=DEFAULT_LOCK_BUFFER):b_capacity(capacity),b_occupied(0){
			b_ids = new lock_id_type[capacity];
			b_locks = new pthread_mutex_t[capacity];
			pthread_mutex_init(&gl_mtx,NULL);
		}
		~LockBuffer(){
			delete []b_ids;
			for(int i = 0; i < b_occupied; i++)
				pthread_mutex_destroy(&b_locks[i]);
			delete []b_locks;
			pthread_mutex_destroy(&gl_mtx);
		}
		void Lock(lock_id_type lockid){
			LOGDEBUG("in %s %d",__FUNCTION__, (int)lockid);
			pthread_mutex_t *lock = GetLock(lockid);
			if(lock)
				pthread_mutex_lock(lock);
		}
		void Unlock(lock_id_type lockid){
			LOGDEBUG("in %s %d",__FUNCTION__, (int)lockid);
			pthread_mutex_t *lock = GetLock(lockid);
			if(lock)
				pthread_mutex_unlock(lock);
		}
	private:
		pthread_mutex_t* GetLock(lock_id_type lockid){
			ScopedMutex mtx(&gl_mtx);
			for(int i = 0; i < b_occupied; i++){
				if(b_ids[i] == lockid)
					return &b_locks[i];
			}
			if(b_occupied == b_capacity){
				LOGERROR("run out of lock buffer");
				return NULL;
			}
			b_ids[b_occupied] = lockid;
			pthread_mutex_init(&b_locks[b_occupied], NULL);
			b_occupied++;
			return &b_locks[b_occupied-1];
		}
		int b_capacity;
		int b_occupied;
		lock_id_type *b_ids;
		pthread_mutex_t *b_locks;
		pthread_mutex_t gl_mtx;
};

#endif
