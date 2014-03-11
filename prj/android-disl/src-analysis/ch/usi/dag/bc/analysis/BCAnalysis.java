package ch.usi.dag.bc.analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.Replicable;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class BCAnalysis extends RemoteAnalysis {

    public static class ClassStatistic implements Replicable {

        int total;

        // for dumping
        int coveredClass;

        int coveredMethod;

        int coveredEdge;


        public ClassStatistic (final int total) {
            this.total = total;

            this.coveredClass = 0;
            this.coveredMethod = 0;
            this.coveredEdge = 0;
        }


        @Override
        public Replicable replicate () {
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
        public Replicable replicate () {
            return new MethodStatistic (className, coverage);
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


    public void printSPResult (final Context context) {
        final HashSet <ShadowString> classes = new HashSet <> ();
        final Iterator <Entry <Long, ShadowObject>> iter = context.getShadowObjectIterator ();

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

//                System.out.printf (
//                    "PROCESS-%d: %s %d %d\n", shadowAddressSpace.getContext ().pid (),
//                    object.toString (), methodStatistic.coverage.length, counter);
                methodStatistic.className.getState (ClassStatistic.class).coveredEdge += counter;
                methodStatistic.className.getState (ClassStatistic.class).coveredMethod++;
            }
        }

        System.out.println ("############### Classes ###############");

        for (final ShadowString klass : classes) {
            final ClassStatistic statistic = klass.getState (ClassStatistic.class);

            System.out.printf (
                "PROCESS-%d: %s %.2f %d %d %d\n", context.pid (), klass.toString (),
                statistic.total == 0
                    ? Float.NaN
                    : (((float) statistic.coveredEdge) / statistic.total),
                statistic.total, statistic.coveredMethod, statistic.coveredEdge);
        }

        System.out.println ("############### Package ###############");

        final HashMap <String, ClassStatistic> packageCovered = new HashMap <> ();

        for (final ShadowString klass : classes) {
            final String className = klass.toString ();

            final int index = className.lastIndexOf ('/');

            final String packageName;

            if (index == -1) {
                packageName = "default";
            } else {
                packageName = className.substring (0, index);
            }

            ClassStatistic packageStatistic = packageCovered.get (packageName);

            if (packageStatistic == null) {
                packageStatistic = new ClassStatistic (0);
                packageCovered.put (packageName, packageStatistic);
            }

            final ClassStatistic classStatistic = klass.getState (ClassStatistic.class);
            packageStatistic.total += classStatistic.total;
            packageStatistic.coveredClass ++;
            packageStatistic.coveredMethod += classStatistic.coveredMethod;
            packageStatistic.coveredEdge += classStatistic.coveredEdge;
        }

        for (final String key : packageCovered.keySet ()) {
            final ClassStatistic packageStatistic = packageCovered.get (key);
            System.out.printf (
                "PROCESS-%d: %s %.2f %d %d %d %d\n",
                context.pid (),
                key,
                packageStatistic.total == 0
                    ? Float.NaN
                    : (((float) packageStatistic.coveredEdge) / packageStatistic.total),
                packageStatistic.total, packageStatistic.coveredMethod,
                packageStatistic.coveredEdge, packageStatistic.coveredClass);
        }

        System.out.println ("############### Summary ###############");

        final HashMap <String, ClassStatistic> summaryCovered = new HashMap <> ();

        for (final ShadowString klass : classes) {
            final String className = klass.toString ();

            final int index = className.indexOf ('/');

            final String summaryName;

            if (index == -1) {
                summaryName = "default";
            } else {
                summaryName = className.substring (0, index);
            }

            ClassStatistic packageStatistic = summaryCovered.get (summaryName);

            if (packageStatistic == null) {
                packageStatistic = new ClassStatistic (0);
                summaryCovered.put (summaryName, packageStatistic);
            }

            final ClassStatistic classStatistic = klass.getState (ClassStatistic.class);
            packageStatistic.total += classStatistic.total;
            packageStatistic.coveredClass ++;
            packageStatistic.coveredMethod += classStatistic.coveredMethod;
            packageStatistic.coveredEdge += classStatistic.coveredEdge;
        }

        for (final String key : summaryCovered.keySet ()) {
            final ClassStatistic packageStatistic = summaryCovered.get (key);
            System.out.printf (
                "PROCESS-%d: %s %.2f %d %d %d %d\n",
                context.pid (),
                key,
                packageStatistic.total == 0
                    ? Float.NaN
                    : (((float) packageStatistic.coveredEdge) / packageStatistic.total),
                packageStatistic.total, packageStatistic.coveredMethod,
                packageStatistic.coveredEdge, packageStatistic.coveredClass);
        }
    }


    public void printResult () {
        for (final Context context : Context.getAllContext ()) {
            printSPResult (context);
        }
    }


    @Override
    public void atExit (final Context context) {
        printSPResult (context);
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }

}
