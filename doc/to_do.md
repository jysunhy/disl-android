##To Do
---
###source code research
* ___loading process___
  * library -> Zygote will load all core java libraries and share with all apps
  * application class loading -> runtime
  * native class -> NDK and ?
* Optimization
  * ___disable optimization option -> refer to TaintDroid___
  * disable verification process with option [refer](http://www.netmite.com/android/mydroid/dalvik/docs/embedded-vm-control.html)
  * [odex](https://code.google.com/p/smali/wiki/DeodexInstructions)
  * [about dvm optimization](http://www.netmite.com/android/mydroid/dalvik/docs/dexopt.html)
* ___dex file entry point___
  * dalvik/libdex/DexFile.cpp -> dexFileParse


* bytecode representation and instrumentation
  * the instrument point
    * test that whether it is ok to pause here
    * test possible deadlock if network deadlock
    * sleep test
    * ___Whether we can use network module in Dalvik???___
    
* native library modification for instrumentation
   * native library loaded by zygote
   * native library loaded for app

* [Optimization](http://www.netmite.com/android/mydroid/dalvik/docs/dexopt.html)
  * pre Dex Optimization(odex)
  * interpreter optimization(JIT)
  * first time
  
* whether class can be reloaded(hotswap) -> not supported



##Done
---
* [done] droidBox
