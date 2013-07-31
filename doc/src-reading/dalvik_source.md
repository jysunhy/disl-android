##Dalvik Source
---
git clone https://android.googlesource.com/platform/dalvik

###vm
#### oo/Object.cpp
	ClassObject

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
	
#### Init.cpp
	* dvmStartUp

		dvmQuasiAtomicsStartup();
    		if (!dvmAllocTrackerStartup()) {
        		return "dvmAllocTrackerStartup failed";
	    }    
	    if (!dvmGcStartup()) {
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
		