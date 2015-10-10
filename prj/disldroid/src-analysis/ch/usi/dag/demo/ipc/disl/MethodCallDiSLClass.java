package ch.usi.dag.demo.ipc.disl;

import ch.usi.dag.demo.ipc.analysis.IPCAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BytecodeMarker;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class MethodCallDiSLClass {
    /* every time before calling a method */
    @Before (
        marker = BytecodeMarker.class,
        scope = "ch.usi.dag.android.example.*.*",
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void beforeInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_start (methodName);
    }

    /* every time after calling a method */
    @After (
        marker = BytecodeMarker.class,
        scope = "ch.usi.dag.android.example.*.*",
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void afterInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_end (methodName);
    }
}
