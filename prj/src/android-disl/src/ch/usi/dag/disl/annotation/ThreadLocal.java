package ch.usi.dag.disl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that accesses to the annotated field should be translated into
 * accesses to a thread-local variable.
 * <p>
 * Thread-local variables are typically used for passing data between snippets
 * inlined into different methods. By default, the thread-local variable is
 * initialized to the default value corresponding to its type. If the
 * {@link #inheritable() inheritable} annotation parameter (optional) is set to
 * {@code true}, the default value will be inherited from the parent thread.
 * <p>
 * This annotation can be only used with fields. The fields should be declared
 * {@code static}, and if they are not shared between multiple instrumentation
 * classes, they should be kept {@code private}.
 */
@Documented
@Target(value = { ElementType.FIELD })
public @interface ThreadLocal {

    // NOTE if you want to change names, you need to change
    // ClassParser.TLAnnotationData class in startutil project

    // NOTE because of implementation of annotations in Java the defaults,
    // are not retrieved from here but from the class mentioned above.

    /**
     * Determines the default value for a thread-local variable.
     * <p>
     * If {@code true} , the default value is inherited from the parent thread,
     * otherwise the value is initialized to the default value corresponding to
     * its type.
     */
    boolean inheritable() default (false);
}
