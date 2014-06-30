#ifndef _BUFFER_H_
#define _BUFFER_H_

#define BUF_SIZE 1024

class Buffer{
	public:
		Buffer(int capacity=BUF_SIZE):buf_capacity(capacity),buf_occupied(0){
			buf_data = new char[capacity];
		}
		~Buffer(){
			delete []buf_data;
			buf_data = NULL;
		}
		void Push(const char* data, int len){
			if(buf_occupied+len > buf_capacity){
				DoubleCapacity();
				Push(data,len);
			}else{
				memcpy(buf_data+buf_occupied, data, len);
				buf_occupied += len;
			}
		}
	private:
		void DoubleCapacity(){
			char* new_buf = new char[buf_capacity*2];
			memcpy(new_buf, buf_data, buf_occupied);
			delete []buf_data;
			buf_data = new_buf;
			buf_capacity *= 2;
		}
		char* buf_data;
		char* buf_capacity;
		char* buf_occupied;
};

#endif
