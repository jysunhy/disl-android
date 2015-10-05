package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.ipc.analysis.IPCAnalysisStub;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class MethodEntryDiSLClass {

    /* every time entering a method */
    @Before (
        marker = BodyMarker.class,
        scope = "de.ecspride.*.*",
        order = 1000)
    public static void before_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_start (msc.thisMethodFullName ());
    }

    /* every time leaving a method */
    @After (
        marker = BodyMarker.class,
        scope = "de.ecspride.*.*",
        order = 1000)
    public static void after_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_end (msc.thisMethodFullName());
    }
}
