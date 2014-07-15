package ch.usi.dag.et.tools.etracks.local;

import ch.usi.dag.et.tools.etracks.agent.Agent;
import ch.usi.dag.et.tools.etracks.util.LogicalClock;


/**
 * Represents the local part of the Elephant Tracks analysis. In particular, it
 * augments the events produced by the instrumentation with logical method
 * entry/exit timestamps and it also handles reporting of multi-dimensional
 * array allocations.
 * <p>
 * <b>Note:</b> This class could be merged either with
 * {@link ElephantTracksStub} or the various instrumentation classes. However,
 * the stub is supposed to be generated, so it really should not do more than it
 * currently does. In case of the instrumentation, the idea is to make the
 * instrumentation unaware of any local state that the analysis may have (the
 * logical clock in this case).
 *
 * @author Lubomir Bulej
 */
public final class LocalElephantTracks {

    private LocalElephantTracks () {
        // not to be instantiated
    }


    //
    // Object allocations
    //

    public static void onObjectAllocation (
        final Object object, final long threadId
    ) {
        ElephantTracksStub.onObjectAllocation (
            LogicalClock.current (), object, __sizeOf (object), threadId
        );
    }


    public static void onReferenceArrayAllocation (
        final Object arrayObject, final long threadId
    ) {
        final Object [] array = (Object []) arrayObject;
        ElephantTracksStub.onReferenceArrayAllocation (
            LogicalClock.current (), arrayObject, __sizeOf (arrayObject),
            array.length, threadId
        );
    }

    private static long __sizeOf (final Object object) {
        return Agent.getObjectSize (object);
    }


    //
    // Multi-array allocations
    //

    /**
     * Handles allocation of multidimensional arrays of primitive types.
     * Emits <i>object allocated</i> event for each sub-array of the
     * multi-dimensional array, except the leaf ones, which contain the
     * primitive values.
     *
     * @param arrayObject
     *      the array to handle, must not be {@code null}
     * @param arrayDimension
     *      the dimension of the array, must be greater than 1
     * @param threadId
     *      identifier of the thread doing the allocation
     */
    public static void onPrimitiveMultiArrayAllocation (
        final Object arrayObject, final int arrayDimension, final long threadId
    ) {
        //
        // The dimension is greater than 1, the array object thus represents
        // an array of arrays -> array of object references. We emit an event
        // for this array and then handle the individual elements depending on
        // the array dimension.
        //
        final Object [] array = (Object []) arrayObject;
        LocalElephantTracks.onReferenceArrayAllocation (array, threadId);

        final int arrayLength = array.length;
        if (arrayDimension > 2) {
            //
            // With more than two dimensions left, the elements of "array"
            // are still arrays of arrays, so we process them recursively.
            //
            for (int i = 0; i < arrayLength; i++) {
                final Object referenceArray = array [i];
                onPrimitiveMultiArrayAllocation (referenceArray, arrayDimension - 1, threadId);
                onReferenceElementUpdate (array, i, null, referenceArray, threadId);
            }

        } else if (arrayDimension == 2) {
            //
            // With just 2 dimensions left, the elements of "array" are
            // arrays of primitive types, so we process them directly.
            //
            // TODO: This could be factored out -- 2D arrays could then be
            // handled by a specialized snippet.
            //
            for (int i = 0; i < arrayLength; i++) {
                final Object primitiveArray = array [i];
                onObjectAllocation (primitiveArray, threadId);
                onReferenceElementUpdate (array, i, null, primitiveArray, threadId);
            }
        }
    }


