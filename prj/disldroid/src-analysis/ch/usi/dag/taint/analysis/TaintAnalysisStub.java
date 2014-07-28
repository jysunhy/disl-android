package ch.usi.dag.taint.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class TaintAnalysisStub {

    public static short TAINTPREPARE= AREDispatch.registerMethod ("ch.usi.dag.taint.analysis.TaintAnalysis.taint_prepare");

    public static void taint_prepare(final Object from, final long time,  final String name, final String location) {
        AREDispatch.analysisStart (TAINTPREPARE);
        AREDispatch.sendObject(from);
        AREDispatch.sendLong (time);
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

    public static void taint_propagate2(final long from, final int frompid, final long time, final Object to, final String name, final String location) {
        AREDispatch.analysisStart (TAINTPROPAGATE2);
        AREDispatch.sendLong (from);
        AREDispatch.sendInt (frompid);
        AREDispatch.sendLong (time);
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
}
