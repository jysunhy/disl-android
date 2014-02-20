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
# Common definitions for host or target builds of libdvm.
#
# If you enable or disable optional features here, make sure you do
# a "clean" build -- not everything depends on Dalvik.h.  (See Android.mk
# for the exact command.)
#


#
# Compiler defines.
#
LOCAL_CFLAGS += -fstrict-aliasing -Wstrict-aliasing=2 -fno-align-jumps
LOCAL_CFLAGS += -Wall -Wextra -Wno-unused-parameter
LOCAL_CFLAGS += -DARCH_VARIANT=\"$(dvm_arch_variant)\"
LOCAL_SHARED_LIBRARIES += libdvm

#
# Optional features.  These may impact the size or performance of the VM.
#

# Make a debugging version when building the simulator (if not told
# otherwise) and when explicitly asked.
dvm_make_debug_vm := false
ifneq ($(strip $(DEBUG_DALVIK_VM)),)
  dvm_make_debug_vm := $(DEBUG_DALVIK_VM)
endif

ifeq ($(dvm_make_debug_vm),true)
  #
  # "Debug" profile:
  # - debugger enabled
  # - profiling enabled
  # - tracked-reference verification enabled
  # - allocation limits enabled
  # - GDB helpers enabled
  # - LOGV
  # - assert()
  #
  LOCAL_CFLAGS += -DWITH_INSTR_CHECKS
  LOCAL_CFLAGS += -DWITH_EXTRA_OBJECT_VALIDATION
  LOCAL_CFLAGS += -DWITH_TRACKREF_CHECKS
  LOCAL_CFLAGS += -DWITH_EXTRA_GC_CHECKS=1
  #LOCAL_CFLAGS += -DCHECK_MUTEX
  LOCAL_CFLAGS += -DDVM_SHOW_EXCEPTION=3
  # add some extra stuff to make it easier to examine with GDB
  LOCAL_CFLAGS += -DEASY_GDB
  # overall config may be for a "release" build, so reconfigure these
  LOCAL_CFLAGS += -UNDEBUG -DDEBUG=1 -DLOG_NDEBUG=1 -DWITH_DALVIK_ASSERT
else  # !dvm_make_debug_vm
  #
  # "Performance" profile:
  # - all development features disabled
  # - compiler optimizations enabled (redundant for "release" builds)
  # - (debugging and profiling still enabled)
  #
  #LOCAL_CFLAGS += -DNDEBUG -DLOG_NDEBUG=1
  # "-O2" is redundant for device (release) but useful for sim (debug)
  #LOCAL_CFLAGS += -O2 -Winline
  #LOCAL_CFLAGS += -DWITH_EXTRA_OBJECT_VALIDATION
  LOCAL_CFLAGS += -DDVM_SHOW_EXCEPTION=1
  # if you want to try with assertions on the device, add:
  #LOCAL_CFLAGS += -UNDEBUG -DDEBUG=1 -DLOG_NDEBUG=1 -DWITH_DALVIK_ASSERT
endif  # !dvm_make_debug_vm

# bug hunting: checksum and verify interpreted stack when making JNI calls
#LOCAL_CFLAGS += -DWITH_JNI_STACK_CHECK

LOCAL_SRC_FILES := \
	ShadowAPI.cpp

# TODO: this is the wrong test, but what's the right one?

WITH_COPYING_GC := $(strip $(WITH_COPYING_GC))

ifeq ($(WITH_COPYING_GC),true)
  LOCAL_CFLAGS += -DWITH_COPYING_GC
endif

WITH_JIT := $(strip $(WITH_JIT))

ifeq ($(WITH_JIT),true)
  LOCAL_CFLAGS += -DWITH_JIT
endif

LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE) \
	dalvik \
	dalvik/vm \
	external/zlib \
	libcore/include \

LOCAL_EXPORT_C_INCLUDES := dalvik/vm

MTERP_ARCH_KNOWN := false

ifeq ($(dvm_arch),arm)
  #dvm_arch_variant := armv7-a
  #LOCAL_CFLAGS += -march=armv7-a -mfloat-abi=softfp -mfpu=vfp
  LOCAL_CFLAGS += -Werror
  MTERP_ARCH_KNOWN := true
  # Select architecture-specific sources (armv5te, armv7-a, etc.)
endif

ifeq ($(dvm_arch),x86)
  ifeq ($(dvm_os),linux)
    MTERP_ARCH_KNOWN := true
    LOCAL_CFLAGS += -DDVM_JMP_TABLE_MTERP=1
  endif
endif

ifeq ($(dvm_arch),sh)
  MTERP_ARCH_KNOWN := true
endif

ifeq ($(MTERP_ARCH_KNOWN),false)
  # unknown architecture, try to use FFI
  LOCAL_C_INCLUDES += external/libffi/$(dvm_os)-$(dvm_arch)

  ifeq ($(dvm_os)-$(dvm_arch),darwin-x86)
      # OSX includes libffi, so just make the linker aware of it directly.
      LOCAL_LDLIBS += -lffi
  else
      LOCAL_SHARED_LIBRARIES += libffi
  endif

  # The following symbols are usually defined in the asm file, but
  # since we don't have an asm file in this case, we instead just
  # peg them at 0 here, and we add an #ifdef'able define for good
  # measure, too.
  LOCAL_CFLAGS += -DdvmAsmInstructionStart=0 -DdvmAsmInstructionEnd=0 \
	-DdvmAsmSisterStart=0 -DdvmAsmSisterEnd=0 -DDVM_NO_ASM_INTERP=1
endif
