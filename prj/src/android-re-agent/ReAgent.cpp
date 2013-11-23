#include <stdio.h>
#include "Common.h"
#include "ReProtocol.h"
#include "Netref.h"
#include "stdlib.h"
#include "stdio.h"

ReProtocol remote("/dev/socket/instrument",11218);

pthread_mutex_t gl_mtx;
static volatile jint ot_class_id = 1;
static volatile jlong ot_object_id = 1;
static volatile jshort method_id = 1;

jint add(JNIEnv *env, jobject thiz, jint x, jint y){
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	return x + y + 1000;
}

// ******************* REDispatch methods *******************

jshort registerMethod
(JNIEnv * jni_env, jclass this_class, jstring analysis_method_desc) {
	    jsize str_len = jni_env->GetStringUTFLength(analysis_method_desc);
		const char * str = 	jni_env->GetStringUTFChars(analysis_method_desc, NULL);
	char tmp[50];
	int tmpsize = str_len<50?str_len:49;
	memcpy(tmp, str, tmpsize);
	tmp[tmpsize] = 0;
	ALOG(LOG_INFO,"HAIYANG","EVENT:register method %s", tmp);
	pthread_mutex_lock(&gl_mtx);
	method_id++;
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, method_id, str, str_len);
	jni_env->ReleaseStringUTFChars(analysis_method_desc,str);
	pthread_mutex_unlock(&gl_mtx);
	return 0;
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, 0, analysis_method_id);
	//analysis_start(jni_env, analysis_method_id, tld_get());
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
		jbyte ordering_id) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, ordering_id, analysis_method_id);
	//analysis_start_buff(jni_env, analysis_method_id, ordering_id, tld_get());
}

void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.AnalysisEndEvent(dvmThreadSelf()->threadId);
	//analysis_end(tld_get());
}

void sendBoolean
(JNIEnv * jni_env, jclass this_class, jboolean to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJboolean(dvmThreadSelf()->threadId, to_send);
	//pack_boolean(tld_get()->analysis_buff, to_send);
}

void sendByte
(JNIEnv * jni_env, jclass this_class, jbyte to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJbyte(dvmThreadSelf()->threadId, to_send);

	//pack_byte(tld_get()->analysis_buff, to_send);
}

void sendChar
(JNIEnv * jni_env, jclass this_class, jchar to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJchar(dvmThreadSelf()->threadId, to_send);

	//pack_char(tld_get()->analysis_buff, to_send);
}

void sendShort
(JNIEnv * jni_env, jclass this_class, jshort to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJshort(dvmThreadSelf()->threadId, to_send);

	//pack_short(tld_get()->analysis_buff, to_send);
}

void sendInt
(JNIEnv * jni_env, jclass this_class, jint to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJint(dvmThreadSelf()->threadId, to_send);

	//pack_int(tld_get()->analysis_buff, to_send);
}

void sendLong
(JNIEnv * jni_env, jclass this_class, jlong to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJlong(dvmThreadSelf()->threadId, to_send);

	//pack_long(tld_get()->analysis_buff, to_send);
}

void sendFloat
(JNIEnv * jni_env, jclass this_class, jfloat to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJfloat(dvmThreadSelf()->threadId, to_send);

	//pack_float(tld_get()->analysis_buff, to_send);
}

void sendDouble
(JNIEnv * jni_env, jclass this_class, jdouble to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	remote.SendJdouble(dvmThreadSelf()->threadId, to_send);

	//pack_double(tld_get()->analysis_buff, to_send);
}

jlong SetAndGetNetref(Object* obj);
jlong newClass(ClassObject *obj){
	if(obj == NULL)  {
		return 0;
	}
	obj->uuid = _set_net_reference(ot_object_id++,ot_class_id++,1,1);
	remote.NewClassInfo(obj->uuid, obj->descriptor, strlen(obj->descriptor), "", 0, SetAndGetNetref(obj->classLoader), SetAndGetNetref(obj->super));
	ALOG(LOG_DEBUG,"HAIYANG","new class fond %lld:%s",obj->uuid, obj->descriptor);
	return obj->uuid;
}

