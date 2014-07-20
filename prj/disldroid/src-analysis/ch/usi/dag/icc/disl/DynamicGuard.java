package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;


public class DynamicGuard {

    @GuardMethod
    public static boolean isApplicable(final CallContext msc) {
		final String name = msc.getCallee();
        final String list[] = {"loadClass","DexClassLoader","getMethod"};
        for (final String element : list) {
            if(name.contains (element)) {
                return true;
            }
        }
		return false;
    }
}

