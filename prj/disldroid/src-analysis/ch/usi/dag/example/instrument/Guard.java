package ch.usi.dag.example.instrument;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

final class Guard {

    static final class MethodWithoutReturnValue {
        @GuardMethod
        public static boolean withoutReturnValue (final MethodStaticContext ctx) {
            //...
            return true;
        }
    }

    static final class MethodReturningObject {
        @GuardMethod
        public static boolean withReturnValue (final MethodStaticContext ctx) {
            //...
            return true;
        }
    }
}
