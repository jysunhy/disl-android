##Step by step view:
---
###Step 1. Doing instrumentation outside the android device
####	Preparation for Step 1:	
#####	i). disable pre-dexopt option during compilation
* The libraries and pre-installed applications will do the dexopt for each startup
* Why: The dex2jar can only deal with *.dex files. If pre-dexopt is enabled, the DVM will directly read the *.odex file from the system image, which are pre-optimized. *.odex is not supported by dex2jar. 
* The only weakness: boot will be slower
* Argue, if any tool that can support read the *.odex files, we don't have to do so
#####	ii) A server(___CENTRAL\_SERVER___) 
* The server is backend running on the android linux. This server is written in C. It's used to communicate among the applications through Unix domain socket. And it communicates with remote server via network socket. It's a multi-thread server and can handle requests from all processes inside the android linux through the Unix domain socket.
	
* __Implementation:__
	* intercept the code in DVM to load the dex code from the system.img and send the code to the ___CENTRAL\_SERVER___
	* ___CENTRAL\_SERVER___ will transfer the code the remote disl server and disl server return the instrumented version of dex
	* The dvm will load the new dex.		
	* This is almost like static instrumentation
	* The advantage of us, is we can do different kinds of instrumentation without reburn or recompile the whole android source code.
		
###Step 2. The disl part
* It's almost the same as current Disl, only a few modification of calling dex2jar and dx. And there is a map from the classname to the JVM bytecode. 
	* The shadowvm remote server will query directly on disl server for the bytecode class information.

###Step 3. The shadowvm Part
* i) Tagging

	I add the tag for the Object Structure(in dalvil/vm/oo/Object.h), and set some relative offsets right(in UtfString.h) to make it work. The 64-bit tag is just as that of shadowvm

		struct Object {
			/* ptr to class object */
			ClassObject*    clazz;

			/*  
			 * A word containing either a "thin" lock or a "fat" monitor.  See
			 * the comments in Sync.c for a description of its layout.
			 */
			u4              lock;
			u8              uuid; // THIS IS MY 8-bytes TAGGING FIELD
		};
		
		
* ii) The events

	* Events: The interfaces currently is the same as original one, except querying for dislserver for the bytecode instead of getting the bytecode from classload events.
	
	* Implementation: Because DVM has no javaagent or jvmti. I modify the DVM to support events.
		* The main idea is __add hooks in the gDvm variable__. gDvm is a global variable structure, and I add the needed function pointer(hooks) to this variable. And in the right place where a specified event happens, I call this hook there.
			* I can give a map of all events and where the instrumentation code is if it's needed in the paper
		* To allow collecting events in appointed process, not all processes, I set these hooks default to null. And only the process to be observed starts, I will set these hooks to collect the events.
			* Currently, the hooks are set in the JNIOnload of the native library 
		* About the JNI native library I added. Currently I added it as an external lib, and can be mannual loaded using disl(e.g., by instrument ActivityThread.main, we can load the library by System.loadLibrary). Only instrumented process will load this library, and so have the events.
			* The problem may be: this may influence the full coverage as I tested yesterday. So I would try to make it internally loaded.
	
	* Selecting the process
	
		Android is an multiprocess enviroment. We may need to observe more than one process. And we have to distinguish the target application through the process name.
		* The mapping of pname to pid is got through instrumentation to android framework(Process.startViaZygote)
			* Use ALocalDisptach.mapPID(PNAME,PID) which will send this mapping to the ___CENTRAL\_SERVER___
		* The process starts from forking the Zygote (Dalvik_dalvik_system_Zygote_forkAndSpecialize in dalvik/native/dalvik_system_Zygote.cpp)
			* setting a flag in the gDvm.bypass by querying the ___CENTRAL\_SERVER___ about the pid and the pname
		* The Java threads are implemented using pthread in DVM. And all starting of a VMThread in Java will call the native function 
			* we can set the bypass field when create each Java Thread(in dalvik/vm/Thread.cpp)
			* The bypass field is added into the Internal Thread Object(add the offset and mapping to the field in the InitRef.cpp)
			
	* Capturing starting of Activity
		* each Activity will start from the ActivityThread.main
		
	* Capturing services and so on:(not tested yet)
		
		
	
	
