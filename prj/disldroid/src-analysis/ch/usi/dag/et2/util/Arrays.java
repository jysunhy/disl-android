package ch.usi.dag.et2.util;

import java.lang.reflect.Array;

import ch.usi.dag.util.Assert;


/**
 * Utility class providing static methods for simple array operations.
 *
 * @author Lubomir Bulej
 */
public final class Arrays {

    /* ***********************************************************************
     * CONSTRUCTORS
     * ***********************************************************************/

    /**
     * Prevents public from creating instances of this class.
     */
    private Arrays () {
        // pure static class - not to be instantiated
    }


    /* ***********************************************************************
     * PUBLIC METHODS
     * ***********************************************************************/

    /**
     * Returns minimal value from a non-empty array of integer values. The
     * caller must ensure that the {@code values} parameter is neither null nor
     * empty.
     *
     * @param values
     *      array of integer values to pick the minimum from
     * @return
     *      minimal value from the given array of values
     * @throws NullPointerException
     *      if the {@code values} parameter is null
     * @throws IllegalArgumentException
     *      if the {@code values} array is empty
     */
    public static int min (final int ... values) {
        Assert.arrayNotEmpty (values, "values");

        //

        if (values.length > 2) {
            int result = values [0];
            for (final int value : values) {
                if (value < result) {
                    result = value;
                }
            }

            return result;

        } else if (values.length == 2) {
            return Math.min (values [0], values [1]);

        } else if (values.length == 1) {
            return values [0];

        } else {
            throw new IllegalArgumentException ("min not defined for empty array");
        }
    }


    /**
     * Utility method to provide a copy of an array prefix. The method takes and
     * old array, the number of bytes to copy from the old array, and the
     * desired length of the new array and returns a new array of desired length
     * with contents (up to the specified count) copied from the old array.
     *
     * @param oldArray
     *      the old array to resize
     * @param prefixLength
     *      the length of the prefix to copy from the old array
     * @param newLength
     *      the desired length of the new array
     * @return
     *      new array of desired length with old content
     */
    public static byte [] copyOfPrefix (
        final byte [] oldArray, final int prefixLength, final int newLength
    ) {
        final byte [] result = new byte [newLength];

        System.arraycopy (
            oldArray, 0, result, 0,
            Math.min (prefixLength, newLength)
        );

        return result;
    }


    /**
     * Concatenates the given arrays into a single array. The concatenation
     * is shallow, therefore if an array element itself is an array, it is
     * copied as is, without expansion.
     *
     * @param <E>
     *      array element type
     * @param arrays
     *      the arrays to concatenate
     * @return
     *      new array with elements from all given arrays
     */
    public static <E> E [] concatenate (final E [] ... arrays) {
        Assert.objectNotNull (arrays, "arrays");

        //
        // Determine the required length of the resulting array.
        //
        int resultLength = 0;
        for (final E [] array : arrays) {
            resultLength += array.length;
        }

        //
        // Create the resulting array. The following cast is safe, because
        // all arrays are of the same type.
        //
        @SuppressWarnings ("unchecked")
        final E [] result = (E []) Array.newInstance (
            arrays [0].getClass ().getComponentType (), resultLength
        );

        //
        // Copy the contents to the new array.
        //
        int destIndex = 0;
        for (final E [] array : arrays) {
            final int length = array.length;

            System.arraycopy (array, 0, result, destIndex, length);
            destIndex += length;
        };

        return result;
    }


    /**
     * Appends elements to a given array, producing a new array.
     *
     * @param <E>
     *      array element type
     * @param array
     *      the array to add elements to
     * @param elements
     *      elements to add to the array
     * @return
     *      new array with the given elements appended
     */
    public static <E> E [] append (final E [] array, final E ... elements) {
        //
        // Create a sufficiently sized resulting array and copy the source array
        // into it.
        //
        final E [] result = java.util.Arrays.copyOf (
            array, array.length + elements.length
        );

        //
        // Add elements to the new array.
        //
        int destIndex = array.length;
        for (final E element : elements) {
            result [destIndex] = element;
            destIndex++;
        };

        return result;
    }


    /**
     * Compares the prefix of two byte arrays for equality.
     *
     * @param first
     *      one array to be tested for prefix equality
     * @param second
     *      the other array to be tested for prefix equality
     * @param prefixLength
     *      the length of the prefix to compare
     * @return
     *      {@code true} if the prefix of both arrays is equal,
     *      {@code false} otherwise
     */
    public static boolean equalsPrefix (
        final byte [] first, final byte [] second, final int prefixLength
    ) {
        for (int i = 0; i < prefixLength; i++) {
            if (first [i] != second [i]) {
                return false;
            }
        }

        return true;
    }


    public static int sequentialSearch (final Object [] array, final Object key) {
        Assert.objectNotNull (array, "array");
        Assert.objectNotNull (key, "key");

        //

        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (array [i].equals (key)) {
                return i;
            }
        }

        // not found
        return -1;
    }


    public static int sequentialSearch (final int [] array, final int key) {
        Assert.objectNotNull (array, "array");
        Assert.objectNotNull (key, "key");

        //

        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (array [i] == (key)) {
                return i;
            }
        }

        // not found
        return -1;
    }

}
