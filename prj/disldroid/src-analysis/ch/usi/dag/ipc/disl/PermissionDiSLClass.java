package ch.usi.dag.ipc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;

public class PermissionDiSLClass {
    @Before (
			marker = BodyMarker.class,
			guard = Guard.PermissionGuard.class)
		public static void detectPermission (
				final CallContext msc, final ArgumentProcessorContext pc) {
			AREDispatch.NativeLog (msc.thisMethodFullName ());
			final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
			for (final Object obj : args) {
			    if(obj.toString ().contains ("permission")){

			    }
			}
			//AREDispatch.CallAPI (api);
		}
}
