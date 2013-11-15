#ifndef _REQUEUE_H_
#define _REQUEUE_H_

#include "Common.h"
using namespace std;

#define DEFAULT_QUEUE_SIZE 1024

union float_jint {
	float f;
	jint i;
};

union double_jlong {
	double d;
	jlong l;
};

class BaseQueue{
	public:
		BaseQueue(){
			BaseQueue(DEFAULT_QUEUE_SIZE);
		}
		BaseQueue(int capacity):q_capacity(capacity),q_occupied(0){
			q_data = new char[capacity];
		}
		virtual ~BaseQueue(){
			delete []q_data;
			q_data = NULL;
		}
		int EnqueueJboolean(jboolean data){
			return Enqueue((char*)&data, sizeof(jboolean));
		}
		int EnqueueJbype(jbyte data){
			return Enqueue((char*)&data, sizeof(jbyte));
		}
		int EnqueueJchar(jchar data){
			jchar nts = htons(data);
			return Enqueue((char*)&nts, sizeof(jchar));
		}
		int EnqueueJshort(jshort data){
			jshort nts = htons(data);
			return Enqueue((char*)&nts, sizeof(jshort));
		}
		int EnqueueJint(jint data){
			jint nts = htons(data);
			return Enqueue((char*)&nts, sizeof(jint));
		}
		int EnqueueJlong(jlong data){
			jlong nts = htobe64(data);
			return Enqueue((char*)&nts, sizeof(jlong));
		}
		int EnqueueJfloat(jfloat data){
			union float_jint convert;
			convert.f = data;
			return EnqueueJint(convert.i);
		}
		int EnqueueJdouble(jdouble data){
			union double_jlong convert;
			convert.d = data;
			return EnqueueJlong(convert.l);
		}
		int EnqueueStringUtf8(const char* string_utf8, uint16_t size_in_bytes){
			uint16_t nsize = htons(size_in_bytes);
			return Enqueue((char*)&nsize, sizeof(uint16_t))+Enqueue((char*)string_utf8, size_in_bytes);
		}

		virtual int Enqueue(const char* data, int length){
			return 0;
		}
		bool IsEmpty(){
			return q_occupied == 0;
		}
		virtual void Reset(){
			q_occupied = 0;
		}
		virtual void GetData(char* &data, int &length){
			data = q_data;
			length = q_occupied;
		}

	protected:
		int q_capacity;
		int q_occupied;
		char* q_data;
};

class ReQueue: public BaseQueue{
	public:
		ReQueue():BaseQueue(){}
		ReQueue(int capacity):BaseQueue(capacity){}
		virtual int Enqueue(const char* data, int length){
			if(length > q_capacity - q_occupied) {
				return false;
			}else{
				memcpy(q_data+q_occupied, data, length);
				q_occupied+=length;
			}
			return true;
		}
};

class Buffer: public BaseQueue{
	public:
		Buffer():BaseQueue(){}
		Buffer(int capacity):BaseQueue(capacity){}
		virtual int Enqueue(const char* data, int length){
			if(q_occupied + length > q_capacity){
				DoubleCapacity();
				return Enqueue(data, length);
			}else{
				memcpy(q_data + q_occupied, data, length);
				q_occupied += length;
				return length;
			}
		}
	private:
		void DoubleCapacity(){
			char* new_buf = new char[q_capacity*2];
			memcpy(new_buf, q_data, q_occupied);
			delete []q_data;
			q_data = new_buf;
			q_capacity *= 2;
		}
};

#endif
