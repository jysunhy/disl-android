package ch.usi.dag.netdiagnose.disl;

import ch.usi.dag.disl.annotation.GuardMethod;


public class Guard {

    public static class IoBridge_connect{
        @GuardMethod
        public static boolean guard (final InvocationFilterContext ifc) {
            return ifc.isConnect ();
        }
    }

    public static class IoBridge_sendto{
        @GuardMethod
        public static boolean guard (final InvocationFilterContext ifc) {
            return ifc.isSendTo ();
        }
    }
    public static class IoBridge_sendto_bytebuffer{
        @GuardMethod
        public static boolean guard (final InvocationFilterContext ifc) {
            return ifc.isSendToByteBuffer ();
        }
    }
}


