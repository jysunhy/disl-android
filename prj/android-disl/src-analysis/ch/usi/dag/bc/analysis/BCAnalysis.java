package ch.usi.dag.bc.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Replicable;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class BCAnalysis extends RemoteAnalysis {

    public static class ClassStatistic implements Replicable {

        int covered;

        final int total;


        public ClassStatistic (final int total) {
            this.total = total;
            this.covered = 0;
        }


        @Override
        public Replicable replicate (final ShadowAddressSpace shadowAddressSpace) {
            return new ClassStatistic (total);
        }
    }


    public static class MethodStatistic implements Replicable {

        ShadowString className;

        boolean [] coverage;


        public MethodStatistic (final ShadowString className, final int local) {
            this.className = className;
            this.coverage = new boolean [local];
        }


        private MethodStatistic (
            final ShadowString className, final boolean [] coverage) {
            this.className = className;
            this.coverage = Arrays.copyOf (coverage, coverage.length);
        }


        @Override
        public Replicable replicate (final ShadowAddressSpace shadowAddressSpace) {
            return new MethodStatistic (
                (ShadowString) shadowAddressSpace.getClonedShadowObject (className),
                coverage);

        }

    }


    public void sendMeta (
        final ShadowString className, final ShadowString methodID, final int total,
        final int local) {
        if (methodID.getState () == null) {
            if (className.getState () == null) {
                className.setStateIfAbsent (new ClassStatistic (total));
            }

            methodID.setStateIfAbsent (new MethodStatistic (className, local));
        }
    }


    public void commitBranch (final ShadowString methodID, final int index) {
        MethodStatistic status;

        if ((status = methodID.getState (MethodStatistic.class)) != null) {
            status.coverage [index] = true;
        }
    }


    @Override
    public void atExit (final ShadowAddressSpace shadowAddressSpace) {
        System.out.println("EXITING ANALYSIS");
        final HashSet <ShadowString> classes = new HashSet <> ();
        final Iterator <Entry <Long, ShadowObject>> iter = shadowAddressSpace.getShadowObjectIterator ();

        while (iter.hasNext ()) {

            final ShadowObject object = iter.next ().getValue ();
            final Object state = object.getState ();

            if (state == null) {
                continue;
            } else if (state instanceof ClassStatistic) {
                classes.add ((ShadowString) object);
            } else if (state instanceof MethodStatistic) {
                final MethodStatistic methodStatistic = (MethodStatistic) state;

                int counter = 0;

                for (final boolean element : methodStatistic.coverage) {
                    if (element) {
                        counter++;
                    }
                }

                methodStatistic.className.getState (ClassStatistic.class).covered += counter;
            }
        }

        for (final ShadowString klass : classes) {
            final ClassStatistic statistic = klass.getState (ClassStatistic.class);

            System.out.println ("PROCESS-"
                + shadowAddressSpace.getContext ().pid () + ": " + klass.toString ()
                + " " + statistic.total + " " + statistic.covered);
        }
    }


    @Override
    public void objectFree (
        final ShadowAddressSpace shadowAddressSpace, final ShadowObject netRef) {
    }

}
