#ifndef _RE_MAP_H_
#define _RE_MAP_H_

#include <pthread.h>
#include "Common.h"

#define DEFAULT_MAP_SIZE 5

template<class K, class V>
class Map{
	public:
		Map(int capacity=DEFAULT_MAP_SIZE):b_capacity(capacity),b_occupied(0){
			b_keys = new K[capacity];
			b_values = new V[capacity];
			pthread_mutex_init(&gl_mtx,NULL);
		}
		~Map(){
			delete []b_keys;
			delete []b_values;
			pthread_mutex_destroy(&gl_mtx);
		}
		bool Exist(const K &key){
			ScopedMutex mtx(&gl_mtx);
			return _Exist(key);
		}
		V& Get(const K &key){
			ScopedMutex mtx(&gl_mtx);
			return _Get(key);
		}
		void Set(const K &key, const V &value) {
			ScopedMutex mtx(&gl_mtx);
			_Set(key, value);
		}
		V& operator[](const K& key){
			ScopedMutex mtx(&gl_mtx);
			return _Get(key);
		}
	private:
		bool _Exist(const K& key){
			for(int i = 0; i < b_occupied; i++){
				if(b_keys[i] == key)
					return true;
			}
			return false;
		}
		V& _Get(const K &key){
			for(int i = 0; i < b_occupied; i++){
				if(b_keys[i] == key)
					return b_values[i];
			}
			ERROR("should check exist before  use Get ");
			//return NULL;
		}
		void _Set(const K &key, const V &value) {
			if(Exist(key)){
				V &res = _Get(key);
				res = value;
				return;
			}
			if(b_occupied == b_capacity){
				_DoubleCapacity();
			}
			b_keys[b_occupied] = key;
			b_values[b_occupied] = value;
			b_occupied++;
			return;
		}
		void _DoubleCapacity(){
			K* new_keys = new K[b_capacity*2];
			V* new_values = new V[b_capacity*2];
			memcpy(new_keys, b_keys, b_occupied * sizeof(K));
			memcpy(new_values, b_values, b_occupied * sizeof(V));
			delete []b_keys;
			delete []b_values;
			b_keys = new_keys;
			b_values = new_values;
			b_capacity *= 2;
		}
		int b_capacity;
		int b_occupied;
		K *b_keys;
		V *b_values;
		pthread_mutex_t gl_mtx;
};

#endif
