##Android
---
###[arch pdf](http://show.docjava.com/posterous/file/2012/12/10222640-The_Dalvik_Virtual_Machine.pdf)

###[process and zygote pdf](http://coltf.blogspot.ch/p/android-os-processes-and-zygote.html)

###[lauch](http://multi-core-dump.blogspot.ch/2010/04/android-application-launch.html)
* Dex file format
	* 	50% cut in size
* GC
* zygote
	* a zygote process preload all libs
	* if written to, copy on write
	* boost VM lauching time
* register-based architecture
	* enhance the performance
	* stack-based:
	
		register-based architecture requires and average of 47% less executed VM instructions than the stack based  architecture. On the other hand the register code is 25% larger than the corresponding stack code but this increased cost of fetching more VM instructions due to larger code size involves only 1.07% extra real machine loads perVM instruction which is negligible. The overall performance of the register-basedVM is that it takes, on average, 32.3% less time to execute standard benchmarks.
* security

	no application, by default, has permission to perform any operations that would adversely impact other applications, the operating system, or the user.This includes reading or writing the user's private data (such as contacts or e-mails), reading or writing another application's files, performing network access, keeping the device awake, etc.
	
	These features are implemented across the operating system (OS) level and framework level of Android and not within the Dalvik VM. 