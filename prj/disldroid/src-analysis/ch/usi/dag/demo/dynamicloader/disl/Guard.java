package ch.usi.dag.demo.dynamicloader.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.monitor.disl.CallContext;

public class Guard {
    public static class ReflectionGuard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("java/lang/reflect/Constructor.newInstance");
            if(res){
            }else {
            }
            return res;
        }
    }
    public static class DeviceIdGuard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/telephony/TelephonyManager.getDeviceId");
            if(res){
                System.out.println("FOUND DEVICEID");
            }
            return res;
        }
    }
}
