#ifndef _REQUEUE_H_
#define _REQUEUE_H_

#include "Common.h"
using namespace std;

#define DEFAULT_QUEUE_SIZE 1024

class ReQueue {
	public:
		ReQueue(){
			ReQueue(DEFAULT_QUEUE_SIZE);
		}
		ReQueue(int capacity):q_capacity(capacity),q_occupied(0){
			q_data = new char[capacity];
		}
		~ReQueue(){
			delete []q_data;
			q_data = NULL;
		}
		bool EnqueueJbype(jbyte data){
			return Enqueue(&data, sizeof(jbyte));
		}
		bool EnqueueJshort(jshort data){
			return Enqueue(&data, sizeof(jshort));
		}
		bool EnqueueJlong(jlong data){
			return Enqueue(&data, sizeof(jlong));
		}
		bool Enqueue(const char* data, int length){
			if(length > q_capacity - q_occupied) {
				return false;
			}else{
				memcpy(q_data+q_occupied, data, length);
				q_occupied+=length;
			}
			return true;
		}
		bool IsEmpty(){
			return q_occupied == 0;
		}
		void Reset(){
			q_occupied = 0;
		}
		void GetData(char* &data, int &length){
			data = &q_data;
			length = q_occupied;
		}

	private:
		//Socket *q_socket;
		int q_capacity;
		//int q_threshold;
		//int q_start;
		int q_occupied;
		char* q_data;
};

#endif
