package ch.usi.dag.disldroidreserver.shadow;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.Type;

import ch.usi.dag.disldroidreserver.DiSLREServer;
import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;


public class ShadowAddressSpace {

    final static ShadowObject BOOTSTRAP_CLASSLOADER = new ShadowObject (
        null, 0, null);

    private final ConcurrentHashMap <ShadowObject, ConcurrentHashMap <String, byte []>> classLoaderMap;

    private final ConcurrentHashMap <Long, ShadowObject> shadowObjects;

    private final ConcurrentHashMap <Integer, ShadowClass> shadowClasses;

    private final ConcurrentHashMap <String, ShadowClass> name2ShadowClasses;

    final Context context;

    ShadowClass JAVA_LANG_CLASS;


    public ShadowAddressSpace (final int processID) {
        context = new Context (processID, null);

        shadowObjects = new ConcurrentHashMap <Long, ShadowObject> (100);
        classLoaderMap = new ConcurrentHashMap <ShadowObject, ConcurrentHashMap<String,byte[]>> (100);
        shadowClasses = new ConcurrentHashMap <Integer, ShadowClass>(100);
        name2ShadowClasses = new ConcurrentHashMap <String, ShadowClass>(100);
        classLoaderMap.put (
            BOOTSTRAP_CLASSLOADER, new ConcurrentHashMap <String, byte []> ());
    }


    public ShadowAddressSpace onFork (final int childProcessID) {
        final ShadowAddressSpace child = new ShadowAddressSpace (childProcessID);

        // clone shadowObjects
        for (final Long key : shadowObjects.keySet ()) {
            child.shadowObjects.put (
                key, (ShadowObject) (shadowObjects.get (key).clone ()));
        }

        // clone shadowClasses
        for (final Integer key : shadowClasses.keySet ()) {
            final ShadowClass thisClass = shadowClasses.get (key);
            final ShadowClass clonedClass = (ShadowClass) child.getClonedShadowObject (thisClass);
            child.shadowClasses.put (key, clonedClass);
            child.name2ShadowClasses.put (thisClass.getCanonicalName (), clonedClass);
            // if (DiSLREServer.debug) {
            // System.out.println (Thread.currentThread ().getName ()
            // + ": PROCESS-"
            // + context.processID + " Forking Shadow Class: "
            // + clonedClass.getName () + " with net ref "
            // + Long.toHexString (clonedClass.getNetRef ()) + " to PROCESS-"
            // + childProcessID);
            // }
        }

        // clone classLoaderMap
        for (final ShadowObject key : classLoaderMap.keySet ()) {
            final ConcurrentHashMap <String, byte []> value = classLoaderMap.get (key);
            ConcurrentHashMap <String, byte []> clone = null;

            if (key.equals (BOOTSTRAP_CLASSLOADER)) {
                clone = child.classLoaderMap.get (key);
            } else {
                clone = new ConcurrentHashMap <String, byte []> ();

                final ShadowObject clonedKey = child.getClonedShadowObject (key);
                child.classLoaderMap.put (clonedKey, clone);
            }

            for (final String classname : value.keySet ()) {
                clone.put (classname, value.get (classname));
            }
        }

        child.JAVA_LANG_CLASS = (ShadowClass) child.getClonedShadowObject (JAVA_LANG_CLASS);

        for (final ShadowObject value : child.shadowObjects.values ()) {
            value.onFork (child);
        }

        for (final ShadowObject value : child.shadowObjects.values ()) {
            final Object state = value.getState ();

            if (state != null) {
                if (state instanceof Replicable) {
                    value.setState (((Replicable) state).replicate ());

                    // TODO (YZ) test the following code
                    final Class <?> klass = state.getClass ();

                    for (final Field field : klass.getFields ()) {
                        if (ShadowObject.class.isAssignableFrom (field.getType ())) {
                            try {
                                field.set (
                                    value,
                                    child.getClonedShadowObject ((ShadowObject) field.get (value)));
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace ();
                            } catch (final IllegalAccessException e) {
                                e.printStackTrace ();
                            }
                        }
                    }
                } else {
                    throw new DiSLREServerFatalException (
                        "ShadowState is not Replicable");
                }
            }
        }

        for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
            if (analysis instanceof Forkable) {
                ((Forkable) analysis).onFork (context, child.context);
            }
        }

        if (shadowAddressSpaces.putIfAbsent (childProcessID, child) != null) {
            throw new DiSLREServerFatalException (
                "Existing child ShadowAddressSpace");
        }

