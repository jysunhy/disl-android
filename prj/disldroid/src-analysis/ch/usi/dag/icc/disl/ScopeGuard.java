package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class ScopeGuard {

    @GuardMethod
    public static boolean isApplicable(final MethodStaticContext msc) {
		//return msc.thisClassName ().contains ("Activity") || msc.thisClassSuperName ().contains("Activity");
		return true;
    }
}

