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
			current_ordering_id = INVALID_ORDERING_ID;
			re_port = port;
			memset(re_host, '\0', MAXHOSTNAME);
			memcpy(re_host, host, strlen(host) < MAXHOSTNAME?strlen(host):MAXHOSTNAME);
		}
		void AnalysisStartEvent(ordering_id_type orderingId, short methodId){
			//UpdateMutexMap(orderingId);
			//pthread_mutex_lock(invocation_mtx[orderingId]);
			lock_buf.Lock(orderingId);
			current_ordering_id = orderingId;
		}
		void AnalysisEndEvent(){
			
			lock_buf.Unlock(current_ordering_id);
			//pthread_mutex_unlock(invocation_mtx[current_ordering_id]);
		}

		void ObjFreeEvent(jlong objectId){
			pthread_mutex_lock(&objfree_mtx);
			if(q_objfree.IsEmpty())
				q_objfree.EnqueueJbype(MSG_OBJ_FREE);
			if(!q_objfree.EnqueueJlong(objectId)){
				char* tmp=NULL;
				int len=0;
				q_objfree.GetData(tmp, len);
				ASSERT(tmp && len, "Error after Get Data");
				Send(tmp, len);
				q_objfree.Reset();
				pthread_mutex_unlock(&objfree_mtx);
				ObjFreeEvent(objectId);
				return;
			}
			pthread_mutex_unlock(&objfree_mtx);
		}

	//	void MethodRegisterEvent(string name, int threadId){
	//	}
		~ReProtocol(){
			pthread_mutex_destroy(&gl_mtx);
			pthread_mutex_destroy(&objfree_mtx);
		}
	private:
		bool Send(const char* data, int length){
			Socket sock;
			sock.connect(re_host, re_port);
			bool res;
			res = sock.send(data, length);
			ASSERT(res, "error in send packets");
			return res;
		}

		pthread_mutex_t gl_mtx;

		pthread_mutex_t objfree_mtx;
		ReQueue q_objfree;

		LockBuffer lock_buf;
		Map<ordering_id_type, Buffer*> invocation_buf;
		Map<ordering_id_type, ReQueue*> analysis_q;

		long current_ordering_id;

		char re_host[MAXHOSTNAME];
		int re_port;
};

#endif
