package ch.usi.dag.disl.transformer;

/**
 * Allows transforming a class before it is passed to DiSL. The transformer
 * class has to be specified in the instrumentation manifest.
 *
 * <b>Note:</b> The {@link Transformer} implementation has to be thread-safe.
 */
public interface Transformer {

	/**
	 * Transforms the given class bytecode and returns the
	 * bytecode of the transformed class.
	 * 
	 * @param classfileBuffer class to be transformed
	 * @return bytecode of the transformed class
	 */
	byte[] transform(byte[] classfileBuffer) throws Exception;


	/**
	 * Determines whether classes that were not modified during the 
	 * transformation are treated by DiSL as modified and propagated
	 * back. If this method returns {@code false}, DiSL will report
	 * such classes as unmodified.

	 * @return propagation flag
	 */
	boolean propagateUninstrumentedClasses();
}
