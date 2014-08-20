package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.ipc.analysis.IPCAnalysisStub;

/* for generating runtime stack */
public class MethodCallDiSLClass {
    /* every time before calling a method */
    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void beforeInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_start (methodName);
    }

    /* every time after calling a method */
    @After (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void afterInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_end (methodName);
    }
}
