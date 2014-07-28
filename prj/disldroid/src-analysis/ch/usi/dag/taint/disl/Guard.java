package ch.usi.dag.taint.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class Guard {
    public static class ScopeGuard {
        @GuardMethod
        public static boolean isApplicable (final MethodStaticContext msc) {
            return true;
        }
    }


    public static class StartActivityForResultGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            return msc.getCallee ().contains ("startActivityForResult");
        }
    }


    public static class SetActivityResultGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            return msc.getCallee ().contains ("setResult");
        }
    }


    public static class IOSinkGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "openFileOutput", "write" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }


    public static class IOSourceGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = {
                "openFileInput", "openConnection", "read", "getInputStream" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }


    public static class DynamicLoadingGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "loadClass", "DexClassLoader" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class ReflectionGuard {
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "invoke", "getMethod" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class APISinkGuard {

        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "sendTextMessage" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }


    public static class APISourceGuard {

        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "getDeviceId" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class PotentialPropagationToThis {

        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "putExtra" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }
    public static class ActivityGetIntentGuard {

        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            final String name = msc.getCallee ();
            final String list[] = { "getIntent" };
            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class PotentialPropagationGuardToResult{
        @GuardMethod
        public static boolean isApplicable (final CallContext msc) {
            //Only methods with result can be applied
            final String name = msc.getCallee ();
            if(name.endsWith ("V")) {
                return false;
            }
            final String list[] = { "Intent.getExtras", "Bundle.getString" };

            for (final String element : list) {
                if (name.contains (element)) {
                    return true;
                }
            }
            return false;
        }
    }
}
