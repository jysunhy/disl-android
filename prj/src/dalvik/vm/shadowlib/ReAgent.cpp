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

static ReProtocol remote("/dev/socket/instrument",11218);

static bool isZygote = true;

//static Socket *sock = NULL;

//static pthread_t *send_thread;
//static pthread_t send_zygote_thread;

static pthread_mutex_t gl_mtx;
static volatile jint ot_class_id = 1;
static volatile jlong ot_object_id = 1;
static volatile jshort method_id = 1;

	Socket *zsock = NULL;

//bool gl_bypass=true;
static bool isDecided = false;

//jint add(JNIEnv *env, jobject thiz, jint x, jint y){
//	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in shadowvm native %s", __FUNCTION__);
//	return x + y + 1000;
//}

// ******************* REDispatch methods *******************

void _mapPID(int pid, const char* pname){
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
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID BYPASS is:%d PID: %d NAME: %s", bp, pid, pname);
	gDvm.isShadow = bp==0?false:true;


	pthread_mutex_lock(&gDvm.s_mtx);
	if(gDvm.isShadow) {
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPID SETTING THE HOOKS TO NULL");
		gDvm.newObjHook = NULL;
		gDvm.freeObjHook = NULL;
		gDvm.threadEndHook = NULL;
		gDvm.vmEndHook = NULL;
	}
	pthread_mutex_unlock(&gDvm.s_mtx);
	isDecided = true;
	delete tmpsock;
}

void mapPID_2
(JNIEnv * jni_env, jclass this_class, jstring pname) {
	jsize str_len = jni_env->GetStringUTFLength(pname);
	const char * str = 	jni_env->GetStringUTFChars(pname, NULL);
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","MAPPING PID: %d NAME: %s", getpid(), str);
	Socket *tmpsock = new Socket(true);
	while(!tmpsock->Connect()){
		LOGDEBUG("Cannot connect through UDS");
		sleep(2);
	}
	int signal = -4;
	tmpsock->Send((char*)&signal,sizeof(int));
	signal = str_len;
	tmpsock->Send((char*)&signal,sizeof(int));
	tmpsock->Send(str, str_len);
	signal = getpid();
	tmpsock->Send((char*)&signal,sizeof(int));

	int bp = -2;
	tmpsock->RecvInt(bp);
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","BYPASS is:%d PID: %d NAME: %s", bp, getpid(), str);
	gDvm.isShadow = bp==0?false:true;


	jni_env->ReleaseStringUTFChars(pname,str);
	pthread_mutex_lock(&gDvm.s_mtx);
	if(gDvm.isShadow) {
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","SETTING THE HOOKS TO NULL");
		gDvm.newObjHook = NULL;
		gDvm.freeObjHook = NULL;
		gDvm.threadEndHook = NULL;
		gDvm.vmEndHook = NULL;
	}
	pthread_mutex_unlock(&gDvm.s_mtx);
	isDecided = true;
	delete tmpsock;
}
void mapPID
(JNIEnv * jni_env, jclass this_class, jstring pname, jint pid) {
	//remote.OpenConnection();
	//jsize str_len = jni_env->GetStringUTFLength(pname);
	const char * str = 	jni_env->GetStringUTFChars(pname, NULL);
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","PID: %d NAME: %s", pid, str);
	/*
	   Socket *tmpsock = new Socket();
	   while(!tmpsock->Connect()){
	   LOGDEBUG("Cannot connect through UDS");
	   sleep(2);
	   }
	   int signal = -4;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   signal = str_len;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   tmpsock->Send(str, str_len);
	   signal = pid;
	   tmpsock->Send((char*)&signal,sizeof(int));
	   jni_env->ReleaseStringUTFChars(pname,str);
	   delete tmpsock;
	   */
}

