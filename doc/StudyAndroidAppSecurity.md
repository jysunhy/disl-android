##A Study of Android Application Security [pdf](http://www.enck.org/pubs/NAS-TR-0144-2011.pdf)
###USENIX Security Symposium August 2011
---
###[The ded decompiler](http://siis.cse.psu.edu/ded)
* ded recovers source code from application package* Retargeting: type inference,instruction translation,etc* Optimization: use Soot to re-optimize for Java bytecode 
* Decompilation: standard Java decompilation(Soot)

####Type inference is needed
- dex bytecode lose some type information than JAVA bytecode
- better than dex2jar(wrong cases are given which uses dex2jar)

###Study Apps
####static analysis
* Control flow analysis: e.g., look at API options* Data flow analysis:e.g., information leaks, injection attacks* Structural analysis: “grep on steroids”* Semantic analysis:look at possible variable values
####misbehaviors
* Information Misuse
	* Phone Identifiers
	* Location Info


* Phone Misuse	* Telephony Services
	* Background Audio/Video
	* Socket API Use
	* Installed Applications
	
	* Included Libraries	* Advertisement and Analytics Libraries
	* Developer Toolkits	* Android-specific Vulnerabilities	* Leaking Information to Logs	* Leaking Information via IPC	* Unprotected Broadcast Receivers	* Intent Injection Attacks	* Delegating Control	* Null Checks on IPC Input	* SDcard Use	* JNI Use
###Limitations* The sample set* Code recovery failures * Android IPC data flows * Fortify SCA language * Obfuscation