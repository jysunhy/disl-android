package ch.usi.dag.disl.staticcontext;

import org.objectweb.asm.Opcodes;

import ch.usi.dag.disl.util.Constants;

/**
 * Provides static context information about instrumented method.
 */
public class MethodStaticContext extends AbstractStaticContext {

	// *** Class ***

	/**
	 * Returns instrumented class name with "/" delimiter.
	 */
	public String thisClassName() {

		return staticContextData.getClassNode().name;
	}
	
	/**
	 * Returns instrumented class name with "." delimiter.
	 */
	public String thisClassCanonicalName() {

		return staticContextData.getClassNode().name.replace('/', '.');
	}
	
	/**
	 * Returns outer class of the instrumented class. 
	 */
	public String thisClassOuterClass() {

		return staticContextData.getClassNode().outerClass;
	}
	
	/**
	 * Returns outer method of the instrumented class. 
	 */
	public String thisClassOuterMethod() {

		return staticContextData.getClassNode().outerMethod;
	}
	
	/**
	 * Returns outer method descriptor of the instrumented class. 
	 */
	public String thisClassOuterMethodDesc() {

		return staticContextData.getClassNode().outerMethodDesc;
	}
	
	/**
	 * Returns signature of the instrumented class. 
	 */
	public String thisClassSignature() {

		return staticContextData.getClassNode().signature;
	}
	
	/**
	 * Returns source file of the instrumented class. 
	 */
	public String thisClassSourceFile() {

		return staticContextData.getClassNode().sourceFile;
	}
	
	/**
	 * Returns super class name of the instrumented class. 
	 */
	public String thisClassSuperName() {

		return staticContextData.getClassNode().superName;
	}
	
	/**
	 * Returns class version as (ASM) integer of the instrumented class. 
	 */
	public int thisClassVersion() {

		return staticContextData.getClassNode().version;
	}
	
	/**
	 * Returns true if the instrumented class is abstract. 
	 */
	public boolean isClassAbstract() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_ABSTRACT) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is an annotation. 
	 */
	public boolean isClassAnnotation() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_ANNOTATION) != 0;
	}

	/**
	 * Returns true if the instrumented class is an enum. 
	 */
	public boolean isClassEnum() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_ENUM) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is final. 
	 */
	public boolean isClassFinal() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_FINAL) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is an interface. 
	 */
	public boolean isClassInterface() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_INTERFACE) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is private. 
	 */
	public boolean isClassPrivate() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_PRIVATE) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is protected. 
	 */
	public boolean isClassProtected() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_PROTECTED) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is public. 
	 */
	public boolean isClassPublic() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_PUBLIC) != 0;
	}
	
	/**
	 * Returns true if the instrumented class is synthetic. 
	 */
	public boolean isClassSynthetic() {

		int access = staticContextData.getClassNode().access;
		return (access & Opcodes.ACC_SYNTHETIC) != 0;
	}
	
	// *** Method ***
	
	/**
	 * Returns name of the instrumented method. 
	 */
	public String thisMethodName() {

		return staticContextData.getMethodNode().name;
	}

	/**
	 * Returns name of the instrumented method plus method's class name. 
	 */
	public String thisMethodFullName() {

		return staticContextData.getClassNode().name
				+ Constants.STATIC_CONTEXT_METHOD_DELIM
				+ staticContextData.getMethodNode().name;
	}
	
	/**
	 * Returns descriptor of the instrumented method. 
	 */
	public String thisMethodDescriptor() {
		
		return staticContextData.getMethodNode().desc;
	}
	
	/**
	 * Returns signature of the instrumented method. 
	 */
	public String thisMethodSignature() {
		
		return staticContextData.getMethodNode().signature;
	}
	
	/**
	 * Returns true if the instrumented method is a bridge. 
	 */
	public boolean isMethodBridge() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_BRIDGE) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is final. 
	 */
	public boolean isMethodFinal() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_FINAL) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is private. 
	 */
	public boolean isMethodPrivate() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_PRIVATE) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is protected. 
	 */
	public boolean isMethodProtected() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_PROTECTED) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is public. 
	 */
	public boolean isMethodPublic() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_PUBLIC) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is static. 
	 */
	public boolean isMethodStatic() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_STATIC) != 0;
	}
	
	/**
	 * Returns true if the instrumented method is synchronized. 
	 */
	public boolean isMethodSynchronized() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_SYNCHRONIZED) != 0;
	}
	
	/**
	 * Returns true if the instrumented method has variable number of arguments. 
	 */
	public boolean isMethodVarArgs() {
		
		int access = staticContextData.getMethodNode().access;
		return (access & Opcodes.ACC_VARARGS) != 0;
	}
}
