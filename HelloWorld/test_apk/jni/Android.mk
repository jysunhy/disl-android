LOCAL_PATH := $(call my-dir)
include $(call all-subdir-makefiles)
include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE    :=driver
LOCAL_SRC_FILES := native.c
include $(BUILD_SHARED_LIBRARY)
