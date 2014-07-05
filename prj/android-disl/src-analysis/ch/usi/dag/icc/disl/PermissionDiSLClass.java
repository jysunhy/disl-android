package ch.usi.dag.icc.disl;

import java.util.Stack;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class PermissionDiSLClass {
    @ThreadLocal
    static Stack<Integer> permission_stk=new Stack <Integer> ();

    @Before (
        marker = BodyMarker.class,
        scope = "*.check*Permission*")
    public static void api_0 (
        final CallContext msc, final ArgumentProcessorContext pc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        final String tabs = "\t\t";
        int api = 0;
        for (final Object obj : args) {
            if(obj == null){
                AREDispatch.NativeLog(tabs+"null");
                continue;
            }

            final String n = obj.getClass().getCanonicalName();
            if (obj instanceof Object[]){
                AREDispatch.NativeLog(tabs+n);
                //final Object[] arr = (Object[])obj;
            }else{


                if(n.equals ("java.lang.Integer")) {
                    AREDispatch.NativeLog(tabs+obj.toString());
                }else if(n.equals ("java.lang.Float")) {
                    AREDispatch.NativeLog(tabs+obj.toString());
                }else if(n.equals ("java.lang.Double")) {
                    AREDispatch.NativeLog(tabs+obj.toString());
                }else if(n.equals ("java.lang.String")) {
                    final String permissions[] = new String[]{"android.permission.READ_CONTACTS","android.permission.READ_PHONE_STATE"};
                    int pos = 0;
                    for (final String p : permissions) {
                        if(obj.toString ().equals (p)) {
                            api = 1<<pos;
                            break;
                        }
                        pos++;
                    }
                    AREDispatch.NativeLog(tabs+obj.toString());
                }else {
                    AREDispatch.NativeLog (tabs+n);
                }
            }
        }
        AREDispatch.CallAPI (api);
    }

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
