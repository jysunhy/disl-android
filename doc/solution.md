SOLUTION
----

###add new service for communication between emulator and host:
refer to adb
	modify system/core/adb/Android.mk
	modify build/core/user_tags.mk

modify init.rc (system/core/rootdir/init.rc)

___done___ use socket to connect from a client to a host
___to-do___ try start a service when the android linux starts as a channel for instrumentation

###Where to inject
@@@found /data/dalvik-cache stored optimized dex
if remove these files, new application won't work

###ShadowVM android support
* fasttag on android DVM
* remote-agent-library re-implement on android
	* JVMTI replacement
		* critical section(RawMonitorEnter/RawMonitorExit)
		* netref(SetTag/GetTag)
		* class_file_load_hook(for new class event)/object_free_hook(can be done on DVM)/vm_start_hook(can be done in DVM)/vm_init_hook(can be done in DVM)/vm_death_hook(can be done in DVM)/thread_end_hook(??);
