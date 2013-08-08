ANDROID PROJECT
----

###[DVM source code](https://github.com/jysunhy/disl-android/blob/master/doc/src-reading/dalvik_source.md)


###[build instructions](https://github.com/jysunhy/disl-android/blob/master/doc/src-reading/build_android.md)

###[DVM architechure](https://github.com/jysunhy/disl-android/blob/master/doc/src-reading/android-arch.md)[ref](http://show.docjava.com/posterous/file/2012/12/10222640-The_Dalvik_Virtual_Machine.pdf)

###Android Command

	#INSTALL/UNINSTALL/RUN in ANDROID
    android create project --name test_apk --path test_apk --package a.b --activity Main --target 1
    android update project --path NotePad/ --target 1 --subprojects
    android list avd #get device list including the name
    emulator -avd "devicename" #launch the emulator
    adb uninstall a.b #uninstall original package (Can get in AndroidManifest.xml)
    adb install test_apk-debug-toast-signed.apk #install new apk
    adb shell am start -n a.b/.Main #run in the emulator

	emulator -avd $1 -logcat [w|v|d|i|e] -system images/system.img -ramdisk images/ramdisk.img -kernel images/zImage -prop dalvik.vm.execution-mode=int:portable &

###Android Debug
####LogCat
		#LogCat(without eclipse)
		in adb
			2.1、adb logcat 显示所有调试信息        
			2.2、adb logcat *:w 显示waring过滤器过滤后的调试信息        
			2.3、adb logcat Test1:V Test2:D 显示标签为Test1的所有调试信息，以及显示标签为Test2Debug过滤器过滤后的调试信息
####[DDVM](http://developer.android.com/tools/debugging/ddms.html)

####Good Blogs
[1. cover many topics of android dvm](http://blog.csdn.net/Luoshengyang/article/category/838604)
[2. some building blogs](http://blog.csdn.net/leonan/article/category/1335976)


####dex optimization
Dalvik Optimization and Verification With dexopt

There are at least three different ways to create a "prepared" DEX file, sometimes known as "ODEX" (for Optimized DEX):

* The VM does it "just in time". The output goes into a special dalvik-cache directory. This works on the desktop and engineering-only device builds where the permissions on the dalvik-cache directory are not restricted. On production devices, this is not allowed.
* The system installer does it when an application is first added. It has the privileges required to write to dalvik-cache.
* The build system does it ahead of time. The relevant jar / apk files are present, but the classes.dex is stripped out. The optimized DEX is stored next to the original zip archive, not in dalvik-cache, and is part of the system image.

make showcommands WITH_DEXPREOPT=false

odex -> dex
1. disable pre opt
	make showcommands WITH_DEXPREOPT=false -j16
2. use tool to de-compile odex to dex

###DVM Options
adb shell dalvikvm -help
-Xdexopt:{none,verified,all,full}
