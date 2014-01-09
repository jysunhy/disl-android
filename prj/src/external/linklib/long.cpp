#define LOG_TAG "LongTest2 long.cpp"
#define ERR_PREFIX "ERR"
#include <utils/Log.h>
#include <cutils/log.h>
#include <stdio.h>
#include "jni.h"
#include <string>
#include "netref.h"
#include "Dalvik.h"
#include "JniInternal.h"


jint add(JNIEnv *env, jobject thiz, jint x, jint y){
	ALOG(LOG_INFO,"SHADOW","ssss native jni in add %d", x+y);
	return x + y;
}

jint substraction(JNIEnv *env, jobject thiz, jint x, jint y){

	return x - y;
}

jfloat multiplication(JNIEnv *env, jobject thiz, jint x, jint y){

	return (float)x * (float)y;
}

jfloat division(JNIEnv *env, jobject thiz, jint x, jint y){
	return (float)x/(float)y;
}

static const char *classPathName = "com/inspur/test2/MainActivity";

static JNINativeMethod methods[]= {
	
	{"add", "(II)I", (void*)add},
	{"substraction", "(II)I", (void*)substraction},
	{"multiplication", "(II)F", (void*)multiplication},
	{"division", "(II)F", (void*)division},
};


typedef union{
	JNIEnv* env;
	void* venv;
}UnionJNIEnvToVoid;



static int registerNativeMethods(JNIEnv* env, const char* className,
	JNINativeMethod* gMethods, int numMethods){

	jclass clazz;
	clazz = env->FindClass(className);

	if (clazz == NULL)
		return JNI_FALSE;
	if (env->RegisterNatives(clazz, gMethods, numMethods)<0)
		return JNI_FALSE;
	return JNI_TRUE;

}


static int registerNatives(JNIEnv *env){

	if (!registerNativeMethods(env, classPathName,
		methods, sizeof(methods)/sizeof(methods[0])))
	{
		return JNI_FALSE;
	}

	return JNI_TRUE;

}



jint JNI_OnLoad(JavaVM* vm, void* reserved){

	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}
	
	env = uenv.env;

	env = uenv.env;

	if (registerNatives(env) != JNI_TRUE){

		goto bail;
	}

	result = JNI_VERSION_1_4;

bail:
	return result;
}
