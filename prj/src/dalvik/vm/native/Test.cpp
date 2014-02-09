#include "Dalvik.h"
#include "native/InternalNativePriv.h"
#include <cutils/log.h>

static void Dalvik_ch_usi_dag_dislre_AREDispatch_test(const u4* args, JValue* pResult){
	ALOG(LOG_DEBUG,"SHADOW","IN TEST NATIVE: %s",__FUNCTION__);
	RETURN_VOID();

}

const DalvikNativeMethod dvm_ch_usi_dag_dislre_AREDispatch[] = {
    { "test",  "()V",
        Dalvik_ch_usi_dag_dislre_AREDispatch_test},
    { NULL, NULL, NULL },
};
