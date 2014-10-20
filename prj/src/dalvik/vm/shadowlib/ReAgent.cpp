#include <stdio.h>
#include "ShadowLib.h"
#include "DalvikHeader.h"
#include "ReProtocol.h"
#include "Netref.h"
#include "stdlib.h"
#include "stdio.h"
#include "Socket.h"
#include <sched.h>
#include <cutils/sockets.h>

#include <sys/types.h>      //  
#include <netinet/in.h>     //  
#include <sys/socket.h>     // for socket system calls
#include <arpa/inet.h>      // for socket system calls (bind)
#include <sched.h>
#include <pthread.h>        /* P-thread implementation        */
#include <signal.h>     /* for signal                     */
#include <semaphore.h>      /* for p-thread semaphores        */
#include <sys/un.h>


#define SERVER_IP "127.0.0.1"

#define MAX_BUFFER 100000
#define UNIX_PATH_MAX 108

jlong SetAndGetNetref(Object* obj);
int64_t getTimeNsec();

int gl_svm_socket = -1;

int get_socket(){
	if(gl_svm_socket > 0){
		return gl_svm_socket;
	}
	struct sockaddr_un address;

	int client_socket = socket(PF_UNIX, SOCK_STREAM, 0);
	if(client_socket < 0)
	{
		ALOG(LOG_INFO,"EPOLL","CL: Create Socket Failed! %d",errno);
		return -1;
	}

	/* start with a clean address structure */
	memset(&address, 0, sizeof(struct sockaddr_un));

	address.sun_family = AF_UNIX;
	snprintf(address.sun_path, UNIX_PATH_MAX, "/dev/socket/epoll_sock");

	void * tmp = &address;

	if(connect(client_socket, 
				(struct sockaddr *) tmp,  
				sizeof(struct sockaddr_un)) != 0)
	{
		ALOG(LOG_INFO,"EPOLL","CL: Connect Socket Failed! %d",errno);
		client_socket = -1;
		return -1;
	}
	gl_svm_socket = client_socket;
	return client_socket;
}

int forward_to_svm(char* buff, int len){
	int esock = -1;
	while(esock <=0 ) {
		esock = get_socket();
		sleep(2);
	}
	int pid;
	memcpy(&pid, buff, sizeof(int));
	pid = htonl(pid);
	int retcode = send(esock, buff, len, 0);
	if(retcode != len){
		ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND 2 : %d", errno);
	}
	return true;
}

static ReProtocol remote("/dev/socket/instrument",11218);

static bool isZygote = true;
char curpname[100] ={0};

static pthread_mutex_t gl_mtx = PTHREAD_MUTEX_INITIALIZER;
static volatile jint ot_class_id = 1;
static volatile jlong ot_object_id = 1;
static volatile jshort method_id = 0;

Socket *sock = NULL;
void send_once();

static bool isDecided = false;

void DebugFunction(JNIEnv* env){
	return;
	/*
	int tid = dvmThreadSelf()->threadId;
	int tid2 = ((JNIEnvExt*)env)->envThreadId;
	ALOG(LOG_INFO,"NATIVELOG", "in %s tid: %d, tid2: %d, %s",__FUNCTION__, tid, tid2, tid==tid2?"EQUAL":"DIFF" );
	*/
}
// ******************* AREDispatch methods *******************
//
bool init_stack(){
	ALOG(LOG_DEBUG,"DEBUG","IN INIT STACK");
	if(!dvmThreadSelf())
		return false;
	if(!dvmThreadSelf()->info_flag){
		ALOG(LOG_DEBUG,"DEBUG","NEW STACK IN INIT STACK");
		dvmThreadSelf()->info_flag = new std::stack<int>();
		dvmThreadSelf()->info_flag->push(0);
	}
	ALOG(LOG_DEBUG,"DEBUG","INIT STACK RETURN TRUE");
	return true;
}
void update_thread_info_flag_or(int orflag){
	
	if(init_stack()){
		int oldvalue = dvmThreadSelf()->info_flag->top();
		int newvalue = oldvalue | orflag;
	
		dvmThreadSelf()->info_flag->pop();
		dvmThreadSelf()->info_flag->push(newvalue);
	}
}

