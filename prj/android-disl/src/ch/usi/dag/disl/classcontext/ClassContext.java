package ch.usi.dag.disl.classcontext;

/**
 * Allows converting Strings to Class objects.
 */
public interface ClassContext {
	
	/**
	 * Converts string name to the class object.
	 * 
	 * @param name string containing name of the class
	 * @return class object
	 */
	Class<?> asClass(String name); 
}
