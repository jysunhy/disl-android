package ch.usi.dag.disl.classparser;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.annotation.Guarded;
import ch.usi.dag.disl.annotation.ProcessAlso;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.exception.GuardException;
import ch.usi.dag.disl.exception.ParserException;
import ch.usi.dag.disl.exception.ProcessorParserException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.guard.GuardHelper;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.processor.ProcArgType;
import ch.usi.dag.disl.processor.ProcMethod;
import ch.usi.dag.disl.processor.ProcUnprocessedCode;
import ch.usi.dag.disl.processorcontext.ArgumentContext;
import ch.usi.dag.disl.staticcontext.StaticContext;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disl.util.ReflectionHelper;

class ProcessorParser extends AbstractParser {

	// first map argument is ASM type representing processor class where the
	// processor is defined
	private Map<Type, Proc> processors = new HashMap<Type, Proc>();

	public Map<Type, Proc> getProcessors() {

		return processors;
	}
	
	public void parse(ClassNode classNode) throws ParserException,
			ProcessorParserException, ReflectionException,
			StaticContextGenException, GuardException {

		// NOTE: this method can be called many times

		// ** local variables **
		processLocalVars(classNode);

		List<ProcMethod> methods = new LinkedList<ProcMethod>();
		
		for (MethodNode method : classNode.methods) {

			// skip the constructor
			if (method.name.equals(Constants.CONSTRUCTOR_NAME)) {
				continue;
			}

			// skip static initializer
			if (method.name.equals(Constants.STATIC_INIT_NAME)) {
				continue;
			}

			methods.add(parseProcessorMethod(classNode.name, method));
		}
		
		if(methods.isEmpty()) {
			throw new ProcessorParserException("ArgumentProcessor class "
					+ classNode.name + " should contain methods");
		}
		
		Type processorClassType = Type.getType("L" + classNode.name + ";"); 
		
		processors.put(processorClassType, 
				new Proc(classNode.name, methods));
	}
	
	private ProcMethod parseProcessorMethod(String className, MethodNode method)
			throws ProcessorParserException, ReflectionException,
			StaticContextGenException, GuardException, ParserException {

		String fullMethodName = className + "." + method.name;
		
		// check static
		if ((method.access & Opcodes.ACC_STATIC) == 0) {
			throw new ProcessorParserException("Method " + fullMethodName
					+ " should be declared as static");
		}

		// check return type
		if (!Type.getReturnType(method.desc).equals(Type.VOID_TYPE)) {
			throw new ProcessorParserException("Method " + fullMethodName
					+ " cannot return value");
		}
		
		// detect empty processors
		if (AsmHelper.containsOnlyReturn(method.instructions)) {
			throw new ProcessorParserException("Method " + fullMethodName
					 + " cannot be empty");
		}
		
		// no exception can be thrown
		if(! method.exceptions.isEmpty()) {
			throw new ProcessorParserException("Method " + fullMethodName
					 + " cannot throw any exception");
		}
		
		// ** parse processor method arguments **
		PMArgData pmArgData = parseProcMethodArgs(
				className + "." + method.name, method.desc);

		// all processed types - add method type
		EnumSet<ProcArgType> allProcessedTypes = 
			EnumSet.of(pmArgData.getType());
		
		// ** parse processor method annotation **
		ProcMethodAnnotationsData pmad = 
			parseMethodAnnotations(fullMethodName, method.invisibleAnnotations);

		// check if process also annotation contains only valid types
		checkProcessAlsoSetValidity(fullMethodName, pmArgData.getType(),
				pmad.processAlsoTypes);
		
		// add types from process also annotation
		allProcessedTypes.addAll(pmad.processAlsoTypes);
		
		// ** guard **
		Class<?> guardClass = ParserHelper.getGuard(pmad.guard);
		Method guardMethod = GuardHelper.findAndValidateGuardMethod(guardClass,
				GuardHelper.processorContextSet());
		
		// ** checks **

		// detect empty snippets
		if (AsmHelper.containsOnlyReturn(method.instructions)) {
			throw new ProcessorParserException("Method " + className + "."
					+ method.name + " cannot be empty");
		}

		// context arguments (local variables 1, 2, ...) cannot be stored or
		// overwritten, may be used only in method calls
		ParserHelper.usesContextProperly(className, method.name, method.desc,
				method.instructions);
		
		// ** create unprocessed code holder class **
		// code is processed after everything is parsed
		ProcUnprocessedCode ucd = new ProcUnprocessedCode(method.instructions,
				method.tryCatchBlocks, pmArgData.getStaticContexts(),
				pmArgData.usesDynamicContext(), pmArgData.usesClassContext(),
				pmArgData.usesArgumentContext());

		// return whole processor method
		return new ProcMethod(className, method.name, allProcessedTypes,
				guardMethod, ucd);

	}

