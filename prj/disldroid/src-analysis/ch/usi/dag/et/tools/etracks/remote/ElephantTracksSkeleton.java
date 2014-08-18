package ch.usi.dag.et.tools.etracks.remote;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.et.tools.etracks.remote.trace.TraceRecorder;
import ch.usi.dag.et.tools.etracks.util.Config;
import ch.usi.dag.et.tools.etracks.util.LogicalClock;
import ch.usi.dag.et.tools.etracks.util.Outputter;


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
        long time, final ShadowObject object, final long size,
        final long threadId
    ) {
        time = LogicalClock.current ();
        __out__.format ("%08d:onObjectAllocation: %s, size %d, thread %d\n",
            time, __shadowToString (object), size, threadId
        );

        __onObjectAllocation (time, object, size, threadId);
    }

    //

    public void onReferenceArrayAllocation (
        final long ovmTime, final ShadowObject object, final long size,
        final int length, final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onReferenceArrayAllocation: %s [%d], size %d, thread %d\n",
            time, __shadowToString (object), length, size, threadId
        );

        __onObjectAllocation (time, object, size, threadId);
    }

    //

    public void onObjectUse1 (
        final long ovmTime, final ShadowObject object1, final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onObjectUse1: %s, thread %d\n",
            time, __shadowToString (object1), threadId
        );

        __timestampObject (object1, time);

        __recorder.recordObjectUse (time, object1.getId (), threadId);
    }


    public void onObjectUse2 (
        final long ovmTime, final ShadowObject object1, final ShadowObject object2,
        final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onObjectUse2: %s, %s, thread %d\n",
            time, __shadowToString (object1), __shadowToString (object2), threadId
        );

        __timestampObject (object1, time);
        __timestampObject (object2, time);

        __recorder.recordObjectUse (time, object1.getId (), threadId);
        __recorder.recordObjectUse (time, object2.getId (), threadId);
    }

    //

    public void onReferenceElementUpdate (
        final long ovmTime, final ShadowObject ownerArray, final int elementIndex,
        final ShadowObject oldTarget, final ShadowObject newTarget, final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:onReferenceElementUpdate: %s [%d], old %s, new %s, thread %d\n",
            time, __shadowToString (ownerArray), elementIndex,
            __shadowToString (oldTarget), __shadowToString (newTarget),
            threadId
        );

        final ShadowState ownerState = __timestampObject (ownerArray, time);
        final ShadowObject shadowOldTarget = ownerState.arrayStore (elementIndex, newTarget);

        if (oldTarget != shadowOldTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%d] = shadow %s, should be %s\n",
                time, ownerArray, elementIndex, __shadowToString (shadowOldTarget), __shadowToString (oldTarget)
            );

            __timestampObject (shadowOldTarget, time);
        }

        __onReferenceUpdate (time, ownerArray, oldTarget, newTarget, threadId);
    }


    public void onInstanceReferenceFieldUpdate (
        final long ovmTime, final ShadowObject ownerObject, final ShadowString fieldName,
        final ShadowObject oldTarget, final ShadowObject newTarget,
        final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format (
            "%08d:onInstanceReferenceFieldUpdate: owner %s [%s], old %s, new %s, thread %d\n",
            time, __shadowToString (ownerObject), fieldName,
            __shadowToString (oldTarget), __shadowToString (newTarget),
            threadId
        );

        final ShadowState ownerState = __timestampObject (ownerObject, time);
        final ShadowObject shadowOldTarget = ownerState.putField (fieldName, newTarget);
        if (oldTarget != shadowOldTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%s] = shadow %s, should be %s\n",
                time, ownerObject, fieldName, __shadowToString (shadowOldTarget), __shadowToString (oldTarget)
            );

            __timestampObject (shadowOldTarget, time);
        }

        __onReferenceUpdate (time, ownerObject, oldTarget, newTarget, threadId);
    }


    public void onStaticReferenceFieldUpdate (
        final long ovmTime, final ShadowClass ownerClass, final ShadowString fieldName,
        final ShadowObject oldTarget, final ShadowObject newTarget,
        final long threadId
    ) {
        final long time = LogicalClock.current ();
        __out__.format (
            "%08d:onStaticReferenceFieldUpdate: owner %s [%s], old %s, new %s, thread %d\n",
            time, ownerClass.getName (), fieldName, __shadowToString (oldTarget),
            __shadowToString (newTarget), threadId
        );

        final ShadowState ownerState = __timestampObject (ownerClass, time);
        final ShadowObject shadowOldTarget = ownerState.putField (fieldName, newTarget);
        if (oldTarget != shadowOldTarget) {
            //
            // If the old reference from the shadow heap does not match the
            // old reference we obtained, complain about inconsistent shadow
            // heap, but timestamp the old shadow reference anyway - we might
            // see it freed, after all.
            //
            __out__.format (
                "%08d:inconsistentShadowHeap: object %s [%s] = shadow %s, should be %s\n",
                time, ownerClass.getName (), fieldName, __shadowToString (shadowOldTarget), __shadowToString (oldTarget)
            );

            __timestampObject (shadowOldTarget, time);
        }

        __onReferenceUpdate (time, ownerClass, oldTarget, newTarget, threadId);
    }


    private void __onReferenceUpdate (
        final long time, final ShadowObject owner, final ShadowObject oldTarget,
        final ShadowObject newTarget, final long threadId
    ) {
        __timestampObject (oldTarget, time);
        __timestampObject (newTarget, time);

        __recorder.recordPointerUpdate (
            time, owner.getId (), __shadowId (oldTarget),
            __shadowId (newTarget), threadId
        );
    }

    //

    public void onMethodEntry (
        final long ovmTime, final int methodId,
        final ShadowObject receiver, final long threadId
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onMethodEntry: method %x, receiver %s, thread %d\n",
            time, methodId, __shadowToString (receiver), threadId
        );

        __timestampObject (receiver, time);
        __recorder.recordMethodEntry (time, methodId, receiver.getId (), threadId);
    }


    public void onMethodExit (
        final long ovmTime, final int methodId,
        final ShadowObject receiver, final long threadId
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onMethodExit: method %x, receiver %s, thread %d\n",
            time, methodId, __shadowToString (receiver), threadId
        );

        __timestampObject (receiver, time);
        __recorder.recordMethodExit (time, methodId, receiver.getId (), threadId);
    }

    //

    public void onStaticMethodEntry (
        final long ovmTime, final int methodId, final long threadId
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onStaticMethodEntry: method %x, thread %d\n",
            time, methodId, threadId
        );

        __recorder.recordMethodEntry (time, methodId, 0, threadId);
    }


    public void onStaticMethodExit (
        final long ovmTime, final int methodId, final long threadId
    ) {
        final long time = LogicalClock.next ();
        __out__.format ("%08d:onStaticMethodExit: method %x, thread %d\n",
            time, methodId, threadId
        );

        __recorder.recordMethodExit (time, methodId, 0, threadId);
    }


    /* ***********************************************************************
     * RemoteAnalysis
    /* ***********************************************************************/

    @Override
    public void objectFree (final Context context, final ShadowObject object) {
        final long time = LogicalClock.current ();
        __out__.format ("%08d:objectFree: %s\n",
            time, __shadowToString (object)
        );

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
            state = __onObjectAllocation (time, object, -1, -1);
        }

        final long deathTime = state.handleShadowDeath ();
        __recorder.recordObjectDeath (deathTime,  object.getId ());
    }

    @Override
    public void atExit (final Context context) {
    	final long currentTime = LogicalClock.current ();

    	for (final ShadowObject leftObject : context.getShadowObjectIterator ()) {
            ShadowState state = leftObject.getState (ShadowState.class);
            if (state == null) {
                state = __onObjectAllocation (currentTime, leftObject, -1, -1);
            }
            final long deathTime =  state.handleShadowDeath ();
            __out__.format ("%08d:objectFree: %s\n",
                    currentTime, __shadowToString (leftObject));
            __recorder.recordObjectDeath (deathTime,  leftObject.getId ());
    	}

    	System.out.println("Freed at death time: " + currentTime);
        __out__.format ("%08d:atExit\n", LogicalClock.current ());
        __recorder.dumpRecords (Config.traceFile ());
    }


    /* ***********************************************************************
     * Common utility methods
    /* ***********************************************************************/

    private ShadowState __timestampObject (final ShadowObject object, final long time) {
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
            return __onObjectAllocation (time, object, -1, -1);
        }
    }


    private ShadowState __onObjectAllocation (
        final long time, final ShadowObject object, final long size,
        final long threadId
    ) {
        final ShadowState shadowState = ShadowState.forObject (object, time);
        object.setState (shadowState);

        final ShadowClass sc = object.getShadowClass ();
        __recorder.recordObjectAllocation (
            time,  object.getId (), (sc != null) ? sc.getName () : "unknown",
            size,  threadId
        );

        return shadowState;
    }

    //

    private static long __shadowId (final ShadowObject shadow) {
        return (shadow != null) ? shadow.getId () : 0;
    }


    private static String __shadowToString (final ShadowObject object) {
        if (object != null) {
            final ShadowClass sc = object.getShadowClass ();
            final String scName = (sc != null) ? sc.getName () : "<missing>";
            return String.format ("%s@%x", scName, object.getId ());
        } else {
            return "<null>";
        }
    }

    //

    static Outputter getOutput () {
        return __out__;
    }

}
