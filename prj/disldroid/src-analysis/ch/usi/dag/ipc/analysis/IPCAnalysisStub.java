package ch.usi.dag.ipc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class IPCAnalysisStub {

    public static short PERMISSION_USED = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.permission_used");

    public static void permission_used (final String permission_str) {
        AREDispatch.analysisStart (PERMISSION_USED);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendLong (AREDispatch.getCPUClock ());
        AREDispatch.sendObjectPlusData (permission_str);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_START = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.boundary_start");

    public static void boundary_start (final String boundaryName) {
        AREDispatch.analysisStart (BOUNDARY_START);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_END = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.boundary_end");

    public static void boundary_end (final String boundaryName) {
        AREDispatch.analysisStart (BOUNDARY_START);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }

}
