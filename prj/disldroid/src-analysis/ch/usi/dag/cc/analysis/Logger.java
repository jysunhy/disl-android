package ch.usi.dag.cc.analysis;

import java.util.HashMap;

import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;


public class Logger {
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




    public static void dump (final Context context) {
        final HashMap <String, StatisticSummary> classCovered = new HashMap <> ();

        System.out.println ("############### Methods ###############");

        for (final ShadowObject object : context.getShadowObjectIterator ()) {
            final Object state = object.getState ();

            if (state == null) {
                continue;
            } else if (state instanceof CoverageState) {
                final CoverageState methodStatistic = (CoverageState) state;
                methodStatistic.updateCoverageData ();

                System.out.printf (
                    "PROCESS-%d-METHODS: %s %s\n",
                    context.pid (),
                    object.toString (),
                    methodStatistic.toString ());

                // TODO
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

        System.out.println ("############### Classes ###############");

        for (final String className : classCovered.keySet ()) {
            final StatisticSummary classStatistic = classCovered.get (className);

            System.out.printf (
                "PROCESS-%d-CLASSES: %s %s\n",
                context.pid (),
                className,
                classStatistic.toString ());
        }

        System.out.println ("############### Package ###############");

        final HashMap <String, StatisticSummary> packageCovered = new HashMap <> ();

        for (final String className : classCovered.keySet ()) {
            final StatisticSummary classStatistic = classCovered.get (className);
            final String packageName = getPackageName (className);
            StatisticSummary packageStatistic = packageCovered.get (packageName);

            if (packageStatistic == null) {
                packageStatistic = new StatisticSummary (0, 0);
                packageCovered.put (packageName, packageStatistic);
            }

            packageStatistic.merge (classStatistic);
        }

        for (final String key : packageCovered.keySet ()) {
            final StatisticSummary packageStatistic = packageCovered.get (key);
            System.out.printf (
                "PROCESS-%d-PACKAGE: %s %s\n",
                context.pid (),
                key,
                packageStatistic.toString ());
        }

        System.out.println ("############### Tex Output ###############");

        for (final String key : packageCovered.keySet ()) {
            final StatisticSummary packageStatistic = packageCovered.get (key);
            System.out.printf (
                "%s %s \\\\\n",
                key,
                packageStatistic.toTexString ());
        }

        System.out.println ("############### Summary ###############");

        final HashMap <String, StatisticSummary> summaryCovered = new HashMap <> ();

        for (final String className : classCovered.keySet ()) {
            final StatisticSummary classStatistic = classCovered.get (className);
            final String summaryName = getSummaryName (className);
            StatisticSummary packageStatistic = summaryCovered.get (summaryName);

            if (packageStatistic == null) {
                packageStatistic = new StatisticSummary (0, 0);
                summaryCovered.put (summaryName, packageStatistic);
            }

            packageStatistic.merge (classStatistic);
        }

        for (final String key : summaryCovered.keySet ()) {
            final StatisticSummary packageStatistic = summaryCovered.get (key);
            System.out.printf (
                "PROCESS-%d-SUMMARY: %s %s\n",
                context.pid (),
                key,
                packageStatistic.toString ());
        }
    }
}
