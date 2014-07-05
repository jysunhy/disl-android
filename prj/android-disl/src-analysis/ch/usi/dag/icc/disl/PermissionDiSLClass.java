package ch.usi.dag.icc.disl;

import java.util.Stack;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class PermissionDiSLClass {
    @ThreadLocal
    static Stack<Integer> permission_stk=new Stack <Integer> ();

    @Before (
        marker = BodyMarker.class,
        scope = "*.*")
    public static void before_enter (final CallContext msc){
        permission_stk.push (0);
        AREDispatch.methodEnter ();
    }

    @After (
        marker = BodyMarker.class,
        scope = "*.*")
    public static void after_enter (final CallContext msc){
        final int permission = AREDispatch.checkThreadPermission ();
        if(permission != 0) {
            final String permissions[] = new String[]{"android.permission.READ_CONTACTS","android.permission.READ_PHONE_STATE"};;
            String res = "";
            for(int i = 0; i < 32; i++){
                if((permission & (1>>i)) == 1) {
                    res+=permissions[i]+";";
                }
            }
            ICCAnalysisStub.permission_alert (msc.thisMethodFullName (), res);
        }
        AREDispatch.methodExit ();

        final int temp = permission_stk.pop ();
        if(temp!=permission){
            AREDispatch.NativeLog ("NOT CONSISTENT AT "+ msc.thisMethodFullName ());
        }
    }
}
