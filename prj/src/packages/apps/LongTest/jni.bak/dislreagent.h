#include <jni.h>

#ifndef _DISLAGENT_H
#define	_DISLAGENT_H

#ifdef __cplusplus
extern "C" {
#endif

jint JNI_OnLoad(JavaVM *jvm, void *reserved);

#ifdef __cplusplus
}
#endif

#endif	/* _DISLAGENT_H */
