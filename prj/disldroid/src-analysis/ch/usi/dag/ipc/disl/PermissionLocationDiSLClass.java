package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;
import ch.usi.dag.icc.disl.CallContext;
import ch.usi.dag.icc.disl.Guard;

/* for target app only */
public class PermissionLocationDiSLClass {

    @Before (
        marker = BodyMarker.class,
        scope = "*.*",
        guard = Guard.ScopeGuard.class,
        order = 1000)
    public static void before_enter (final CallContext msc) {
    }


    @After (
        marker = BodyMarker.class,
        scope = "*.*",
        guard = Guard.ScopeGuard.class,
        order = 1000)
    public static void after_enter (final CallContext msc) {
        final int permission = AREDispatch.checkThreadPermission ();
        if (permission != 0) {
            ICCAnalysisStub.permission_alert (permission, msc.thisMethodFullName ());
        }
    }


    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void beforeInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        AREDispatch.methodEnter ();
        // AREDispatch.NativeLog ("Before method call"+methodName);
    }


    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
    public static void afterInvoke (final CallContext ac) {
        final String methodName = ac.getCallee ();
        final int permission = AREDispatch.checkThreadPermission ();
        if (permission != 0) {
            ICCAnalysisStub.permission_alert (permission, methodName);
        }
        AREDispatch.methodExit ();
        // AREDispatch.NativeLog ("After method call"+methodName);
    }
}
