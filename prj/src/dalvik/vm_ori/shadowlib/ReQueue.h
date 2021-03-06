#ifndef _REQUEUE_H_
#define _REQUEUE_H_

#include "ShadowLib.h"
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
			q_capacity = DEFAULT_QUEUE_SIZE;
			q_occupied = 0;
			q_data = new char[q_capacity];
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
		int EnqueueJbyte(jbyte data){
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
			jint nts = htonl(data);
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
		virtual bool Update(int pos, const char* input, int len){
			if(pos+len > q_occupied){
				return false;
			}
			memcpy(q_data+pos, input, len);
			return true;
		}
		virtual void Print(){
			LOGDEBUG("Print Queue");
			for(int i = 0; i < q_occupied; i++)
				LOGDEBUG("\t %d:%d",i, (int)q_data[i]);
		}

		int q_capacity;
		int q_occupied;
		char* q_data;
};

class ReQueue: public BaseQueue{
	public:
		ReQueue():BaseQueue(){ event_count = 0; }
		ReQueue(int capacity):BaseQueue(capacity){ event_count = 0; }
		virtual int Enqueue(const char* data, int length){
			//ALOG(LOG_DEBUG,"SHADOW","in %s, %d %d %d pushed back to queue", __FUNCTION__, length, q_capacity, q_occupied);
			if(length > q_capacity - q_occupied) {
				return false;
			}else{
				memcpy(q_data+q_occupied, data, length);
				q_occupied+=length;
			}
			return true;
		}
		virtual void Reset(){
			q_occupied = 0;
			event_count = 0;
		}
		jint event_count; // count how many events accumulated
};

class Buffer: public BaseQueue{
	public:
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
		Buffer* Duplicate(){
			Buffer* res = new Buffer(q_capacity);
			memcpy(res->q_data, q_data, q_occupied);
			return res;
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
