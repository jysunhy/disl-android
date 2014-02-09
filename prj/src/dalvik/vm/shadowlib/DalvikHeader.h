#ifndef _DALVIKHEADER_H_
#define _DALVIKHEADER_H_
#include <Dalvik.h>
#include <JniInternal.h>
#include <cutils/log.h>

#define MY_LOG_TAG "SHADOW"

#define LOGASSERT(cond, ...) do{if(!cond) ALOG(LOG_ERROR,MY_LOG_TAG,__VA_ARGS__);}while(0)

#define LOGERROR(...) do{ALOG(LOG_ERROR,MY_LOG_TAG,__VA_ARGS__);}while(0)

#define LOGDEBUG(...) do{ALOG(LOG_DEBUG,MY_LOG_TAG,__VA_ARGS__);}while(0)

#endif
