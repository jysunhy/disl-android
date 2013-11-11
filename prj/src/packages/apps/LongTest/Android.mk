LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

WITH_DEXPREOPT := false

LOCAL_MODULE_TAGS := samples 

LOCAL_SRC_FILES := $(call all-java-files-under, src)

APP_STL := stlport_static

LOCAL_JNI_SHARED_LIBRARIES := libarithmetic


LOCAL_PACKAGE_NAME := LongTest

LOCAL_SHARED_LIBRARIES := \
		libutils \
		liblog

#	libstdc++ \
#		libdvm 

include $(BUILD_PACKAGE)
include $(LOCAL_PATH)/jni/Android.mk 
# ============================================================

# Also build all of the sub-targets under this one: the shared library.
include $(call all-makefiles-under,$(LOCAL_PATH))

