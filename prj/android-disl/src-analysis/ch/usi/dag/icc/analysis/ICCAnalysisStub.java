package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class ICCAnalysisStub {

    public static short PERMISSION = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.permission_alert");

    public static void permission_alert (final String methodname, final String permission) {
        AREDispatch.analysisStart (PERMISSION);
        AREDispatch.sendObjectPlusData (methodname);
        AREDispatch.sendObjectPlusData (permission);
        AREDispatch.analysisEnd ();
    }


    public static short CSC = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.callServiceInClient");

    public static void callServiceInClient (final String methodName) {
        AREDispatch.analysisStart (CSC);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.analysisEnd ();
    }




    public static short PL = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.println");


    public static void println (final String methodName) {
        AREDispatch.analysisStart (PL);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.analysisEnd ();
    }


    public static short SS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onStartService");

    public static short BS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onBindService");

    public static short SA = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onStartActivity");

    public static short SR = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onSystemReady");

    public static short SC = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onServiceConstructor");

    public static short BT = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onBinderTransact");

    public static short OT = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onBinderOnTransact");


    public static void onStartService (final int caller) {
        AREDispatch.analysisStart (SS);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void onBindService (final int caller) {
        AREDispatch.analysisStart (BS);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void onStartActivity (final int caller) {
        AREDispatch.analysisStart (SA);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static void onSystemReady () {
        AREDispatch.analysisStart (SR);
        AREDispatch.analysisEnd ();
    }


    public static void onServiceConstructor () {
        AREDispatch.analysisStart (SC);
        AREDispatch.analysisEnd ();
    }


    public static void onBinderTransact () {
        AREDispatch.analysisStart (BT);
        AREDispatch.analysisEnd ();
    }


    public static void onBinderOnTransact () {
        AREDispatch.analysisStart (OT);
        AREDispatch.analysisEnd ();
    }


    public static short CS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.callService");


    public static void callService (
        final String methodName, final int caller) {
        AREDispatch.analysisStart (CS);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static short CP = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onCheckPermission");


    public static void onCheckPermission (final int caller, final String permission) {
        AREDispatch.analysisStart (CP);
        AREDispatch.sendInt (caller);
        AREDispatch.sendObjectPlusData (permission);
        AREDispatch.analysisEnd ();
    }


    public static short GCP = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onGetContentProvider");


    public static void onGetContentProvider (final int caller, final String name) {
        AREDispatch.analysisStart (GCP);
        AREDispatch.sendInt (caller);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.analysisEnd ();
    }




//    public static short CS = AREDispatch.registerMethod ("ch.usi.dag.icc.analysis.ICCAnalysis.onCreateService");
//
//
//
//    public static void onCreateService (final int callee, final boolean isIsolated) {
//        AREDispatch.analysisStart (CS);
//        AREDispatch.sendInt (callee);
//        AREDispatch.sendBoolean (isIsolated);
//        AREDispatch.analysisEnd ();
//    }

}
