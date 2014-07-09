package ch.usi.dag.icc.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class ICCAnalysis extends RemoteAnalysis {

    public void permission_alert(final String alertinfo){
        System.out.println ("Permission use detected in "+alertinfo);
    }

    public void callServiceInClient (
        final ShadowString methodName, final Context context) {
        callService (methodName, context.pid ());
    }

    public void println (final ShadowString methodName) {
        System.out.println (methodName.toString ());
    }

    ConcurrentHashMap <Integer, AtomicInteger> ssCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

    ConcurrentHashMap <Integer, AtomicInteger> bsCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

    ConcurrentHashMap <Integer, AtomicInteger> saCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

    ConcurrentHashMap <Integer, AtomicInteger> scCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

    ConcurrentHashMap <Integer, AtomicInteger> btCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

    ConcurrentHashMap <Integer, AtomicInteger> otCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();


    public void increase (
        final Integer caller,
        final ConcurrentHashMap <Integer, AtomicInteger> counters) {
        AtomicInteger counter;

        if ((counter = counters.get (caller)) == null) {
            final AtomicInteger temp = new AtomicInteger ();

            if ((counter = counters.putIfAbsent (caller, temp)) == null) {
                counter = temp;
            }
        }

        counter.incrementAndGet ();
    }


    public void onStartService (final int caller) {
        increase (caller, ssCounters);
    }


    public void onBindService (final int caller) {
        increase (caller, bsCounters);
    }


    public void onStartActivity (final int caller) {
        increase (caller, saCounters);
    }


    public void onServiceConstructor (final Context context) {
        increase (context.pid (), scCounters);
    }


    public void onBinderTransact (final Context context) {
        increase (context.pid (), btCounters);
    }


    public void onBinderOnTransact (final Context context) {
        increase (context.pid (), otCounters);
    }


    public void onCreateService (final int callee, final boolean isIsolated) {
    }


    ConcurrentHashMap <Integer, ConcurrentHashMap <String, AtomicInteger>> allAMSCounters = new ConcurrentHashMap <Integer, ConcurrentHashMap <String, AtomicInteger>> ();


    public void callService (final ShadowString methodName, final int caller) {
        ConcurrentHashMap <String, AtomicInteger> currentAMSCounters;

        if ((currentAMSCounters = allAMSCounters.get (caller)) == null) {
            final ConcurrentHashMap <String, AtomicInteger> temp = new ConcurrentHashMap <String, AtomicInteger> ();

            if ((currentAMSCounters = allAMSCounters.putIfAbsent (caller, temp)) == null) {
                currentAMSCounters = temp;
            }
        }

        final String methodKey = methodName.toString ();
        AtomicInteger counter;

        if ((counter = currentAMSCounters.get (methodKey)) == null) {
            final AtomicInteger temp = new AtomicInteger ();

            if ((counter = currentAMSCounters.put (methodKey, temp)) == null) {
                counter = temp;
            }
        }

        counter.incrementAndGet ();
    }


    public void onCheckPermission (
        final int caller, final ShadowString permission, final Context context) {
        if (caller != context.pid ()) {
            System.out.printf (
                "PROCESS-%d %s: checkPermission %s\n", caller,
                Context.getContext (caller).getPname (),
                permission.toString ());
        }
    }


    public void onGetContentProvider (final int caller, final ShadowString name) {
        System.out.printf (
            "PROCESS-%d %s: getContentProvider %s\n", caller,
            Context.getContext (caller).getPname (),
            name.toString ());
    }


    public void onSystemReady () {
//        final HashSet <Integer> processes = new HashSet <> ();

//        processes.addAll (ssCounters.keySet ());
//        processes.addAll (bsCounters.keySet ());
//        processes.addAll (saCounters.keySet ());
//        processes.addAll (scCounters.keySet ());
//        processes.addAll (btCounters.keySet ());
//        processes.addAll (otCounters.keySet ());
//
//        for (final Integer pid : processes) {
//            final AtomicInteger ssCounter = ssCounters.get (pid);
//            final AtomicInteger bsCounter = bsCounters.get (pid);
//            final AtomicInteger saCounter = saCounters.get (pid);
//            final AtomicInteger scCounter = scCounters.get (pid);
//            final AtomicInteger btCounter = btCounters.get (pid);
//            final AtomicInteger otCounter = otCounters.get (pid);
//
//            System.out.printf (
//                "PROCESS-%d %s: %d %d %d %d %d %d\n", pid,
//                Context.getContext (pid).getPname (),
//                ssCounter == null ? 0 : ssCounter.get (),
//                bsCounter == null ? 0 : bsCounter.get (),
//                saCounter == null ? 0 : saCounter.get (),
//                scCounter == null ? 0 : scCounter.get (),
//                btCounter == null ? 0 : btCounter.get (),
//                otCounter == null ? 0 : otCounter.get ());
//        }


        System.out.println ("+++++++++++++++++++++++++++++");

        final ArrayList <Integer> pids = new ArrayList <Integer> (allAMSCounters.keySet ());
        Collections.sort (pids);

        for (final Integer pid : pids) {
            final String name = Context.getContext (pid).getPname ();

            //if (!("dalvikvm".equals (name))) {
            //    continue;
            //}

            final ConcurrentHashMap <String, AtomicInteger> currentAMSCounters = allAMSCounters.get (pid);
            final ArrayList <String> methodNames = new ArrayList<String> (currentAMSCounters.keySet ());
            Collections.sort (methodNames);

            final HashMap <String, Integer> classCounters = new HashMap<String, Integer> ();

            for (final String methodName : methodNames) {
                final int methodCounter = currentAMSCounters.get (methodName).get ();
                System.out.printf (
                    "PROCESS-%05d-%s-METHODS: %s %d\n", pid, name, methodName,
                    methodCounter);

                final String className = methodName.substring (
                    0, methodName.indexOf ('.'));

                final Integer classCounter = classCounters.get (className);
                classCounters.put (className, classCounter == null
                    ? methodCounter : classCounter + methodCounter);
            }

            for (final String className : classCounters.keySet ()) {
                System.out.printf (
                    "PROCESS-%05d-%s-CLASSES: %s %d\n", pid, name, className,
                    classCounters.get (className));
            }
        }
    }


    @Override
    public void atExit (final Context context) {
    }


    @Override
    public void objectFree (
        final Context context, final ShadowObject netRef) {
    }

}
