##TaintDroid
---
###[pdf](http://appanalysis.org/) [slides](https://www.usenix.org/legacy/events/osdi10/tech/slides/enck.pdf) [github](https://github.com/TaintDroid)
###Intro
TaintDroid is an extension to the Android mobile-phone platform that tracks the flow of privacy sensitive data through third-party applications.

TaintDroid automatically labels (taints) data from privacy-sensitive sources and transitively applies labels as sensitive data propagates through program variables, files, and interprocess mes- sages. When tainted data are transmitted over the net- work, or otherwise leave the system, TaintDroid logs the data’s labels, the application responsible for transmitting the data, and the data’s destination.

###Drawbacks
TaintDroid can be circumvented through leaks via implicit flows

* can be solved combined with static analysis
	
###Strengths
* real-time(dynamic)
* good performance(not in an emulator)
###Background
• Smartphones are resource constrained. The re- source limitations of smartphones precludes the use of heavyweight information tracking systems such as Panorama [57].• Third-party applications are entrusted with several types of privacy sensitive information. The mon- itoring system must distinguish multiple informa- tion types, which requires additional computation and storage.• Context-based privacy sensitive information is dy- namic and can be difficult to identify even when sent in the clear. For example, geographic locations are pairs of floating point numbers that frequently change and are hard to predict.• Applications can share information. Limiting the monitoring system to a single application does not account for flows via files and IPC between applica- tions, including core system applications designed to disseminate privacy sensitive information.

###Related work
* __approaches that rely on instruction-level dynamic taint analysis using whole sys- tem emulation [57, 7, 26] incur high performance penal- ties. __

*  variable tracking within an interpreter 
###Implementation
* __instrument the VM interpreter to provide variable-level tracking within untrusted ap- plication code.__
* use message-level tracking between applications.
* for system-provided native libraries, we use method-level tracking. Here, we run native code without instrumentation and patch the taint propagation on return. 
	* 	modified the native library loader to ensure that applications can only load native li- braries from the firmware and not those downloaded by the application.

####two types of native methods
####Binder RPC


 
 