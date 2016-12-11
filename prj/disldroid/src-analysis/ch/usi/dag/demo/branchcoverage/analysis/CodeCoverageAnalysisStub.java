package ch.usi.dag.demo.branchcoverage.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class CodeCoverageAnalysisStub {

    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.demo.branchcoverage.analysis.CodeCoverageAnalysis.branchTaken");

    public static void branchTaken (
        final String classSignature, final String methodSignature, final int idx) {
        AREDispatch.analysisStart (CB);
        //AREDispatch.sendObjectPlusData (classSignature);
        //AREDispatch.sendObjectPlusData (methodSignature);
        AREDispatch.sendString (classSignature);
        AREDispatch.sendString (methodSignature);
        AREDispatch.sendInt (idx);
        AREDispatch.analysisEnd ();
    }

    public static short CBB = AREDispatch.registerMethod ("ch.usi.dag.demo.branchcoverage.analysis.CodeCoverageAnalysis.bbTaken");

    public static void bbTaken (
        final String classSignature, final String methodSignature, final int idx) {
        AREDispatch.analysisStart (CBB);
        AREDispatch.sendString (classSignature);
        AREDispatch.sendString (methodSignature);
        AREDispatch.sendInt (idx);
        AREDispatch.analysisEnd ();
    }
}