jlong SetAndGetNetref(Object* obj){
	jlong res;
	if(obj == NULL) //to_send is null or is weak reference which has already been cleared
	{
		res = 0;
	}else if(obj->uuid != 0){
		res = obj->uuid;
	}else if(dvmIsClassObject(obj)){
		res = newClass((ClassObject*)obj);
	}else {
		//ASSERT(obj->clazz != NULL,"object class is null");
		if(obj->clazz->uuid == 0){ //its class not registered
			newClass(obj->clazz);
		}
		obj->uuid = _set_net_reference(ot_object_id++,obj->clazz->uuid,0,0);
		res = obj->uuid;
		ALOG(LOG_DEBUG,"HAIYANG","new object fond %lld, instance of %s",obj->uuid, obj->clazz->descriptor);
	}
	return res;
}

void sendObject
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ScopedMutex mtx(&gl_mtx);
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);
	//TODO
	//struct tldata * tld = tld_get ();
	//pack_object(jni_env, tld->analysis_buff, tld->command_buff, to_send,
	//		OT_OBJECT);
	
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	//Object* obj2 = dvmDecodeIndirectRef(self, to_send);
	//ALOG(LOG_DEBUG,"HAIYANG","after set %lld",obj2->uuid);
	if(obj == NULL){
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s NULL", __FUNCTION__);
		remote.SendJobject(self->threadId, 0);
		return;
	}
	if(obj->clazz == gDvm.classJavaLangString){
		int len=((StringObject*)obj)->length();
		int utflen = ((StringObject*)obj)->utfLength();
		const u2* tmp = ((StringObject*)obj)->chars();
		const jchar* content = (jchar*)tmp;
		remote.SendStringObject(self->threadId, obj->uuid, content, len);
		ALOG(LOG_DEBUG,"HAIYANG","in %s string object %d %d %s", __FUNCTION__, len, utflen, (const char*)content);
		ALOG(LOG_DEBUG,"HAIYANG","in %s string object %d %d %s", __FUNCTION__, len, utflen, (const char*)(content + 1));
		ALOG(LOG_DEBUG,"HAIYANG","in %s string object %d %d %s", __FUNCTION__, len, utflen, (const char*)(content + 2));
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		ALOG(LOG_DEBUG,"HAIYANG","in %s thread object", __FUNCTION__);
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		//char tmp[16]="abcd";
		//myitoa(self->threadId, tmp, 10);
		//remote.SendThreadObject(self->threadId, obj->uuid, tmp, strlen(tmp), isDaemon);
	}

	remote.SendJobject(self->threadId, netref);
}

void sendObjectPlusData
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ALOG(LOG_DEBUG,"HAIYANG","in shadowvm native %s", __FUNCTION__);

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

void objNewHook(Object* obj){
	//SetAndGetNetref(obj);
	//ALOG(LOG_DEBUG,"HAIYANG","in hook %s %s %lld", __FUNCTION__, obj->clazz->descriptor, obj->uuid);
}
void threadEndHook(Thread* self){
	bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
	ALOG(LOG_DEBUG,"HAIYANG","in hook %s %d %s", __FUNCTION__, self->threadId, isDaemon?"daemon":"not daemon");
}
void vmEndHook(JavaVM* vm){
	ALOG(LOG_DEBUG,"HAIYANG","in hook %s", __FUNCTION__);
}
void objFreeHook(Object* obj, Thread* self){
	ALOG(LOG_DEBUG,"HAIYANG","in hook %s %lld", obj->clazz->descriptor, obj->uuid);
	//remote.ObjFreeEvent(obj->uuid);
}

jint JNI_OnLoad(JavaVM* vm, void* reserved){

	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//gDvm.shadowHook = &testShadowHook;
	gDvm.newObjHook = &objNewHook;
	gDvm.freeObjHook = &objFreeHook;
	gDvm.threadEndHook = &threadEndHook;
	gDvm.vmEndHook = &vmEndHook;

	pthread_mutex_init(&gl_mtx, NULL);

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
