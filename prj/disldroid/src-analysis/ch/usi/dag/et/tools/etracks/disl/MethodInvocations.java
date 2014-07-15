package ch.usi.dag.et.tools.etracks.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.AfterInitBodyMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.staticcontext.uid.UniqueMethodId;
import ch.usi.dag.et.tools.etracks.local.LocalElephantTracks;


/**
 * Instruments constructor (except {@link Object}), instance method, and static
 * method invocations to maintain the logical clock.
 *
 * @author Lubomir Bulej
 */
public final class MethodInvocations {

    /**
     * Caches the current thread identifier in a method invocation. Initialized
     * by a method entry snippet.
     * <p>
     * TODO Consider moving the thread ID query to methods in {@link LocalElephantTracks}.
     */
    @SyntheticLocal
    static long threadId;

    //

    private MethodInvocations () {
        // prevent class instantiation from outside
    }


    //
    // Constructor invocations (except Object)
    //

    /**
     * Instruments all (except {@link Object}) constructors after calling the
     * parent constructor to initialize the {@link #threadId} synthetic
     * local variable and emit a <i>method entry</i> event.
     */
    @Before (
        marker = AfterInitBodyMarker.class,
        guard = Guard.NonObjectConstructors.class
    )
    public static void onNonObjectConstructorEntry (final UniqueMethodId umi,
        final ArgumentProcessorContext args, final DynamicContext dc
    ) {
        threadId = Thread.currentThread ().getId ();
        LocalElephantTracks.onMethodEntry (umi.get (), dc.getThis (), threadId);
    }


    /**
     * Instruments the exit from all (except {@link Object}) constructors to
     * emit a <i>method exit</i> event.
     * <p>
     * Relies on the {@link #threadId} synthetic local variable to have been
     * initialized.
     */
    @After (
        marker = AfterInitBodyMarker.class,
        guard = Guard.NonObjectConstructors.class
    )
    public static void onNonObjectConstructorExit (final UniqueMethodId umi,
        final MethodStaticContext msc, final DynamicContext dc
    ) {
        LocalElephantTracks.onMethodExit (umi.get (), dc.getThis (), threadId);
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
    public static void onMethodEntry (final UniqueMethodId umi,
        final ArgumentProcessorContext args, final DynamicContext dc
    ) {
        threadId = Thread.currentThread ().getId ();
        LocalElephantTracks.onMethodEntry (umi.get (), dc.getThis (), threadId);
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
        final UniqueMethodId umi, final DynamicContext dc
    ) {
        LocalElephantTracks.onMethodExit (umi.get (), dc.getThis (), threadId);
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
        final UniqueMethodId umi, final ArgumentProcessorContext args
    ) {
        threadId = Thread.currentThread ().getId ();
        LocalElephantTracks.onStaticMethodEntry (umi.get (), threadId);
    }


    /**
     * Instruments exit from all static methods (including static initializers)
     * to emit a <i>method exit</i> event.
     * <p>
     * Relies on the {@link #threadId} synthetic local variable to have been
     * initialized.
     */
    @After (marker = BodyMarker.class, guard = Guard.StaticMethods.class)
    public static void onStaticMethodExit (final UniqueMethodId umi) {
        LocalElephantTracks.onStaticMethodExit (umi.get (), threadId);
    }

}
