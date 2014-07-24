package ch.usi.dag.et2.tools.etracks.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.AfterInitBodyMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.staticcontext.uid.SequentialMethodUid;
import ch.usi.dag.et2.tools.etracks.local.ElephantTracksStub;


/**
 * Instruments constructor (except {@link Object}), instance method, and static
 * method invocations to maintain the logical clock.
 *
 * @author Lubomir Bulej
 */
public final class MethodInvocations {

    private MethodInvocations () {
        // prevent class instantiation from outside
    }

    //
    // Constructor invocations (except Object)
    //

    /**
     * Instruments all (except {@link Object}) constructors after calling the
     * parent constructor to emit a <i>method entry</i> event.
     */
    @Before (
        marker = AfterInitBodyMarker.class,
        guard = Guard.NonObjectConstructors.class
    )
    public static void onNonObjectConstructorEntry (final SequentialMethodUid mid,
        final ArgumentProcessorContext args, final DynamicContext dc
    ) {
        ElephantTracksStub.onMethodEntry (
            1 /* TODO Advance and get native logical clock. */, 
            mid.get (), dc.getThis ()
        );
    }


    /**
     * Instruments the exit from all (except {@link Object}) constructors to
     * emit a <i>method exit</i> event.
     */
    @After (
        marker = AfterInitBodyMarker.class,
        guard = Guard.NonObjectConstructors.class
    )
    public static void onNonObjectConstructorExit (final SequentialMethodUid mid,
        final MethodStaticContext msc, final DynamicContext dc
    ) {
        ElephantTracksStub.onMethodExit (
            2 /* TODO Advance and get native logical clock. */, 
            mid.get (), dc.getThis ()
        );
    }


    //
    // Method invocations
    //

    /**
     * Instruments the entry to all instance methods (except constructors) to
     * initialize the {@link #threadId} synthetic local variable and emit a
     * <i>method entry</i> event.
     */
    @Before (
        marker = BodyMarker.class,
        guard = Guard.InstanceMethodsExceptConstructors.class
    )
    public static void onMethodEntry (final SequentialMethodUid mid,
        final ArgumentProcessorContext args, final DynamicContext dc
    ) {
        ElephantTracksStub.onMethodEntry (
            1 /* TODO Advance and get native logical clock. */, 
            mid.get (), dc.getThis ()
        );
    }


    /**
     * Instruments the exit from all instance methods (except constructors) to
     * emit a <i>method exit</i> event.
     * <p>
     * Relies on the {@link #threadId} synthetic local variable to have been
     * initialized.
     */
    @After (
        marker = BodyMarker.class,
        guard = Guard.InstanceMethodsExceptConstructors.class
    )
    public static void onMethodExit (
        final SequentialMethodUid mid, final DynamicContext dc
    ) {
        ElephantTracksStub.onMethodExit (
            2 /* TODO Advance and get native logical clock. */, 
            mid.get (), dc.getThis ()
        );
    }


    //
    // Static method invocations
    //

    /**
     * Instrument the entry to all static methods (including static
     * initializers) to initialize the {@link #threadId} synthetic local
     * variable and emit a <i>method entry</i> event.
     */
    @Before (marker = BodyMarker.class, guard = Guard.StaticMethods.class)
    public static void onStaticMethodEntry (
        final SequentialMethodUid mid, final ArgumentProcessorContext args
    ) {
        ElephantTracksStub.onStaticMethodEntry (
            3 /* TODO Advance and get native logical clock. */, mid.get ()
        );
    }


    /**
     * Instruments exit from all static methods (including static initializers)
     * to emit a <i>method exit</i> event.
     * <p>
     * Relies on the {@link #threadId} synthetic local variable to have been
     * initialized.
     */
    @After (marker = BodyMarker.class, guard = Guard.StaticMethods.class)
    public static void onStaticMethodExit (final SequentialMethodUid mid) {
        ElephantTracksStub.onStaticMethodExit (
            4 /* TODO Advance and get native logical clock. */, mid.get ()
        );
    }

}
