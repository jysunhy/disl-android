#include "ShadowAPI.h"

#include "Dalvik.h"
#include <stdlib.h>
#include "oo/Object.h"

long GetObjectId(Thread* self, jobject jo){
	return dvmDecodeIndirectRef(self, jo)->uuid;
}

int test(){
	return 123;
}
//Object* GetObject(jobject *jo){
//	return NULL;
//}
