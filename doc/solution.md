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
found /data/dalvik-cache stored optimized dex
