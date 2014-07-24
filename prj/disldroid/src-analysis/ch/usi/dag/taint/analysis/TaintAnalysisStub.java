package ch.usi.dag.taint.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class TaintAnalysisStub {

    public static short TAINTPREPARE= AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_prepare");

    public static void taint_prepare(final Object from, final String name, final String location) {
        AREDispatch.analysisStart (TAINTPREPARE);
        AREDispatch.sendObject(from);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short TAINTPROPAGATE= AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_propagate");

    public static void taint_propagate(final Object from, final Object to, final String name, final String location) {
        AREDispatch.analysisStart (TAINTPROPAGATE);
        AREDispatch.sendObject(from);
        AREDispatch.sendObject(to);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short TAINTPROPAGATE2= AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_propagate2");

    public static void taint_propagate2(final long from, final int frompid, final Object to, final String name, final String location) {
        AREDispatch.analysisStart (TAINTPROPAGATE2);
        AREDispatch.sendLong (from);
        AREDispatch.sendInt (frompid);
        AREDispatch.sendObject(to);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short TAINTSINK= AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_sink");

    public static void taint_sink(final Object obj, final String name, final String location) {
        AREDispatch.analysisStart (TAINTSINK);
        AREDispatch.sendObject(obj);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short TAINTOBJECT = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_source");

    public static void taint_source(final Object obj, final int flag, final String name, final String location) {
        AREDispatch.analysisStart (TAINTOBJECT);
        AREDispatch.sendObject(obj);
        AREDispatch.sendInt (flag);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short DYNAMIC = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.dynamic_alert");

    public static void dynamic_alert (final String name, final String location, final String args) {
        AREDispatch.analysisStart (DYNAMIC);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.sendObjectPlusData (args);
        AREDispatch.analysisEnd ();
    }

    public static short SOURCE = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.source_alert");

    public static void source_alert (final String name, final String location) {
        AREDispatch.analysisStart (SOURCE);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short SINK = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.sink_alert");

    public static void sink_alert (final String name, final String location) {
        AREDispatch.analysisStart (SINK);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.sendObjectPlusData (location);
        AREDispatch.analysisEnd ();
    }

    public static short PERMISSION = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.permission_alert");

    public static void permission_alert (final int permission, final String info) {
        AREDispatch.analysisStart (PERMISSION);
        AREDispatch.sendInt (permission);
        AREDispatch.sendObjectPlusData (info);
        AREDispatch.analysisEnd ();
    }


    public static short CSC = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.callServiceInClient");

    public static void callServiceInClient (final String methodName) {
        AREDispatch.analysisStart (CSC);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.analysisEnd ();
    }




    public static short PL = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.println");


    public static void println (final String methodName) {
        AREDispatch.analysisStart (PL);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.analysisEnd ();
    }


    public static short SS = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onStartService");

    public static short BS = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onBindService");

    public static short SA = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onStartActivity");

    public static short SR = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onSystemReady");

    public static short SC = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onServiceConstructor");

    public static short BT = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onBinderTransact");

    public static short OT = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onBinderOnTransact");


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


    public static short CS = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.callService");


    public static void callService (
        final String methodName, final int caller) {
        AREDispatch.analysisStart (CS);
        AREDispatch.sendObjectPlusData (methodName);
        AREDispatch.sendInt (caller);
        AREDispatch.analysisEnd ();
    }


    public static short CP = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onCheckPermission");


    public static void onCheckPermission (final int caller, final String permission) {
        AREDispatch.analysisStart (CP);
        AREDispatch.sendInt (caller);
        AREDispatch.sendObjectPlusData (permission);
        AREDispatch.analysisEnd ();
    }


    public static short GCP = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onGetContentProvider");


    public static void onGetContentProvider (final int caller, final String name) {
        AREDispatch.analysisStart (GCP);
        AREDispatch.sendInt (caller);
        AREDispatch.sendObjectPlusData (name);
        AREDispatch.analysisEnd ();
    }




//    public static short CS = AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.onCreateService");
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
