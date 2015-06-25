package ch.usi.dag.netdiagnose.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.monitor.disl.CallContext;


public class Guard {
    public static class IoBridge_bind{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.bind");
            return res;
        }
    }

    public static class IoBridge_connect{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.connect");
            //TODO
            //final boolean res = name.equals("libcore/io/IoBridge.connect");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }

    public static class IoBridge_open{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.open");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_read{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.read");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_write{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.write");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_sendto{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.sendto") && !name.contains ("ByteBuffer");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_sendto_bytebuffer{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.sendto") && name.contains ("ByteBuffer");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_recvfrom{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.recvfrom") && !name.contains ("ByteBuffer");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class IoBridge_recvfrom_bytebuffer{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("libcore/io/IoBridge.recvfrom") && name.contains ("ByteBuffer");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
}


