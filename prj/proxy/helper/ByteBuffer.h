#ifndef _BYTE_BUFFER_H_
#define _BYTE_BUFFER_H_

#include <pthread.h>
#include <sys/mman.h>
#include <sys/types.h>

class ByteBuffer{
public:
	ByteBuffer(int init_size);
	ByteBuffer();
	~ByteBuffer();
	char* GetBuffer(int size);
	void ReturnBuffer(char* start, int size);
private:
	void InitMutex();
	void CleanMutex();
	int IncreaseCapacity(int newsize);
	pthread_mutex_t *_mutex;
	char *_buffer;
	int _capacity;
	int _used;
};

#endif
