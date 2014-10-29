package ch.usi.dag.monitor.disl;

import ch.usi.dag.disl.annotation.GuardMethod;

public class SandboxGuard {
    /*
     * Any data stored by an application will be assigned that application's user ID,
     * and not normally accessible to other packages. When creating a new file with getSharedPreferences(String, int),
     * openFileOutput(String, int),
     * openOrCreateDatabase(String, int, SQLiteDatabase.CursorFactory),
     * you can use the MODE_WORLD_READABLE and/or MODE_WORLD_WRITEABLE flags to allow any other package to read/write the file.
     * When setting these flags, the file is still owned by your application, but its global read and/or write permissions
     * have been set appropriately so any other application can see it.
     */
    public static class IOGuard{
        public static class IoBridge_bind{
            @GuardMethod
            public static boolean guard (final CallContext msc) {
                final String name = msc.getCallee ();
                final boolean res = name.contains("libcore/io/IoBridge.bind");
                if(res){
                    System.out.println (name);
                }
                return res;
            }
        }

        public static class IoBridge_connect{
            @GuardMethod
            public static boolean guard (final CallContext msc) {
                final String name = msc.getCallee ();
                final boolean res = name.contains("libcore/io/IoBridge.connect");
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
        public static class IoBridge_sendto_2{
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
        public static class IoBridge_recvfrom_2{
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


    public static class ContentResolver_query_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }

    public static class ContentResolver_insert_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.insert");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }

    public static class ContentResolver_bulkInsert_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.bulkInsert");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }

    public static class ContentResolver_delete_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.delete");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
    public static class ContentResolver_update_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.update");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }

    public static class ContentResolver_call_Guard {
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/content/ContentResolver.call");
            if(res){
                System.out.println (name);
            }
            return res;
        }
    }
}
