package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;


public class DiSLClass {

    /*@Before (
        marker = BodyMarker.class,
        scope = "com.android.server.am.ActivityManagerService.startService")
    public static void onStartService () {
        ICCAnalysisStub.onStartService (Binder.getCallingPid ());
    }


    @Before (
        marker = InvokeMarker.class, args = "realStartServiceLocked",
        scope = "com.android.server.am.ActivityManagerService.bringUpServiceLocked")
    public static void createService (final DynamicContext dc) {
        ICCAnalysisStub.onCreateService (
            dc.getStackValue (1, ProcessRecord.class).pid, false);
    }


    @Before (
        marker = LastReturnMarker.class,
        scope = "com.android.server.am.ActivityManagerService.bringUpServiceLocked")
    public static void createServiceIsolated (final DynamicContext dc) {
        final ProcessRecord record = dc.getMethodArgumentValue (
            0, ServiceRecord.class).isolatedProc;

        if (record != null) {
            ICCAnalysisStub.onCreateService (record.pid, true);
        }
    }


    @AfterReturning (marker = BodyMarker.class, scope = "android.app.Service.<init>")
    public static void actualCreateService () {
        ICCAnalysisStub.actualCreateService ();
    }


    @Before (marker = BodyMarker.class, scope = "LauncherApplication.onCreate")
    public static void onSystemReady () {
        ICCAnalysisStub.onSystemReady ();
    }*/


    @Before (marker = BodyMarker.class, scope = "spec.*.*")
    public static void test3 (final MethodStaticContext sc, final ArgumentProcessorContext pc){
        final Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
        AREDispatch.NativeLog (sc.thisMethodFullName()+"->"+sc.thisMethodDescriptor());
    }

    @Before (marker = BodyMarker.class, scope = "*.onTransact")
    public static void test (final MethodStaticContext sc, final ArgumentProcessorContext pc){
        final Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
        AREDispatch.NativeLog (sc.thisMethodFullName()+"->"+sc.thisMethodDescriptor());
    }

    @Before (marker = BodyMarker.class, scope = "*.transact")
    public static void test2 (final MethodStaticContext sc, final ArgumentProcessorContext pc){
        final Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
        AREDispatch.NativeLog (sc.thisMethodFullName()+"->"+sc.thisMethodDescriptor());
    }

}
