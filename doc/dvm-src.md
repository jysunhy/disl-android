##DVM source code
---
###[build instructions](https://github.com/jysunhy/disl-android/blob/master/doc/src-reading/build_android.md)

###[DVM architechure](http://show.docjava.com/posterous/file/2012/12/10222640-The_Dalvik_Virtual_Machine.pdf)

###Android Command

###INSTALL/UNINSTALL/RUN in ANDROID
    android create project --name test_apk --path test_apk --package a.b --activity Main --target 1
    android update project --path NotePad/ --target 1 --subprojects
    android list avd #get device list including the name
    emulator -avd "devicename" #launch the emulator
    adb uninstall a.b #uninstall original package (Can get in AndroidManifest.xml)
    adb install test_apk-debug-toast-signed.apk #install new apk
    adb shell am start -n a.b/.Main #run in the emulator

