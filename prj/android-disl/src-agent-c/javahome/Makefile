UNAME := Unknown 
ifeq ($(OS),Windows_NT)
	UNAME := Windows

	ifneq (,$(findstring /cygdrive/,$(PATH)))
		UNAME := Cygwin
	endif
else
	UNAME_KNOWN := $(shell uname -s)

	ifeq ($(UNAME_KNOWN),Linux)
		UNAME := Linux
	endif
	
	ifeq ($(UNAME_KNOWN),Darwin)
		UNAME := Darwin
	endif
endif


.PHONY: all, clean


all: $(UNAME)-all

Linux-all:
	@./find.sh

Darwin-all:
	@./find.sh

Cygwin-all:
	@./find.sh

Windows-all:
	@.\\find.bat

Unknown-all:
	$(error error: unsupported operating system $(UNAME))


clean: $(UNAME)-clean

Linux-clean:
	@rm -f var

Darwin-clean:
	@rm -f var

Cygwin-clean:
	@rm -f var

Windows-clean:
	@CMD /C IF EXIST var DEL /Q/F var

Unknown-clean:
	$(error error: unsupported operating system $(UNAME))

