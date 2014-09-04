package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class Guard {
    public static class PermissionGuard {

        @GuardMethod
        public static boolean permissionCheck (final MethodStaticContext msc) {
            final String name = msc.thisMethodFullName ();
            return name.contains ("ActivityManager.checkComponentPermission");
        }
    }
}