void vmEndHook(JavaVM* vm);
void printStack(JNIEnv * jni_env, jclass this_class){
	dvmDumpThread(dvmThreadSelf(), false);
}
void methodEnter(JNIEnv * jni_env, jclass this_class){
	if(init_stack()){
		dvmThreadSelf()->info_flag->push(0);
	}
}
void methodExit(JNIEnv * jni_env, jclass this_class){
	if(init_stack())
		dvmThreadSelf()->info_flag->pop();
}
int checkThreadPermission(JNIEnv * jni_env, jclass this_class){
	ALOG(LOG_DEBUG,"DEBUG","IN CHECK THREAD PERMISSION");
	if(init_stack())
		return dvmThreadSelf()->info_flag->top();
	return 0;
}
void CallAPI(JNIEnv * jni_env, jclass this_class, jint api){
	dvmThreadSelf()->transaction_info_flag |= api;
	update_thread_info_flag_or(api);
}
jlong getCPUClock(JNIEnv * jni_env, jclass this_class){
	return getTimeNsec();
}
jlong getObjectId(JNIEnv * jni_env, jclass this_class, jobject jobj){
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, jobj);
	jlong netref = SetAndGetNetref(obj);
	return netref;
}
jint getThisThreadId(JNIEnv * jni_env, jclass this_class){
	Thread *self = dvmThreadSelf();
	return (jint)self->threadId;
}
jint getThisProcId(JNIEnv * jni_env, jclass this_class){
	return getpid();
}
void NativeLog(JNIEnv * jni_env, jclass this_class, jstring text){
	const char * str = 	jni_env->GetStringUTFChars(text, NULL);
	DebugFunction(jni_env);
	ALOG(LOG_INFO,"NATIVELOG", "LOG: %s in %d tid: %d",str, getpid(),dvmThreadSelf()->threadId );
	jni_env->ReleaseStringUTFChars(text,str);
}

void _mapPID(int pid, const char* pname){
	memcpy(curpname, pname, strlen(pname)+1);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS PID: %d NAME: %s", pid, pname);
	Socket *tmpsock = new Socket(false);
	while(!tmpsock->Connect()){
		LOGDEBUG("Cannot connect through UDS in %d", getpid());
		sleep(2);
	}
	int signal = -4;
	tmpsock->Send((char*)&signal,sizeof(int));
	signal = strlen(pname);
	tmpsock->Send((char*)&signal,sizeof(int));
	tmpsock->Send(pname, strlen(pname));
	signal = pid;
	tmpsock->Send((char*)&signal,sizeof(int));

	int bp = -2;
	tmpsock->RecvInt(bp);
	//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS is:%d PID: %d NAME: %s TID:%d", bp, pid, pname,dvmThreadSelf()->threadId);
	gDvm.bypass = bp==0?false:true;


	pthread_mutex_lock(&gDvm.s_mtx);
	if(gDvm.bypass) {
		//ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","THIS PROCESS NEED NO HOOKS, SET TO NULL");
		gDvm.newObjHook = NULL;
		gDvm.freeObjHook = NULL;
		gDvm.threadEndHook = NULL;
		gDvm.vmEndHook = NULL;
	}
	pthread_mutex_unlock(&gDvm.s_mtx);
	isDecided = true;
	delete tmpsock;
	remote.MapPidPname(pid, pname);
}

jshort registerMethod
(JNIEnv * jni_env, jclass this_class, jstring analysis_method_desc) {
	DebugFunction(jni_env);
	jsize str_len = jni_env->GetStringUTFLength(analysis_method_desc);
	const char * str = 	jni_env->GetStringUTFChars(analysis_method_desc, NULL);
	char tmp[100];
	int tmpsize = str_len<100?str_len:99;
	memcpy(tmp, str, tmpsize);
	tmp[tmpsize] = 0;
	method_id++;
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, method_id, str, str_len);
	jni_env->ReleaseStringUTFChars(analysis_method_desc,str);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT:register method %s pid:%d tid:%d", tmp, getpid(),dvmThreadSelf()->threadId);
	return method_id;
}

