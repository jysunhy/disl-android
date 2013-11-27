#include <stdio.h>
#include "Common.h"
#include "DalvikHeader.h"
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
	//pthread_mutex_lock(&gl_mtx);
	method_id++;
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, method_id, str, str_len);
	jni_env->ReleaseStringUTFChars(analysis_method_desc,str);
	//pthread_mutex_unlock(&gl_mtx);
	return method_id;
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	ALOG(LOG_INFO,"HAIYANG","EVENT: analysis start for method %d", (int)analysis_method_id);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, 0, analysis_method_id);
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
		jbyte ordering_id) {
	ALOG(LOG_INFO,"HAIYANG","EVENT: analysis start for method %d with ordering %d", (int)analysis_method_id, (int)ordering_id);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, ordering_id, analysis_method_id);
}

void manuallyClose
(JNIEnv * jni_env, jclass this_class) {
	ALOG(LOG_INFO,"HAIYANG","EVENT: close connection");
	remote.ConnectionClose();
}
void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	ALOG(LOG_INFO,"HAIYANG","EVENT: analysis end for method");
	remote.AnalysisEndEvent(dvmThreadSelf()->threadId);
}

void sendBoolean
(JNIEnv * jni_env, jclass this_class, jboolean to_send) {
	remote.SendJboolean(dvmThreadSelf()->threadId, to_send);
}

void sendByte
(JNIEnv * jni_env, jclass this_class, jbyte to_send) {
	remote.SendJbyte(dvmThreadSelf()->threadId, to_send);
}

void sendChar
(JNIEnv * jni_env, jclass this_class, jchar to_send) {
	remote.SendJchar(dvmThreadSelf()->threadId, to_send);
}

void sendShort
(JNIEnv * jni_env, jclass this_class, jshort to_send) {
	remote.SendJshort(dvmThreadSelf()->threadId, to_send);
}

void sendInt
(JNIEnv * jni_env, jclass this_class, jint to_send) {
	remote.SendJint(dvmThreadSelf()->threadId, to_send);
}

void sendLong
(JNIEnv * jni_env, jclass this_class, jlong to_send) {
	remote.SendJlong(dvmThreadSelf()->threadId, to_send);
}

void sendFloat
(JNIEnv * jni_env, jclass this_class, jfloat to_send) {
	remote.SendJfloat(dvmThreadSelf()->threadId, to_send);
}

void sendDouble
(JNIEnv * jni_env, jclass this_class, jdouble to_send) {
	remote.SendJdouble(dvmThreadSelf()->threadId, to_send);
}

jlong SetAndGetNetref(Object* obj);
jlong newClass(ClassObject *obj){
	if(obj == NULL)  {
		return 0;
	}
	jlong superid = SetAndGetNetref(obj->super);
	jlong loaderid = SetAndGetNetref(obj->classLoader);
	obj->uuid = _set_net_reference(ot_object_id++,ot_class_id++,1,1);
	char tmp;
	ALOG(LOG_DEBUG,"HAIYANG","NEW class found %s id %d, loaderid: %lld, superid: %lld",obj->descriptor, ot_class_id-1, loaderid, superid);
	remote.NewClassEvent(obj->descriptor, strlen(obj->descriptor), loaderid, 0, &tmp);
	remote.NewClassInfo(obj->uuid, obj->descriptor, strlen(obj->descriptor), "", 0, loaderid, superid);
	//ALOG(LOG_DEBUG,"HAIYANG","NEW class found %lld:%s",obj->uuid, obj->descriptor);
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
		obj->uuid = _set_net_reference(ot_object_id++,net_ref_get_class_id(obj->clazz->uuid),0,0);
		res = obj->uuid;
		ALOG(LOG_DEBUG,"HAIYANG","NEW object found tag:%lld, instance of %s classid %d",obj->uuid, obj->clazz->descriptor,net_ref_get_class_id(obj->uuid));
	}
	return res;
}

void sendObject
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
		ALOG(LOG_DEBUG,"HAIYANG","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	/*
	if(obj->clazz == gDvm.classJavaLangString){
		int len=((StringObject*)obj)->length();
		int utflen = ((StringObject*)obj)->utfLength();
		const u2* tmp = ((StringObject*)obj)->chars();
		const jchar* content = (jchar*)tmp;
		ALOG(LOG_DEBUG,"HAIYANG","send string object");
		remote.SendStringObject(self->threadId, obj->uuid, content, len);
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		ALOG(LOG_DEBUG,"HAIYANG","send thread object");
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		//char tmp[16]="abcd";
		//myitoa(self->threadId, tmp, 10);
		//remote.SendThreadObject(self->threadId, obj->uuid, tmp, strlen(tmp), isDaemon);
	}
	*/
	remote.SendJobject(self->threadId, netref);
}

void sendObjectPlusData
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	//TODO
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
		ALOG(LOG_DEBUG,"HAIYANG","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	if(obj->clazz == gDvm.classJavaLangString){
		int len=((StringObject*)obj)->length();
		int utflen = ((StringObject*)obj)->utfLength();
		const u2* tmp = ((StringObject*)obj)->chars();
		//const jchar* content = (jchar*)tmp;
		char* str = new char[utflen+1];
		for(int i = 0; i < utflen; i++){
			str[i] = tmp[i];
		}
		str[utflen] = 0;
		ALOG(LOG_DEBUG,"HAIYANG","send string object %s, %d, %d",str, len, utflen);

		remote.SendStringObject(self->threadId, obj->uuid, str, utflen);
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		ALOG(LOG_DEBUG,"HAIYANG","send thread object");
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		bool isDaemon = false;
		char tmp[16]="abcd";
		//myitoa(self->threadId, tmp, 10);
		remote.SendThreadObject(self->threadId, obj->uuid, tmp, strlen(tmp), isDaemon);
	}

	remote.SendJobject(self->threadId, netref);
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
	{"manuallyClose", "()V", (void*)manuallyClose},
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
	//ALOG(LOG_DEBUG,"HAIYANG","in FREE hook %s %lld", obj->clazz->descriptor, obj->uuid);
	//remote.ObjFreeEvent(obj->uuid);
}
int classfileLoadHook(const char* name, int len){
	ALOG(LOG_DEBUG,"HAIYANG","LOADING CLASS %s", name);
	return 1;
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
	gDvm.classfileLoadHook = &classfileLoadHook;

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
