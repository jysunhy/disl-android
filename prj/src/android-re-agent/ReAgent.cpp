#include <stdio.h>
#include "Common.h"
#include "ReProtocol.h"

ReProtocol remote("192.168.1.103",7777);

jint add(JNIEnv *env, jobject thiz, jint x, jint y){
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	return x + y + 1000;
}

// ******************* REDispatch methods *******************

jshort registerMethod
(JNIEnv * jni_env, jclass this_class, jstring analysis_method_desc) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	    jsize str_len = jni_env->GetStringUTFLength(analysis_method_desc);
		const char * str = 	jni_env->GetStringUTFChars(analysis_method_desc, NULL);
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, str, str_len);
	return 0;
	//return register_method(jni_env, analysis_method_desc, tld_get()->id);
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, 0, analysis_method_id);
	//analysis_start(jni_env, analysis_method_id, tld_get());
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
		jbyte ordering_id) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, ordering_id, analysis_method_id);
	//analysis_start_buff(jni_env, analysis_method_id, ordering_id, tld_get());
}

void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisEndEvent(dvmThreadSelf()->threadId);
	//analysis_end(tld_get());
}

void sendBoolean
(JNIEnv * jni_env, jclass this_class, jboolean to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJboolean(dvmThreadSelf()->threadId, to_send);
	//pack_boolean(tld_get()->analysis_buff, to_send);
}

void sendByte
(JNIEnv * jni_env, jclass this_class, jbyte to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJbyte(dvmThreadSelf()->threadId, to_send);

	//pack_byte(tld_get()->analysis_buff, to_send);
}

void sendChar
(JNIEnv * jni_env, jclass this_class, jchar to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJchar(dvmThreadSelf()->threadId, to_send);

	//pack_char(tld_get()->analysis_buff, to_send);
}

void sendShort
(JNIEnv * jni_env, jclass this_class, jshort to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJshort(dvmThreadSelf()->threadId, to_send);

	//pack_short(tld_get()->analysis_buff, to_send);
}

void sendInt
(JNIEnv * jni_env, jclass this_class, jint to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJint(dvmThreadSelf()->threadId, to_send);

	//pack_int(tld_get()->analysis_buff, to_send);
}

void sendLong
(JNIEnv * jni_env, jclass this_class, jlong to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJlong(dvmThreadSelf()->threadId, to_send);

	//pack_long(tld_get()->analysis_buff, to_send);
}

void sendFloat
(JNIEnv * jni_env, jclass this_class, jfloat to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJfloat(dvmThreadSelf()->threadId, to_send);

	//pack_float(tld_get()->analysis_buff, to_send);
}

void sendDouble
(JNIEnv * jni_env, jclass this_class, jdouble to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJdouble(dvmThreadSelf()->threadId, to_send);

	//pack_double(tld_get()->analysis_buff, to_send);
}

void sendObject
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	//TODO
	//struct tldata * tld = tld_get ();
	//pack_object(jni_env, tld->analysis_buff, tld->command_buff, to_send,
	//		OT_OBJECT);
}

void sendObjectPlusData
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ALOG(LOG_INFO,"HAIYANG","in shadowvm native %s", __FUNCTION__);

	//TODO
	//struct tldata * tld = tld_get ();
	//pack_object(jni_env, tld->analysis_buff, tld->command_buff, to_send,
	//		OT_DATA_OBJECT);
}

static const char *classPathName = "ch/usi/dag/dislre/REDispatch";

static JNINativeMethod methods[]= {
	{"add", "(II)I", (void*)add},
	{"registerMethod", "(Ljava/lang/String;)S", (void*)registerMethod},
	{"analysisStart", "(S)V", (void*)analysisStart__S},
	{"analysisStart", "(SB)V", (void*)analysisStart__SB},
	{"analysisEnd", "()V", (void*)analysisEnd},
	{"sendBoolean", "(Z)V", (void*)sendBoolean},
	{"sendByte", "(B)V", (void*)sendByte},
	{"sendChar", "(C)V", (void*)sendChar},
	{"sendShort", "(S)V", (void*)sendShort},
	{"sendInt", "(I)V", (void*)sendInt},
	{"sendLong", "(J)V", (void*)sendLong},
	{"sendFloat", "(F)V", (void*)sendFloat},
	{"sendDouble", "(D)V", (void*)sendDouble},
	{"sendObject", "(Ljava/lang/Object;)V", (void*)sendObject},
	{"sendObjectPlusData", "(Ljava/lang/Object;)V", (void*)sendObjectPlusData},
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

void testShadowHook(Object* obj){
	ALOG(LOG_INFO,"HAIYANG","in shadow hook %llu", obj->uuid);
	//remote.ObjFreeEvent(obj->uuid);
}

jint JNI_OnLoad(JavaVM* vm, void* reserved){

	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	gDvm.shadowHook = &testShadowHook;

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
