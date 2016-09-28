package ch.usi.dag.example.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class AnalysisStub {

    public static short ENTER = AREDispatch.registerMethod ("analysis.Analysis.enter");
    public static void enter (final String thisMethodFullName) {
        AREDispatch.analysisStart (ENTER);
        AREDispatch.sendCurrentThread ();
        AREDispatch.sendObjectPlusData (thisMethodFullName);
        AREDispatch.analysisEnd ();
    }

    public static short LEAVE = AREDispatch.registerMethod ("analysis.Analysis.leave");
    public static void leave (final String thisMethodFullName) {
        AREDispatch.analysisStart (LEAVE);
        AREDispatch.sendCurrentThread ();
        AREDispatch.sendObjectPlusData (thisMethodFullName);
        AREDispatch.analysisEnd ();
    }


    public static short LEAVE_WITH_OBJECT = AREDispatch.registerMethod ("analysis.Analysis.leaveWithObject");
    public static void leaveWithObject (
        final String thisMethodFullName, final Object returnValue) {
        AREDispatch.analysisStart (LEAVE_WITH_OBJECT);
        AREDispatch.sendCurrentThread ();
        AREDispatch.sendObjectPlusData (thisMethodFullName);
        AREDispatch.sendObjectPlusData (returnValue);
        AREDispatch.analysisEnd ();
    }
}