void onFork
(int para) {
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","ONFORK happens currentid-paraid: %d-%d",getpid(),para);
	if(para == 0) {
		pthread_mutex_init(&gl_mtx, NULL);//RE INIT LOCK
		remote.UpdateMutex();
		Buffer* zygote_buff;
		zygote_buff = remote.ReturnAndResetBuffer();
		gl_svm_socket = -1;
		if(DEBUGMODE)
			ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","DROPPING PACKETS FROM PARENT: %d-%d sized %d",getpid(),para, zygote_buff->q_occupied);
		delete zygote_buff;
		remote.OnForkEvent(para);
	}
	if(para != 0){
		//the parent
		remote.OnForkEvent(para);
		Buffer* zygote_buff;
		pthread_mutex_lock(&gl_mtx);
		zygote_buff = remote.ReturnAndResetBuffer();

		forward_to_svm(zygote_buff->q_data, zygote_buff->q_occupied);

		pthread_mutex_unlock(&gl_mtx);

		if(zygote_buff)
			delete zygote_buff;
		zygote_buff=NULL;
	}
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	DebugFunction(jni_env);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d, tid:%d", (int)analysis_method_id,dvmThreadSelf()->threadId);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, getpid()*1000+0, analysis_method_id);
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
 jbyte ordering_id) {
	DebugFunction(jni_env);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d with ordering %d", (int)analysis_method_id, (int)ordering_id);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, getpid()*1000+ordering_id, analysis_method_id);
}
void manuallyOpen
(JNIEnv * jni_env, jclass this_class) {
	//OpenConnection();
}
void manuallyClose
(JNIEnv * jni_env, jclass this_class) {
	DebugFunction(jni_env);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: close connection at %d", getpid());
	vmEndHook(NULL);
}
void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	DebugFunction(jni_env);
	if(DEBUGMODE)
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis end for method in %d", dvmThreadSelf()->threadId);

	remote.AnalysisEndEvent(dvmThreadSelf()->threadId);
	//Here to restrict the possible heavy burden of forwarder, we send immediately the buffer if it exceeds a threshold
	if(remote.GetCurrentSize() > MAX_BUFFER){
		pthread_mutex_lock(&gl_mtx);
		send_once();
		pthread_mutex_unlock(&gl_mtx);
	}
}

void sendBoolean
(JNIEnv * jni_env, jclass this_class, jboolean to_send) {
	DebugFunction(jni_env);
	remote.SendJboolean(dvmThreadSelf()->threadId, to_send);
}

void sendByte
(JNIEnv * jni_env, jclass this_class, jbyte to_send) {
	DebugFunction(jni_env);
	remote.SendJbyte(dvmThreadSelf()->threadId, to_send);
}

void sendChar
(JNIEnv * jni_env, jclass this_class, jchar to_send) {
	DebugFunction(jni_env);
	remote.SendJchar(dvmThreadSelf()->threadId, to_send);
}

void sendShort
(JNIEnv * jni_env, jclass this_class, jshort to_send) {
	DebugFunction(jni_env);
	remote.SendJshort(dvmThreadSelf()->threadId, to_send);
}

void sendInt
(JNIEnv * jni_env, jclass this_class, jint to_send) {
	DebugFunction(jni_env);
	remote.SendJint(dvmThreadSelf()->threadId, to_send);
}

void sendLong
(JNIEnv * jni_env, jclass this_class, jlong to_send) {
	DebugFunction(jni_env);
	remote.SendJlong(dvmThreadSelf()->threadId, to_send);
}

void sendFloat
(JNIEnv * jni_env, jclass this_class, jfloat to_send) {
	DebugFunction(jni_env);
	remote.SendJfloat(dvmThreadSelf()->threadId, to_send);
}

void sendDouble
(JNIEnv * jni_env, jclass this_class, jdouble to_send) {
	DebugFunction(jni_env);
	remote.SendJdouble(dvmThreadSelf()->threadId, to_send);
}


void printDebug(ClassObject* obj){
	/*
	if(obj == NULL)
		return;
	if(obj->classLoader) {
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","Class %s has loader %lu, loader class, %s, the DEXFILE: %s",obj->descriptor, (unsigned long)obj->tag, obj->classLoader->clazz->descriptor, obj->pDvmDex->name);
		if(obj->classLoader != obj)
			printDebug(obj->classLoader->clazz);
	}
	else{
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","Class %s has loader NULL",obj->descriptor);
	}*/
}

