package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class PermissionDiSLClass {
    @Before (
        marker = BodyMarker.class,
        scope = "*.*")
    public static void before_enter (final CallContext msc){
        AREDispatch.clearThreadPermission();
    }

    @After (
        marker = BodyMarker.class,
        scope = "*.*")
    public static void after_enter (final CallContext msc){
        final int permission = AREDispatch.checkThreadPermission ();
        if(permission!=0) {
            ICCAnalysisStub.permission_alert (msc.thisMethodFullName (), msc.getPermissionString (permission));
        }
    }
}
