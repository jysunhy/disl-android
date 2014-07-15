package ch.usi.dag.et.tools.etracks.local;

import ch.usi.dag.dislre.REDispatch;
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


    private static final Outputter __out__ = Outputter.create ("stub.log").mute ();

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
        REDispatch.analysisStart (__onObjectAllocationId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object);
            REDispatch.sendLong (size);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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
        REDispatch.analysisStart (__onReferenceArrayAllocationId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object);
            REDispatch.sendLong (size);
            REDispatch.sendInt (length);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
    }

    //

    private static final short __onObjectUse1Id__  = __registerMethod ("onObjectUse1");

    public static void onObjectUse1 (
        final long time, final Object object1, final long threadId
    ) {
        __out__.format ("%08d:onObjectUse1: %s, thread %d\n",
            time, __toString (object1), threadId
        );
        REDispatch.analysisStart (__onObjectUse1Id__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object1);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
    }


    private static final short __onObjectUse2Id__  = __registerMethod ("onObjectUse2");

    public static void onObjectUse2 (
        final long time, final Object object1, final Object object2, final long threadId
    ) {
        __out__.format ("%08d:onObjectUse2: %s, %s, thread %d\n",
            time, __toString (object1), __toString (object2), threadId
        );
        REDispatch.analysisStart (__onObjectUse2Id__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (object1);
            REDispatch.sendObject (object2);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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

        REDispatch.analysisStart (__onReferenceElementUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (owner);
            REDispatch.sendInt (elementIndex);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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

        REDispatch.analysisStart (__onInstanceReferenceFieldUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObject (owner);
            REDispatch.sendObjectPlusData (fieldName);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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

        REDispatch.analysisStart (__onStaticReferenceFieldUpdateId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendObjectPlusData (ownerClass);
            REDispatch.sendObjectPlusData (fieldName);
            REDispatch.sendObject (oldTarget);
            REDispatch.sendObject (newTarget);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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
        REDispatch.analysisStart (__onMethodEntryId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendObject (receiver);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
    }


    private static final short __onMethodExitId__ = __registerMethod ("onMethodExit");

    public static void onMethodExit (
        final long time, final int methodId,
        final Object receiver, final long threadId
    ) {
        __out__.format ("%08d:onMethodExit: method %x, receiver %s, thread %d\n",
            time, methodId, __toString (receiver), threadId
        );
        REDispatch.analysisStart (__onMethodExitId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendObject (receiver);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
    }

    //

    private static final short __onStaticMethodEntryId__ = __registerMethod ("onStaticMethodEntry");

    public static void onStaticMethodEntry (
        final long time, final int methodId, final long threadId
    ) {
        __out__.format ("%08d:onStaticMethodEntry: method %x, thread %d\n",
            time, methodId, threadId
        );
        REDispatch.analysisStart (__onStaticMethodEntryId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
    }


    private static final short __onStaticMethodExitId__ = __registerMethod ("onStaticMethodExit");

    public static void onStaticMethodExit (
        final long time, final int methodId, final long threadId
    ) {
        __out__.format ("%08d:onStaticMethodExit: method %x, thread %d\n",
            time, methodId, threadId
        );
        REDispatch.analysisStart (__onStaticMethodExitId__, __ORDERING_ID__);

            REDispatch.sendLong (time);
            REDispatch.sendInt (methodId);
            REDispatch.sendLong (threadId);

        REDispatch.analysisEnd ();
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
            return REDispatch.registerMethod (method.getName ());
        } else {
            throw new RuntimeException (String.format (
                "no method '%s' in class '$%s'", methodName, remoteClass.getName ())
            );
        }
        */
        return REDispatch.registerMethod (__REMOTE_CLASS_NAME__ +"."+ methodName);
    }

}
