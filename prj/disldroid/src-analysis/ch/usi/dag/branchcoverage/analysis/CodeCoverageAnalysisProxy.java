package ch.usi.dag.branchcoverage.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class CodeCoverageAnalysisProxy {
    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.branchcoverage.analysis.CodeCoverageAnalysis.branchTaken");

    public static void branchTaken (
        final String classSignature, final String methodSignature, final int idx) {
        AREDispatch.analysisStart (CB);
        AREDispatch.sendObjectPlusData (classSignature);
        AREDispatch.sendObjectPlusData (methodSignature);
        AREDispatch.sendInt (idx);
        AREDispatch.analysisEnd ();
    }
}