package ch.usi.dag.et2.tools.etracks.util;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Represents a simple global logical clock.
 */
public final class LogicalClock {

    private static final AtomicLong __counter__ = new AtomicLong (1);

    //

    private LogicalClock () {
        // prevent instantiation from outside
    }

    //

    public static long current () {
        return __counter__.get ();
    }


    public static long next () {
        return __counter__.incrementAndGet ();
    }

}
