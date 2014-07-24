package ch.usi.dag.et2.tools.etracks.local;

import ch.usi.dag.dislre.AREDispatch;


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

    private static final short __onObjectAllocationId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectAllocation");
    private static final short __onReferenceArrayAllocationId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onReferenceArrayAllocation");

    private static final short __onObjectUse1Id__  = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectUse1");
    private static final short __onObjectUse2Id__  = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onObjectUse2");

    private static final short __onReferenceElementUpdateId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onReferenceElementUpdate");
    private static final short __onInstanceReferenceFieldUpdateId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onInstanceReferenceFieldUpdate");
    private static final short __onStaticReferenceFieldUpdateId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticReferenceFieldUpdate");

    private static final short __onMethodEntryId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onMethodEntry");
    private static final short __onMethodExitId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onMethodExit");
    private static final short __onStaticMethodEntryId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticMethodEntry");
    private static final short __onStaticMethodExitId__ = AREDispatch.registerMethod ("ch.usi.dag.et2.tools.etracks.remote.ElephantTracksSkeleton.onStaticMethodExit");

    //


    public static void onObjectAllocation (
        final long time, final Object object
    ) {
        AREDispatch.analysisStart (__onObjectAllocationId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object);
            AREDispatch.sendObjectSize (object);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onReferenceArrayAllocation (
        final long time, final Object object, final int length
    ) {
        AREDispatch.analysisStart (__onReferenceArrayAllocationId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object);
            AREDispatch.sendObjectSize (object);
            AREDispatch.sendInt (length);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }

    //

    public static void onObjectUse1 (
        final long time, final Object object1
    ) {
        AREDispatch.analysisStart (__onObjectUse1Id__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object1);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onObjectUse2 (
        final long time, final Object object1, final Object object2
    ) {
        AREDispatch.analysisStart (__onObjectUse2Id__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object1);
            AREDispatch.sendObject (object2);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }

    //


    public static void onReferenceElementUpdate (
        final long time, final Object owner, final int elementIndex,
        final Object oldTarget, final Object newTarget
    ) {
        AREDispatch.analysisStart (__onReferenceElementUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (owner);
            AREDispatch.sendInt (elementIndex);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }



    public static void onInstanceReferenceFieldUpdate (
        final long time, final Object owner, final String fieldName,
        final Object oldTarget, final Object newTarget
    ) {
        AREDispatch.analysisStart (__onInstanceReferenceFieldUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (owner);
            AREDispatch.sendObjectPlusData (fieldName);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onStaticReferenceFieldUpdate (
        final long time, final Class <?> ownerClass, final String fieldName,
        final Object oldTarget, final Object newTarget
    ) {
        AREDispatch.analysisStart (__onStaticReferenceFieldUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObjectPlusData (ownerClass);
            AREDispatch.sendObjectPlusData (fieldName);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }

    //


    public static void onMethodEntry (
        final long time, final int methodId, final Object receiver
    ) {
        AREDispatch.analysisStart (__onMethodEntryId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendObject (receiver);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onMethodExit (
        final long time, final int methodId, final Object receiver
    ) {
        AREDispatch.analysisStart (__onMethodExitId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendObject (receiver);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onStaticMethodEntry (
        final long time, final int methodId
    ) {
        AREDispatch.analysisStart (__onStaticMethodEntryId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }


    public static void onStaticMethodExit (
        final long time, final int methodId
    ) {
        AREDispatch.analysisStart (__onStaticMethodExitId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendCurrentThread ();

        AREDispatch.analysisEnd ();
    }

}
