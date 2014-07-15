package ch.usi.dag.et.util;

/**
 * Utility class providing various functional interfaces.
 * <p>
 * <b>Note:</b> The names were intentionally adapted to Java 8 terminology to
 * make future transitions to Java 8 a bit easier.
 *
 * @author Lubomir Bulej
 */
public final class Functor {

    private Functor () {
        // pure static class - not to be instantiated
    }

    //

    /**
     * Represents a functor which determines whether a given object has a
     * certain (implementation specific) property.
     * <p>
     * A {@link Predicate} functor is typically used to filter elements
     * from an iterable container.
     */
    public interface Predicate <E> {
        /**
         * Determines whether the predicate holds for the given element.
         *
         * @param element
         *      element to check, may be {@code null}
         *
         * @return
         *      {@code true} if the element is acceptable by the filter,
         *      {@code false} otherwise
         */
        boolean test (E element);
    }


    /**
     * Represents a functor which performs an operation on a single object. The
     * functor's {@link #accept(Object)} method accepts a single object as an
     * argument and does not return anything. Unlike other functors, the
     * {@link Consumer} functor is expected to operate via side effects.
     * <p>
     * A {@link Consumer} functor is typically passed to the {@code forEach()}
     * methods in the {@link Iterables} class along with an iterable container,
     * to perform an operation on each element.
     */
    public interface Consumer <T> {
        /**
         * Performs an operation on the given element.
         *
         * @param target
         *      the object to perform an operation on, may be {@code null}
         */
        void accept (T target);
    }


    /**
     * Represents a functor which maps one object to another. The functor's
     * {@link #apply(Object)} method accepts a single object as an argument,
     * performs the computation, and returns the resulting object.
     * <p>
     * A {@link Functor} functor is typically passed to the {@code map()}
     * methods in the {@link Iterables} class along with an array, an
     * {@link Iterable}, or an {@link Iterator}, to "apply" the computation on
     * each element, with the results collected into a new container of the same
     * type as the functor return type.
     */
    public interface Function <F, T> {
        /**
         * Applies the function to the given object and returns the
         * resulting object.
         *
         * @param target
         *      target object to apply the function on, may be {@code null}
         * @return
         *      result of the application of the function on the target
         */
        T apply (F target);
    }

}
