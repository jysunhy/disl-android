package ch.usi.dag.bc.disl;


import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.icc.disl.CallContext;


public class ExitGuard {
    public static class ScopeGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            return msc.getCallee ().contains ("exit")|msc.getCallee ().contains ("halt");
        }
    }
}
