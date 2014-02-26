package ch.usi.dag.icc.disl;

import android.os.Binder;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

import com.android.server.am.ProcessRecord;


public class DiSLClass {

    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.am.ActivityManagerService.startService")
    public static void onStartService () {
        ICCAnalysisStub.onStartService (Binder.getCallingPid ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.am.ActiveServices.realStartServiceLocked")
    public static void onScheduleCreateService (final DynamicContext dc) {
        ICCAnalysisStub.onScheduleCreateService (dc.getMethodArgumentValue (1, ProcessRecord.class).pid);
    }


    @AfterReturning (marker = BodyMarker.class, scope = "android.app.Service.<init>")
    public static void actualCreateService () {
        ICCAnalysisStub.actualCreateService ();
    }

}
