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
            final String permissions[] = new String[]{"android.permission.READ_CONTACTS","android.permission.READ_PHONE_STATE"};;
            String res = "";
            for(int i = 0; i < 32; i++){
                if((permission & (1>>i)) == 1) {
                    res+=permissions[i]+";";
                }
            }
            ICCAnalysisStub.permission_alert (msc.thisMethodFullName (), res);
        }
    }
}