jshort registerMethod
(JNIEnv * jni_env, jclass this_class, jstring analysis_method_desc) {
	jsize str_len = jni_env->GetStringUTFLength(analysis_method_desc);
	const char * str = 	jni_env->GetStringUTFChars(analysis_method_desc, NULL);
	char tmp[100];
	int tmpsize = str_len<100?str_len:99;
	memcpy(tmp, str, tmpsize);
	tmp[tmpsize] = 0;
	//pthread_mutex_lock(&gl_mtx);
	method_id++;
	remote.MethodRegisterEvent(dvmThreadSelf()->threadId, method_id, str, str_len);
	jni_env->ReleaseStringUTFChars(analysis_method_desc,str);
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT:register method %s pid:%d tid:%d", tmp, getpid(),dvmThreadSelf()->threadId);
	//pthread_mutex_unlock(&gl_mtx);
	//dvmDumpThread(dvmThreadSelf(),true);
	return method_id;
}

void onFork
(int para) {
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","ONFORK happens currentid-paraid: %d-%d",getpid(),para);
	remote.OnForkEvent(para);
	if(para != 0){
		//the parent
		Buffer* zygote_buff;
		zygote_buff = remote.ReturnAndResetBuffer();
		ALOG(LOG_DEBUG,"SHADOWDEBUG","BEFORE FORK, BUFF OF ZYGOTE: %d", zygote_buff->q_occupied);
		zsock->Send(zygote_buff->q_data,zygote_buff->q_occupied);
		if(zygote_buff)
			delete zygote_buff;
		zygote_buff=NULL;
	}
}

void analysisStart__S
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id) {
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d, tid:%d", (int)analysis_method_id,dvmThreadSelf()->threadId);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, 0, analysis_method_id);
	//dvmDumpAllThreads(false);
}

