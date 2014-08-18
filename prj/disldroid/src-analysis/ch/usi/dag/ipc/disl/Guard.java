package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class Guard {
    public static class PermissionGuard {

        /*
         * android/app/ActivityManager.checkComponentPermission
         * android/app/ActivityManagerProxy.checkPermission
         * android/app/ContextImpl.checkCallingOrSelfPermission
         * android/app/ContextImpl.checkCallingPermission
         * android/app/ContextImpl.checkPermission
         */
        @GuardMethod
        public static boolean permissionCheck (final MethodStaticContext msc) {
            final String name = msc.thisMethodFullName ();
            return name.contains ("ActivityManager.checkComponentPermission");
            //|| name.contains ("ActivityManagerProxy.checkPermission")
            //|| name.contains ("ContextImpl.checkCallingOrSelfPermission")
            //|| name.contains ("ContextImpl.checkCallingPermission")
            //|| name.contains ("ContextImpl.checkPermission");
        }
    }
}
