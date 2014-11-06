package ch.usi.dag.monitor.disl;

import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;


public class DiSLClass {
    /*@Monitor(marker=BytecodeMarker.class, guard=Guard.String_valueOf.class, args = "invokestatic")
    public static String java_lang_String_valueOf(final int i){
        System.out.println("Monitoring valueOf");
        AREDispatch.NativeLog ("Monitoring valueOf");
        return String.valueOf (i);
    }*/
/*
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=Guard.String_equals.class, args = "invokespecial, invokevirtual")
    public static boolean java_lang_String_equals(final String instance, final Object obj){
        //System.out.println("Monitoring java/lang/String equals");
        AREDispatch.NativeLog("Monitoring java/lang/String equals");
        final boolean res = instance.equals (obj);
        AREDispatch.NativeLog("Monitoring res "+res);
        return res;
    }*/
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=Guard.String_String.class, args = "invokespecial, invokevirtual")
    public static void java_lang_String_String(String instance){
        //System.out.println("Monitoring java/lang/String equals");
        instance = new String("abc");
    }
    /*@Before(marker=BytecodeMarker.class, scope="*.*", guard=Guard.String_equals.class, args = "invokespecial, invokevirtual")
    public static void java_lang_String_equals_debug(final CallContext cc){
        AREDispatch.NativeLog("before monitoring java/lang/String equals "+cc.thisClassName ()+" "+cc.thisMethodFullName ());
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=Guard.String_equals.class, args = "invokespecial, invokevirtual")
    public static boolean java_lang_String_equals(final String instance, final Object obj){
        //System.out.println("Monitoring java/lang/String equals");
        AREDispatch.NativeLog("Monitoring java/lang/String equals");
        final boolean res = instance.equals (obj);
        AREDispatch.NativeLog("Monitoring res "+res);
        return res;
    }*/
/*
    @Monitor(marker=BytecodeMarker.class, guard=Guard.API0.class, args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static String test_TargetClass_api0(int a, String b){
        System.out.println("in TargetClass api0, args monitored : "+a+","+b);
        a++;
        b+="-hacked";
        System.out.println("in TargetClass api0, args changed to: "+a+","+b);
        String ret = TargetClass.api0(a,b);
        System.out.println("in TargetClass api0, ret value: "+ret);
        ret="hacked-"+ret;
        System.out.println("in TargetClass api0, ret value: "+ret);
        return ret;
    }

    @Monitor(marker=BytecodeMarker.class, guard=Guard.API1.class, args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static void test_TargetClass_api1(final TargetClass instance, int a, String b){
        System.out.println("in TargetClass api1, args monitored : "+a+","+b);
        a++;
        b+="-hacked";
        System.out.println("in TargetClass api1, args changed to: "+a+","+b);
        instance.api1(a,b);
    }

    @Monitor(marker=BytecodeMarker.class, guard=Guard.API2.class, args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static void test_TargetClass_api2(final TargetClass instance, int a, String b){
        System.out.println("in TargetClass api2, args monitored : "+a+","+b);
        a++;
        b+="-hacked";
        System.out.println("in TargetClass api2, args changed to: "+a+","+b);

        Method m;
        try {
            m = TargetClass.class.getDeclaredMethod("api2", new Class[]{int.class, String.class});
            m.setAccessible(true);
            m.invoke(instance, new Object[]{a,b});
        } catch (final NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Monitor(marker=BytecodeMarker.class,  guard=Guard.API2_2.class, args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static void test_TargetClass_api2(final TargetClass instance, int a){
        System.out.println("in TargetClass api2, args monitored : "+a);
        a++;
        System.out.println("in TargetClass api2, args changed to: "+a);

        Method m;
        try {
            m = TargetClass.class.getDeclaredMethod("api2", new Class[]{int.class});
            m.setAccessible(true);
            m.invoke(instance, new Object[]{a});
        } catch (final NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Monitor(marker=BytecodeMarker.class, guard=Guard.IF0.class,  args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static String test_TargetInterface_if0(final TargetInterface instance, int a, String b){
        System.out.println("in TargetInterface if0, args monitored : "+a+","+b);
        a++;
        b+="-hacked";
        System.out.println("in TargetInterface if0, args changed to: "+a+","+b);
        String ret = instance.if0(a,b);
        System.out.println("in TargetInterface if0, ret value: "+ret);
        ret="hacked-"+ret;
        System.out.println("in TargetInterface if0, ret value: "+ret);
        return ret;
    }*/

}
