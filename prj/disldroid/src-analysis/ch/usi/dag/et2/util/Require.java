package ch.usi.dag.et2.util;

import java.util.Collection;


/**
 * Utility class providing static methods for expressing common requirements. These
 * methods are intended for use at external interface boundaries to enforce
 * contract preconditions. For use at internal interface boundaries, use methods
 * from the {@link Assert} class.
 *
 * @author Lubomir Bulej
 */
public final class Require {

    /* ***********************************************************************
     * CONSTRUCTORS
     * ***********************************************************************/

    /**
     * Prevents public from creating instances of this utility class.
     */
    private Require () {
        // pure static class - not to be instantiated
    }


    /* ***********************************************************************
     * PUBLIC STATIC METHODS (generic checks)
     * ***********************************************************************/

    public static void objectNotNull (final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException (name + " is null");
        }
    }

    public static void classIsInterface (final Class <?> itfClass, final String name) {
        Require.objectNotNull (itfClass, name);
        if (!itfClass.isInterface ()) {
            throw new IllegalArgumentException (
                name + itfClass.getSimpleName () + " is not an interface"
            );
        }
    }

    public static void stringNotEmpty (final String string, final String name) {
        Require.objectNotNull (string, name);

        if (string.isEmpty ()) {
            throw new IllegalArgumentException (name + " is empty");
        }
    }

    public static void valueNotNegative (final long value, final String name) {
        if (value < 0) {
            throw new IllegalArgumentException (name + " is negative: "+ value);
        }
    }

    public static void valueIsPositive (final int value, final String name) {
        if (value <= 0) {
            throw new IllegalArgumentException (name +" is not positive ("+ value +")");
        }
    }

    public static void collectionNotEmpty (
        final Collection <?> collection, final String name
    ) {
       Require.objectNotNull (collection, name);

       if (collection.size () < 1) {
           throw new IllegalArgumentException (name + " is empty");
       }
    }

}
