package ch.usi.dag.et.tools.etracks.agent;

import java.lang.instrument.Instrumentation;


public final class Agent {

    private static volatile Instrumentation __instrumentation__;

    public static void premain (
        String agentArguments, Instrumentation instrumentation
    ) {
        __instrumentation__ = instrumentation;
    }

    public static long getObjectSize (final Object object) {
        return (__instrumentation__ != null) ? 
            __instrumentation__.getObjectSize (object) : -1;
    }
    
}
