package ch.usi.dag.et2.tools.etracks.remote;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.disldroidreserver.shadow.ShadowThread;
import ch.usi.dag.et2.tools.etracks.remote.trace.TraceRecorder;
import ch.usi.dag.et2.tools.etracks.util.Config;
import ch.usi.dag.et2.tools.etracks.util.LogicalClock;
import ch.usi.dag.et2.tools.etracks.util.Outputter;


/**
 * Represents the remote skeleton of the Elephant Tracks analysis. This class
 * only forwards method invocations to the proper analysis class, decoupling it
 * from DiSL.
 * <p>
 * An instance of this class is created automatically by DiSL-RE when the stub
 * part of the analysis registers the skeleton methods.
 *
 * @author Lubomir Bulej
 */
public final class ElephantTracksSkeleton extends RemoteAnalysis /* XXX: why not an interface? */ {

    private static final Outputter __out__ = Outputter.create ("remote.log");

    //

    private final TraceRecorder __recorder = new TraceRecorder ();

    //

    /**
     * Creates a new remote skeleton for the analysis.
     * <p>
     * This constructor is called by DiSL-RE.
     */
    public ElephantTracksSkeleton () {
        // nothing special to do
    }

    //

    public void onObjectAllocation (
        final long ovmTime, final ShadowObject object, final long size,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onObjectAllocation: %s, size %d, thread %s\n",
            time, object, size, thread
        );

