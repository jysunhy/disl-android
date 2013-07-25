#Related Work
---

##AndroidAnalysis

Android
    [2010 OSDI] TaintDroid  
        dynamic, run on mobile phone, modify dvm, only monitor java code
    [2011 Security] A study of android application security
    [2012 Security] DroidScope
        dynamic, virtualization, emulator, low-level instrumentation, reconstruct semantic context, OS-level, Native level, JAVA level APIs
    [2011 CCS]  Android permissions demystified
        detect over-privileged app
    [2012 NDSS] DroidRanger
    [app]       DroidBox


##TaintDroid
open source and include the dynamic instrumentation needed.
http://appanalysis.org/tdro1d.html
https://dl-ssl.google.com/dl/googlesource/git-repo/repo

## Bytecode manipulation
* [Android official bytecode document](http://source.android.com/tech/dalvik/dalvik-bytecode.html)
* [Redexer](http://www.cs.umd.edu/projects/PL/redexer/): a project from UMD that can rewrite dex file statically.
* [Dedexer](http://dedexer.sourceforge.net/): a disassembler tool for DEX files
* [Understanding bytecode by using dedexer](http://www.slideshare.net/paller/understanding-the-dalvik-bytecode-with-the-dedexer-tool)
* [Implementation of JVM Tool Interface on Dalvik Virtual Machine] (http://ieeexplore.ieee.org/stamp/stamp.jsp?arnumber=05496711)
* [ANEPROF: Energy Profiling for Android Java Virtual Machine and Applications] 2011 ICPADS(http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6121300)

## Binder
* [Intercept binder IPC (google discussion)](https://groups.google.com/forum/?fromgroups#!topic/android-platform/qKPTUch1XX8)
