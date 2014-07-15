package ch.usi.dag.et.tools.etracks.util;

import java.io.File;

import ch.usi.dag.util.Strings;


public final class Config {

    private static final String __CONF_PREFIX__ = "etracks";

    private static final String __CONF_TRACE_FILE__ = "traceFile";

    private static final String __DEFAULT_TRACE_FILE__ = "etracks.trace";

    //

    private Config () {
        // not to be instantiated
    }

    //

    public static File traceFile () {
        return new File (__getProperty (__CONF_TRACE_FILE__,
            __DEFAULT_TRACE_FILE__));
    }


    private static String __getProperty (final String key, final String def) {
        return System.getProperty (
            Strings.join (".", __CONF_PREFIX__, key), def
        );
    }

}
