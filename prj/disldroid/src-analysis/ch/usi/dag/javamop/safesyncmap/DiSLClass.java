package ch.usi.dag.javamop.safesyncmap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
/**
 * <p>
 * This example shows how to insert snippets at various regions and entries or exits of these regions.
 *
 * <p>
 * It also shows how to implements custom code marker.
 */
public class DiSLClass {


	@Before(marker = HasNextInvocationMarker.class)
	public static void method1(final ArgumentProcessorContext pc){

     final Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

       SafeSyncMapRuntimeMonitor.accessIterEvent(iter);
	}


    @Before(marker = NextInvocationMarker.class)
	public static void method2(final ArgumentProcessorContext pc){
         final Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

         SafeSyncMapRuntimeMonitor.accessIterEvent(iter);
	}

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
	public static void method3(final DynamicContext di){

        final Map syncMap = (Map) di.getStackValue(0,Object.class);

        SafeSyncMapRuntimeMonitor.syncEvent(syncMap);
    }

       @AfterReturning(marker = MapKeyInvocationMarker.class)
	public static void method4(final ArgumentProcessorContext pc,final DynamicContext di ){

        final Set mapSet = (Set) di.getStackValue(0,Object.class);
        final Map syncMap =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        SafeSyncMapRuntimeMonitor.createSetEvent(syncMap, mapSet);
	}

      @AfterReturning(marker = SetInvocationMarker.class)
	public static void method5(final ArgumentProcessorContext pc,final DynamicContext di ){

          final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
        final Set mapSet =(Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        SafeSyncMapRuntimeMonitor.syncCreateIterEvent(mapSet, iter);
		//SafeSyncMap_asyncCreateIter
		SafeSyncMapRuntimeMonitor.asyncCreateIterEvent(mapSet, iter);

	}



}
