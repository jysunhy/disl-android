##Dalvik Source
---
git clone https://android.googlesource.com/platform/dalvik

[taintdroid commits](https://github.com/TaintDroid/android_platform_dalvik.git)

###The vm folder

###structures
	DvmGlobals
		gDvm.inlinedMethods
		gDvm.loadedClasses //hashmap
		gDvm.pBootLoaderAlloc
	JarFile
	DvmDex

#### oo/Object.cpp
	ClassObject
	findMethodInListByDescriptor
#### oo/Class.cpp
	* createPrimitiveType
		
			static bool createPrimitiveType(PrimitiveType primitiveType, ClassObject** pClass)
	* createInitialClasses
	
			gDvm.classJavaLangClass = clazz	
			ok &= createPrimitiveType(PRIM_VOID,    &gDvm.typeVoid);
		    ok &= createPrimitiveType(PRIM_BOOLEAN, &gDvm.typeBoolean);
		    ok &= createPrimitiveType(PRIM_BYTE,    &gDvm.typeByte);
		    ok &= createPrimitiveType(PRIM_SHORT,   &gDvm.typeShort);
		    ok &= createPrimitiveType(PRIM_CHAR,    &gDvm.typeChar);
		    ok &= createPrimitiveType(PRIM_INT,     &gDvm.typeInt);
		    ok &= createPrimitiveType(PRIM_LONG,    &gDvm.typeLong);
	  	    ok &= createPrimitiveType(PRIM_FLOAT,   &gDvm.typeFloat);
		    ok &= createPrimitiveType(PRIM_DOUBLE,  &gDvm.typeDouble);
	* dvmClassStartUp
			
    		/* 
		     * Create the initial classes. These are the first objects constructed
		     * within the nascent VM.                               
		     */
		    if (!createInitialClasses()) {                          
		        return false;
		    }  

		    /* 
		     * Process the bootstrap class path.  This means opening the specified
			 * DEX or Jar files and possibly running them through the optimizer.
		     */
		    assert(gDvm.bootClassPath == NULL);                     
		    processClassPath(gDvm.bootClassPathStr, true);  


	* dvmClassShutDown
	* ->dvmInitClass
	* ->loadClassFromDex0
	* ->loadMethodFromDex
			meth->insns = pDexCode->insns;   



	###################################
	####about loading a class  ########        
	* dvmFindClass
		call dvmInitClass which loads from dex

	####about loading system class           	
	* dvmFindSystemClass
		first call findSystemClassNoInit, then call dvmInitClass
		/*
		 * Find the named class (by descriptor), scanning through the
		 * bootclasspath if it hasn't already been loaded.
		 *
		 * "descriptor" looks like "Landroid/debug/Stuff;".
		 *
		 * Uses NULL as the defining class loader.
		 */
		ClassObject* dvmFindSystemClass(const char* descriptor)
		call findClassNoInit
	* 	findClassNoInit
		/*
		 * Find the named class (by descriptor). If it's not already loaded,
		 * we load it and link it, but don't execute <clinit>. (The VM has
		 * specific limitations on which events can cause initialization.)
		 *
		 * If "pDexFile" is NULL, we will search the bootclasspath for an entry.
		 ...
		 */
		static ClassObject* findClassNoInit(const char* descriptor, Object* loader,
    	DvmDex* pDvmDex)

    	- LookupClass in Hash
    		if not
	    	- search for class def in bootpath or from dexfile
	    	- loadClassFromDex
	    	- add lock for linking and use initThreadId to check recursion
	    	-try adding to hash table
	    		if failed, use other thread's addition
	    - prepare and resolve

    * dvmLookupClass
   		 /*
		 * Search through the hash table to find an entry with a matching descriptor
		 * and an initiating class loader that matches "loader".
		 *
		 * The table entries are hashed on descriptor only, because they're unique
		 * on *defining* class loader, not *initiating* class loader.  This isn't
		 * great, because it guarantees we will have to probe when multiple
		 * class loaders are used.
		 *
		 * Note this does NOT try to load a class; it just finds a class that
		 * has already been loaded.
		 *
		 * If "unprepOkay" is set, this will return classes that have been added
		 * to the hash table but are not yet fully loaded and linked.  Otherwise,
		 * such classes are ignored.  (The only place that should set "unprepOkay"
		 * is findClassNoInit(), which will wait for the prep to finish.)
		 *
		 * Returns NULL if not found.
		 */
		ClassObject* dvmLookupClass(const char* descriptor, Object* loader,
		    bool unprepOkay)

	* dvmLinkClass(complex)
		vtable
		interface method table
		/*
		 * Link (prepare and resolve).  Verification is deferred until later.
		 *
		 * This converts symbolic references into pointers.  It's independent of
		 * the source file format.
		 *
		 * If clazz->status is CLASS_IDX, then clazz->super and interfaces[] are
		 * holding class reference indices rather than pointers.  The class
		 * references will be resolved during link.  (This is done when
		 * loading from DEX to avoid having to create additional storage to
		 * pass the indices around.)
		 *
		 * Returns "false" with an exception pending on failure.
		 */
		bool dvmLinkClass(ClassObject* clazz)


#### Init.cpp
	* processOptions
		if (strncmp(argv[i], "-Xdexopt:", 9) == 0) {
            if (strcmp(argv[i] + 9, "none") == 0)
                gDvm.dexOptMode = OPTIMIZE_MODE_NONE;
            else if (strcmp(argv[i] + 9, "verified") == 0)
                gDvm.dexOptMode = OPTIMIZE_MODE_VERIFIED;
            else if (strcmp(argv[i] + 9, "all") == 0)
                gDvm.dexOptMode = OPTIMIZE_MODE_ALL;
            else if (strcmp(argv[i] + 9, "full") == 0)
                gDvm.dexOptMode = OPTIMIZE_MODE_FULL;
                ...
        }
	* dvmStartUp

		dvmQuasiAtomicsStartup();
    		if (!dvmAllocTrackerStartup()) {
        		return "dvmAllocTrackerStartup failed";
	    }    
	    if (!dvmGcStartup()) { /* Ljava/lang/Daemons from libcore* run start method of daemons/
    	    return "dvmGcStartup failed";
	    }    
		

	    if (!dvmThreadStartup()) {
        return "dvmThreadStartup failed";
	    }
	    if (!dvmInlineNativeStartup()) {
	        return "dvmInlineNativeStartup";
	    }    
	    if (!dvmRegisterMapStartup()) {
	        return "dvmRegisterMapStartup failed";
	    }    
	    if (!dvmInstanceofStartup()) {
	        return "dvmInstanceofStartup failed";
	    }    
	    if (!dvmClassStartup()) {
	        return "dvmClassStartup failed";
	    }
		
	* AllocTracker
		if we add support for class unloading


###About loading the dex
	dexGetCode
	/*
	 * Get the DexCode for a DexMethod.  Returns NULL if the class is native
	 * or abstract.
	 */
	DEX_INLINE const DexCode* dexGetCode(const DexFile* pDexFile,
	    const DexMethod* pDexMethod)
	{
	    if (pDexMethod->codeOff == 0)
	        return NULL;
	    return (const DexCode*) (pDexFile->baseAddr + pDexMethod->codeOff);
	}

###About loading the class

###About JNI
	dvmCallJNIMethod
	dvmPlatformInvoke
		written in assemble code

###About zygote