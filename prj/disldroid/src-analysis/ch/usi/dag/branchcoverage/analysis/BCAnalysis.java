package ch.usi.dag.branchcoverage.analysis;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.Replicable;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class BCAnalysis extends RemoteAnalysis {

    private static float divide (final int a, final int b) {
        return b == 0 ? Float.NaN : (((float) a) / b);
    }

    public static class ClassStatistic implements Replicable {

        int classBranch;

        // for dumping
        int coveredClass;

        int coveredMethod;

        int coveredBranches;


        public ClassStatistic (final int classBranch) {
            this.classBranch = classBranch;
            this.coveredClass = 0;
            this.coveredMethod = 0;
            this.coveredBranches = 0;
        }


        @Override
        public Replicable replicate () {
            return new ClassStatistic (classBranch);
        }


        @Override
        public String toString () {
            final Formatter formatter = new Formatter ();

            formatter.format ("%d %d %.2f %d %d",
                classBranch,
                coveredBranches,
                divide (coveredBranches, classBranch),
                coveredMethod,
                coveredClass);

            final String res = formatter.toString ();
            formatter.close ();

            return res;
        }


        public String toTexString () {
            final Formatter formatter = new Formatter ();

            formatter.format (" &  %.2f   &  %d  &  %d",
                divide (coveredBranches, classBranch),
                coveredMethod,
                coveredClass);

            final String res = formatter.toString ();
            formatter.close ();

            return res;
        }


        public void merge (final ClassStatistic classStatistic) {
            classBranch += classStatistic.classBranch;
            coveredBranches += classStatistic.coveredBranches;

            coveredClass++;
            coveredMethod += classStatistic.coveredMethod;
        }

    }


    public static class MethodStatistic implements Replicable {

        ShadowString className;

        boolean [] branches;

        // for dumping
        int coveredBranches;

        public MethodStatistic (
            final ShadowString className, final int methodBranchCount) {
            this.className = className;
            this.branches = new boolean [methodBranchCount];

            this.coveredBranches = 0;
        }


        private MethodStatistic (
            final ShadowString className, final boolean [] branches) {
            this.className = className;
            this.branches = Arrays.copyOf (branches, branches.length);

            this.coveredBranches = 0;
        }


        @Override
        public Replicable replicate () {
            return new MethodStatistic (className, branches);
        }


        public void updateCoverageData () {
            coveredBranches = 0;

            for (final boolean element : branches) {
                if (element) {
                    coveredBranches++;
                }
            }

        }


        @Override
        public String toString () {
            final Formatter formatter = new Formatter ();

            final StringBuilder branchBitmap = new StringBuilder ();

            for (final boolean element : branches) {
                if (element) {
                    branchBitmap.append ('1');
                } else {
                    branchBitmap.append ('0');
                }
            }

            final StringBuilder basicblockBitmap = new StringBuilder ();

            formatter.format ("%d %d %.2f %s %s",
                branches.length,
                coveredBranches,
                divide (coveredBranches, branches.length),
                branchBitmap.toString (),
                basicblockBitmap.toString ());

            final String res = formatter.toString ();
            formatter.close ();

            return res;
        }

    }


    public void sendMeta (
        final ShadowString className, final ShadowString methodID,
        final int classBranch,
        final int methodBranch) {
        if (methodID.getState () == null) {
            if (className.getState () == null) {
                className.setStateIfAbsent (new ClassStatistic (
                    classBranch));
            }

            methodID.setStateIfAbsent (new MethodStatistic (
                className, methodBranch));
        }
    }


    public void commitBranch (final ShadowString methodID, final int index) {
        MethodStatistic status;

        if ((status = methodID.getState (MethodStatistic.class)) != null) {
            status.branches [index] = true;
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


    private static String getPackageName (final String className) {
        final int index = className.lastIndexOf ('/');

        if (index == -1) {
            return "default";
        } else {
            return className.substring (0, index);
        }
    }


    private static String getSummaryName (final String className) {
        final int index = className.indexOf ('/');

        if (index == -1) {
            return "default";
        } else {
            return className.substring (0, index);
        }
    }


    // TODO write map-reduce friendly code
    public void printSPResult (final Context context) {
        final HashSet <ShadowString> classes = new HashSet <> ();

        System.out.println ("############### Methods ###############");

        for (final ShadowObject object : context.getShadowObjectIterator ()) {
            final Object state = object.getState ();

            if (state == null) {
                continue;
            } else if (state instanceof ClassStatistic) {
                classes.add ((ShadowString) object);
            } else if (state instanceof MethodStatistic) {
                final MethodStatistic methodStatistic = (MethodStatistic) state;
                methodStatistic.updateCoverageData ();

                System.out.printf (
                    "PROCESS-%d-METHODS: %s %s\n",
                    context.pid (),
                    object.toString (),
                    methodStatistic.toString ());

                final ClassStatistic classStatistic = methodStatistic.className.getState (ClassStatistic.class);

                classStatistic.coveredBranches += methodStatistic.coveredBranches;
                classStatistic.coveredMethod++;
            }
        }

        System.out.println ("############### Classes ###############");

        for (final ShadowString klass : classes) {
            final ClassStatistic statistic = klass.getState (ClassStatistic.class);

            System.out.printf (
                "PROCESS-%d-CLASSES: %s %s\n",
                context.pid (),
                klass.toString (),
                statistic.toString ());
        }

        System.out.println ("############### Package ###############");

        final HashMap <String, ClassStatistic> packageCovered = new HashMap <> ();

        for (final ShadowString klass : classes) {
            final String packageName = getPackageName (klass.toString ());
            ClassStatistic packageStatistic = packageCovered.get (packageName);

            if (packageStatistic == null) {
                packageStatistic = new ClassStatistic (0);
                packageCovered.put (packageName, packageStatistic);
            }

            final ClassStatistic classStatistic = klass.getState (ClassStatistic.class);
            packageStatistic.merge (classStatistic);
        }

        for (final String key : packageCovered.keySet ()) {
            final ClassStatistic packageStatistic = packageCovered.get (key);
            System.out.printf (
                "PROCESS-%d-PACKAGE: %s %s\n",
                context.pid (),
                key,
                packageStatistic.toString ());
        }

        System.out.println ("############### Tex Output ###############");

        for (final String key : packageCovered.keySet ()) {
            final ClassStatistic packageStatistic = packageCovered.get (key);
            System.out.printf (
                "%s %s \\\\\n",
                key,
                packageStatistic.toTexString ());
        }

        System.out.println ("############### Summary ###############");

        final HashMap <String, ClassStatistic> summaryCovered = new HashMap <> ();

        for (final ShadowString klass : classes) {
            final String summaryName = getSummaryName (klass.toString ());
            ClassStatistic packageStatistic = summaryCovered.get (summaryName);

            if (packageStatistic == null) {
                packageStatistic = new ClassStatistic (0);
                summaryCovered.put (summaryName, packageStatistic);
            }

            final ClassStatistic classStatistic = klass.getState (ClassStatistic.class);
            packageStatistic.merge (classStatistic);
        }

        for (final String key : summaryCovered.keySet ()) {
            final ClassStatistic packageStatistic = summaryCovered.get (key);
            System.out.printf (
                "PROCESS-%d-SUMMARY: %s %s\n",
                context.pid (),
                key,
                packageStatistic.toString ());
        }
    }


}
