#include "ByteBuffer.h"
#include "string.h"

ByteBuffer::ByteBuffer(int init_size):_capacity(init_size),_used(0){
	InitMutex();
	_buffer = new char[init_size];
}
ByteBuffer::ByteBuffer():_capacity(1024),_used(0){
	InitMutex();
	_buffer = new char[1024];
}
ByteBuffer::~ByteBuffer(){
	CleanMutex();
	if(_buffer)
		delete []_buffer;
}
void ByteBuffer::InitMutex(){
	_mutex=new pthread_mutex_t;
	pthread_mutex_init(_mutex,NULL);
}
void ByteBuffer::CleanMutex(){
	if(_mutex)
		delete _mutex;
	_mutex=NULL;
}

int ByteBuffer::IncreaseCapacity(int newsize){
	if(newsize < _capacity)
		return -1;
	char* newbuf = new char[newsize];
	strncpy(newbuf,_buffer,_used);
	delete []_buffer;
	_buffer=newbuf;
}
char* ByteBuffer::GetBuffer(int size){
	pthread_mutex_lock(_mutex);
	if(_used+size>_capacity){
		int tmp = _capacity;
		while(tmp<_used+size)
			tmp*=2;
		IncreaseCapacity(tmp);
	}
	char* result = _buffer+_used;
	_used+=size;
	pthread_mutex_unlock(_mutex);
	return result;
}
void ByteBuffer::ReturnBuffer(char* start, int size){
	pthread_mutex_lock(_mutex);
	int index = start-_buffer;
	for(int i = index+size; i < _used; i++){
		_buffer[i-size] = _buffer[i];
	}
	_used-=size;
	pthread_mutex_unlock(_mutex);
}
