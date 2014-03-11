#ifndef _REPROTOCOL_H_
#define _REPROTOCOL_H_

#include "ShadowLib.h"
#include "ReQueue.h"
//#include "Socket.h"
#include "Map.h"
#include "Lock.h"
//#include <map>
using namespace std;

#define INVALID_ORDERING_ID -1
#define INVALID_THREAD_ID -1

#define ANALYSIS_QUEUE_SIZE 64
#define INVOCATION_SIZE 32

#define BUFFER_INIT_SIZE 100

#define MP 1

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
	MSG_THREAD_END,
	// sending fork
	MSG_FORK,
	// MAP PID TO PNAME
	MSG_MAPPID
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
			pthread_cond_init(&new_event_cond, NULL);

			//re_port = port;
			//memset(re_host, '\0', MAXHOSTNAME);
			//memcpy(re_host, host, strlen(host) < MAXHOSTNAME?strlen(host):MAXHOSTNAME);

			sendBuf = new Buffer(BUFFER_INIT_SIZE);
			isClosed = false;

		}
		void UpdateMutex(){
			pthread_mutex_init(&gl_mtx, NULL);
			pthread_mutex_init(&objfree_mtx, NULL);
			pthread_mutex_init(&analysis_mtx, NULL);
			pthread_cond_init(&new_event_cond, NULL);
		}

		~ReProtocol(){
			pthread_mutex_destroy(&gl_mtx);
			pthread_cond_destroy(&new_event_cond);
			pthread_mutex_destroy(&analysis_mtx);
			pthread_mutex_destroy(&objfree_mtx);

			//delete sock;
			delete sendBuf;
			//TODO//
		}
		bool IsClosed(){
			return isClosed;
		}
		bool ConnectionClose(){
			//send all buffers
			//TODO ADD ALL to BUFFER
			Buffer tmp(10);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_CLOSE);

			Send(tmp.q_data,tmp.q_occupied);
			isClosed = true;
			return true;
		}

		bool OnForkEvent(int para){
			Buffer tmp(20);

			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_FORK);
			tmp.EnqueueJint(para);

			if(MP)
				Send(tmp.q_data, tmp.q_occupied);
			return true;
		}



		bool AnalysisStartEvent(thread_id_type tid, ordering_id_type oid, short methodId){
			if(GetOrderingId(tid) != INVALID_ORDERING_ID) {
				return false;
			}
			//LOGDEBUG("IN %s", __FUNCTION__);

			lock_buf.Lock(oid);
			SetOrderingId(tid, oid);
			if(!analysis_queue.Exist(oid))
				analysis_queue.Set(oid, new ReQueue(ANALYSIS_QUEUE_SIZE)); //NEED TO FREE THIS QUEUE IN DESTRUCTURE

			if(analysis_queue[oid]->IsEmpty()){
				LOGASSERT((jbyte)MSG_ANALYZE == MSG_ANALYZE, "WRONG TYPE CONVERSION");
				if(MP)
					analysis_queue[oid]->EnqueueJint(getpid());

				analysis_queue[oid]->EnqueueJbyte(MSG_ANALYZE);
				analysis_queue[oid]->EnqueueJlong(oid);
				analysis_queue[oid]->EnqueueJint(0);
				analysis_queue[oid]->event_count=0;
			}
			analysis_queue[oid]->event_count++;
			jint tmp = htonl(analysis_queue[oid]->event_count);
			//LOGDEBUG("%d,%d,%d", (int)analysis_queue[oid]->event_count,tmp,sizeof(jbyte)+sizeof(jlong));
			if(MP)
				analysis_queue[oid]->Update(sizeof(jint)+sizeof(jbyte)+sizeof(jlong),(char*)&tmp, sizeof(jint));
			else
				analysis_queue[oid]->Update(sizeof(jbyte)+sizeof(jlong),(char*)&tmp, sizeof(jint));
			if(!invocation_buf.Exist(oid)){
				invocation_buf.Set(oid, new Buffer(INVOCATION_SIZE));
			}
			invocation_buf[oid]->Reset();
			//LOGDEBUG("analysis Id: %d",(int)methodId);
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
			jint nts = htonl(data);
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
			return SendJlong(tid, netref);
		}
		bool SendStringObject(thread_id_type tid, jlong netref, const jchar* utf8, int len){
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_STRING_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8((const char*)utf8, len*sizeof(jchar));
			Send(tmp.q_data, tmp.q_occupied);
			//char* content;
			//int packet_len;
			//tmp.GetData(content, packet_len);
			//if(!isClosed)
			//	sendBuf->Enqueue(content,packet_len);
			return true;
			//return sock->Send(content,packet_len);
		}
		int SendStringObject(thread_id_type tid, jlong netref, const char* utf8, int len){
			//ScopedMutex mtx(&gl_mtx);
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_STRING_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(utf8, len);
			Send(tmp.q_data, tmp.q_occupied);
			return tmp.q_occupied;

			//char* content;
			//int packet_len;
			//tmp.GetData(content, packet_len);
			//if(!isClosed)
			//	sendBuf->Enqueue(content,packet_len);
			//return packet_len;
			//return sock->Send(content,packet_len);
		}
		int SendThreadObject(thread_id_type tid, jlong netref, const char* threadName, int len, jboolean isDaemon){
			//ScopedMutex mtx(&gl_mtx);
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_THREAD_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(threadName, len);
			tmp.EnqueueJboolean(isDaemon);
			Send(tmp.q_data, tmp.q_occupied);
			return tmp.q_occupied;
			//char* content;
			//int packet_len;
			//tmp.GetData(content, packet_len);
			//if(!isClosed)
			//	sendBuf->Enqueue(content,packet_len);
			//return packet_len;
			//return sock->Send(content,packet_len);
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
			if(oid == INVALID_ORDERING_ID) {
				//ALOG(LOG_DEBUG,"SHADOW","in %s thread id: %d, error end event",__FUNCTION__, tid);
				LOGERROR("invalid tid in end event");
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
			//analysis_queue[oid]->Print();
			bool full = !(analysis_queue[oid]->Enqueue(buf, len_buf));
			if(full){
				char* q=NULL;
				int len_q;
				analysis_queue[oid]->GetData(q, len_q);
				Send(q, len_q, (char*)buf, len_buf);
				//analysis_queue[oid]->Print();
				analysis_queue[oid]->Reset();
			}else{ //EACH ANALYSIS IS SENT ALONE
				char* q=NULL;
				int len_q;
				analysis_queue[oid]->GetData(q, len_q);
				Send(q, len_q);
				analysis_queue[oid]->Reset();
			}

			invocation_buf[oid]->Reset();
			SetOrderingId(tid, INVALID_ORDERING_ID);
			lock_buf.Unlock(oid);
			return true;
		}
		bool MapPidPname(jint pid, const char* pname){
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_MAPPID);
			tmp.EnqueueStringUtf8(pname, strlen(pname));
			return Send(tmp.q_data, tmp.q_occupied);
		}
		bool NewClassInfo(jlong netref, const char* className, int namelen, const char* generic, int glen, jlong netrefClassLoader, jlong netrefSuperClass){
			//ALOG(LOG_DEBUG,"SHADOW","new class info %s:%lld", className, netref);
			//ScopedMutex mtx(&gl_mtx);
			//TODO optimization with pool
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_CLASS_INFO);
			tmp.EnqueueJlong(netref);
			/*const char *tmpname = className;
			  int tmplen = namelen;
			  if(className[0]=='L'){
			  tmpname++;
			  tmplen--;
			  }
			  if(className[namelen-1] == ';')
			  tmplen--;
			  tmp.EnqueueStringUtf8(tmpname, tmplen);
			  */
			tmp.EnqueueStringUtf8(className, namelen);
			tmp.EnqueueStringUtf8(generic, glen);
			tmp.EnqueueJlong(netrefClassLoader);
			tmp.EnqueueJlong(netrefSuperClass);
			//char* content;
			//int len;
			//tmp.GetData(content, len);
			return Send(tmp.q_data, tmp.q_occupied);
		}
		void ObjFreeEvent(jlong objectId){
			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			ScopedMutex mtx(&objfree_mtx);
			Buffer* tmp = new Buffer(40);
			tmp->EnqueueJint(getpid());
			tmp->EnqueueJbyte(MSG_OBJ_FREE);
			tmp->EnqueueJint(1);
			tmp->EnqueueJlong(objectId);
			Send(tmp->q_data, tmp->q_occupied);
			delete tmp;

			/*
			   if(q_objfree.IsEmpty()) {
			   if(MP)
			   q_objfree.EnqueueJint(getpid());

			   q_objfree.EnqueueJbyte(MSG_OBJ_FREE);
			   q_objfree.event_count = 0;
			   q_objfree.EnqueueJint(0);
			   }
			   q_objfree.event_count++;
			   if(!q_objfree.EnqueueJlong(objectId)){
			   jint tmpint = htonl(q_objfree.event_count);
			   if(MP)
			   q_objfree.Update(sizeof(jint)+sizeof(jbyte), (char*)&tmpint, sizeof(jint));
			   else
			   q_objfree.Update(sizeof(jbyte), (char*)&tmpint, sizeof(jint));
			   char* tmp=NULL;
			   int len=0;
			   q_objfree.GetData(tmp, len);
			   LOGASSERT(tmp && len, "Error after Get Data");
			   jlong nts = htobe64(objectId);
			   Send(tmp, len, (char*)&nts, sizeof(jlong));
			   q_objfree.Reset();
			   return;
			   }
			   */
		}
		bool NewClassEvent(const char* name, uint16_t nameLength, jlong classLoaderId, jint codeLength, const char *bytes){
			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			//ScopedMutex mtx(&gl_mtx);
			//TODO optimization with pool
			Buffer tmp(100);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_NEW_CLASS);
			const char *tmpname = name;
			int tmplen = nameLength;
			if(name[0]=='L'){
				tmpname++;
				tmplen--;
			}
			if(name[nameLength-1] == ';')
				tmplen--;

			tmp.EnqueueStringUtf8(tmpname, tmplen);
			tmp.EnqueueJlong(classLoaderId);
			tmp.EnqueueJint(codeLength);
			tmp.Enqueue(bytes, codeLength);
			//char* content;
			//int len;
			//tmp.GetData(content, len);
			return Send(tmp.q_data, tmp.q_occupied);
			//return Send(content, len);
		}
		bool MethodRegisterEvent(int threadId, jshort methodId, const char* name, int length){
			//TODO optimization with pool
			Buffer tmp(40);
			if(MP)
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_REG_ANALYSIS);
			tmp.EnqueueJshort(methodId);
			tmp.EnqueueStringUtf8(name, length);
			//char* content;
			//int len;
			//tmp.GetData(content, len);
			return Send(tmp.q_data, tmp.q_occupied);
			//return Send(content, len);
		}
		int GetCurrentSize(){
			return sendBuf->q_occupied;
		}
		Buffer* ReturnAndResetBufferNolock(){
			Buffer *res = sendBuf;
			sendBuf = new Buffer(BUFFER_INIT_SIZE);
			return res;
		}
		Buffer* ReturnAndResetBuffer(){
			ScopedMutex mtx(&gl_mtx);
			Buffer *res = sendBuf;
			sendBuf = new Buffer(BUFFER_INIT_SIZE);
			return res;
		}
		pthread_mutex_t gl_mtx;
		pthread_cond_t new_event_cond;
	private:
		ordering_id_type GetOrderingId(thread_id_type tid){
			//ScopedMutex mtx(&analysis_mtx);
			if(!running_oid.Exist(tid))
				running_oid.Set(tid, INVALID_ORDERING_ID);
			//ALOG(LOG_DEBUG,"SHADOW","in %s %d:%d",__FUNCTION__, tid, (int)running_oid[tid]);
			return running_oid[tid];
		}
		void SetOrderingId(thread_id_type tid, ordering_id_type oid){
			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			//ScopedMutex mtx(&analysis_mtx);
			running_oid.Set(tid, oid);
		}
		bool Send(MsgType msg){
			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			char type = msg;
			return Send(&type, 1);
		}
		bool Send(const char* data, int length){
			pthread_mutex_lock(&gl_mtx);

			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			//for(int i = 0; i < length; i++){
			//	printf("%d:%d ", i, (int)data[i]);
			//}
			//return true;
			//bool res = true;
			//size = length+1;
			//sock.Send((char*)(&size),sizeof(int));
			//while(!sock) {
			//	OpenConnection();
			//}
			bool needSignal = sendBuf->q_occupied == 0;
			if(!isClosed)
				sendBuf->Enqueue(data,length);
			if(DEBUGMODE)
				ALOG(LOG_DEBUG, "PACKET", "TOTAL: %d FIRST 10 BYTES:\t\t: %d %d %d %d, %d, %d %d %d %d", length, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
			//res = sock->Send(data, length);

			//char close = MSG_CLOSE;
			//sock.Send(&close, 1);

			//LOGASSERT(res, "error in send packets");
			//ALOG(LOG_DEBUG,"SHADOW","out %s",__FUNCTION__);
			pthread_mutex_unlock(&gl_mtx);
			if(needSignal) {
				pthread_cond_signal(&new_event_cond);
				ALOG(LOG_DEBUG,"SHADOW","SENDING SIGNAL");
				sleep(1);
			}
			return true;
		}
		bool Send(const char* data, int length, const char* lastdata, int lastlength){
			pthread_mutex_lock(&gl_mtx);
			//ALOG(LOG_DEBUG,"SHADOW","in %s",__FUNCTION__);
			//for(int i = 0; i < length; i++){
			//	ALOG(LOG_DEBUG,"SHADOW","Send content %d: %d", i, (int)data[i]);
			//	}
			//	for(int i = 0; i < lastlength; i++){
			//	ALOG(LOG_DEBUG,"SHADOW","Send content %d: %d", i+length, (int)lastdata[i]);
			//	}
			//return true;
			//for(int i = 0; i < length; i++){
			//	printf("%d:%d ", i, (int)data[i]);
			//}
			//for(int i = 0; i < lastlength; i++){
			//	printf("%d:%d ", i+length, (int)lastdata[i]);
			//}
			//bool res=true;
			//size = length+lastlength+1;
			//sock.Send((char*)(&size),sizeof(int));
			//while(!sock){
			//	OpenConnection();
			//}
			//res = sock->Send(data, length);
			//LOGASSERT(res, "error in send packets");
			//res = sock->Send(lastdata, lastlength);
			//LOGASSERT(res, "error in send packets");
			//char close = MSG_CLOSE;
			//sock.Send(&close, 1);
			bool needSignal = sendBuf->q_occupied == 0;
			if(!isClosed){
				sendBuf->Enqueue(data,length);
				sendBuf->Enqueue(lastdata, lastlength);
			}
			if(DEBUGMODE)
				ALOG(LOG_DEBUG, "PACKET", "TOTAL: %d FIRST 5 BYTES:\t\t: %d %d %d %d, %d", length+lastlength, data[0], data[1], data[2], data[3], data[4]);
			pthread_mutex_unlock(&gl_mtx);
			if(needSignal) {
				pthread_cond_signal(&new_event_cond);
				ALOG(LOG_DEBUG,"SHADOW","SENDING SIGNAL");
				sleep(1);
			}
			return true;
		}

		ReQueue q_objfree;
		pthread_mutex_t objfree_mtx;


		Map<thread_id_type, ordering_id_type> running_oid;
		pthread_mutex_t analysis_mtx;

		Map<ordering_id_type, ReQueue*> analysis_queue;
		LockBuffer lock_buf;
		Map<ordering_id_type, Buffer*> invocation_buf;

		//char re_host[MAXHOSTNAME];
		//int re_port;

		//Socket *sock;
		Buffer *sendBuf;
		bool isClosed;
};

#endif
