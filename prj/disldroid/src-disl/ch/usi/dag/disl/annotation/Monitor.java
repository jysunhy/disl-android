package ch.usi.dag.disl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import ch.usi.dag.disl.marker.Marker;


@Documented
@Target (ElementType.METHOD)
public @interface Monitor {

    /**
     * Selects the marker class. A marker determines the region of code within a
     * method where to apply the snippet.
     *
     * @see ch.usi.dag.disl.marker.Marker Implementation details
     */
    Class <? extends Marker> marker();

    /**
     * Optional argument for the marker class, passed as a {@link String}.
     * <p>
     * Default value: {@code ""}, means "no arguments".
     */
    String args() default "";


    /**
     * Selects methods in which to apply the snippet.
     * <p>
     * See the {@link ch.usi.dag.disl.scope} package for more information about
     * the scoping language.
     * <p>
     * Default value: {@code "*"}, means "everywhere".
     */
    String scope() default "*";


    /**
     * Selects the guard class. A guard class determines whether a snippet
     * will be inlined at a particular location or not. In general, guards
     * provide more fine-grained control compared to scopes.
     * <p>
     * Default value: {@code void.class}, means "no guard used".
     */
    Class <? extends Object> guard() default void.class;
}
