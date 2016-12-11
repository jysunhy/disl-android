package ch.usi.dag.rv.logging;

import android.util.Log;

public class DefaultLog {
    public static void v(final String tag, final String args){
        final long tid = Thread.currentThread ().getId ();
        Log.v(tag, "(Thread-"+tid+")" + args);
    }
    public static void d(final String tag, final String args){
        final long tid = Thread.currentThread ().getId ();
        Log.d(tag, "(Thread-"+tid+")" + args);
    }
    public static long getTID(){
        return Thread.currentThread ().getId ();
    }
}
