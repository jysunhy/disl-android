package ch.usi.dag.demo.dynamicloader.disl;

import ch.usi.dag.demo.dynamicloader.analysis.DynamicLoadingStub;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class DynamicClassLoader {

    @Before(marker=BodyMarker.class, scope="dalvik.system.BaseDexClassLoader.<init>")
    public static void newClassloader(final ArgumentProcessorContext apc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        if(args != null && args.length > 0 && args[0] instanceof String){
            DynamicLoadingStub.newBaseDexClassLoader(args[0].toString ());
        }
    }
}
