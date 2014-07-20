package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;

public class SetActivityResultGuard {
    @GuardMethod
    public static boolean isApplicable(final CallContext msc) {
        return msc.getCallee ().contains ("setResult");
    }
}
