package ch.usi.dag.branchcoverage.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class BCAnalysisStub {
    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.branchcoverage.analysis.BCAnalysis.commitBranch");
    public static short PR = AREDispatch.registerMethod ("ch.usi.dag.branchcoverage.analysis.BCAnalysis.printAllResult");

    public static void commitBranch (
        final String className, final String methodName, final boolean [] branches) {
        for (int i = 0; i < branches.length; i++) {
            if (branches [i]) {
                AREDispatch.analysisStart (CB);
                AREDispatch.sendObjectPlusData (className);
                AREDispatch.sendObjectPlusData (methodName);
                AREDispatch.sendInt (i);
                AREDispatch.analysisEnd ();
            }
        }
    }

    public static void printAllResult () {
        AREDispatch.analysisStart (PR);
        AREDispatch.analysisEnd ();
    }

}