void analysisStart__SB
(JNIEnv * jni_env, jclass this_class, jshort analysis_method_id,
 jbyte ordering_id) {
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis start for method %d with ordering %d", (int)analysis_method_id, (int)ordering_id);
	remote.AnalysisStartEvent(dvmThreadSelf()->threadId, ordering_id, analysis_method_id);
	//dvmDumpAllThreads(false);
}
/*
   void OpenConnection(){
   ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: open connection");
//remote.OpenConnection();
if(sock) {
return;
}
sock = new Socket();
while(!sock->Connect()){
LOGDEBUG("Cannot connect through UDS");
sleep(2);
}
int signal = -3;
sock->Send((char*)&signal,sizeof(int));
}
*/
void manuallyOpen
(JNIEnv * jni_env, jclass this_class) {
	//OpenConnection();
}
void manuallyClose
(JNIEnv * jni_env, jclass this_class) {
	//remote.ConnectionClose();
	   Socket *sock = new Socket(false);
	   int pid = getpid();
		ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: close connection at %d %d", getpid(), pid);
	   pid = htonl(pid);
	   sock->Send((char*)&pid, sizeof(int));
	   char tmp = MSG_CLOSE;
	   sock->Send(&tmp, 1);
	   delete sock;
	   sock = NULL;
	  // exit(0);
}
void analysisEnd
(JNIEnv * jni_env, jclass this_class) {
	ALOG(LOG_INFO,isZygote?"SHADOWZYGOTE":"SHADOW","EVENT: analysis end for method");
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
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW class found %s id %d, loaderid: %lld, superid: %lld",obj->descriptor, ot_class_id-1, loaderid, superid);
	remote.NewClassEvent(obj->descriptor, strlen(obj->descriptor), loaderid, 0, &tmp);
	remote.NewClassInfo(obj->uuid, obj->descriptor, strlen(obj->descriptor), "", 0, loaderid, superid);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW class found %lld:%s",obj->uuid, obj->descriptor);
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
		//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","NEW object found tag:%lld, instance of %s classid %d",obj->uuid, obj->clazz->descriptor,net_ref_get_class_id(obj->uuid));
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
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
		remote.SendJobject(self->threadId, 0);
		return;
	}
	/*
	   if(obj->clazz == gDvm.classJavaLangString){
	   int len=((StringObject*)obj)->length();
	   int utflen = ((StringObject*)obj)->utfLength();
	   const u2* tmp = ((StringObject*)obj)->chars();
	   const jchar* content = (jchar*)tmp;
	   ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send string object");
	   remote.SendStringObject(self->threadId, obj->uuid, content, len);
	   }
	   if(obj->clazz == gDvm.classJavaLangThread){
	   ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
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
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send NULL object");
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
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send string object %s, %d, %d",str, len, utflen);

		remote.SendStringObject(self->threadId, obj->uuid, str, utflen);
	}
	if(obj->clazz == gDvm.classJavaLangThread){
		ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object");
		//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
		bool isDaemon = false;
		char *name =  dvmGetThreadName_cstr(self);
		if(name!=NULL) {
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is %s", name);
			remote.SendThreadObject(self->threadId, obj->uuid, name, strlen(name), isDaemon);
			free(name);
		}else{
			ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","send thread object name is null");
			remote.SendThreadObject(self->threadId, obj->uuid, "default", strlen("default"), isDaemon);
		}
	}

	remote.SendJobject(self->threadId, netref);
}

static const char *classPathName = "ch/usi/dag/dislre/AREDispatch";

static JNINativeMethod methods[]= {
	//{"add", "(II)I", (void*)add},
	{"mapPID", "(Ljava/lang/String;I)V", (void*)mapPID},
	{"mapPID", "(Ljava/lang/String;)V", (void*)mapPID_2},
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
	{"manuallyOpen", "()V", (void*)manuallyOpen},
	{"manuallyClose", "()V", (void*)manuallyClose},
	//{"onFork", "(I)V", (void*)onFork},
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
	//return;
	SetAndGetNetref(obj);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s %s %lld", __FUNCTION__, obj->clazz->descriptor, obj->uuid);
}
void threadEndHook(Thread* self){
	//bool isDaemon = dvmGetFieldBoolean(self->threadObj, gDvm.offJavaLangThread_daemon);
	bool isDaemon = false;
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s %d %s", __FUNCTION__, self->threadId, isDaemon?"daemon":"not daemon");
}
void vmEndHook(JavaVM* vm){
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in hook %s", __FUNCTION__);
}
void objFreeHook(Object* obj, Thread* self){
	//if(self->isDaemon)
	//	return;
	if(obj == NULL)
		return;
	if(obj->uuid == 0)
		return;
	SetAndGetNetref(obj);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","in FREE hook %s %lld", obj->clazz->descriptor, obj->uuid);
	if(!dvmIsClassObject(obj))
		remote.ObjFreeEvent(obj->uuid);
}
int classfileLoadHook(const char* name, int len){
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","LOADING CLASS %s", name);
	return 1;
}
int classInitHook(ClassObject* co){
	SetAndGetNetref(co);
	return 1;
}

/* static void * zygote_send_thread_loop(void * obj) {
   while(true){
   ALOG(LOG_DEBUG,"SHADOWDEBUG","IN ZYGOTE WHILE LOOOP");
   sleep(15);
//sched_yield();
//std::this_thread::yield();
}
return NULL;
}
*/

