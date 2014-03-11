package ch.usi.dag.icc.disl;

import android.os.Binder;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

import com.android.server.am.ProcessRecord;
import com.android.server.am.ServiceRecord;


public class DiSLClass {

    @Before (
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
            dc.getStackValue (0, ProcessRecord.class).pid, false);
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

    /*@Before (marker = BodyMarker.class, scope="*.endTest")
    public static void testStart (final MethodStaticContext sc) {
        //BCAnalysisStub.printResult ();
        //AREDispatch.NativeLog("IN TEST START");
        AREDispatch.NativeLog("IN TEARDOWN: "+" "+sc.thisMethodFullName()+" "+sc.thisMethodDescriptor());
        ICCAnalysisStub.onSystemReady ();
        //AREDispatch.manuallyClose();
    }

    @Before (marker = BodyMarker.class, scope="*.tearDown")
    public static void testEnd (final MethodStaticContext sc) {
        AREDispatch.NativeLog("IN TEARDOWN: "+" "+sc.thisMethodFullName()+" "+sc.thisMethodDescriptor());
        ICCAnalysisStub.onSystemReady ();
        //AREDispatch.manuallyClose();
        //BCAnalysisStub.printResult ();
    }*/

    @Before (marker = BodyMarker.class, scope = "*.onTransact")
    public static void test1 () {
        AREDispatch.NativeLog("IN OnTransact");
    }

    @Before (marker = BodyMarker.class, scope = "*.transact")
    public static void test2 () {
        AREDispatch.NativeLog("IN tansact");
    }

    @Before (marker = BodyMarker.class, scope = "LauncherApplication.onCreate")
    public static void onSystemReady () {
        ICCAnalysisStub.onSystemReady ();
    }

}
