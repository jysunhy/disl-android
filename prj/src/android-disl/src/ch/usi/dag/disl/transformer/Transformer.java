package ch.usi.dag.disl.transformer;

/**
 * Allows transformation of a class before it is passed to DiSL. The transformer
 * class has to be specified in the instrumentation manifest.
 * 
 * Transformer implementation has to be thread-safe.  
 */
public interface Transformer {

	/**
	 * The class to be transformed is passed as an argument and the transformed
	 * class is returned.
	 * 
	 * @param classfileBuffer class to be transformed
	 * @return transformed class
	 */
	byte[] transform(byte[] classfileBuffer) throws Exception;
	
	/**
	 * If this method returns true, not instrumented classes are still
	 * treated as modified (by DiSL) and propagated out. Otherwise, the DiSL
	 * will report class as unmodified. 
	 * 
	 * @return propagation flag
	 */
	boolean propagateUninstrumentedClasses();
}