jlong newClass(ClassObject *obj){
	if(obj == NULL)  {
		return 0;
	}
	jlong superid = SetAndGetNetref(obj->super);
	jlong loaderid = SetAndGetNetref(obj->classLoader);
	obj->tag = _set_net_reference(ot_object_id++,ot_class_id++,1,1);
	char tmp;
	//if(DEBUGMODE)
	//	printDebug(obj);
	//NewClass may be removed
	remote.NewClassEvent(obj->descriptor, strlen(obj->descriptor), loaderid, 0, &tmp);
	remote.NewClassInfo(obj->tag, obj->descriptor, strlen(obj->descriptor), "", 0, loaderid, superid);
	return obj->tag;
}

jlong SetAndGetNetref(Object* obj){
	jlong res;
	if(obj == NULL) //to_send is null or is weak reference which has already been cleared
	{
		res = 0;
	}else if(obj->tag != 0){
		res = obj->tag;
	}else if(dvmIsClassObject(obj)){
		res = newClass((ClassObject*)obj);
	}else {
		if(obj->clazz->tag == 0){ //its class not registered
			newClass(obj->clazz);
		}
		obj->tag = _set_net_reference(ot_object_id++,net_ref_get_class_id(obj->clazz->tag),0,0);
		res = obj->tag;
	}
	return res;
}

void sendCurrentThread
(JNIEnv * jni_env, jclass this_class) {
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN %s", __FUNCTION__);
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = NULL;
   	if(self)
		obj	= self->threadObj;
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
		//TODO isDaemon is not set to false for all
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		bool isDaemon = false;
		char *name =  dvmGetThreadName_cstr(self);
		if(name!=NULL) {
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is %s", name);
			remote.SendThreadObject(self->threadId, obj->tag, name, strlen(name), isDaemon);
			free(name);
		}else{
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is null");
			if(!net_ref_get_spec(obj->tag)) {
				remote.SendThreadObject(self->threadId, obj->tag, "default", strlen("default"), isDaemon);
				net_ref_set_spec((jlong*)&(obj->tag), 1);
			}else{
				if(DEBUGMODE)
					ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","AVOID SENDING THREAD");
			}

		}
	}else{
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","ERROR SHOULD SEND thread object");
	}
	remote.SendJobject(self->threadId, netref);
}
void sendObjectSize
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	DebugFunction(jni_env);
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);

	if(obj == NULL){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJlong(self->threadId, 0);
		return;
	}
	remote.SendJlong(self->threadId, obj->clazz->objectSize);
}
void sendObject
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	DebugFunction(jni_env);
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	remote.SendJobject(self->threadId, netref);
}