static void * send_ss_thread_loop(void * obj) {
	int sock_host = -1;
	int retcode;
	while(true) {
		sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
		if(sock_host <= 0){
			ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
			sleep(5);
		}else
			break;
	}
	Buffer *undecidedBuf = new Buffer(1024);
	//int signal = -3;
	//sock->Send((char*)&signal,sizeof(int));
	while(true){
		ALOG(LOG_DEBUG,"SHADOWDEBUG","IN WHILE LOOOP");
		Buffer* tmp = NULL;
		tmp = remote.ReturnAndResetBuffer();
		if(!isDecided){
			undecidedBuf->Enqueue(tmp->q_data, tmp->q_occupied);
			ALOG(LOG_DEBUG,"SHADOWDEBUG","PUTTING UNDECIDED EVENTS INTO BUFFER SIZED: %d", undecidedBuf->q_occupied);
		}else{
			if(gDvm.isShadow){
				ALOG(LOG_DEBUG,"SHADOWDEBUG","DROP THE BUFFER FOR UNOBSERVED PROCESS SIZED %d", undecidedBuf->q_occupied);
				delete tmp;
				break;
			}

			if(undecidedBuf){
				ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING UNDECIDED BUFFER SIZED: %d", undecidedBuf->q_occupied);
				retcode = send(sock_host, undecidedBuf->q_data, undecidedBuf->q_occupied, 0);
				while(retcode <0)
				{
					while(true) {
						ALOG (LOG_INFO,"INSTRUMENTSERVER","SEND ERROR RETRYING");
						sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
						if(sock_host <= 0){
							ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
							sleep(5);
						}else
							break;
					}
					retcode = send(sock_host, undecidedBuf->q_data, undecidedBuf->q_occupied, 0);
				}
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d retcode: %d", undecidedBuf->q_occupied, retcode);

				delete undecidedBuf;
				undecidedBuf = NULL;
			}
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d", tmp->q_occupied);
			retcode = send(sock_host,tmp->q_data, tmp->q_occupied, 0);
			while(retcode <0){
				ALOG (LOG_INFO,"INSTRUMENTSERVER","SEND ERROR RETRYING");
				while(true) {
					sock_host = socket_network_client("192.168.1.103", 11218, SOCK_STREAM);
					if(sock_host <= 0){
						ALOG (LOG_INFO,"INSTRUMENTSERVER","new host sock error");
						sleep(5);
					}else
						break;
				}
				retcode = send(sock_host,tmp->q_data, tmp->q_occupied, 0);
			}
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d retcode: %d", tmp->q_occupied, retcode);
		}
		if(tmp)
			delete tmp;
		sleep(2);
		//if(remote.IsClosed())
		//	break;
	}
	if(undecidedBuf)
		delete undecidedBuf;

	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
}
static void * send_thread_loop(void * obj) {
	Buffer *undecidedBuf = new Buffer(1024);
	Socket *sock = new Socket();
	while(!sock->Connect()){
		LOGDEBUG("Cannot connect through UDS in %s for %d",__FUNCTION__, getpid());
		sleep(2);
	}
	int signal = -3;
	sock->Send((char*)&signal,sizeof(int));
	while(true){
		ALOG(LOG_DEBUG,"SHADOWDEBUG","IN WHILE LOOOP");
		Buffer* tmp = NULL;
		tmp = remote.ReturnAndResetBuffer();
		if(!isDecided){
			undecidedBuf->Enqueue(tmp->q_data, tmp->q_occupied);
			ALOG(LOG_DEBUG,"SHADOWDEBUG","PUTTING UNDECIDED EVENTS INTO BUFFER SIZED: %d", undecidedBuf->q_occupied);
		}else{
			if(gDvm.isShadow){
				ALOG(LOG_DEBUG,"SHADOWDEBUG","DROP THE BUFFER FOR UNOBSERVED PROCESS SIZED %d", undecidedBuf->q_occupied);
				delete tmp;
				break;
			}

			if(undecidedBuf){
				ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING UNDECIDED BUFFER SIZED: %d", undecidedBuf->q_occupied);
				if(!sock->Send(undecidedBuf->q_data, undecidedBuf->q_occupied)){
					ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND");
					delete sock;
					sock = NULL;
				}
				delete undecidedBuf;
				undecidedBuf = NULL;
			}
			ALOG(LOG_DEBUG,"SHADOWDEBUG","SENDING BUFFER SIZED: %d", tmp->q_occupied);
			if(!sock->Send(tmp->q_data, tmp->q_occupied)){
				ALOG(LOG_DEBUG,"SHADOWDEBUG","SERVER ERROR DURING SEND");
				delete sock;
				sock = NULL;
			}
		}
		if(tmp)
			delete tmp;
		sleep(15);
		//if(remote.IsClosed())
		//	break;
	}
	if(undecidedBuf)
		delete undecidedBuf;

	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","SENDING LOOP END");

	pthread_detach(pthread_self());
	pthread_exit(NULL);

	return NULL;
}
jint ShadowLib_Zygote_OnLoad(JavaVM* vm, void* reserved){
	pthread_mutex_init(&gl_mtx, NULL);
	isZygote = true;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;
	//zygote_buff = NULL;

	//gDvm.shadowHook = &testShadowHook;
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, Zygote PID: %d", getpid());

	if(gDvm.zygote){
		_mapPID(getpid(),"zygote");
	}
	zsock = new Socket();
	while(!zsock->Connect()){
		LOGDEBUG("Zygote Cannot connect through UDS");
		sleep(2);
	}
	int signal = -3;
	zsock->Send((char*)&signal,sizeof(int));
	//OpenConnection();
	gDvm.newObjHook = &objNewHook;
	gDvm.freeObjHook = &objFreeHook;
	gDvm.threadEndHook = &threadEndHook;
	gDvm.vmEndHook = &vmEndHook;
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

	//pthread_t send_thread;
	//pthread_create(&send_thread, NULL, zygote_send_thread_loop, NULL);
	//ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","CAN BE PRINTED?");

bail:
	return result;
}

