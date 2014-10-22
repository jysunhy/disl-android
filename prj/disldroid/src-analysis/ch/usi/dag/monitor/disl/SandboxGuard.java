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

    public static class ContentResolver_delete_Guard{
        @GuardMethod
        public static boolean guard (final CallContext msc) {
            final String name = msc.getCallee ();
            final boolean res = name.contains("android/contentContentResolver.delete");
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
}
