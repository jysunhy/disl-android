package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.ipc.analysis.IPCAnalysisStub;
/* for framework.jar */
public class PermissionDiSLClass {

    /* instrument the framework library for every check permission functions
     * send the permission used to the analysis server
    */
    @Before (
			marker = BodyMarker.class,
			//guard = Guard.PermissionGuard.class
			scope = "*.check*Permission*"
			)
		public static void detectPermission (
				final CallContext msc, final ArgumentProcessorContext pc) {
			AREDispatch.NativeLog (msc.thisMethodFullName ());
			final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
			for (final Object obj : args) {
			    if(obj!=null){
    			    if(obj.getClass().getCanonicalName().equals ("java.lang.String")) {
                        if(obj.toString ().contains ("permission")){
                            IPCAnalysisStub.permission_used (obj.toString ());
                        }
                    }
			    }
			}
		}
}
