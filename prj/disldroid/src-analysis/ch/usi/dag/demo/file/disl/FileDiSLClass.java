package ch.usi.dag.demo.file.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;

public class FileDiSLClass {
    @AfterReturning(marker=BodyMarker.class,
    order = 1,
    scope="libcore.io.IoBridge.open(java.lang.String,int)")
    public static void libcore_io_IoBridge_bind (final ArgumentProcessorContext apc, final DynamicContext dc) {
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        final String path = (args.length > 0 && args[0] != null)?args[0].toString ():"";
        AREDispatch.NativeLog ("FILE OPEN "+path);
        //NetworkAnalysisStub.fileopen ("File:"+path);
        //final FileDescriptor fd = (FileDescriptor)args[0];
        //final InetAddress address = (InetAddress)args[1];
        //final int port = (int)args[2];
        //NetworkAnalysisStub.bind(fd, address, port);
    }
}
