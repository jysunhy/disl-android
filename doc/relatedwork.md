#Arguement
---
##Related Papers
TaintDroid
DroidScope
DroidRanger
##Performance
better than DroidScope - Emulator-based
##User-Friendly

##Coverage

#Papers recently on android
---
* Vetting Undesirable Behaviors in Android Apps with Permission Use Analysis[CCS 2013](http://faculty.cs.tamu.edu/guofei/paper/VetDroid-CCS13.pdf)
* AppIntent: Analyzing Sensitive Data Transmission in Android for Privacy Leakage Detection[CCS 2013](http://faculty.cs.tamu.edu/guofei/paper/AppIntent-CCS13.pdf)
* On the Effectiveness of API-Level Access Control Using Bytecode Rewriting in Android [ASIACCS 2013](http://www.cis.syr.edu/~wedu/Research/paper/api_level_ac_asiaccs2013.pdf)
* practical and lightweight domain isolation on Android [SPSM 2011](http://www.informatik.tu-darmstadt.de/fileadmin/user_upload/Group_TRUST/PubsPDF/spsm18-bugiel.pdf)


#Previous papers(Some Related Work I have read in summer)
---
##AndroidAnalysis

* [2010 OSDI] [TaintDroid](http://appanalysis.org/)

   	dynamic, run on mobile phone, modify dvm, only monitor java code

* [2011 Security] [A study of android application security](http://www.enck.org/pubs/enck-sec11.pdf)

	static analysis
	introduce the compilation of DVM:

* [2012 Security] [DroidScope](https://www.usenix.org/conference/usenixsecurity12/droidscope-seamlessly-reconstructing-os-and-dalvik-semantic-views)

	dynamic, virtualization, emulator, low-level instrumentation, reconstruct semantic context, OS-level, Native level, JAVA level APIs

* [2011 CCS]  [Android permissions demystified](http://dl.acm.org/citation.cfm?id=2046779)

	detect over-privileged app

* [2012 NDSS] [DroidRanger](http://www.csd.uoc.gr/~hy558/papers/mal_apps.pdf) [ppt](http://www.jrmcclurg.com/papers/talk_overview_hey_you_get_off_my_market.pdf)
	
	static analysis, on Dalvik bytecode

* [app]       [DroidBox](https://code.google.com/p/droidbox/)

* [2012 S&P] [Dissecting android malware: Characterization and evolution](http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6234407)

* [2012 Asia JCIS] [DroidMat](http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6298136)

	static analysis, malware detection through manifest and API calls tracing
* [CODASPY '13] [AppsPlayground: automatic security analysis of smartphone applications](http://dl.acm.org/citation.cfm?id=2435379&CFID=236376075&CFTOKEN=80349731)

* [2011 Workshop on S&P] [Crowdroid](http://dl.acm.org/citation.cfm?id=2046619)

	dynamic analysis, Strace, Behavior based (Linux system call), lightweight client and analysis server
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

