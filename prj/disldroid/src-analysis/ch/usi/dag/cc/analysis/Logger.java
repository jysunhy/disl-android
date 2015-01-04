package ch.usi.dag.cc.analysis;

import java.util.HashMap;

import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;


public class Logger {
    static String getPackageName (final String className) {
        final int index = className.lastIndexOf ('/');

        if (index == -1) {
            return "default";
        } else {
            return className.substring (0, index);
        }
    }


    static String getSummaryName (final String className) {
        final int index = className.indexOf ('/');

        if (index == -1) {
            return "default";
        } else {
            return className.substring (0, index);
        }
    }


    // TODO write map-reduce friendly code
    public static void dump (final Context context) {
        System.out.println ("in dump");
        final HashMap <String, StatisticSummary> classCovered = new HashMap <> ();

         boolean observed = false;

        for (final ShadowObject object : context.getShadowObjectIterator ()) {
            final Object state = object.getState ();

            if (state == null) {
                continue;
            } else if (state instanceof CoverageState) {
                final CoverageState methodStatistic = (CoverageState) state;
                methodStatistic.updateCoverageData ();

                if (!observed) {
                     System.out.println ("############### Methods ###############");
                    observed = true;
                }

                System.out.printf (
                    "PROCESS-%d-METHODS: %s %s\n",
                    context.pid (),
                    object.toString (),
                    methodStatistic.toString ());

                final String className = methodStatistic.className;

                StatisticSummary classStatistic = classCovered.get (className);

                if (classStatistic == null) {
                    classStatistic = new StatisticSummary (
                        methodStatistic.classBranch, methodStatistic.classBasicBlock);
                    classCovered.put (className, classStatistic);
                }

                classStatistic.coveredBranches += methodStatistic.coveredBranches;
                classStatistic.coveredBasicBlocks += methodStatistic.coveredBasicBlocks;
                classStatistic.coveredMethod++;
            }
        }

        if (!observed) {
            return;
        }

        System.out.println ("############### Classes ###############");

        for (final String className : classCovered.keySet ()) {
            final StatisticSummary classStatistic = classCovered.get (className);

            System.out.printf (
                "PROCESS-%d-CLASSES: %s %s\n",
                context.pid (),
                className,
                classStatistic.toString ());
        }

    }
}
