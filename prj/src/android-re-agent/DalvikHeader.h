#ifndef _DALVIKHEADER_H_
#define _DALVIKHEADER_H_
#include <Dalvik.h>
#include <JniInternal.h>
#include <cutils/log.h>

#define MY_LOG_TAG "HAIYANG"

#define ASSERT(cond, ...) do{if(!cond) ALOG(LOG_ERROR,MY_LOG_TAG,__VA_ARGS__);exit(-1);}while(0)

#define ERROR(...) do{ALOG(LOG_ERROR,MY_LOG_TAG,__VA_ARGS__);}while(0)

#define DEBUG(...) do{ALOG(LOG_DEBUG,MY_LOG_TAG,__VA_ARGS__);}while(0)

#endif
