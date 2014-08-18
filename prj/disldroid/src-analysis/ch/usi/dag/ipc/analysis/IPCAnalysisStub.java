package ch.usi.dag.ipc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class IPCAnalysisStub {

    public static short PERMISSION_USED = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.permissionUsed");

    public static void permission_used (final String permission_str) {
        AREDispatch.analysisStart (PERMISSION_USED);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        //AREDispatch.sendLong (AREDispatch.getCPUClock ());
        //AREDispatch.sendCurrentThread ();
        AREDispatch.sendObjectPlusData (permission_str);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_START = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.boundaryStart");

    public static void boundary_start (final String boundaryName) {
        AREDispatch.analysisStart (BOUNDARY_START);
        //AREDispatch.sendCurrentThread ();
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_END = AREDispatch.registerMethod ("ch.usi.dag.ipc.analysis.IPCAnalysis.boundaryEnd");

    public static void boundary_end (final String boundaryName) {
        AREDispatch.analysisStart (BOUNDARY_END);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        //AREDispatch.sendCurrentThread ();
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }

}
