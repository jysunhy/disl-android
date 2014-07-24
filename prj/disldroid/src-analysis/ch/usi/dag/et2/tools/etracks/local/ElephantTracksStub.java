package ch.usi.dag.et2.tools.etracks.local;

import ch.usi.dag.dislre.REDispatch;


/**
 * Represents the local stub of the Elephant Tracks analysis.
 * <p>
 * The code of this class only performs the remote invocation and should be
 * generated. Fully qualified method names must be used during the registration
 * of the analysis methods and cannot result from any string operations, because
 * this cause premature initialization of certain classes.
 *
 * @author Lubomir Bulej
 */
public final class ElephantTracksStub {

    private ElephantTracksStub () {
        // prevent class instantiation from outside
    }

    private static final byte __ORDERING_ID__ = 0;

    //
    
    private static final short __onObjectAllocationId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectAllocation");
    private static final short __onReferenceArrayAllocationId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onReferenceArrayAllocation");
    
    private static final short __onObjectUse1Id__  = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectUse1");
    private static final short __onObjectUse2Id__  = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectUse2");

    private static final short __onReferenceElementUpdateId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onReferenceElementUpdate");
    private static final short __onInstanceReferenceFieldUpdateId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onInstanceReferenceFieldUpdate");
    private static final short __onStaticReferenceFieldUpdateId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticReferenceFieldUpdate");

    private static final short __onMethodEntryId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onMethodEntry");
    private static final short __onMethodExitId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onMethodExit");
    private static final short __onStaticMethodEntryId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticMethodEntry");
    private static final short __onStaticMethodExitId__ = REDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticMethodExit");

    //


    public static void onObjectAllocation (
        final long time, final Object object
    ) {
        REDispatch.analysisStart (__onObjectAllocationId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object);
            REDispatch.sendObjectSize (object);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onReferenceArrayAllocation (
        final long time, final Object object, final int length
    ) {
        REDispatch.analysisStart (__onReferenceArrayAllocationId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object);
            REDispatch.sendObjectSize (object);
            REDispatch.sendInt (length); 
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }

    //

    public static void onObjectUse1 (
        final long time, final Object object1
    ) {
        REDispatch.analysisStart (__onObjectUse1Id__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object1);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onObjectUse2 (
        final long time, final Object object1, final Object object2
    ) {
        REDispatch.analysisStart (__onObjectUse2Id__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object1);
            REDispatch.sendObject (object2);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }

    //


    public static void onReferenceElementUpdate (
        final long time, final Object owner, final int elementIndex,
        final Object oldTarget, final Object newTarget
    ) {
        REDispatch.analysisStart (__onReferenceElementUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (owner);
            REDispatch.sendInt (elementIndex);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    
    public static void onInstanceReferenceFieldUpdate (
        final long time, final Object owner, final String fieldName,
        final Object oldTarget, final Object newTarget
    ) {
        REDispatch.analysisStart (__onInstanceReferenceFieldUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (owner);
            REDispatch.sendObjectPlusData (fieldName);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onStaticReferenceFieldUpdate (
        final long time, final Class <?> ownerClass, final String fieldName,
        final Object oldTarget, final Object newTarget
    ) {
        REDispatch.analysisStart (__onStaticReferenceFieldUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObjectPlusData (ownerClass);
            REDispatch.sendObjectPlusData (fieldName);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }

    //


    public static void onMethodEntry (
        final long time, final int methodId, final Object receiver
    ) {
        REDispatch.analysisStart (__onMethodEntryId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendObject (receiver);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onMethodExit (
        final long time, final int methodId, final Object receiver
    ) {
        REDispatch.analysisStart (__onMethodExitId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendObject (receiver);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onStaticMethodEntry (
        final long time, final int methodId
    ) {
        REDispatch.analysisStart (__onStaticMethodEntryId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }


    public static void onStaticMethodExit (
        final long time, final int methodId
    ) {
        REDispatch.analysisStart (__onStaticMethodExitId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendCurrentThread ();

        REDispatch.analysisEnd ();
    }

}
