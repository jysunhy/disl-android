package ch.usi.dag.demo.ipc.disl;

import android.os.Message;
import ch.usi.dag.demo.callstack.analysis.CallStackAnalysisStub;
import ch.usi.dag.demo.ipc.analysis.IPCAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
/* for framework.jar */
public class PermissionDiSLClass {

    /* instrument the framework library for every check permission functions
     * send the permission used to the analysis server
     *
     * This takes effects on framework.jar
     */
    @Before (
			marker = BodyMarker.class,
			//guard = Guard.PermissionGuard.class
			scope = "ActivityManager.checkComponentPermission"
			)
		public static void detectPermission (
				final MethodStaticContext msc, final ArgumentProcessorContext pc) {
            final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
			if(args[0] != null) {
			    final String permisssionUsed = args[0].toString ();
    			if(permisssionUsed!=null) {
    			    IPCAnalysisStub.permission_used (permisssionUsed);
    			}
			}
		}

    @Before (
        marker = BodyMarker.class,
        scope ="Handler.handleCallback"
        //scope = "android.os.Handler.handleCallback"
        //scope ="*.*.handleMessage(android.os.Message)"
        )
    public static void beforeMessage (
            final MethodStaticContext msc, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        if(args[0] != null) {
            final Message m = (Message)(args[0]);
            final String msgName = m.getTarget ().getMessageName (m);
            CallStackAnalysisStub.boundary_start (msgName + " ");
        }
    }

    @After (
        marker = BodyMarker.class,
        //scope ="*.*.handleMessage(android.os.Message)"
        scope ="Handler.handleCallback"
        )
    public static void afterMessage (
            final MethodStaticContext msc, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        if(args[0] != null) {
            final Message m = (Message)(args[0]);
            final String msgName = m.getTarget ().getMessageName (m);
            CallStackAnalysisStub.boundary_end (msgName);
        }
    }
}
