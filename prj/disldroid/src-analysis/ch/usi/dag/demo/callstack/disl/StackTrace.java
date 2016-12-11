package ch.usi.dag.demo.callstack.disl;

import ch.usi.dag.demo.callstack.analysis.CallStackAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.marker.BodyMarker;

public class StackTrace {
    @After (
        marker = BodyMarker.class, scope = "ch.usi.dag.android.example.Client.onGetDeviceID")
    public static void afterMethod () {
        CallStackAnalysisStub.sendStackTrace ();
    }
}