	private static class PMArgData {
		
		private ProcArgType type;
		private Set<String> staticContexts;
		private boolean usesDynamicContext;
		private boolean usesClassContext;
		private boolean usesArgumentContext;
		
		public PMArgData(ProcArgType type, Set<String> staticContexts,
				boolean usesDynamicContext, boolean usesClassContext,
				boolean usesArgumentContext) {
			super();
			this.type = type;
			this.staticContexts = staticContexts;
			this.usesDynamicContext = usesDynamicContext;
			this.usesClassContext = usesClassContext;
			this.usesArgumentContext = usesArgumentContext;
		}

		public ProcArgType getType() {
			return type;
		}

		public Set<String> getStaticContexts() {
			return staticContexts;
		}

		public boolean usesDynamicContext() {
			return usesDynamicContext;
		}

		public boolean usesClassContext() {
			return usesClassContext;
		}
		
		public boolean usesArgumentContext() {
			return usesArgumentContext;
		}
	}
	
	private PMArgData parseProcMethodArgs(String methodID, String methodDesc)
			throws ProcessorParserException, StaticContextGenException,
			ReflectionException {

		Type[] argTypes = Type.getArgumentTypes(methodDesc);
		
		ProcArgType procArgType = ProcArgType.valueOf(argTypes[0]);
		
		// if the ProcArgType is converted to OBJECT, test that first argument
		// is really Object.class - nothing else is allowed
		if(procArgType == ProcArgType.OBJECT
				&& ! Type.getType(Object.class).equals(argTypes[0])) {
			
			throw new ProcessorParserException("In method " + methodID + ": " +
					"Only basic types and Object are allowed as the" +
					" first (type) parameter");
		}
		
		Set<String> knownStCo = new HashSet<String>();
		boolean usesDynamicContext = false;
		boolean usesClassContext = false;
		boolean usesArgumentContext = false;

		// parse rest of the arguments
		for (int i = 1; i < argTypes.length; ++i) {

			Type argType = argTypes[i];
			
			// skip dynamic context class - don't check anything
			if (argType.equals(Type.getType(DynamicContext.class))) {
				usesDynamicContext = true;
				continue;
			}
			
			// skip class context class - don't check anything
			if (argType.equals(Type.getType(ClassContext.class))) {
				usesClassContext = true;
				continue;
			}
			
			// skip argument context class - don't check anything
			if (argType.equals(Type.getType(ArgumentContext.class))) {
				usesArgumentContext = true;
				continue;
			}

			Class<?> argClass = ReflectionHelper.resolveClass(argType);

			// static context should implement static context interface
			if (!ReflectionHelper.implementsInterface(argClass,
					StaticContext.class)) {
			
				throw new StaticContextGenException(argClass.getName()
						+ " does not implement StaticContext interface and"
						+ " cannot be used as snippet method parameter");
			}

			knownStCo.add(argType.getInternalName());
		}

		return new PMArgData(procArgType, knownStCo, usesDynamicContext,
				usesClassContext, usesArgumentContext);
	}
	
	// data holder for parseMethodAnnotation methods
	private static class ProcMethodAnnotationsData {

		public EnumSet<ProcArgType> processAlsoTypes = 
			EnumSet.noneOf(ProcArgType.class);
		
		public Type guard = null;
	}

