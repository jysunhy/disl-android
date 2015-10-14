package ch.usi.dag.demo.ipc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class IPCAnalysisStub {

    public static short PERMISSION_USED = AREDispatch.registerMethod ("ch.usi.dag.demo.ipc.analysis.IPCAnalysis.permissionUsed");

    public static void permission_used (final String permission_str) {
        AREDispatch.analysisStart (PERMISSION_USED);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (permission_str);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_START = AREDispatch.registerMethod ("ch.usi.dag.demo.ipc.analysis.IPCAnalysis.boundaryStart");

    public static void boundary_start (final String boundaryName) {
        //AREDispatch.NativeLog ("boundary start");
        AREDispatch.analysisStart (BOUNDARY_START);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_END = AREDispatch.registerMethod ("ch.usi.dag.demo.ipc.analysis.IPCAnalysis.boundaryEnd");

    public static void boundary_end (final String boundaryName) {
        //AREDispatch.NativeLog ("boundary end");
        AREDispatch.analysisStart (BOUNDARY_END);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }

}
