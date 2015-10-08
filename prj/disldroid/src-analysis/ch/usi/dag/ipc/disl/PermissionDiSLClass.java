package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.ipc.analysis.IPCAnalysisStub;
/* for framework.jar */
public class PermissionDiSLClass {

    /* instrument the framework library for every check permission functions
     * send the permission used to the analysis server
     *
     * This takes effects on framework.jar
     */
    @Before (
			marker = BodyMarker.class,
			guard = Guard.PermissionGuard.class
			)
		public static void detectPermission (
				final MethodStaticContext msc, final ArgumentProcessorContext pc) {
			AREDispatch.NativeLog (msc.thisMethodFullName ());
			final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
			for(final Object obj : args){
			    if(obj != null) {
                    AREDispatch.NativeLog (obj.toString ());
                }
			}
			if(args[0] != null) {
			    final String permisssionUsed = args[0].toString ();
    			if(permisssionUsed!=null) {
    			    IPCAnalysisStub.permission_used (permisssionUsed);
    			}
			}
		}
}
