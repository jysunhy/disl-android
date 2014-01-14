#include <stdio.h>
#include "stdlib.h"
#include "stdio.h"
#include <jni.h>
#include <pthread.h>
#include "DalvikHeader.h"
#include "Socket.h"

Socket *sock = NULL;

void testCoverage
(JNIEnv * jni_env, jclass this_class, jint pid) {
	ALOG(LOG_INFO,"COVERAGE","PID: %d", pid);
}

void mapPID
(JNIEnv * jni_env, jclass this_class, jstring pname, jint pid) {
	//remote.OpenConnection();
	jsize str_len = jni_env->GetStringUTFLength(pname);
	const char * str = 	jni_env->GetStringUTFChars(pname, NULL);
	ALOG(LOG_INFO,"SHADOW","PID: %d NAME: %s", pid, str);
	/*
	Socket *tmpsock = new Socket();
	while(!tmpsock->Connect()){
		DEBUG("Cannot connect through UDS");
		sleep(2);
	}
	int signal = -4;
	tmpsock->Send((char*)&signal,sizeof(int));
	if(str_len > 1023)
		signal = 1023;
	else
		signal = str_len;
	tmpsock->Send((char*)&signal,sizeof(int));
	tmpsock->Send(str, signal);
	signal = pid;
	tmpsock->Send((char*)&signal,sizeof(int));
	jni_env->ReleaseStringUTFChars(pname,str);
	delete tmpsock;
*/
}

static const char *classPathName = "ch/usi/dag/dislre/ALocalDispatch";

static JNINativeMethod methods[]= {
	{"mapPID", "(Ljava/lang/String;I)V", (void*)mapPID},
	{"testCoverage", "(I)V", (void*)testCoverage},
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
