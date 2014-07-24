package ch.usi.dag.et2.tools.etracks.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.et2.tools.etracks.local.ElephantTracksStub;
import ch.usi.dag.et2.tools.etracks.local.LocalElephantTracks;


/**
 * Instruments object and array allocations to keep track of object births.
 *
 * @author Lubomir Bulej
 */
public final class ObjectAllocations {

    private ObjectAllocations () {
        // prevent class instantiation from outside
    }

    //
    // Object allocations
    //

    /**
     * Instruments the entry to the {@link Object} constructor to report an
     * <i>object allocation</i> event.
     * <p>
     * Since there is no <i>method entry</i> event for the {@link Object}
     * constructor, we cannot use the {@link MethodInvocations#threadId thread
     * id} cached on other method entries.
     */
    @Before (marker = BodyMarker.class, guard = Guard.ObjectConstructorOnly.class)
    public static void onObjectConstructorEntry (final DynamicContext dc) {
        ElephantTracksStub.onObjectAllocation (
            0 /* TODO Sample native logical clock. */, dc.getThis ()
        );
    }


    //
    // Single-dimensional array allocations
    //

    /**
     * Instruments all single-dimensional primitive array allocations (including
     * 1D arrays allocated using MULTIANEWARRAY) to emit an <i>object
     * allocation</i> event.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     * <p>
     * <b>Note:</b> This instrumentation <b>MUST</b> use the @AfterReturning
     * annotation to avoid inserting exception handler for a bytecode that
     * cannot produce exception.
     */
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "newarray,multianewarray",
        guard = Guard.SinglePrimitiveArrayAllocations.class
    )
    public static void afterPrimitiveArrayAllocation (final DynamicContext dc) {
        ElephantTracksStub.onObjectAllocation (
            0 /* TODO Sample native logical clock. */, 
            dc.getStackValue (StackIndex.STACK_TOP, Object.class)
        );
    }


    /**
     * Instruments all one-dimensional reference array allocations (including 1D
     * arrays allocated using MULTIANEWARRAY) to emit an <i>array allocation</i>
     * event.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     * <p>
     * <b>Note:</b> This instrumentation <b>MUST</b> use the @AfterReturning
     * annotation to avoid inserting exception handler for a bytecode that
     * cannot produce exception.
     */
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "anewarray,multianewarray",
        guard = Guard.SingleReferenceArrayAllocations.class
    )
    public static void afterReferenceArrayAllocation (final DynamicContext dc) {
        LocalElephantTracks.onReferenceArrayAllocation (
            0 /* TODO Sample native logical clock. */, 
            dc.getStackValue (StackIndex.STACK_TOP, Object.class)
        );
    }


    //
    // Multi-dimensional array allocations
    //

    /**
     * Emits an <i>array allocation</i> event for all intermediate arrays of a
     * multi-dimensional (at least 2) array of primitive-type elements. This
     * does not include the leaf arrays, which contain the primitive values.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     * <p>
     * <b>Note:</b> This instrumentation <b>MUST</b> use the @AfterReturning
     * annotation to avoid inserting exception handler for a bytecode that
     * cannot produce exception.
     */
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "multianewarray",
        guard = Guard.MultiPrimitiveArrayAllocations.class
    )
    public static void afterMultiPrimitiveArrayAllocation (
        final DynamicContext dc, final MultiArrayStaticContext masc
    ) {
        LocalElephantTracks.onPrimitiveMultiArrayAllocation (
            0 /* TODO Sample native logical clock. */, 
            dc.getStackValue (StackIndex.STACK_TOP, Object.class),
            masc.getDimensions ()
        );
    }


    /**
     * Emits an <i>array allocation</i> event for all intermediate arrays of a
     * multi-dimensional (at least 2) array of reference-type elements. This
     * also includes the leaf arrays, which contain the actual references and
     * where we want to track reference updates.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     * <p>
     * <b>Note:</b> This instrumentation <b>MUST</b> use the @AfterReturning
     * annotation to avoid inserting exception handler for a bytecode that
     * cannot produce exception.
     */
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "multianewarray",
        guard = Guard.MultiReferenceArrayAllocations.class
    )
    public static void afterMultiReferenceArrayAllocation (
        final DynamicContext dc, final MultiArrayStaticContext masc
    ) {
        LocalElephantTracks.onReferenceMultiArrayAllocation (
            0 /* TODO Sample native logical clock. */, 
            dc.getStackValue (StackIndex.STACK_TOP, Object.class),
            masc.getDimensions ()
        );
    }

}