        __onObjectAllocation (time, object, size, thread);
    }

    //

    public void onReferenceArrayAllocation (
        final long ovmTime, final ShadowObject object, final long size,
        final int length, final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onReferenceArrayAllocation: %s [%d], size %d, thread %s\n",
            time, object, length, size, thread
        );

        __onObjectAllocation (time, object, size, thread);
    }

    //

    public void onObjectUse1 (
        final long ovmTime, final ShadowObject object1,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onObjectUse1: %s, thread %s\n",
            time, object1, thread
        );

        __timestampObject (object1, time, thread);

        __recorder.recordObjectUse (time, object1.getId (), __safeId (thread));
    }


    public void onObjectUse2 (
        final long ovmTime, final ShadowObject object1, final ShadowObject object2,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onObjectUse2: %s, %s, thread %s\n",
            time, object1, object2, thread
        );

        __timestampObject (object1, time, thread);
        __timestampObject (object2, time, thread);

        __recorder.recordObjectUse (time, object1.getId (), __safeId (thread));
        __recorder.recordObjectUse (time, object2.getId (), __safeId (thread));
    }

    //

    public void onReferenceElementUpdate (
        final long ovmTime, final ShadowObject ownerArray, final int elementIndex,
        final ShadowObject oldTarget, final ShadowObject newTarget,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onReferenceElementUpdate: %s [%d], old %s, new %s, thread %s\n",
            time, ownerArray, elementIndex, oldTarget, newTarget, thread
        );

        final ShadowState ownerState = __timestampObject (ownerArray, time, thread);
        final ShadowObject shadowOldTarget = ownerState.arrayStore (elementIndex, newTarget);

        if (oldTarget != shadowOldTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%d] = %s (expected %s)\n",
                time, ownerArray, elementIndex, shadowOldTarget, oldTarget
            );

            __timestampObject (shadowOldTarget, time, thread);
        }

        __onReferenceUpdate (time, ownerArray, oldTarget, newTarget, thread);
    }


    public void onInstanceReferenceFieldUpdate (
        final long ovmTime, final ShadowObject ownerObject, final ShadowString fieldName,
        final ShadowObject oldTarget, final ShadowObject newTarget,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format (
            "%08d:onInstanceReferenceFieldUpdate: owner %s [%s], old %s, new %s, thread %s\n",
            time, ownerObject, fieldName.toString (), oldTarget, newTarget, thread
        );

        final ShadowState ownerState = __timestampObject (ownerObject, time, thread);
        final ShadowObject oldShadowTarget = ownerState.putField (fieldName, newTarget);
        if (oldTarget != oldShadowTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%s] = %s (expected %s)\n",
                time, ownerObject, fieldName.toString (), oldShadowTarget, oldTarget
            );

            __timestampObject (oldShadowTarget, time, thread);
        }

        __onReferenceUpdate (time, ownerObject, oldTarget, newTarget, thread);
    }


    public void onStaticReferenceFieldUpdate (
        final long ovmTime, final ShadowClass ownerClass, final ShadowString fieldName,
        final ShadowObject oldTarget, final ShadowObject newTarget,
        final ShadowThread thread
    ) {
        final long time = LogicalClock.current ();
        __out__.format (
            "%08d:onStaticReferenceFieldUpdate: owner %s [%s], old %s, new %s, thread %s\n",
            time, ownerClass, fieldName.toString (), oldTarget, newTarget, thread
        );

        final ShadowState ownerState = __timestampObject (ownerClass, time, thread);
        final ShadowObject oldShadowTarget = ownerState.putField (fieldName, newTarget);
        if (oldTarget != oldShadowTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%s] = %s (expected %s)\n",
                time, ownerClass, fieldName.toString (), oldShadowTarget, oldTarget
            );

            __timestampObject (oldShadowTarget, time, thread);
        }

        __onReferenceUpdate (time, ownerClass, oldTarget, newTarget, thread);
    }


    private void __onReferenceUpdate (
        final long time, final ShadowObject owner, final ShadowObject oldTarget,
        final ShadowObject newTarget, final ShadowThread thread
    ) {
        __timestampObject (oldTarget, time, thread);
        __timestampObject (newTarget, time, thread);

        __recorder.recordPointerUpdate (
            time, owner.getId (),
            __safeId (oldTarget), __safeId (newTarget), __safeId (thread)
        );
    }

    //

    public void onMethodEntry (
        final long ovmTime, final int methodId,
        final ShadowObject receiver, final ShadowThread thread
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onMethodEntry: method %x, receiver %s, thread %s\n",
            time, methodId, receiver, thread
        );

        __timestampObject (receiver, time, thread);
        __recorder.recordMethodEntry (
            time, methodId, receiver.getId (), __safeId (thread)
        );
    }


    public void onMethodExit (
        final long ovmTime, final int methodId,
        final ShadowObject receiver, final ShadowThread thread
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onMethodExit: method %x, receiver %s, thread %s\n",
            time, methodId, receiver, thread
        );

        __timestampObject (receiver, time, thread);
        __recorder.recordMethodExit (
            time, methodId, receiver.getId (), __safeId (thread)
        );
    }

    //

    public void onStaticMethodEntry (
        final long ovmTime, final int methodId, final ShadowThread thread
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onStaticMethodEntry: method %x, thread %s\n",
            time, methodId, thread
        );

        __recorder.recordMethodEntry (time, methodId, 0, __safeId (thread));
    }


    public void onStaticMethodExit (
        final long ovmTime, final int methodId, final ShadowThread thread
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onStaticMethodExit: method %x, thread %s\n",
            time, methodId, thread
        );

        __recorder.recordMethodExit (time, methodId, 0, __safeId (thread));
    }


    /* ***********************************************************************
     * RemoteAnalysis
    /* ***********************************************************************/

    @Override
    public void objectFree (final Context context, final ShadowObject object) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:objectFree: %s\n", time, object);

        //
        // TODO: Properly handle logical time on objectFree()
        //
        // - DiSL could make the call back locally (not this version)
        // - we maintain the logical time in the analysis on this (remote) side
        // - we keep track of the time stamps here (*)
        //
        // (*) This is a temporary solution to make the signatures of
        // the analysis class symmetric. If we move the time keeping to
        // the analysis, it will disappear from here.
        //
        ShadowState state = object.getState (ShadowState.class);
        if (state == null) {
            state = __onObjectAllocation (time, object, -1, null);
        }

        final long deathTime = state.handleShadowDeath ();
        __recorder.recordObjectDeath (deathTime,  object.getId ());
    }

    @Override
    public void atExit (final Context context) {
    	final long time = LogicalClock.current ();
        __out__.format ("%08d:atExit: start\n", time);

        //
        // Go through all shadow objects and perform a fake GC. Unfortunately,
        // we don't know the live roots, so we just propagate the timestamps
        // so that every object will have the maximum last-use timestamp that
        // it would receive from objects it was reachable from.
        //
        for (final ShadowObject object : context.getShadowObjectIterator ()) {
            ShadowState state = object.getState (ShadowState.class);
            if (state == null) {
                // Objects not seen before are fake-allocated
                state = __onObjectAllocation (time, object, -1, null);
            }

            state.deepPropagateLastUse ();
        }

        //
        // We then make all the objects dead in a sequential manner and
        // output their corresponding death records.
        //
        // This is inherently imprecise, because we cannot distinguish between
        // objects that died but remained on the heap since last GC.
        //
        for (final ShadowObject object : context.getShadowObjectIterator ()) {
            final ShadowState state = object.getState (ShadowState.class);

            final long deathTime =  state.handleShadowDeath ();
            __out__.format ("%08d:fakeObjectFree: %s\n", time, object);
            __recorder.recordObjectExitDeath (time, object.getId (), deathTime);
        }

        __recorder.dumpRecords (Config.traceFile ());
        __out__.format ("%08d:atExit: end\n", time);
    }


    /* ***********************************************************************
     * Common utility methods
    /* ***********************************************************************/

    private ShadowState __timestampObject (
        final ShadowObject object, final long time, final ShadowThread thread
    ) {
        //
        // Ignore null shadow objects
        //
        if (object == null) {
            return null;
        }

        //
        // If the shadow object has a shadow state, we have seen it before,
        // so just timestamp it. If the object has no shadow state associated,
        // create it and log object creation at this time, but with unknown
        // size and allocating thread.
        //
        final ShadowState state = object.getState (ShadowState.class);
        if (state != null) {
            state.updateLastUseTime (time);
            return state;

        } else {
            __out__.format ("%08d:previouslyUnseen: %s, identity %x\n",
                time, object, System.identityHashCode (object)
            );
            return __onObjectAllocation (time, object, -1, thread);
        }
    }


    private ShadowState __onObjectAllocation (
        final long time, final ShadowObject object, final long size,
        final ShadowThread thread
    ) {
        final ShadowState shadowState = ShadowState.forObject (object, time);
        object.setState (shadowState);

        final ShadowClass sc = object.getShadowClass ();
        __recorder.recordObjectAllocation (
            time,  object.getId (), (sc != null) ? sc.getName () : "unknown",
            size,  __safeId (thread)
        );

        return shadowState;
    }

    //

    private static long __safeId (final ShadowObject shadow) {
        return (shadow != null) ? shadow.getId () : 0;
    }

    private static long __safeId (final ShadowThread thread) {
        return (thread != null) ? thread.getId () : -1;
    }

    //

    static Outputter getOutput () {
        return __out__;
    }

}
