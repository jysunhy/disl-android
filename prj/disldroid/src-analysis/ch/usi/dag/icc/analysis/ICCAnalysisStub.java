package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class ICCAnalysisStub {

    public static short PERMISSION = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.permission_alert");

    public static void permission_alert (final int permission, final String info) {
        AREDispatch.analysisStart (PERMISSION);
        AREDispatch.sendInt (permission);
        AREDispatch.sendObjectPlusData (info);
        AREDispatch.analysisEnd ();
    }
}
