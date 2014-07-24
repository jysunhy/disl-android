package ch.usi.dag.et2.tools.etracks.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.et2.tools.etracks.local.ElephantTracksStub;


/**
 * Instruments reference updates to keep track of neighbors.
 *
 * @author Lubomir Bulej
 */
public final class ReferenceUpdates {

    private ReferenceUpdates () {
        // prevent class instantiation from outside
    }


    //
    // Storing references into static fields.
    //

    /**
     * Instruments all stores to static reference fields to emit a <i>reference
     * update</i> event before the store.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     */
    @Before (
        marker = BytecodeMarker.class, args = "putstatic",
        guard = Guard.ReferenceFields.class
    )
    public static void beforeStaticReferenceFieldStore (
        final FieldAccessStaticContext fsc, final DynamicContext dc,
        final ClassContext cc
    ) {
        ElephantTracksStub.onStaticReferenceFieldUpdate (
            0 /* TODO Sample native logical clock. */, 
            cc.asClass (fsc.getOwnerInternalName ()), fsc.getName (), 
            /* old target */ dc.getStaticFieldValue (
                fsc.getOwnerInternalName (),
                fsc.getName (), fsc.getDescriptor (), Object.class
            ), 
            /* new target */ dc.getStackValue (
                StackIndex.Before.PUTSTATIC.VALUE, Object.class
            )
        );
    }


    //
    // Storing references into instance fields.
    //

    /**
     * Instruments all stores to instance reference fields to emit an
     * <i>reference update</i> event before the store. The reference to the
     * field owner might be {@code null}.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     */
    @Before (
        marker = BytecodeMarker.class, args = "putfield",
        guard = Guard.ReferenceFields.class
    )
    public static void beforeInstanceReferenceFieldStore (
        final FieldAccessStaticContext fsc, final DynamicContext dc
    ) {
        final Object owner = dc.getStackValue (
            StackIndex.Before.PUTFIELD.OBJECT_REF, Object.class
        );

        if (owner != null) {
            //
            // Arguments to dynamic context calls MUST be constant loads.
            // Static context invocations provide the constants, but they cannot
            // be stored into a variable and loaded later.
            //
            ElephantTracksStub.onInstanceReferenceFieldUpdate (
                0 /* TODO Sample native logical clock. */, 
                owner, fsc.getName (), 
                /* old target */ dc.getInstanceFieldValue (
                    owner, fsc.getOwnerInternalName (),
                    fsc.getName (), fsc.getDescriptor (), Object.class
                ), 
                /* new target */ dc.getStackValue (
                    StackIndex.Before.PUTFIELD.VALUE, Object.class
                )
            );
        }
    }


    //
    // Storing references into array elements.
    //

    /**
     * Instruments all reference array stores to emit a <i>reference element
     * update</i> before the access.
     * <p>
     * The array reference may be {@code null} and the index may be out of
     * bounds, so we need to step carefully. We cannot wait until after the
     * instruction was executed, because we would lose the old value.
     * <p>
     * Relies on the {@link MethodInvocations#threadId thread id} to have been
     * cached on method entry.
     */
    @Before (marker = BytecodeMarker.class, args = "aastore")
    public static void beforeReferenceArrayStore (final DynamicContext dc) {
        final Object owner = dc.getStackValue (
            StackIndex.Before.xASTORE.ARRAY_REF, Object.class
        );

        if (owner != null) {
            final Object [] array = (Object []) owner;
            final int index = dc.getStackValue (StackIndex.Before.xASTORE.INDEX, int.class);
            if (index < array.length) {
                // avoid local variables to make instrumentation shorter
                ElephantTracksStub.onReferenceElementUpdate (
                    0 /* TODO Sample native logical clock. */,
                    owner, index, 
                    /* old target */ array [index], 
                    /* new target */ dc.getStackValue (
                        StackIndex.Before.xASTORE.OBJECT_REF, Object.class
                    )
                );
            }
        }
    }

}