    /**
     * Handles allocation of multidimensional arrays of reference types.
     * Emits <i>object allocated</i> event for each sub-array of the
     * multi-dimensional array, except the leaf ones, which contain object
     * references, and for which the <i>reference array allocated</i> event
     * is emitted.
     *
     * @param arrayObject
     *      the array to handle, must not be {@code null}
     * @param arrayDimension
     *      the dimension of the array, must be greater than 1
     * @param threadId
     *      identifier of the thread doing the allocation
     */
    public static void onReferenceMultiArrayAllocation (
        final Object arrayObject, final int arrayDimension, final long threadId
    ) {
        //
        // The dimension is greater than 1, the array object thus represents
        // an array of arrays -> array of object references. We emit an event
        // for this array and then handle the individual elements depending on
        // the array dimension.
        //
        final Object [] array = (Object []) arrayObject;
        onReferenceArrayAllocation (array, threadId);

        if (arrayDimension > 2) {
            //
            // With more than two dimensions left, the elements of "array"
            // are still arrays of arrays, so we process them recursively.
            //
            final int arrayLength = array.length;
            for (int i = 0; i < arrayLength; i++) {
                final Object referenceArray = array [i];
                onReferenceMultiArrayAllocation (referenceArray, arrayDimension - 1, threadId);
                onReferenceElementUpdate (array, i, null, referenceArray, threadId);
            }

        } else if (arrayDimension == 2) {
            //
            // With just 2 dimensions left, the elements of "array" are
            // arrays of references, so we process them directly.
            //
            // TODO: This could be factored out -- 2D arrays could then be handled by a specialized snippet.
            //
            final int arrayLength = array.length;
            for (int i = 0; i < arrayLength; i++) {
                final Object referenceArray = array [i];
                onReferenceArrayAllocation (referenceArray, threadId);
                onReferenceElementUpdate (array, i, null, referenceArray, threadId);
            }
        }
    }


    //
    // Method invocations
    //

    public static void onMethodEntry (
        final int methodId, final Object receiver, final long threadId
    ) {
        ElephantTracksStub.onMethodEntry (
            LogicalClock.next (), methodId, receiver, threadId
        );
    }


    public static void onMethodExit (
        final int methodId, final Object receiver, final long threadId
    ) {
        ElephantTracksStub.onMethodExit (
            LogicalClock.next (), methodId, receiver, threadId
        );
    }


    public static void onStaticMethodEntry (
        final int methodId, final long threadId
    ) {
        ElephantTracksStub.onStaticMethodEntry (
            LogicalClock.next (), methodId, threadId
        );
    }


    public static void onStaticMethodExit (
        final int methodId, final long threadId
    ) {
        ElephantTracksStub.onStaticMethodExit (
            LogicalClock.next (), methodId, threadId
        );
    }


    //
    // Object usage
    //

    /**
     * Sends an <i>object use</i> event to the remote analysis, provided the
     * object is not {@code null}.
     */
    public static void onObjectUse1 (final Object object1, final long threadId) {
        //
        // Don't send null references to the other side.
        //
        if (object1 != null) {
            ElephantTracksStub.onObjectUse1 (
                LogicalClock.current (), object1, threadId
            );
        }
    }


    /**
     * Sends <i>object use</i> event for two objects to the remote analysis if
     * both objects are not {@code null}, otherwise sends the event only for the
     * non-null object.
     */
    public static void onObjectUse2 (
        final Object object1, final Object object2, final long threadId
    ) {
        //
        // Bail out quickly if both objects are null.
        //
        if (object1 == null && object2 == null) {
            return;
        }

        //
        // Don't send null references to the other side.
        //
        long time = LogicalClock.current ();
        if (object1 != null && object2 != null) {
            ElephantTracksStub.onObjectUse2 (
                time, object1, object2, threadId
            );
        } else {
            final Object nonNullObject = (object1 != null) ? object1 : object2;
            ElephantTracksStub.onObjectUse1 (time, nonNullObject, threadId);
        }
    }


    //
    // Reference updates
    //

    public static void onReferenceElementUpdate (
        final Object owner, final int elementIndex,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        ElephantTracksStub.onReferenceElementUpdate (
            LogicalClock.current (), owner,
            elementIndex, oldTarget, newTarget, threadId
        );
    }


    public static void onStaticReferenceFieldUpdate (
        final String ownerClassName, final String fieldName,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        try {
            ElephantTracksStub.onStaticReferenceFieldUpdate (
                LogicalClock.current (), Class.forName (ownerClassName),
                fieldName, oldTarget, newTarget, threadId
            );

        } catch (final ClassNotFoundException cnfe) {
            // there is not much we can do
            System.err.println ("could not get class for "+ ownerClassName);
        }
    }


    public static void onInstanceReferenceFieldUpdate (
        final Object owner, final String fieldName,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        ElephantTracksStub.onInstanceReferenceFieldUpdate (
            LogicalClock.current (), owner,
            fieldName, oldTarget, newTarget, threadId
        );
    }

}
