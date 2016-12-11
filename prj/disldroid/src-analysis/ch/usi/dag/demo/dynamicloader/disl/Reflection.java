package ch.usi.dag.demo.dynamicloader.disl;

import ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysisStub;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;

public class Reflection {
    @Before(marker=BytecodeMarker.class,guard=Guard.ReflectionGuard.class, args = "invokevirtual")
    public static void reflect(final ArgumentProcessorContext apc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);

        if(args.length > 0 && args[0] != null) {
            AREDispatch.NativeLog ("REFLECTION "+args[0].getClass ().toString ());
            if(args[0] instanceof java.lang.Class){
                final Class clz = (Class)args[0];
                AREDispatch.NativeLog ("REFLECTION real class "+clz.getName ());
            }
        }else {
            AREDispatch.NativeLog ("REFLECTION empty");
        }
    }
    @Before(marker=BytecodeMarker.class,guard=Guard.DeviceIdGuard.class, args = "invokevirtual")
    public static void getDeviceId () {
        AREDispatch.NativeLog ("GET DEVICE ID");
        NetworkAnalysisStub.fileopen ("DeviceId");
    }
}
