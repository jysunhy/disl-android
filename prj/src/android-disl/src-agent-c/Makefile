# if there is JAVA_HOME var file then include it
ifneq (, $(wildcard javahome/var))
include javahome/var
endif

BUILDDIR=../build

# Source lists
LIBNAME=dislagent
SOURCES=dislagent.c

# Object files needed to create library
OBJECTS=$(SOURCES:%.c=%.o)

# Library name and options needed to build it
UNAME := $(shell uname)
ifeq ($(UNAME), Linux)
LIBRARY=lib$(LIBNAME).so
JNI_OS=linux
else ifeq ($(UNAME), Darwin)
LIBRARY=lib$(LIBNAME).jnilib
JNI_OS=darwin
else
$(error error: unsupported operating system: $(UNAME))
endif


# Building a shared library
LINK_SHARED=$(LINK.c) -shared -o $@

# GNU Compiler options needed to build it
COMMON_FLAGS=-fPIC
# Options that help find errors
COMMON_FLAGS+= -W -Wall -Wextra -O3 -Wno-unused-parameter

CFLAGS += $(COMMON_FLAGS)

CFLAGS += -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(JNI_OS)

# add debugging output
ifeq ($(DEBUG), TRUE)
CFLAGS += -DDEBUG
endif

build: $(LIBRARY)
	cp $(LIBRARY) $(BUILDDIR)

# Build native library
$(LIBRARY): $(OBJECTS)
	$(LINK_SHARED) $(OBJECTS) $(LIBRARIES)

# Cleanup the built bits
clean:
	rm -f $(LIBRARY) $(OBJECTS)

debug:
	$(MAKE) DEBUG=TRUE

all: build
