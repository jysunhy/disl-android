#include "Debug.h"
//#include "Common.h"
#include "ReProtocol.h"

int main(){
	ReProtocol p("127.0.0.1", 11218);
	const char* method="ch.usi.dag.disl.test.dispatch.CodeExecuted.testingBasic";
	p.MethodRegisterEvent(1,123, method, strlen(method));
	for(int i = 0; i < 15; i++) {
		DEBUG("IN %d", i);
		p.AnalysisStartEvent(1, 127, 123);
		p.SendJboolean(1, true);
		p.SendJbyte(1, 125);
		p.SendJchar(1, 's');
		p.SendJshort(1, 50000);
		p.SendJint(1, 100000);
		p.SendJlong(1, 10000000000L);
		p.SendJfloat(1, 1.5F);
		p.SendJdouble(1, 2.5);
		p.AnalysisEndEvent(1);
		sleep(2);
	}
	sleep(5);
	p.ConnectionClose();
}
