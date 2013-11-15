#ifndef _REPROTOCOL_H_
#define _REPROTOCOL_H_

#include "Common.h"
#include "ReQueue.h"
#include "Socket.h"
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

ReQueue q_objfree;
//map<long, ReQueue> q_invocation_map;

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
		void InvocationStartEvent(long orderingId, short methodId){
			UpdateMutexMap(orderingId);
			//pthread_mutex_lock(invocation_mtx[orderingId]);
			current_ordering_id = orderingId;
		}
		void InvocationEndEvent(){
			//pthread_mutex_unlock(invocation_mtx[current_ordering_id]);
		}

		void ObjFreeEvent(jlong objectId){
			ScopedMutex mtx(&objfree_mtx);
			if(q_objfree.IsEmpty())
			{
				q_objfree.EnqueueJbype(MSG_OBJ_FREE);
			}
			if(!q_objfree.EnqueueJlong(objectId)){
				char* tmp=NULL;
				int len=0;
				q_objfree.GetData(tmp, len);
				ASSERT(tmp && len, "Error after Get Data");
				Send(tmp, len);
				q_objfree.Reset();
				ObjFreeEvent(objectId);
			}
		}

	//	void MethodRegisterEvent(string name, int threadId){
	//	}
		~ReProtocol(){
			//destroy the invocation_mtx and gl_mtx;
			pthread_mutex_destroy(&gl_mtx);
			pthread_mutex_destroy(&objfree_mtx);
			/*for(map<long, pthread_mutex_t*>::iterator iter = invocation_mtx.begin(); iter != invocation_mtx.end(); iter++){
				pthread_mutex_destroy(iter->second);
				delete iter->second;
			}*/

		}
	private:
		void UpdateMutexMap(long orderingId){
			ScopedMutex mtx(&gl_mtx);
			//if(invocation_mtx.find(orderingId) == invocation_mtx.end()){
			//	invocation_mtx[orderingId] = new pthread_mutex_t;
			//	pthread_mutex_init(invocation_mtx[orderingId], NULL);
			//}
		}

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
		//map<long, pthread_mutex_t*> invocation_mtx;
		//map<long, Buffer*> invocation_buf;

		long current_ordering_id;

		char re_host[MAXHOSTNAME];
		int re_port;
};

#endif
