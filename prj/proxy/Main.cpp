#include "ByteBuffer.h"
#include "instr_header.h"

int main(){
	ByteBuffer bf(1);
	char* a=bf.GetBuffer(1000);
	char* b=bf.GetBuffer(2000);
	bf.ReturnBuffer(a,1000);
	bf.ReturnBuffer(b,2000);
}
