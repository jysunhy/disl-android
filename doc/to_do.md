##To Do
---
###source code research
* ___loading process___
  * library -> Zygote will load all core java libraries and share with all apps
  * application class loading -> runtime
  * native class -> NDK and ?
* ___disable optimization option -> refer to TaintDroid___
* ___dex file entry point___


* bytecode representation and instrumentation
  * the instrument point
    * test that whether it is ok to pause here
    * test possible deadlock if network deadlock
    * sleep test
    
* native library modification for instrumentation
* [Optimization](http://www.netmite.com/android/mydroid/dalvik/docs/dexopt.html)
  * pre Dex Optimization(odex)
  * interpreter optimization(JIT)
  * first time
* whether class can be reloaded(hotswap) -> not supported

##Done
---
* [done] droidBox
