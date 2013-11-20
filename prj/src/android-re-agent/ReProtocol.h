#ifndef _REPROTOCOL_H_
#define _REPROTOCOL_H_

#include "Common.h"
#include "ReQueue.h"
#include "Socket.h"
#include "Map.h"
#include "Lock.h"
//#include <map>
using namespace std;

#define INVALID_ORDERING_ID -1
#define INVALID_THREAD_ID -1

#define ANALYSIS_QUEUE_SIZE 100
#define INVOCATION_SIZE 128

enum MsgType{
	// closing connection
	MSG_CLOSE,
	// sending analysis
	MSG_ANALYZE,
	// sending object free
	MSG_OBJ_FREE,
	// sending new class
	MSG_NEW_CLASS,
	// sending class info
	MSG_CLASS_INFO,
	// sending string info
	MSG_STRING_INFO,
	// sending registration for analysis method
	MSG_REG_ANALYSIS,
	// sending thread info
	MSG_THREAD_INFO,
	// sending thread end message
	MSG_THREAD_END
};

enum QueueType{
	QUEUE_INVOCATION,
	QUEUE_OBJFREE
};

class ReProtocol{
	public:
		ReProtocol(const char* host, int port){
			pthread_mutex_init(&gl_mtx, NULL);
			pthread_mutex_init(&objfree_mtx, NULL);
			pthread_mutex_init(&analysis_mtx, NULL);

			re_port = port;
			memset(re_host, '\0', MAXHOSTNAME);
			memcpy(re_host, host, strlen(host) < MAXHOSTNAME?strlen(host):MAXHOSTNAME);
		}

		bool ConnectionClose(){
			//TODO********************************************//
			//send all buffers
			Send(MSG_CLOSE);
			return true;
		}
		bool AnalysisStartEvent(thread_id_type tid, ordering_id_type oid, short methodId){
			if(GetOrderingId(tid) != INVALID_ORDERING_ID) {
				return false;
			}
			ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);

			lock_buf.Lock(oid);
			SetOrderingId(tid, oid);
			if(!analysis_queue.Exist(oid))
				analysis_queue.Set(oid, new ReQueue(ANALYSIS_QUEUE_SIZE)); //NEED TO FREE THIS QUEUE IN DESTRUCTURE

