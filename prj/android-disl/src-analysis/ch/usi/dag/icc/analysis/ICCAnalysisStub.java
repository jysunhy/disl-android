package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislre.REDispatch;


public class ICCAnalysisStub {

    public static short SS = REDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onStartService");

    public static short SCS = REDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onScheduleCreateService");

    public static short SC = REDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.actualCreateService");


    public static void onStartService (final int caller) {
        REDispatch.analysisStart (SS);
        REDispatch.sendInt (caller);
        REDispatch.analysisEnd ();
    }


    public static void onScheduleCreateService (final int caller) {
        REDispatch.analysisStart (SCS);
        REDispatch.sendInt (caller);
        REDispatch.analysisEnd ();
    }


    public static void actualCreateService () {
        REDispatch.analysisStart (SC);
        REDispatch.analysisEnd ();
    }

}