	private ProcMethodAnnotationsData parseMethodAnnotations(
			String fullMethodName,
			List<AnnotationNode> invisibleAnnotations)
			throws ProcessorParserException {

		ProcMethodAnnotationsData pmad = new ProcMethodAnnotationsData();
		
		// check no annotation
		if (invisibleAnnotations == null) {
			return pmad;
		}
		
		for(AnnotationNode annotation : invisibleAnnotations) {

			Type annotationType = Type.getType(annotation.desc);

			// guarded annotation
			if (annotationType.equals(Type.getType(Guarded.class))) {
				parseGuardedAnnotation(pmad, annotation);
				continue;
			}
			
			// process also annotation
			if (annotationType.equals(Type.getType(ProcessAlso.class))) {
				parseProcessAlsoAnnotation(pmad, annotation);
				continue;
			}
			
			// unknown annotation
			throw new ProcessorParserException("Method " + fullMethodName
					+ " has unsupported DiSL annotation");
		}
		
		return pmad;
	}

	// NOTE: pmad is modified by this function
	private void parseGuardedAnnotation(
			ProcMethodAnnotationsData pmad, AnnotationNode annotation) {

		ParserHelper.parseAnnotation(pmad, annotation);
		
		if(pmad.guard == null) {

			throw new DiSLFatalException("Missing attribute in annotation "
					+ Type.getType(annotation.desc).toString()
					+ ". This may happen if annotation class is changed but"
					+ " data holder class is not.");
		}
	}
	
	private static class ProcessAlsoAnnotationData {
		
		public Collection<String[]> types = null;
	}
	
	// NOTE: pmad is modified by this function
	private void parseProcessAlsoAnnotation(ProcMethodAnnotationsData pmad,
			AnnotationNode annotation) {

		ProcessAlsoAnnotationData paData = new ProcessAlsoAnnotationData();
		
		ParserHelper.parseAnnotation(paData, annotation);
		
		if(paData.types == null) {

			throw new DiSLFatalException("Missing attribute in annotation "
					+ Type.getType(annotation.desc).toString()
					+ ". This may happen if annotation class is changed but"
					+ " data holder class is not.");
		}
		
		// array is converted to collection
		for(String[] enumType : paData.types) {
		
			// enum is converted to array
			//  - first value is class name
			//  - second value is value name
			ProcessAlso.Type paType = ProcessAlso.Type.valueOf(enumType[1]);
			
			pmad.processAlsoTypes.add(ProcArgType.valueOf(paType));
		}
	}
	
	private void checkProcessAlsoSetValidity(String fullMethodName,
			ProcArgType methodArgType,
			EnumSet<ProcArgType> processAlsoTypes)
			throws ProcessorParserException {
		
		EnumSet<ProcArgType> validSet;

		// valid sets for types
		switch(methodArgType) {
		
		case INT: {
			validSet = EnumSet.of(
					ProcArgType.BOOLEAN,
					ProcArgType.BYTE,
					ProcArgType.SHORT);
			break;
		}
		
		case SHORT: {
			validSet = EnumSet.of(
					ProcArgType.BOOLEAN,
					ProcArgType.BYTE);
			break;
		}
		
		case BYTE: {
			validSet = EnumSet.of(
					ProcArgType.BOOLEAN);
			break;
		}
		
		default: {
			// for other types empty set
			validSet = EnumSet.noneOf(ProcArgType.class);
		}
		}
		
		// create set of non valid types
		EnumSet<ProcArgType> nonValidTypes = processAlsoTypes.clone();
		nonValidTypes.removeAll(validSet);
		
		if(! nonValidTypes.isEmpty()) {
			
			StringBuilder strNonValidTypes = new StringBuilder();
			
			final String STR_DELIM = ", ";
			
			for(ProcArgType paType : nonValidTypes) {
				strNonValidTypes.append(paType.toString() + STR_DELIM);
			}
			
			// remove last STR_DELIM
			int delimSize = STR_DELIM.length();
			strNonValidTypes.delete(strNonValidTypes.length() - delimSize,
					strNonValidTypes.length());
			
			throw new ProcessorParserException(methodArgType.toString() 
					+ " processor in method "
					+ fullMethodName
					+ " cannot process "
					+ strNonValidTypes.toString());
		}
	}
}
