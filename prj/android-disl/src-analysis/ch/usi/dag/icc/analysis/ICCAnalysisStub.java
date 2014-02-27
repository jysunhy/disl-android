package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class ICCAnalysisStub {

    public static short SS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onStartService");

    public static short SCS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onScheduleCreateService");

    public static short SC = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.actualCreateService");


    public static void onStartService (final int caller) {
        AREDispatch.analysisStart (SS);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void onScheduleCreateService (final int caller) {
        AREDispatch.analysisStart (SCS);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void actualCreateService () {
        AREDispatch.analysisStart (SC);
        AREDispatch.analysisEnd ();
    }

}
