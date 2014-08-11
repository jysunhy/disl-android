#ifndef _REPROTOCOL_H_
#define _REPROTOCOL_H_

#include "ShadowLib.h"
#include "ReQueue.h"
#include "Map.h"
#include "Lock.h"
using namespace std;

#define INVALID_ORDERING_ID -1
#define INVALID_THREAD_ID -1

#define ANALYSIS_QUEUE_SIZE 64
#define INVOCATION_SIZE 32

#define BUFFER_INIT_SIZE 100

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
	MSG_MAPPID,
	// IPC EVENT
	MSG_IPC
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

			sendBuf = new Buffer(BUFFER_INIT_SIZE);
			isClosed = false;

		}
		//Called after fork
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

			delete sendBuf;
		}
		bool IsClosed(){
			return isClosed;
		}
		bool ConnectionClose(){
			//send all buffers
			Buffer tmp(10);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_CLOSE);

			Send(tmp.q_data,tmp.q_occupied);
			isClosed = true;
			return true;
		}

		bool OnIPCEvent(int tid, int transaction_id, short phase, int pid2, int tid2, jlong timestamp, bool isOneway){
			Buffer tmp(60);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_IPC);
			tmp.EnqueueJint(tid);
			tmp.EnqueueJint(transaction_id);
			tmp.EnqueueJshort(phase);
			tmp.EnqueueJint(pid2);
			tmp.EnqueueJint(tid2);
			tmp.EnqueueJlong(timestamp);
			tmp.EnqueueJboolean(isOneway);
			Send(tmp.q_data, tmp.q_occupied);
			return true;
		}
		bool OnForkEvent(int para){
			Buffer tmp(20);

			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_FORK);
			tmp.EnqueueJint(para);

			Send(tmp.q_data, tmp.q_occupied);
			return true;
		}



		bool AnalysisStartEvent(thread_id_type tid, ordering_id_type oid, short methodId){
			if(GetOrderingId(tid) != INVALID_ORDERING_ID) {
				return false;
			}
			//acquire the lock for the ordering id
			lock_buf.Lock(oid);
			//set thread tid's current ordering to oid
			SetOrderingId(tid, oid);
			if(!analysis_queue.Exist(oid))
				analysis_queue.Set(oid, new ReQueue(ANALYSIS_QUEUE_SIZE)); //TODO: NEED TO FREE THIS QUEUE IN DESTRUCTURE

			if(analysis_queue[oid]->IsEmpty()){
				LOGASSERT((jbyte)MSG_ANALYZE == MSG_ANALYZE, "WRONG TYPE CONVERSION");
				analysis_queue[oid]->EnqueueJint(getpid());

				analysis_queue[oid]->EnqueueJbyte(MSG_ANALYZE);
				analysis_queue[oid]->EnqueueJlong(oid);
				analysis_queue[oid]->EnqueueJint(0);
				analysis_queue[oid]->event_count=0;
			}
			analysis_queue[oid]->event_count++;
			jint tmp = htonl(analysis_queue[oid]->event_count);
			analysis_queue[oid]->Update(sizeof(jint)+sizeof(jbyte)+sizeof(jlong),(char*)&tmp, sizeof(jint));
			if(!invocation_buf.Exist(oid)){
				invocation_buf.Set(oid, new Buffer(INVOCATION_SIZE));
			}
			invocation_buf[oid]->Reset();
			invocation_buf[oid]->EnqueueJshort(methodId);
			invocation_buf[oid]->EnqueueJshort(0); //space for arg length
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
				tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_STRING_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8((const char*)utf8, len*sizeof(jchar));
			Send(tmp.q_data, tmp.q_occupied);
			return true;
		}
		int SendStringObject(thread_id_type tid, jlong netref, const char* utf8, int len){
			Buffer tmp(100);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_STRING_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(utf8, len);
			Send(tmp.q_data, tmp.q_occupied);
			return tmp.q_occupied;
		}
		int SendThreadObject(thread_id_type tid, jlong netref, const char* threadName, int len, jboolean isDaemon){
			Buffer tmp(100);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_THREAD_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(threadName, len);
			tmp.EnqueueJboolean(isDaemon);
			Send(tmp.q_data, tmp.q_occupied);
			return tmp.q_occupied;
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
				LOGERROR("invalid tid in end event");
				return false;
			}
			char *buf=NULL;
			int len_buf;
			invocation_buf[oid]->GetData(buf, len_buf);
			jshort arglen = len_buf - sizeof(jshort)*2;
			arglen = htons(arglen);
			//update the argument size
			invocation_buf[oid]->Update(sizeof(jshort),(char*)&arglen, sizeof(jshort));
			invocation_buf[oid]->GetData(buf, len_buf);
			bool full = !(analysis_queue[oid]->Enqueue(buf, len_buf));
			//TODO & NOTE
			//currently, because fork will introduce a problem that the analysis_queue contatining events from two different pids
			//to avoid so currently we send a single event a time, and the analysis_count is always 1
			if(full){
				char* q=NULL;
				int len_q;
				analysis_queue[oid]->GetData(q, len_q);
				Send(q, len_q, (char*)buf, len_buf);
				analysis_queue[oid]->Reset();
			}else{ 
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
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_MAPPID);
			tmp.EnqueueStringUtf8(pname, strlen(pname));
			return Send(tmp.q_data, tmp.q_occupied);
		}
		bool NewClassInfo(jlong netref, const char* className, int namelen, const char* generic, int glen, jlong netrefClassLoader, jlong netrefSuperClass){
			//TODO optimization with a buffer pool for reuse of buffer
			Buffer tmp(100);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_CLASS_INFO);
			tmp.EnqueueJlong(netref);
			tmp.EnqueueStringUtf8(className, namelen);
			tmp.EnqueueStringUtf8(generic, glen);
			tmp.EnqueueJlong(netrefClassLoader);
			tmp.EnqueueJlong(netrefSuperClass);
			return Send(tmp.q_data, tmp.q_occupied);
		}
		void ObjFreeEvent(jlong objectId){
			ScopedMutex mtx(&objfree_mtx);
			Buffer* tmp = new Buffer(40);
			tmp->EnqueueJint(getpid());
			tmp->EnqueueJbyte(MSG_OBJ_FREE);
			tmp->EnqueueJint(1);
			tmp->EnqueueJlong(objectId);
			Send(tmp->q_data, tmp->q_occupied);
			delete tmp;
		}
		bool NewClassEvent(const char* name, uint16_t nameLength, jlong classLoaderId, jint codeLength, const char *bytes){
			//TODO optimization with pool
			Buffer tmp(100);
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
			return Send(tmp.q_data, tmp.q_occupied);
		}
		bool MethodRegisterEvent(int threadId, jshort methodId, const char* name, int length){
			//TODO optimization with pool
			Buffer tmp(40);
			tmp.EnqueueJint(getpid());
			tmp.EnqueueJbyte(MSG_REG_ANALYSIS);
			tmp.EnqueueJshort(methodId);
			tmp.EnqueueStringUtf8(name, length);
			return Send(tmp.q_data, tmp.q_occupied);
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
			if(!running_oid.Exist(tid))
				running_oid.Set(tid, INVALID_ORDERING_ID);
			return running_oid[tid];
		}
		void SetOrderingId(thread_id_type tid, ordering_id_type oid){
			running_oid.Set(tid, oid);
		}
		bool Send(MsgType msg){
			char type = msg;
			return Send(&type, 1);
		}
		bool Send(const char* data, int length){
			pthread_mutex_lock(&gl_mtx);
			//we need trigger the signal for the send loop if the buffer is empty
			bool needSignal = sendBuf->q_occupied == 0;
			if(!isClosed)
				sendBuf->Enqueue(data,length);
			if(DEBUGMODE)
				ALOG(LOG_DEBUG, "PACKET", "TOTAL: %d FIRST 10 BYTES:\t\t: %d %d %d %d, %d, %d %d %d %d", length, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
			pthread_mutex_unlock(&gl_mtx);
			if(needSignal) {
				pthread_cond_signal(&new_event_cond);
			}
			return true;
		}
		bool Send(const char* data, int length, const char* lastdata, int lastlength){
			pthread_mutex_lock(&gl_mtx);
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

		Buffer *sendBuf;
		bool isClosed;
};

#endif
