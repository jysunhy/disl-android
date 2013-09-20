##To Do
---

##Dynamic Instrumentation
###source code research
* ___loading process___
  * library -> Zygote will load all core java libraries and share with all apps
  * application class loading -> runtime
  * native class -> NDK and ?
* Optimization
  * (preopt can be disabled)
  * ___disable optimization option -> refer to TaintDroid___
  * disable verification process with option [refer](http://www.netmite.com/android/mydroid/dalvik/docs/embedded-vm-control.html)
  * [odex](https://code.google.com/p/smali/wiki/DeodexInstructions)
  * [about dvm optimization](http://www.netmite.com/android/mydroid/dalvik/docs/dexopt.html)
* ___dex file entry point___
  * dalvik/libdex/DexFile.cpp -> dexFileParse(this is after optimization)
  * dexopt/OptMain.cpp -> main (without optimization)

* bytecode representation and instrumentation
  * Instrument before loading the dex [done]
    * test that whether it is ok to pause
    * test possible deadlock if network deadlock
    * sleep test
    * ___Whether we can use network module in Dalvik???___ or we cannot load the system library since it is loaded before android is all ready
  * Instrument before loading from memory
    * where is the bytecode stored in memory
    * how to instrument from bytecodes in memory(which is already optimized)
    
* native library modification for instrumentation
   * native library loaded by zygote
   * native library loaded for app

* [Optimization](http://www.netmite.com/android/mydroid/dalvik/docs/dexopt.html)
  * pre Dex Optimization(odex)
  * interpreter optimization(JIT)
  * first time
  
* whether class can be reloaded(hotswap) -> not supported

* Try adb/ddms tool using jdwp for communication [finished with socket]

### How to use different system libraries for different app
 * change dexopt options

## Shadow VM support
 * Fast tag

## Static Analysis
 * asmdex
 * smali/baksmali
 * dex2jar


##Some related jobs
---
* TaindDroid
* droidBox
