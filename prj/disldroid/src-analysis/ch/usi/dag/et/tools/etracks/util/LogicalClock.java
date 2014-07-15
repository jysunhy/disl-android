package ch.usi.dag.et.tools.etracks.util;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Represents a simple global logical clock.
 */
public final class LogicalClock {

    private static final AtomicLong __counter__ = new AtomicLong (1);

    //

    private LogicalClock () {
        // prevent class instantiation from outside
    }

    //

    public static long current () {
        return __counter__.get ();
    }


    public static long next () {
        return __counter__.incrementAndGet ();
    }
    
    public static long updateMax (final long value) {
		long current = __counter__.get();   	
		SET_MAX: while (current < value) {
    		if (__counter__.compareAndSet(current, value)) {
    			break SET_MAX;
    		} else {
    			current = __counter__.get();
    		}
    	}
		
		return value;
    }

}