			if(analysis_queue[oid]->IsEmpty()){
				ASSERT((jbyte)MSG_ANALYZE == MSG_ANALYZE, "WRONG TYPE CONVERSION");
				analysis_queue[oid]->EnqueueJbyte(MSG_ANALYZE);
				analysis_queue[oid]->EnqueueJlong(oid);
				analysis_queue[oid]->EnqueueJint(0);
				analysis_queue[oid]->event_count=0;
			}
			analysis_queue[oid]->event_count++;
			jint tmp = htons(analysis_queue[oid]->event_count);
			analysis_queue[oid]->Update(sizeof(jbyte)+sizeof(jlong),(char*)&tmp, sizeof(jint));
			if(!invocation_buf.Exist(oid)){
				invocation_buf.Set(oid, new Buffer(INVOCATION_SIZE));
			}
			invocation_buf[oid]->EnqueueJshort(methodId);
			invocation_buf[oid]->EnqueueJshort(0); //space for arg length
			//running_oid.Print();
			return true;
		}
		int SendJboolean(thread_id_type tid, jboolean data){
			return SendArgument(tid, (char*)&data, sizeof(jboolean));
		}
		int SendJbyte(thread_id_type tid, jbyte data){
			return SendArgument(tid, (char*)&data, sizeof(jbyte));
		}
		int SendJchar(thread_id_type tid, jchar data){
			jchar nts = htons(data);
			return SendArgument(tid, (char*)&nts, sizeof(jchar));
		}
		int SendJshort(thread_id_type tid, jshort data){
			jshort nts = htons(data);
			return SendArgument(tid, (char*)&nts, sizeof(jshort));
		}
		int SendJint(thread_id_type tid, jint data){
			jint nts = htons(data);
			return SendArgument(tid, (char*)&nts, sizeof(jint));
		}
		int SendJlong(thread_id_type tid, jlong data){
			jlong nts = htobe64(data);
			return SendArgument(tid, (char*)&nts, sizeof(jlong));
		}
		int SendJfloat(thread_id_type tid, jfloat data){
			union float_jint convert;
			convert.f = data;
			return SendJint(tid, convert.i);
		}
		int SendJdouble(thread_id_type tid, jdouble data){
			union double_jlong convert;
			convert.d = data;
			return SendJlong(tid, convert.l);
		}
		int SendStringUtf8(thread_id_type tid, const char* string_utf8, uint16_t size_in_bytes){
			uint16_t nsize = htons(size_in_bytes);
			return SendArgument(tid, (char*)&nsize, sizeof(uint16_t))+SendArgument(tid, (char*)string_utf8, size_in_bytes);
		}
		int SendJobject(thread_id_type tid, jlong netref){
			ALOG(LOG_DEBUG,"HAIYANG","new obj %lld", netref);
			return SendJlong(tid, netref);
		}
		bool SendArgument(thread_id_type tid, const char* data, int length){
			ordering_id_type oid = GetOrderingId(tid);
			if(oid == INVALID_ORDERING_ID)
				return false;
			invocation_buf[oid]->Enqueue(data, length);
			return true;
		}
		bool AnalysisEndEvent(thread_id_type tid){
			ordering_id_type oid = GetOrderingId(tid);
			ALOG(LOG_DEBUG,"HAIYANG","in %s %d %d",__FUNCTION__, tid, (int)oid);
			if(oid == INVALID_ORDERING_ID) {
				ALOG(LOG_DEBUG,"HAIYANG","in %s thread id: %d, error end event",__FUNCTION__, tid);
				//lock_buf.Unlock(oid);
				return false;
			}
			char *buf=NULL;
			int len_buf;
			invocation_buf[oid]->GetData(buf, len_buf);
			jshort arglen = len_buf - sizeof(jshort)*2;
			arglen = htons(arglen);
			invocation_buf[oid]->Update(sizeof(jshort),(char*)&arglen, sizeof(jshort));
			invocation_buf[oid]->GetData(buf, len_buf);
			//invocation_buf[oid]->Print();
			bool full = !(analysis_queue[oid]->Enqueue(buf, len_buf));
			if(full){
				char* q=NULL;
				int len_q;
				analysis_queue[oid]->GetData(q, len_q);
				Send(q, len_q, (char*)buf, len_buf);
				analysis_queue[oid]->Reset();
			}

			invocation_buf[oid]->Reset();
			SetOrderingId(tid, INVALID_ORDERING_ID);
			lock_buf.Unlock(oid);
			return true;
		}
		bool NewClassInfo(jlong netref, const char* className, int namelen, const char* generic, int glen, jlong netrefClassLoader, jlong netrefSuperClass){
			ALOG(LOG_DEBUG,"HAIYANG","new class info %s:%lld", className, netref);
			//ScopedMutex mtx(&gl_mtx);
			//TODO optimization with pool
			Buffer tmp(100);
			tmp.EnqueueJbyte(MSG_CLASS_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(className, namelen);
			tmp.EnqueueStringUtf8(generic, glen);
			tmp.EnqueueJlong(netrefClassLoader);
			tmp.EnqueueJlong(netrefSuperClass);
			char* content;
			int len;
			tmp.GetData(content, len);
			return Send(content, len);
		}
		void ObjFreeEvent(jlong objectId){
			ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			ScopedMutex mtx(&objfree_mtx);
			if(q_objfree.IsEmpty()) {
				q_objfree.EnqueueJbyte(MSG_OBJ_FREE);
				q_objfree.event_count = 0;
				q_objfree.EnqueueJint(0);
			}
			q_objfree.event_count++;
			if(!q_objfree.EnqueueJlong(objectId)){
				jint tmpint = htons(q_objfree.event_count);
				q_objfree.Update(sizeof(jbyte), (char*)&tmpint, sizeof(jint));
				char* tmp=NULL;
				int len=0;
				q_objfree.GetData(tmp, len);
				ASSERT(tmp && len, "Error after Get Data");
				jlong nts = htobe64(objectId);
				Send(tmp, len, (char*)&nts, sizeof(jlong));
				q_objfree.Reset();
				return;
			}
		}
		bool NewClassEvent(const char* name, uint16_t nameLength, jlong classLoaderId, jint codeLength, jbyte *bytes){
			ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			//ScopedMutex mtx(&gl_mtx);
			//TODO optimization with pool
			Buffer tmp(100);
			tmp.EnqueueJbyte(MSG_NEW_CLASS);
			tmp.EnqueueStringUtf8(name, nameLength);
			tmp.EnqueueJlong(classLoaderId);
			tmp.EnqueueJint(codeLength);
			tmp.Enqueue((char*)bytes, codeLength);
			char* content;
			int len;
			tmp.GetData(content, len);
			return Send(content, len);
		}
		bool MethodRegisterEvent(int threadId, jshort methodId, const char* name, int length){
			ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			//TODO optimization with pool
			Buffer tmp(40);
			tmp.EnqueueJbyte(MSG_REG_ANALYSIS);
			tmp.EnqueueJshort(methodId);
			tmp.EnqueueStringUtf8(name, length);
			char* content;
			int len;
			tmp.GetData(content, len);
			return Send(content, len);
		}
		~ReProtocol(){
			pthread_mutex_destroy(&gl_mtx);
			pthread_mutex_destroy(&analysis_mtx);
			pthread_mutex_destroy(&objfree_mtx);

			//TODO//
		}
	private:
		ordering_id_type GetOrderingId(thread_id_type tid){
			//ScopedMutex mtx(&analysis_mtx);
			if(!running_oid.Exist(tid))
				running_oid.Set(tid, INVALID_ORDERING_ID);
			//ALOG(LOG_DEBUG,"HAIYANG","in %s %d:%d",__FUNCTION__, tid, (int)running_oid[tid]);
			return running_oid[tid];
		}
		void SetOrderingId(thread_id_type tid, ordering_id_type oid){
			//ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			//ScopedMutex mtx(&analysis_mtx);
			running_oid.Set(tid, oid);
		}
		bool Send(MsgType msg){
			//ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			char type = msg;
			return Send(&type, 1);
		}
		bool Send(const char* data, int length){
			//ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			for(int i = 0; i < length; i++){
			//	ALOG(LOG_DEBUG,"HAIYANG","Send content %d: %d", i, (int)data[i]);
			}
			return true;
			Socket sock;
			sock.connect(re_host, re_port);
			bool res;
			res = sock.send(data, length);
			ASSERT(res, "error in send packets");
			return res;
		}
		bool Send(const char* data, int length, const char* lastdata, int lastlength){
			//ALOG(LOG_DEBUG,"HAIYANG","in %s",__FUNCTION__);
			for(int i = 0; i < length; i++){
			//	ALOG(LOG_DEBUG,"HAIYANG","Send content %d: %d", i, (int)data[i]);
			}
			for(int i = 0; i < lastlength; i++){
			//	ALOG(LOG_DEBUG,"HAIYANG","Send content %d: %d", i+length, (int)lastdata[i]);
			}
			return true;
			Socket sock;
			sock.connect(re_host, re_port);
			bool res;
			res = sock.send(data, length);
			ASSERT(res, "error in send packets");
			res = sock.send(lastdata, lastlength);
			ASSERT(res, "error in send packets");
			return res;
		}

		pthread_mutex_t gl_mtx;

		ReQueue q_objfree;
		pthread_mutex_t objfree_mtx;


		Map<thread_id_type, ordering_id_type> running_oid;
		pthread_mutex_t analysis_mtx;

		Map<ordering_id_type, ReQueue*> analysis_queue;
		LockBuffer lock_buf;
		Map<ordering_id_type, Buffer*> invocation_buf;

		char re_host[MAXHOSTNAME];
		int re_port;
};

#endif
