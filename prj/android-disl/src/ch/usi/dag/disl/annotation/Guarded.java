package ch.usi.dag.disl.annotation;

/**
 * Annotation used in ArgumentProcessor to guard specific methods.
 * <br>
 * <br>
 * This annotation should be used with methods.
 */
public @interface Guarded {

	// NOTE if you want to change names, you need to change 
	// ProcessorParser.ProcMethodAnnotationData class
	
	// NOTE because of implementation of annotations in java the defaults
	// are not retrieved from here but from class mentioned above
	/**
	 * The guard class defining if the processor method will be inlined or not.
	 */
	Class<? extends Object> guard();
}
