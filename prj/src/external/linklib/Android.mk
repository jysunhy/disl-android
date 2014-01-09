#
# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This makefile supplies the rules for building a library of JNI code for
# use by our example of how to bundle a shared library with an APK.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := samples

# This is the target being built.
LOCAL_MODULE:= liblink
#APP_STL := stlport_static

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
	ReAgent.cpp \
	Socket.cpp
#	ReSocket.cpp

# All of the shared libraries we link against.
LOCAL_SHARED_LIBRARIES := \
	libutils libcutils libdvm #libstlport libstdc++

# No static libraries.
#LOCAL_STATIC_LIBRARIES := libstlport_static

# Also need the JNI headers.
LOCAL_C_INCLUDES +=	\
	$(JNI_H_INCLUDE) 
#	ndk/sources/cxx-stl/stlport/stlport

LOCAL_C_INCLUDES +=	\
	dalvik \
	dalvik/vm \
	external/zlib \
	libcore/include 

# $(DVM_H_INCLUDE)
# No special compiler flags.
LOCAL_CFLAGS += -DANDROID_SMP=1

# Don't prelink this library.  For more efficient code, you may want
# to add this library to the prelink map and set this to true. However,
# it's difficult to do this for applications that are not supplied as
# part of a system image.

LOCAL_PRELINK_MODULE := false

ifdef HISTORICAL_NDK_VERSIONS_ROOT # In the platform build system
include external/stlport/libstlport.mk
else # In the NDK build system
	LOCAL_C_INCLUDES += external/stlport/stlport bionic
endif

include $(BUILD_SHARED_LIBRARY)
#######################################################################
#include $(CLEAR_VARS)
# All code in LOCAL_WHOLE_STATIC_LIBRARIES will be built into this shared library.
#LOCAL_WHOLE_STATIC_LIBRARIES := libshadowvm_static

#ifdef HISTORICAL_NDK_VERSIONS_ROOT # In the platform build system
#LOCAL_SHARED_LIBRARIES := libstlport
#else # In the NDK build system
#LOCAL_SHARED_LIBRARIES := libstlport_static
#endif

#LOCAL_SHARED_LIBRARIES := \
	libutils libcutils libdvm #libstlport libstdc++

#LOCAL_MODULE := libshadowvm
#LOCAL_MODULE_TAGS := optional

#ifdef HISTORICAL_NDK_VERSIONS_ROOT # In the platform build system
#include external/stlport/libstlport.mk
#endif

#include $(BUILD_SHARED_LIBRARY)
