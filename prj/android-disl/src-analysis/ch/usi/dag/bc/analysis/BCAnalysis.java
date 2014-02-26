package ch.usi.dag.bc.analysis;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class BCAnalysis extends RemoteAnalysis {

    ConcurrentHashMap <ShadowString, ShadowString> coverages = new ConcurrentHashMap <> ();


    public static class ClassStatistic {
        public int covered;

        public int total;


        public ClassStatistic (final int total) {
            this.total = total;
            this.covered = 0;
        }
    }


    public static class MethodStatistic {

        public ShadowString className;

        public boolean [] coverage;


        public MethodStatistic (final ShadowString className, final int local) {
            this.className = className;
            this.coverage = new boolean [local];
        }
    }


    public void sendMeta (
        final ShadowString className, final ShadowString methodID, final int total,
        final int local) {
        if (methodID.getState () == null) {
            className.setStateIfAbsent (new ClassStatistic (total));
            methodID.setStateIfAbsent (new boolean [local]);
            coverages.putIfAbsent (methodID, className);
        }
    }


    public void commitBranch (final ShadowString methodID, final int index) {
        boolean [] status;

        if ((status = methodID.getState (boolean [].class)) != null) {
            status [index] = true;
        }
    }


    @Override
    public void atExit (final ShadowAddressSpace shadowAddressSpace) {
        final HashSet <ShadowString> classes = new HashSet <> ();

        for (final ShadowString method : coverages.keySet ()) {
            final boolean [] coverage = method.getState (boolean [].class);

            if (coverage == null) {
                continue;
            }

            int counter = 0;

            for (final boolean element : coverage) {
                if (element) {
                    counter++;
                }
            }

            final ShadowString klass = coverages.get (method);
            classes.add (klass);

            klass.getState (ClassStatistic.class).covered += counter;
        }

        for (final ShadowString klass : classes) {
            final ClassStatistic statistic = klass.getState (ClassStatistic.class);

            System.out.println ("Result:"+klass.toString ()
                + " " + statistic.total + " " + statistic.covered);
        }
    }


    @Override
    public void objectFree (
        final ShadowAddressSpace shadowAddressSpace, final ShadowObject netRef) {
    }

}
