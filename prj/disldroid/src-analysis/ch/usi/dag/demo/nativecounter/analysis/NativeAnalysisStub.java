package ch.usi.dag.demo.nativecounter.analysis;

import ch.usi.dag.dislre.AREDispatch;



public class NativeAnalysisStub {
    public static short CALL_NATIVE = AREDispatch.registerMethod ("ch.usi.dag.demo.nativecounter.analysis.NativeAnalysis.nativeCall");

    public static void report (final String from, final String to) {
        AREDispatch.analysisStart (CALL_NATIVE);
        AREDispatch.sendObjectPlusData (from);
        AREDispatch.sendObjectPlusData (to);
        AREDispatch.analysisEnd ();
    }
}
