package ch.usi.dag.et.tools.etracks.local;

import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.et.tools.etracks.util.Outputter;


/**
 * Represents the local stub of the Elephant Tracks analysis.
 * <p>
 * The code of this class only performs the remote invocation and should be
 * generated.
 *
 * @author Lubomir Bulej
 */
final class ElephantTracksStub {

    private ElephantTracksStub () {
        // prevent class instantiation from outside
    }


    private static final Outputter __out__ = Outputter.NULL;//Outputter.create ("/data/stub.log").mute ();

    private static final byte __ORDERING_ID__ = 0;

    private static final String __REMOTE_CLASS_NAME__ =
        "ch.usi.dag.et.tools.etracks.remote.ElephantTracksSkeleton";

    //

    private static final short __onObjectAllocationId__ = __registerMethod ("onObjectAllocation");

    public static void onObjectAllocation (
        final long time, final Object object, final long size, final long threadId
    ) {
        __out__.format ("%08d:onObjectAllocation: %s, size %d, thread %d\n",
            time, __toString (object), size, threadId
        );
		AREDispatch.NativeLog("IN 1");
        AREDispatch.analysisStart (__onObjectAllocationId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object);
            AREDispatch.sendLong (size);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 1");
    }

    //

    private static final short __onReferenceArrayAllocationId__ = __registerMethod ("onReferenceArrayAllocation");

    public static void onReferenceArrayAllocation (
        final long time, final Object object, final long size,
        final int length, final long threadId
    ) {
        __out__.format ("%08d:onReferenceArrayAllocation: %s [%d], size %d, thread %d\n",
            time, __toString (object), length, size, threadId
        );
		AREDispatch.NativeLog("IN 2");
        AREDispatch.analysisStart (__onReferenceArrayAllocationId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object);
            AREDispatch.sendLong (size);
            AREDispatch.sendInt (length);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 2");
    }

    //

    private static final short __onObjectUse1Id__  = __registerMethod ("onObjectUse1");

    public static void onObjectUse1 (
        final long time, final Object object1, final long threadId
    ) {
        __out__.format ("%08d:onObjectUse1: %s, thread %d\n",
            time, __toString (object1), threadId
        );
		AREDispatch.NativeLog("IN 3");
        AREDispatch.analysisStart (__onObjectUse1Id__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object1);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 3");
    }


    private static final short __onObjectUse2Id__  = __registerMethod ("onObjectUse2");

