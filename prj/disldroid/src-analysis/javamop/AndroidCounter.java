package javamop;

import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.dislre.AREDispatch;

public class AndroidCounter {
    static AtomicInteger[] counters = new AtomicInteger[10];
    static AtomicInteger[] counters2 = new AtomicInteger[10];
    static AtomicInteger[] counters3 = new AtomicInteger[10];
    public static void log(final int id){
        if(counters[id]==null){
            counters[id] = new AtomicInteger ();
        }
        final int value = counters[id].incrementAndGet ();
        AREDispatch.NativeLog("JAVAMOP 1 "+id+" "+value);
    }
    public static void log2(final int id){
        if(counters2[id]==null){
            counters2[id] = new AtomicInteger ();
        }
        final int value = counters2[id].incrementAndGet ();
        AREDispatch.NativeLog("JAVAMOP 2 "+id+" "+value);
    }
    public static void log3(final int id){
        if(counters3[id]==null){
            counters3[id] = new AtomicInteger ();
        }
        final int value = counters3[id].incrementAndGet ();
        AREDispatch.NativeLog("JAVAMOP 3 "+id+" "+value);
    }
}