void sendObjectPlusData
(JNIEnv * jni_env, jclass this_class, jobject to_send) {
	DebugFunction(jni_env);
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN %s", __FUNCTION__);
	ScopedMutex mtx(&gl_mtx);
	Thread *self = dvmThreadSelf();
	Object* obj = dvmDecodeIndirectRef(self, to_send);
	jlong netref = SetAndGetNetref(obj);

	if(obj == NULL){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	if(obj->clazz == gDvm.classJavaLangString){
		int len=((StringObject*)obj)->length();
		int utflen = ((StringObject*)obj)->utfLength();
		const u2* tmp = ((StringObject*)obj)->chars();
		char* str = new char[utflen+1];
		for(int i = 0; i < utflen; i++){
			str[i] = tmp[i];
		}
		str[utflen] = 0;
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send string object %s, %d, %d",str, len, utflen);

		if(!net_ref_get_spec(obj->tag)) {
			remote.SendStringObject(self->threadId, obj->tag, str, utflen);
			net_ref_set_spec((jlong*)&(obj->tag), 1);
		}else{
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","AVOID SENDING STRING");
		}
		delete []str;
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
		//TODO isDaemon is not set to false for all
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		bool isDaemon = false;
		char *name =  dvmGetThreadName_cstr(self);
		if(name!=NULL) {
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is %s", name);
			remote.SendThreadObject(self->threadId, obj->tag, name, strlen(name), isDaemon);
			free(name);
		}else{
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is null");
			if(!net_ref_get_spec(obj->tag)) {
				remote.SendThreadObject(self->threadId, obj->tag, "default", strlen("default"), isDaemon);
				net_ref_set_spec((jlong*)&(obj->tag), 1);
			}else{
				if(DEBUGMODE)
					ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","AVOID SENDING THREAD");
			}

		}
	}
	remote.SendJobject(self->threadId, netref);
}

static const char *classPathName = "ch/usi/dag/dislre/AREDispatch";

static JNINativeMethod methods[]= {
	{"getObjectId", "(Ljava/lang/Object;)J", (void*)getObjectId},
	{"getThisThreadId", "()I", (void*)getThisThreadId},
	{"getThisProcId", "()I", (void*)getThisProcId},
	{"NativeLog", "(Ljava/lang/String;)V", (void*)NativeLog},
	{"CallAPI", "(I)V", (void*)CallAPI},
	{"methodEnter", "()V", (void*)methodEnter},
	{"methodExit", "()V", (void*)methodExit},
	{"printStack", "()V", (void*)printStack},
	{"checkThreadPermission", "()I", (void*)checkThreadPermission},
	{"getCPUClock", "()J", (void*)getCPUClock},
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
	{"sendObjectSize", "(Ljava/lang/Object;)V", (void*)sendObjectSize},
	{"sendCurrentThread", "()V", (void*)sendCurrentThread},
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

int registerShadowNatives(JNIEnv *env){

	if (!registerNativeMethods(env, classPathName,
				methods, sizeof(methods)/sizeof(methods[0])))
	{
		return JNI_FALSE;
	}

	return JNI_TRUE;

}

void objNewHook(Object* obj){
	SetAndGetNetref(obj);
}
void threadEndHook(Thread* self){
	//TODO
	//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
	bool isDaemon = false;
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s %d %s", __FUNCTION__, self->threadId, isDaemon?"daemon":"not daemon");
}
void vmEndHook(JavaVM* vm){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s for %s", __FUNCTION__, curpname);
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","VM instance %s-%d  ended", curpname, getpid());

	if(!gDvm.bypass) {
		pthread_mutex_lock(&gl_mtx);
		remote.ConnectionClose();
		Buffer* tmp = remote.ReturnAndResetBuffer();

		forward_to_svm(tmp->q_data, tmp->q_occupied);

		pthread_mutex_unlock(&gl_mtx);

		if(tmp)
			delete tmp;
	}

}
void objFreeHook(Object* obj, Thread* self){
	if(obj == NULL)
		return;
	if(obj->tag == 0)
		return;
	SetAndGetNetref(obj);
	if(!dvmIsClassObject(obj))
		remote.ObjFreeEvent(obj->tag);
}
int classfileLoadHook(const char* name, int len){
	if(DEBUGMODE)
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","LOADING CLASS %s", name);
	return 1;
}
int classInitHook(ClassObject* co){
	SetAndGetNetref(co);
	return 1;
}

//FOR FLOW CONTROL
void send_once(){
	if(remote.GetCurrentSize() <= MAX_BUFFER)
		return;
	Buffer* tmp = NULL;
	tmp = remote.ReturnAndResetBuffer();
	if(tmp->q_occupied >0 ){
	if(DEBUGMODE)
		ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d in %d:%d", tmp->q_occupied, getpid(), dvmThreadSelf()->threadId);

		forward_to_svm(tmp->q_data, tmp->q_occupied);
	}else {
		if(DEBUGMODE)
			ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
	}
	if(tmp)
		delete tmp;
}
static void * send_thread_loop(void * obj) {
	while(!isDecided){
		sleep(3);
	}
	if(gDvm.bypass){
		pthread_detach(pthread_self());
		pthread_exit(NULL);
		return NULL;
	}

	while(true){
		Buffer* tmp = NULL;
		pthread_mutex_lock(&gl_mtx);
		pthread_mutex_lock(&remote.gl_mtx);
		tmp = remote.ReturnAndResetBufferNolock();
		if(tmp->q_occupied >0 ){
			pthread_mutex_unlock(&remote.gl_mtx);
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d from pid %d", tmp->q_occupied, getpid());
			forward_to_svm(tmp->q_data, tmp->q_occupied);
			pthread_mutex_unlock(&gl_mtx);
			sleep(2);
		}else {
			if(DEBUGMODE)
				ALOG(LOG_DEBUG,"SHADOWDEBUG","EMPTY BUFFER");
			pthread_mutex_unlock(&gl_mtx);
			pthread_cond_wait(&remote.new_event_cond, &remote.gl_mtx);
			pthread_mutex_unlock(&remote.gl_mtx);
			sleep(2);
		}
		if(tmp)
			delete tmp;
	}

	if(DEBUGMODE)
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
}

int64_t getTimeNsec(){
	struct timespec now;
	clock_gettime(CLOCK_MONOTONIC, &now);
	return (int64_t) now.tv_sec*1000000000LL + now.tv_nsec;
}

int clientTransactionStart(int transaction_id, bool isOneWay){
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","CLIENT(%d-%d) starts new transaction", getpid(), dvmThreadSelf()->threadId);
	//could pass flag of client thread to server thread, for sink
	jlong time = getTimeNsec();
	//ALOG(LOG_DEBUG,"CFG","%d %d %d 0 %d %d %llu %d 0", getpid(), dvmThreadSelf()->threadId, transaction_id, -1, -1, time, isOneWay?1:0);
	
	if(!gDvm.bypass)
		remote.OnIPCEvent(dvmThreadSelf()->threadId, transaction_id, 0, -1, -1, time, isOneWay);
	return dvmThreadSelf()->threadId;
}
int serverTransactionRecv(int transaction_id, int from_pid, int from_tid, bool isOneWay){
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SERVER(%d-%d) receives transaction from client(%d-%d)", getpid(), dvmThreadSelf()->threadId, from_pid, from_tid);
	//prepare to update server thread's flag
	dvmThreadSelf()->transaction_info_flag = 0;
	jlong time = getTimeNsec();
	//ALOG(LOG_DEBUG,"CFG","%d %d %d 1 %d %d %llu %d 0", from_pid, from_tid, transaction_id, getpid(), dvmThreadSelf()->threadId, time, isOneWay?1:0);
	if(!gDvm.bypass)
		remote.OnIPCEvent(dvmThreadSelf()->threadId, transaction_id, 1, from_pid, from_tid,time, isOneWay);
	return dvmThreadSelf()->threadId;
}
int serverReplySent(int transaction_id, int from_pid, int from_tid, bool isOneWay,int &t_flag){
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SERVER(%d-%d) sent reply to client", getpid(), dvmThreadSelf()->threadId);
	//add two flags to event
	init_stack();
	jlong time = getTimeNsec();
	if(dvmThreadSelf()->info_flag->top()){
	//	ALOG(LOG_DEBUG,"LEAKSOURCE","%d %d %d x %d %d %llu %d", from_pid, from_tid, transaction_id, getpid(), dvmThreadSelf()->threadId, time, isOneWay?1:0);
	}
	if(dvmThreadSelf()->transaction_info_flag){
	//	ALOG(LOG_DEBUG,"LEAKSOURCE","%d %d %d y %d %d %llu %d", from_pid, from_tid, transaction_id, getpid(), dvmThreadSelf()->threadId, time, isOneWay?1:0);
	}
	t_flag = dvmThreadSelf()->transaction_info_flag;
	//clear server thread's transaction local flag
	//ALOG(LOG_DEBUG,"CFG","%d %d %d 2 %d %d %llu %d %d", from_pid, from_tid, transaction_id, getpid(), dvmThreadSelf()->threadId, time, isOneWay?1:0, t_flag);
	if(!gDvm.bypass)
		remote.OnIPCEvent(dvmThreadSelf()->threadId, transaction_id, 2, from_pid, from_tid,time, isOneWay);
	return dvmThreadSelf()->threadId;
}
int clientReplyRecv(int transaction_id, int from_pid, int from_tid, bool isOneWay, int t_flag){
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","CLIENT(%d-%d) receives reply from server(%d-%d)", getpid(), dvmThreadSelf()->threadId, from_pid, from_tid);
	init_stack();
	jlong time = getTimeNsec();

	int ti_old = dvmThreadSelf()->transaction_info_flag;
	int i_old =dvmThreadSelf()->info_flag->top();
	dvmThreadSelf()->transaction_info_flag |= t_flag;
	//dvmThreadSelf()->info_flag |= t_flag;
	update_thread_info_flag_or(t_flag);
	if(dvmThreadSelf()->transaction_info_flag != ti_old){
		//ALOG(LOG_DEBUG,"DETECT0", "%d %d %d %d %d transact_flag %d | %d -> %d", getpid(), dvmThreadSelf()->threadId, transaction_id, from_pid, from_tid, ti_old, t_flag, ti_old | t_flag);
		dvmDumpThread(dvmThreadSelf(), false);
	}
	if(dvmThreadSelf()->info_flag->top() != i_old){
		//ALOG(LOG_DEBUG,"CFG","%d %d %d 3 %d %d %llu %d %d", getpid(), dvmThreadSelf()->threadId, transaction_id, from_pid, from_tid, time, isOneWay?1:0, t_flag);
		//ALOG(LOG_DEBUG,"DETECT1", "%d %d %d %d %d thread_flag %d | %d -> %d", getpid(), dvmThreadSelf()->threadId, transaction_id, from_pid, from_tid, i_old, t_flag, i_old | t_flag);
	}else{
		//ALOG(LOG_DEBUG,"CFG","%d %d %d 3 %d %d %llu %d %d", getpid(), dvmThreadSelf()->threadId, transaction_id, from_pid, from_tid, time, isOneWay?1:0, 0);
    }
	if(!gDvm.bypass)
		remote.OnIPCEvent(dvmThreadSelf()->threadId, transaction_id, 3, from_pid, from_tid,time, isOneWay);
	return dvmThreadSelf()->threadId;
}

jint ShadowLib_Zygote_OnLoad(JavaVM* vm, void* reserved){
	pthread_mutex_init(&gl_mtx, NULL);
	isZygote = true;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//TODO current it's tricky here, to distinguish a zygote process with dalvikvm
	if(getpid()<100){
		_mapPID(getpid(),"zygote");
	}else{
		_mapPID(getpid(),"dalvikvm");
	}
	gDvm.newObjHook = NULL;
	gDvm.freeObjHook = &objFreeHook;
	gDvm.threadEndHook = &threadEndHook;
	gDvm.vmEndHook = &vmEndHook;

	gDvm.clientTransactionStart = clientTransactionStart;
	gDvm.serverTransactionRecv = serverTransactionRecv;
	gDvm.serverReplySent = serverReplySent;
	gDvm.clientReplyRecv = clientReplyRecv;

	char* addr = (((char*)(&gDvm))+sizeof(gDvm));
	ALOG(LOG_DEBUG,"TESTTMP","ADDR %p-%p %p-%p %p-%p %p-%p",gDvm.clientReplyRecv, *(void **)(addr-8),  gDvm.serverReplySent, *(void**)(addr-12), gDvm.serverTransactionRecv, *(void**)(addr-16), gDvm.clientTransactionStart, *(void**)(addr-20));
	
	//HOOK TAHT NOT USED
	//gDvm.classInitHook = &classInitHook;
	//gDvm.classfileLoadHook = &classfileLoadHook;

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}
	env = uenv.env;
	env = uenv.env;

	if (registerShadowNatives(env) != JNI_TRUE){

		goto bail;
	}
	result = JNI_VERSION_1_4;

bail:
	return result;
}

jint ShadowLib_SystemServer_OnLoad(JavaVM* vm, void* reserved){
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	pthread_t send_thread;
	pthread_create(&send_thread, NULL, send_thread_loop, NULL);

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}

	env = uenv.env;

	env = uenv.env;

	if (registerShadowNatives(env) != JNI_TRUE){
		goto bail;
	}

	result = JNI_VERSION_1_4;
bail:
	return 0;
}
jint ShadowLib_OnLoad(JavaVM* vm, void* reserved){
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	pthread_t send_thread;
	pthread_create(&send_thread, NULL, send_thread_loop, NULL);

	if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK){
		goto bail;
	}

	env = uenv.env;

	env = uenv.env;

	if (registerShadowNatives(env) != JNI_TRUE){

		goto bail;
	}

	result = JNI_VERSION_1_4;


bail:
	return 0;
}
