package ch.usi.dag.demo.ipc.disl;

import ch.usi.dag.demo.ipc.analysis.IPCAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class MethodEntryDiSLClass {

    /* every time entering a method */
    @Before (
        marker = BodyMarker.class,
        scope = "ch.usi.dag.android.example.*.*",
        order = 1000)
    public static void before_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_start (msc.thisMethodFullName ());
    }

    /* every time leaving a method */
    @After (
        marker = BodyMarker.class,
        scope = "ch.usi.dag.android.example.*.*",
        order = 1000)
    public static void after_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_end (msc.thisMethodFullName());
    }
}
