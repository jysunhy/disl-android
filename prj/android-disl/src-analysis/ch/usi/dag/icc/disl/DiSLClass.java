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

    @Before (marker = BodyMarker.class, scope = "Launcher.onCreate")
    public static void onSystemReady_2 () {
        AREDispatch.NativeLog ("SYSTEM_READY IS SENT IN LAUNCHER.ONCREAET");
    }

    @Before (marker = BodyMarker.class, scope = "LauncherApplication.onCreate")
    public static void onSystemReady () {
        AREDispatch.NativeLog ("SYSTEM_READY IS SENT IN LAUNCHERAPPLICATION.ONCREATE");
        ICCAnalysisStub.onSystemReady ();
    }

}
