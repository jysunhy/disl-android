package ch.usi.dag.ipc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class ICCAnalysisStub {

    public static short PERMISSION = AREDispatch.registerMethod ("ch.usi.dag.icc2.analysis.ICCAnalysis.permission_used");

    public static void permission_used (final String permission_str) {
        AREDispatch.analysisStart (PERMISSION);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (permission_str);
        AREDispatch.analysisEnd ();
    }
}
