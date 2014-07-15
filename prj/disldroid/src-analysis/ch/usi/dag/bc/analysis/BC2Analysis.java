package ch.usi.dag.bc.analysis;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.Forkable;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class BC2Analysis extends RemoteAnalysis implements Forkable {

    public static class ContextStatistic {

        ConcurrentHashMap <String, ClassStatistic> classStatistics = new ConcurrentHashMap <String, ClassStatistic> ();


        public ClassStatistic getClassStatistic (
            final String className, final int totalEdge) {
            ClassStatistic classStatistic;

            if ((classStatistic = classStatistics.get (className)) == null) {
                final ClassStatistic temp = new ClassStatistic (totalEdge);

                if ((classStatistic = classStatistics.putIfAbsent (className, temp)) == null) {
                    classStatistic = temp;
                }
            }

            return classStatistic;
        }


        @Override
        public ContextStatistic clone () {
            final ContextStatistic contextStatistic = new ContextStatistic ();

            for (final String className : classStatistics.keySet ()) {
                contextStatistic.classStatistics.put (
                    className, classStatistics.get (className).clone ());
            }

            return contextStatistic;
        }

    }


    public static class ClassStatistic {

        ConcurrentHashMap <String, boolean []> methodStatistics = new ConcurrentHashMap <String, boolean []> ();

        int totalEdge;

        // for dumping data
        int coveredEdge = 0;

        // for dumping data
        int coveredMethod = 0;


        public ClassStatistic (final int totalEdge) {
            this.totalEdge = totalEdge;
        }


        public boolean [] getMethodStatistic (final String methodID, final int size) {
            boolean [] methodStatistic;

            if ((methodStatistic = methodStatistics.get (methodID)) == null) {
                final boolean [] temp = new boolean [size];

                if ((methodStatistic = methodStatistics.putIfAbsent (methodID, temp)) == null) {
                    methodStatistic = temp;
                }
            }

            return methodStatistic;
        }


        @Override
        public ClassStatistic clone () {
            final ClassStatistic classStatistic = new ClassStatistic (totalEdge);

            for (final String methodName : methodStatistics.keySet ()) {
                final boolean [] methodStatistic = methodStatistics.get (methodName);
                classStatistic.methodStatistics.put (
                    methodName,
                    Arrays.copyOf (methodStatistic, methodStatistic.length));
            }

            return classStatistic;
        }

    }


    ConcurrentHashMap <Context, ContextStatistic> contextStatistics = new ConcurrentHashMap <Context, ContextStatistic> ();


    private String getClassName (final String methodName) {
        final String methodNameWithoutDesc = methodName.substring (
            0, methodName.lastIndexOf ('('));
        return methodNameWithoutDesc.substring (
            0, methodNameWithoutDesc.lastIndexOf ('.'));
    }


    public void sendMeta (
        final ShadowString className, final ShadowString methodID, final int total,
        final int local, final Context context) {

        ContextStatistic contextStatistic;

        if ((contextStatistic = contextStatistics.get (context)) == null) {
            final ContextStatistic temp = new ContextStatistic ();

            if ((contextStatistic = contextStatistics.putIfAbsent (context, temp)) == null) {
                contextStatistic = temp;
            }
        }

        // Create entry
        contextStatistic.getClassStatistic (
            getClassName (methodID.toString ()), total).getMethodStatistic (
            methodID.toString (), local);
    }


    public void commitBranch (
        final ShadowString methodID, final int index, final Context context) {
        ContextStatistic contextStatistic;

        if ((contextStatistic = contextStatistics.get (context)) == null) {
            final ContextStatistic temp = new ContextStatistic ();

            if ((contextStatistic = contextStatistics.putIfAbsent (context, temp)) == null) {
                contextStatistic = temp;
            }
        }

        if (contextStatistic != null) {
            final ClassStatistic classStatistic = contextStatistic.classStatistics.get (getClassName (methodID.toString ()));

            if (classStatistic != null) {
                final boolean [] methodStatistic = classStatistic.methodStatistics.get (methodID.toString ());

                if (methodStatistic != null) {
                    methodStatistic [index] = true;
                }
            }
        }
    }


    public void printResult () {
    }


    @Override
    public void atExit (final Context context) {
        final ContextStatistic contextStatistic = contextStatistics.get (context);

        if (contextStatistic == null) {
            return;
        }

        for (final ClassStatistic classStatistic : contextStatistic.classStatistics.values ()) {
            for (final boolean [] methodStatistic : classStatistic.methodStatistics.values ()) {
                classStatistic.coveredMethod++;

                for (final boolean flag : methodStatistic) {
                    if (flag) {
                        classStatistic.coveredEdge++;
                    }
                }
            }
        }

        System.out.println ("############### Classes ###############");
        for (final String className : contextStatistic.classStatistics.keySet ()) {
            final ClassStatistic classStatistic = contextStatistic.classStatistics.get (className);

            System.out.printf (
                "PROCESS-%d: %s %.2f %d %d %d\n",
                context.pid (),
                className,
                classStatistic.totalEdge == 0
                    ? Float.NaN
                    : (((float) classStatistic.coveredEdge) / classStatistic.totalEdge),
                classStatistic.totalEdge, classStatistic.coveredMethod,
                classStatistic.coveredEdge);
        }
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }


    @Override
    public void onFork (final Context parent, final Context child) {
        final ContextStatistic parentStatistic = contextStatistics.get (parent);

        if (parentStatistic != null) {
            contextStatistics.put (child, parentStatistic.clone ());
        }
    }

}
