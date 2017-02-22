package ch.usi.dag.rv.logging;

//import android.util.Log;

public class DefaultLog {
    static String TAG = "RVTOOL-";
    public static void v(final String tag, final String args){
        final long tid = Thread.currentThread ().getId ();
        //Log.v(TAG+tag, "(Thread-"+tid+")" + args);
        System.out.println(tag + " " + args);
    }
    public static void d(final String tag, final String args){
        final long tid = Thread.currentThread ().getId ();
        //Log.d(TAG+tag, "(Thread-"+tid+")" + args);
        System.out.println(tag + " " + args);
    }
    public static long getTID(){
        return Thread.currentThread ().getId ();
    }
}
