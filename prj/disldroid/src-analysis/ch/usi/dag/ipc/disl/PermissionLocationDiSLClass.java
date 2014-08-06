package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.icc.disl.CallContext;
import ch.usi.dag.icc.disl.Guard;
import ch.usi.dag.ipc.analysis.IPCAnalysisStub;

/* for generating runtime stack */
public class PermissionLocationDiSLClass {


    /* every time entering a method */
    @Before (
        marker = BodyMarker.class,
        scope = "*.*",
        guard = Guard.ScopeGuard.class,
        order = 1000)
    public static void before_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_start (msc.thisMethodFullName ());
    }

    /* every time leaving a method */
    @After (
        marker = BodyMarker.class,
        scope = "*.*",
        guard = Guard.ScopeGuard.class,
        order = 1000)
    public static void after_enter (final CallContext msc) {
        IPCAnalysisStub.boundary_end (msc.thisMethodFullName());
    }

    /* every time before calling a method */
    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void beforeInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_start (methodName);
    }

    /* every time after calling a method */
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void afterInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        IPCAnalysisStub.boundary_end (methodName);
    }
}
