package ch.usi.dag.et2.tools.etracks.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.ArgumentProcessor;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.AfterInitBodyMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.et2.tools.etracks.local.LocalElephantTracks;


/**
 * Instruments method entries, and returns, and various other bytecodes to track
 * usage of objects in (logical) time.
 *
 * @author Lubomir Bulej
 */
public final class ObjectUses {

    private ObjectUses () {
        // prevent class instantiation from outside
    }


    //
    // Use of method arguments
    //
    // This might be slightly imprecise, depending on what we understand by
    // argument use. The values of the arguments were first used when loaded
    // onto the stack prior to the invocation, which might not use them at all.
    //

    /**
     * Instruments all methods and constructors (except {@link Object}) with
     * reference arguments to emit an <i>object use</i> event for each reference
     * argument.
     * <p>
     * <b>Note:</b> The <i>object use</i> events produced by this snippet
     * <b>MUST</b> be produced <b>after</b> the <i>method entry</i> events
     * produced by the snippets in {@link MethodInvocations}, which also cache
     * the thread id in a synthetic local. The {@code order} annotation
     * parameter ensures that this snippet is <i>enclosed</i> by the
     * method/constructor entry snippets.
     */
    @Before (
        marker = AfterInitBodyMarker.class,
        guard = Guard.ReferenceAcceptingInvocations.class,
        order = 0
    )
    public static void onInvocationReferenceArgumentUse (
        final ArgumentProcessorContext args
    ) {
        args.apply (
            ReferenceArgumentProcessor.class, ArgumentProcessorMode.METHOD_ARGS
        );
    }


    @ArgumentProcessor
    static class ReferenceArgumentProcessor {
        public static void onReferenceArgument (
            final Object argument, final ArgumentContext ac
        ) {
            LocalElephantTracks.onObjectUse1 (argument);
        }
    }


    //
    // Use of method results
    //

    /**
     * Instruments return from methods returning a reference to emit an
     * <i>object use</i> event for the returned value.
     * <p>
     * <b>Note:</b> The <i>object use</i> events produced by this snippet
     * <b>MUST</b> be produced <b>after</b> the <i>method exit</i> events
     * produced by the snippets in {@link MethodInvocations}. The {@code order}
     * annotation parameter ensures that this snippet <i>encloses</i> the
     * method/constructor exit snippets.
     */
    @AfterReturning (
        marker = BodyMarker.class,
        guard = Guard.ReferenceReturningInvocations.class,
        order = 1000
    )
    public static void onInvocationReferenceResultUse (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.STACK_TOP, Object.class)
        );
    }


    //
    // Array loads
    //

    /**
     * Instruments all array loads to emit an <i>object use</i> event for the
     * <b>array object</b> before the access. The array reference may be
     * {@code null}.
     */
    @Before (
        marker = BytecodeMarker.class,
        args = "aaload,baload,caload,daload,faload,iaload,laload,saload"
    )
    public static void beforeAnyArrayLoad (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.Before.xALOAD.ARRAY_REF, Object.class)
        );
    }


    /**
     * Instruments all reference array loads to emit an <i>object use</i> event
     * for the <b>content</b> of the element being loaded. The loaded reference
     * may be {@code null}.
     * <p>
     * <b>Note:</b> This instrumentation <b>MUST</b> use the @AfterReturning
     * annotation to ignore exceptions caused by the AALOAD instruction (null
     * array reference, index of out bounds).
     */
    @AfterReturning (marker = BytecodeMarker.class, args = "aaload")
    public static void afterReferenceArrayLoad (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.STACK_TOP, Object.class)
        );
    }


    //
    // Primitive array stores (only)
    //
    // Reference array stores are subsumed by the reference update mechanism.
    //

    /**
     * Instruments all primitive array stores to emit an <i>object use</i> event
     * for the <b>array object</b> before the access. The array reference may be
     * {@code null}.
     */
    @Before (
        marker = BytecodeMarker.class,
        args = "bastore,castore,dastore,fastore,iastore,lastore,sastore"
    )
    public static void beforePrimitiveArrayStore (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.Before.xASTORE.ARRAY_REF, Object.class)
        );
    }


    //
    // Primitive instance field stores (only).
    //
    // Reference field stores are subsumed by the reference update mechanism.
    //

    /**
     * Instruments all stores to primitive instance fields to emit an <i>object
     * use</i> event for the owner before the store. The reference to the field
     * owner might be {@code null}.
     */
    @Before (
        marker = BytecodeMarker.class, args = "putfield",
        guard = Guard.PrimitiveFields.class
    )
    public static void beforeInstancePrimitiveFieldStore (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.Before.PUTFIELD.OBJECT_REF, Object.class)
        );
    }


    //
    // Other object uses
    //

    /**
     * Instruments all instructions that operate on a reference to emit an
     * <i>object use</i> event for the reference before using it. The object
     * reference may be {@code null}.
     */
    @Before(
        marker = BytecodeMarker.class,
        args = "athrow,arraylength,checkcast,getfield,ifnull,ifnonnull,instanceof"
    )
    public static void beforeUsingOneReference (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse1 (
            dc.getStackValue (StackIndex.STACK_TOP, Object.class)
        );
    }


    /**
     * Instruments all instructions that use two object references to emit
     * <i>object use</i> events for both references before using them. Either of
     * the references may be {@code null}.
     */
    @Before (marker = BytecodeMarker.class, args = "if_acmpne,if_acmpeq")
    public static void beforeUsingTwoReferences (final DynamicContext dc) {
        LocalElephantTracks.onObjectUse2 (
            dc.getStackValue (StackIndex.Before.IF_ACMPx.OBJECT_REF1, Object.class),
            dc.getStackValue (StackIndex.Before.IF_ACMPx.OBJECT_REF2, Object.class)
        );
    }

}