void BeforeFork(){
	Buffer* zygote_buff;
	zygote_buff = remote.ReturnAndResetBuffer();
	ALOG(LOG_DEBUG,"SHADOWDEBUG","BEFORE FORK, BUFF OF ZYGOTE: %d", zygote_buff->q_occupied);
	zsock->Send(zygote_buff->q_data,zygote_buff->q_occupied);
	if(zygote_buff)
		delete zygote_buff;
	zygote_buff=NULL;
}

jint ShadowLib_SystemServer_OnLoad(JavaVM* vm, void* reserved){
	remote.UpdateMutex();
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//gDvm.shadowHook = &testShadowHook;
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, PID: %d", getpid());
	pthread_mutex_init(&gl_mtx, NULL);
	//if(gDvm.isShadow) {

	//OpenConnection();
	//pthread_mutex_lock(&gDvm.s_mtx);
	//gDvm.newObjHook = &objNewHook;
	//gDvm.freeObjHook = &objFreeHook;
	//gDvm.threadEndHook = &threadEndHook;
	//gDvm.vmEndHook = &vmEndHook;
	//gDvm.classInitHook = &classInitHook;
	//pthread_mutex_unlock(&gDvm.s_mtx);
	pthread_t send_thread;
	pthread_create(&send_thread, NULL, send_ss_thread_loop, NULL);
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
	return 0;
}
jint ShadowLib_OnLoad(JavaVM* vm, void* reserved){
	remote.UpdateMutex();
	isZygote = false;
	UnionJNIEnvToVoid uenv;
	uenv.venv = NULL;
	jint result = -1;
	JNIEnv *env = NULL;

	//gDvm.shadowHook = &testShadowHook;
	ALOG(LOG_DEBUG,isZygote?"SHADOWZYGOTE":"SHADOW","IN ONLOAD, PID: %d", getpid());
	pthread_mutex_init(&gl_mtx, NULL);
	//if(gDvm.isShadow) {

	//OpenConnection();
	//pthread_mutex_lock(&gDvm.s_mtx);
	//gDvm.newObjHook = &objNewHook;
	//gDvm.freeObjHook = &objFreeHook;
	//gDvm.threadEndHook = &threadEndHook;
	//gDvm.vmEndHook = &vmEndHook;
	//gDvm.classInitHook = &classInitHook;
	//pthread_mutex_unlock(&gDvm.s_mtx);
	pthread_t send_thread;
	pthread_create(&send_thread, NULL, send_thread_loop, NULL);
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
	return 0;
}
