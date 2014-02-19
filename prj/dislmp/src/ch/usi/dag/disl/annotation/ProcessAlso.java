package ch.usi.dag.disl.annotation;

/**
 * If attached to the processor method, it allows to process:
 * short, byte and boolean in int processor,
 * byte and boolean in short processor,
 * boolean in byte processor.
 */
public @interface ProcessAlso {

	// NOTE if you want to change names, you need to change 
	// ProcessorParser.ProcessAlsoAnnotationData class
	
	// NOTE because of implementation of annotations in java the defaults
	// are not retrieved from here but from class mentioned above

	/**
	 * @see ch.usi.dag.disl.annotation.ProcessAlso
	 */
	public enum Type {
		BOOLEAN,
		BYTE,
		SHORT
	}
	
	Type[] types();
}
