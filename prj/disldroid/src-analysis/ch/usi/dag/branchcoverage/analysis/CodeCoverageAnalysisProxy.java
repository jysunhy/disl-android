package ch.usi.dag.branchcoverage.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class CodeCoverageAnalysisProxy {
    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.branchcoverage.analysis.CodeCoverageAnalysis.commitBranch");

    public static void commitBranch (
        final String classSignature, final String methodName, final int idx) {
        AREDispatch.analysisStart (CB);
        AREDispatch.sendObjectPlusData (classSignature);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.sendInt (idx);
        AREDispatch.analysisEnd ();
    }
}