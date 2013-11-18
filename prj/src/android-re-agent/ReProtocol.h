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

#define ANALYSIS_QUEUE_SIZE 1024
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
			if(GetOrderingId(tid) != INVALID_ORDERING_ID)
				return false;
			
			lock_buf.Lock(oid);
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
			return true;
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
			if(oid == INVALID_ORDERING_ID)
				return false;
			char *buf=NULL;
			int len_buf;
			invocation_buf[oid]->GetData(buf, len_buf);
			invocation_buf[oid]->Update(sizeof(jbyte)+sizeof(jshort),(char*)buf,len_buf-sizeof(jshort)*2);
			invocation_buf[oid]->GetData(buf, len_buf);
			bool full = analysis_queue[oid]->Enqueue(buf, len_buf);
			if(full){
				char* q=NULL;
				int len_q;
				analysis_queue[oid]->GetData(q, len_q);
				Send(q, len_q, (char*)buf, len_buf);
				analysis_queue[oid]->Reset();
				invocation_buf[oid]->Reset();
			}
			SetOrderingId(tid, INVALID_ORDERING_ID);
			lock_buf.Unlock(oid);
			return true;
		}
		void ObjFreeEvent(jlong objectId){
			pthread_mutex_lock(&objfree_mtx);
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
				pthread_mutex_unlock(&objfree_mtx);
				return;
			}
			pthread_mutex_unlock(&objfree_mtx);
		}
		void MethodRegisterEvent(string name, int threadId){
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
			return running_oid[tid];
		}
		void SetOrderingId(thread_id_type tid, ordering_id_type oid){
			//ScopedMutex mtx(&analysis_mtx);
			running_oid.Set(tid, oid);
		}
		bool Send(MsgType msg){
			char type = msg;
			return Send(&type, 1);
		}
		bool Send(const char* data, int length){
			Socket sock;
			sock.connect(re_host, re_port);
			bool res;
			res = sock.send(data, length);
			ASSERT(res, "error in send packets");
			return res;
		}
		bool Send(const char* data, int length, const char* lastdata, int lastlength){
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
