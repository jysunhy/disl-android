ANDROID PROJECT
----

###(DVM source code)(https://github.com/jysunhy/disl-android/blob/master/doc/src-reading/dalvik_source.md)


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

###[JNI](http://en.wikipedia.org/wiki/Java_Native_Interface#JNIEnv.2A)