        return child;
    }


    public Context getContext () {
        return context;
    }


    public ShadowObject getClonedShadowObject (final ShadowObject shadowObject) {
        if (shadowObject == null) {
            return null;
        }

        return getShadowObject (shadowObject.getNetRef ());
    }


    // ShadowObject Utilities

    public ShadowObject getShadowObject (final long net_ref) {
        final long objID = NetReferenceHelper.get_object_id (net_ref);

        if (objID == 0) {
            // reserved ID for null
            return null;
        }

        ShadowObject retVal = shadowObjects.get (objID);

        if (retVal != null) {
            return retVal;
        }

        if (NetReferenceHelper.isClassInstance (net_ref)) {
            throw new DiSLREServerFatalException (Thread.currentThread ().getName ()
                + ": Unknown class instance "
                + Long.toHexString (net_ref));
        } else {
            // Only common shadow object will be generated here
            final ShadowClass klass = getShadowClass (net_ref);
            ShadowObject tmp = null;

            if ("java.lang.String".equals (klass.getName ())) {
                tmp = new ShadowString (this, net_ref, null, klass);
            } else if (isAssignableFromThread (klass)) {
                tmp = new ShadowThread (this, net_ref, null, false, klass);
            } else {
                tmp = new ShadowObject (this, net_ref, klass);
            }

            if ((retVal = shadowObjects.putIfAbsent (objID, tmp)) == null) {
                retVal = tmp;
            }

            return retVal;
        }
    }


    public ShadowObject getShadowObjectWithoutCreating (final long net_ref) {
        final long objID = NetReferenceHelper.get_object_id (net_ref);

        if (objID == 0) {
            // reserved ID for null
            return null;
        }

        return shadowObjects.get (objID);
    }


    public void registerShadowObject (final ShadowObject newObj, final boolean debug) {
        if (newObj == null) {
            throw new DiSLREServerFatalException (
                "Attempting to register a null as a shadow object");
        }

        if (DiSLREServer.debug) {
            System.out.println (Thread.currentThread ().getName ()
                + ": PROCESS-" + context.processID + " Registering object "
                + Long.toHexString (newObj.getNetRef ()));
        }

        final long objID = newObj.getId ();
        final ShadowObject exist = shadowObjects.putIfAbsent (objID, newObj);

        if (exist != null) {

            if (newObj.getId () == exist.getId ()) {
                if (debug) {
                    System.out.println ("Re-register a shadow object.");
                }

                if (newObj.equals (exist)) {
                    return;
                }

                if (newObj instanceof ShadowString) {

                    if (exist instanceof ShadowString) {

                        final ShadowString existShadowString = (ShadowString) exist;
                        final ShadowString newShadowString = (ShadowString) newObj;

                        if (existShadowString.toString () == null) {
                            existShadowString.setValue (newShadowString
                                .toString ());
                            return;
                        }
                    }
                } else if (newObj instanceof ShadowThread) {

                    if (exist instanceof ShadowThread) {

                        final ShadowThread existShadowThread = (ShadowThread) exist;
                        final ShadowThread newShadowThread = (ShadowThread) newObj;

                        if (existShadowThread.getName () == null) {
                            existShadowThread
                                .setName (newShadowThread.getName ());
                            existShadowThread.setDaemon (newShadowThread
                                .isDaemon ());
                            return;
                        }
                    }
                }
            }

            throw new DiSLREServerFatalException ("Duplicated net reference");
        }
    }


    public void freeShadowObject (final ShadowObject obj) {
        shadowObjects.remove (obj.getId ());

        if (NetReferenceHelper.isClassInstance (obj.getNetRef ())) {
            if (DiSLREServer.debug) {
                System.out.println (Thread.currentThread ().getName () + "PROCESS-"
                    + context.processID + " Removing ShadowClass-"
                    + Long.toHexString (obj.getNetRef ()));
            }
            final int classID = NetReferenceHelper.get_class_id (obj.getNetRef ());
            shadowClasses.remove (classID);
            name2ShadowClasses.remove (getShadowClass (classID).getCanonicalName ());
        } else if (classLoaderMap.keySet ().contains (obj)) {
            classLoaderMap.remove (obj);
        }
    }


    private boolean isAssignableFromThread (ShadowClass klass) {

        while (!"java.lang.Object".equals (klass.getName ())) {

            if ("java.lang.Thread".equals (klass.getName ())) {
                return true;
            }

            klass = klass.getSuperclass ();
        }

        return false;
    }


    public ShadowThread createShadowThread (final long net_ref,
        final String name, final boolean isDaemon,
        final ShadowClass klass) {
        return new ShadowThread (this, net_ref, name, isDaemon, klass);
    }


    public ShadowString createShadowString (final long net_ref,
        final String value, final ShadowClass klass) {
        return new ShadowString (this, net_ref, value, klass);
    }


    public Iterable <ShadowObject> getShadowObjectIterator () {
        return shadowObjects.values ();
    }


    // ShadowClass Utilities
    public ShadowClass getShadowClass (final String name) {

        final ShadowClass klass = name2ShadowClasses.get (name);

        if (klass != null) {
            return klass;
        }

        throw new DiSLREServerFatalException (Thread.currentThread ().getName ()
            + " PROCESS-"
            + context.processID + ": Unknown class instance "
            + name);
    }
    public ShadowClass getShadowClass (final long net_ref) {
        final int classID = NetReferenceHelper.get_class_id (net_ref);

        if (classID == 0) {
            // reserved ID for java/lang/Class
            return JAVA_LANG_CLASS;
        }

        final ShadowClass klass = shadowClasses.get (classID);

        if (klass != null) {
            return klass;
        }

        throw new DiSLREServerFatalException (Thread.currentThread ().getName ()
            + " PROCESS-"
            + context.processID + ": Unknown class instance "
            + Integer.toHexString (classID));
    }


    public void loadBytecode (
        ShadowObject loader, final String className, final byte [] classCode,
        final boolean debug) {
        ConcurrentHashMap <String, byte []> classNameMap;

        if (loader == null) {
            // bootstrap loader
            loader = BOOTSTRAP_CLASSLOADER;
        }

        classNameMap = classLoaderMap.get (loader);

        if (classNameMap == null) {

            final ConcurrentHashMap <String, byte []> tmp = new ConcurrentHashMap <String, byte []> ();

            if ((classNameMap = classLoaderMap.putIfAbsent (loader, tmp)) == null) {
                classNameMap = tmp;
            }
        }

        if (classNameMap.putIfAbsent (className.replace ('/', '.'), classCode) != null) {
            if (debug) {
                System.out.println ("DiSL-RE: Reloading/Redefining class "
                    + className);
            }
        }
    }



    public ShadowClass createAndRegisterShadowClass (
        final long net_ref, final ShadowClass superClass, ShadowObject loader,
        final String classSignature, final String classGenericStr,
        final boolean debug) {
        if (!NetReferenceHelper.isClassInstance (net_ref)) {
            throw new DiSLREServerFatalException ("Unknown class instance");
        }

        ShadowClass klass = null;
        final Type t = Type.getType (classSignature);

        if (t.getSort () == Type.ARRAY) {
            // TODO unknown array component type
            klass = new ShadowArrayClass (this, net_ref, loader, superClass, null, t);
        } else if (t.getSort () == Type.OBJECT) {
            ConcurrentHashMap <String, byte []> classNameMap;

            if (loader == null) {
                // bootstrap loader
                loader = BOOTSTRAP_CLASSLOADER;
            }

            classNameMap = classLoaderMap.get (loader);

            if (classNameMap == null) {
                throw new DiSLREServerFatalException ("Unknown class loader");
            }

            final byte [] classCode = classNameMap.get (t.getClassName ());

            if (classCode == null) {
                throw new DiSLREServerFatalException ("Class "
                    + t.getClassName () + " has not been loaded");
            }

            klass = new ShadowCommonClass (this, net_ref, classSignature, loader,
                superClass, classCode);
        } else {

            klass = new ShadowPrimitiveClass (this, net_ref, loader, t);
        }

        final int classID = NetReferenceHelper.get_class_id (net_ref);
        final ShadowClass exist = shadowClasses.putIfAbsent (classID, klass);
        name2ShadowClasses.putIfAbsent (klass.getCanonicalName (), klass);
        if (DiSLREServer.debug) {
            System.out.println (Thread.currentThread ().getName () + ": PROCESS-"
                + context.processID + " Creating Shadow Class: "
                + classSignature + " with net ref " + Long.toHexString (net_ref));
        }

        if (exist == null) {
            registerShadowObject (klass, debug);
        } else if (!exist.equals (klass)) {
            throw new DiSLREServerFatalException ("Duplicated class ID");
        }

        if (JAVA_LANG_CLASS == null
            && "Ljava/lang/Class;".equals (classSignature)) {
            JAVA_LANG_CLASS = klass;
        }

        return klass;
    }


    // Singleton Utilities

    private static final ConcurrentHashMap <Integer, ShadowAddressSpace> shadowAddressSpaces = new ConcurrentHashMap <Integer, ShadowAddressSpace>();


    public static Collection <ShadowAddressSpace> getAllShadowAddressSpace () {
        return shadowAddressSpaces.values ();
    }


    public static ShadowAddressSpace getShadowAddressSpace (final Integer pid) {
        ShadowAddressSpace shadowAddressSpace = null;

        if ((shadowAddressSpace = shadowAddressSpaces.get (pid)) == null) {
            final ShadowAddressSpace temp = new ShadowAddressSpace (pid);

            if ((shadowAddressSpace = shadowAddressSpaces.putIfAbsent (pid, temp)) == null) {
                shadowAddressSpace = temp;
            }
        }

        return shadowAddressSpace;
    }


    public static ShadowAddressSpace getShadowAddressSpaceBlocked (final Integer pid) {
        ShadowAddressSpace shadowAddressSpace;

        while ((shadowAddressSpace = shadowAddressSpaces.get (pid)) == null) {
            try {
                Thread.sleep (10);
            } catch (final InterruptedException e) {}
        }

        return shadowAddressSpace;
    }


    /**
     * @return true if no process left
     */
    public static boolean removeShadowAddressSpace (
        final ShadowAddressSpace shadowAddressSpace) {
        shadowAddressSpaces.remove (shadowAddressSpace.context.processID);
        return shadowAddressSpaces.size () == 0;
    }


	public static void cleanup(){
		shadowAddressSpaces.clear();
	}

}
