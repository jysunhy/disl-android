package ch.usi.dag.monitor.disl;

import ch.usi.dag.disl.annotation.GuardMethod;


public class Guard {
    public static class String_valueOf {
        @GuardMethod
        public static boolean valueOf (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("java/lang/String.valueOf(I)Ljava/lang/String;");
        }
    }
    public static class String_equals {
        @GuardMethod
        public static boolean equals (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("java/lang/String.equals(Ljava/lang/Object;)Z") && !msc.thisClassName ().equals("java/lang/String");
        }
    }
    public static class String_String {
        @GuardMethod
        public static boolean equals (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.contains ("java/lang/String.<init>()") && !msc.thisClassName ().equals("java/lang/String");
        }
    }
    public static class API0 {
        @GuardMethod
        public static boolean api0 (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("test/TargetClass.api0(ILjava/lang/String;)Ljava/lang/String;");
        }
    }
    public static class API1 {
        @GuardMethod
        public static boolean api1 (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("test/TargetClass.api1(ILjava/lang/String;)V");
        }
    }
    public static class API2 {
        @GuardMethod
        public static boolean api0 (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("test/TargetClass.api2(ILjava/lang/String;)V");
        }
    }
    public static class API2_2 {
        @GuardMethod
        public static boolean api0 (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("test/TargetClass.api2(I)V");
        }
    }
    public static class IF0 {
        @GuardMethod
        public static boolean api0 (final CallContext msc) {
            final String name = msc.getCallee ();
            return name.equals ("test/TargetInterface.if0(ILjava/lang/String;)Ljava/lang/String;");
        }
    }
}
