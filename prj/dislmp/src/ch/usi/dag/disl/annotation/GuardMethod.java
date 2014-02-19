package ch.usi.dag.disl.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks guard validation method.
 * <br>
 * <br>
 * This annotation should be used with methods.
 * <br>
 * Guard method should be static and state-less.
 * <br>
 * Method argument can be Shadow, StaticContext, GuardContext and for
 * ArgumentProcessor guard also ArgumentContext.
 */
@Retention(RetentionPolicy.RUNTIME) // to resolve annotation using reflection 
public @interface GuardMethod {

}
