package ch.usi.dag.bc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class BCAnalysisStub {

    public static short SM = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.sendMeta");

    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.commitBranch");


    public static void sendMeta (
        final String className, final String methodID, final int total,
        final int local) {
        AREDispatch.analysisStart (SM);
        AREDispatch.sendObjectPlusData (className);
        AREDispatch.sendObjectPlusData (methodID);
        AREDispatch.sendInt (total);
        AREDispatch.sendInt (local);
        AREDispatch.analysisEnd ();
    }


    public static void commitBranch (final String methodID, final int index) {
        AREDispatch.analysisStart (CB);
        AREDispatch.sendObjectPlusData (methodID);
        AREDispatch.sendInt (index);
        AREDispatch.analysisEnd ();
    }

}
