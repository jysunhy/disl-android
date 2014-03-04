package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class ICCAnalysisStub {

    public static short SS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onStartService");

    public static short CS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onCreateService");

    public static short SC = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.actualCreateService");

    public static short SR = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onSystemReady");


    public static void onStartService (final int caller) {
        AREDispatch.analysisStart (SS);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void onCreateService (final int callee, final boolean isIsolated) {
        AREDispatch.analysisStart (CS);
        AREDispatch.sendInt (callee);
        AREDispatch.sendBoolean (isIsolated);
        AREDispatch.analysisEnd ();
    }


    public static void actualCreateService () {
        AREDispatch.analysisStart (SC);
        AREDispatch.analysisEnd ();
    }


    public static void onSystemReady () {
        AREDispatch.analysisStart (SR);
        AREDispatch.analysisEnd ();
    }

}