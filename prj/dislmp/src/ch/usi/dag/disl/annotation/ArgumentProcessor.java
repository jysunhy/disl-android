package ch.usi.dag.disl.annotation;

/**
 * Annotated class defines method for processing method arguments. The specified
 * methods will be inlined into snippets to process one method argument value.
 * <br>
 * <br>
 * First argument of the method is a type, that will be processed by the method.
 * The allowed types are all basic types, String and Object class. The processed
 * type can be extend in some special cases by ProcessAlso annotation. 
 * During run-time, the argument will contain a processed method argument value.
 * ArgumentContext can be used to fetch additional data about the argument.
 * <br>
 * <br>
 * This annotation should be used with classes.
 * <br>
 * The method should be static, not return any values and not throw any
 * exceptions.
 * <br>
 * Method argument can be StaticContext, DynamicContext, ClassContext and
 * ArgumentContext.
 */
public @interface ArgumentProcessor {

}
