package ch.usi.dag.icc.analysis;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.ShadowObject;


public class ICCAnalysis extends RemoteAnalysis {


    ConcurrentHashMap <Integer, AtomicInteger> ssCounters = new ConcurrentHashMap <> ();
    ConcurrentHashMap <Integer, AtomicInteger> bsCounters = new ConcurrentHashMap <> ();
    ConcurrentHashMap <Integer, AtomicInteger> saCounters = new ConcurrentHashMap <> ();


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


    public void onCreateService (final int callee, final boolean isIsolated) {
    }


    public void onSystemReady () {
        final HashSet <Integer> processes = new HashSet <> ();

        processes.addAll (ssCounters.keySet ());
        processes.addAll (bsCounters.keySet ());
        processes.addAll (saCounters.keySet ());

        for (final Integer pid : processes) {
            final AtomicInteger ssCounter = ssCounters.get (pid);
            final AtomicInteger bsCounter = bsCounters.get (pid);
            final AtomicInteger saCounter = saCounters.get (pid);

            System.out.printf (
                "PROCESS-%d %s: %d %d %d\n", pid,
                Context.getContext (pid).getPname (),
                ssCounter == null ? 0 : ssCounter.get (),
                bsCounter == null ? 0 : bsCounter.get (),
                saCounter == null ? 0 : saCounter.get ());
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
