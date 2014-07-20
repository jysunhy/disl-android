package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class SinkGuard {

    @GuardMethod
    public static boolean isApplicable(final CallContext msc) {
		final String name = msc.getCallee();
        final String list[] = {"openFileOutput", "write"};
        for (final String element : list) {
            if(name.contains (element)) {
                return true;
            }
        }
		return false;
    }
}

