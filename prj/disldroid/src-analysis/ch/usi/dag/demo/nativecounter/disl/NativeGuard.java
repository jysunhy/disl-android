package ch.usi.dag.demo.nativecounter.disl;

import ch.usi.dag.disl.annotation.GuardMethod;

public class NativeGuard {
    private NativeGuard () {
        // prevent instantiation from outside
    }

    //
    // Guards
    //
    @GuardMethod
    public static boolean isNative (final NativeStaticContext msc) {
        return msc.isNative ();
    }
}