    public static void onObjectUse2 (
        final long time, final Object object1, final Object object2, final long threadId
    ) {
        __out__.format ("%08d:onObjectUse2: %s, %s, thread %d\n",
            time, __toString (object1), __toString (object2), threadId
        );
		AREDispatch.NativeLog("IN 4");
        AREDispatch.analysisStart (__onObjectUse2Id__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (object1);
            AREDispatch.sendObject (object2);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 4");
    }

    //

    private static final short __onReferenceElementUpdateId__ = __registerMethod ("onReferenceElementUpdate");

    public static void onReferenceElementUpdate (
        final long time, final Object owner, final int elementIndex,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        __out__.format ("%08d:onReferenceElementUpdate: %s [%d], old %s, new %s, thread %d\n",
            time, __toString (owner), elementIndex,
            __toString (oldTarget), __toString (newTarget), threadId
        );

		AREDispatch.NativeLog("IN 5");
        AREDispatch.analysisStart (__onReferenceElementUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (owner);
            AREDispatch.sendInt (elementIndex);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 5");
    }


    private static final short __onInstanceReferenceFieldUpdateId__ =  __registerMethod ("onInstanceReferenceFieldUpdate");

    public static void onInstanceReferenceFieldUpdate (
        final long time, final Object owner, final String fieldName,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        __out__.format ("%08d:onInstanceReferenceFieldUpdate: %s [%s], old %s, new %s, thread %d\n",
            time, __toString (owner), fieldName,
            __toString (oldTarget), __toString (newTarget), threadId
        );

		AREDispatch.NativeLog("IN 6");
        AREDispatch.analysisStart (__onInstanceReferenceFieldUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObject (owner);
            AREDispatch.sendObjectPlusData (fieldName);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 6");
    }


    private static final short __onStaticReferenceFieldUpdateId__ =  __registerMethod ("onStaticReferenceFieldUpdate");

    public static void onStaticReferenceFieldUpdate (
        final long time, final Class <?> ownerClass, final String fieldName,
        final Object oldTarget, final Object newTarget, final long threadId
    ) {
        __out__.format ("%08d:onStaticReferenceFieldUpdate: %s [%s], old %s, new %s, thread %d\n",
            time, ownerClass.getName (), fieldName,
            __toString (oldTarget), __toString (newTarget), threadId
        );

		AREDispatch.NativeLog("IN 7");
        AREDispatch.analysisStart (__onStaticReferenceFieldUpdateId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendObjectPlusData (ownerClass);
            AREDispatch.sendObjectPlusData (fieldName);
            AREDispatch.sendObject (oldTarget);
            AREDispatch.sendObject (newTarget);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 7");
    }

    //

    private static final short __onMethodEntryId__ = __registerMethod ("onMethodEntry");

    public static void onMethodEntry (
        final long time, final int methodId, final Object receiver,
        final long threadId
    ) {
        __out__.format ("%08d:onMethodEntry: method %x, receiver %s, thread %d\n",
            time, methodId, __toString (receiver), threadId
        );
		AREDispatch.NativeLog("IN 8");
        AREDispatch.analysisStart (__onMethodEntryId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendObject (receiver);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 8");
    }


    private static final short __onMethodExitId__ = __registerMethod ("onMethodExit");

    public static void onMethodExit (
        final long time, final int methodId,
        final Object receiver, final long threadId
    ) {
        __out__.format ("%08d:onMethodExit: method %x, receiver %s, thread %d\n",
            time, methodId, __toString (receiver), threadId
        );
		AREDispatch.NativeLog("IN 9");
        AREDispatch.analysisStart (__onMethodExitId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendObject (receiver);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 9");
    }

    //

    private static final short __onStaticMethodEntryId__ = __registerMethod ("onStaticMethodEntry");

    public static void onStaticMethodEntry (
        final long time, final int methodId, final long threadId
    ) {
        __out__.format ("%08d:onStaticMethodEntry: method %x, thread %d\n",
            time, methodId, threadId
        );
		AREDispatch.NativeLog("IN 10");
        AREDispatch.analysisStart (__onStaticMethodEntryId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 10");
    }


    private static final short __onStaticMethodExitId__ = __registerMethod ("onStaticMethodExit");

    public static void onStaticMethodExit (
        final long time, final int methodId, final long threadId
    ) {
        __out__.format ("%08d:onStaticMethodExit: method %x, thread %d\n",
            time, methodId, threadId
        );
		AREDispatch.NativeLog("IN 11");
        AREDispatch.analysisStart (__onStaticMethodExitId__, __ORDERING_ID__);

            AREDispatch.sendLong (time);
            AREDispatch.sendInt (methodId);
            AREDispatch.sendLong (threadId);

        AREDispatch.analysisEnd ();
		AREDispatch.NativeLog("OUT 11");
    }

    //

    private static String __toString (final Object object) {
        if (object != null) {
            return String.format ("%s@%x",
                object.getClass ().getName (), System.identityHashCode (object)
            );
        } else {
            return "<null>";
        }
    }

    //

    private static short __registerMethod (final String methodName) {
        //
        // We cannot use reflection, because this would also need the
        // RemoteAnalysis class. If it was an interface, it would be easier to
        // include it with the "local" package.
        //
        /*
        final Class <ElephantTracksSkeleton> remoteClass = ElephantTracksSkeleton.class;

        final Method method = Reflection.findClassMethod (remoteClass, methodName);
        if (method != null) {
            return AREDispatch.registerMethod (method.getName ());
        } else {
            throw new RuntimeException (String.format (
                "no method '%s' in class '$%s'", methodName, remoteClass.getName ())
            );
        }
        */
        return AREDispatch.registerMethod (__REMOTE_CLASS_NAME__ +"."+ methodName);
    }

}
