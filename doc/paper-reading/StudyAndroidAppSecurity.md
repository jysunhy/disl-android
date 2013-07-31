##A Study of Android Application Security [pdf](http://www.enck.org/pubs/NAS-TR-0144-2011.pdf)
###USENIX Security Symposium August 2011
---
###[The ded decompiler](http://siis.cse.psu.edu/ded)
* ded recovers source code from application package
* Decompilation: standard Java decompilation(Soot)

####Type inference is needed
- dex bytecode lose some type information than JAVA bytecode
- better than dex2jar(wrong cases are given which uses dex2jar)

###Study Apps
####static analysis
* Control flow analysis: e.g., look at API options


	* Phone Identifiers
	* Location Info


* Phone Misuse
	* Background Audio/Video
	* Socket API Use
	* Installed Applications
	
	
	* Developer Toolkits
