package ch.usi.dag.disl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target (ElementType.METHOD)
public @interface Property {
    String ere() default "";

    String name() default "";

    String scope() default "*";

    String complement() default "false";

    String binder() default "false";

    String reverse() default "false";
}
