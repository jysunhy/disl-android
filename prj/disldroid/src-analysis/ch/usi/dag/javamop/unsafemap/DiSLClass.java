package ch.usi.dag.javamop.unsafemap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.usi.dag.disl.annotation.After;
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

     final Iterator i = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

       UnsafeMapIteratorRuntimeMonitor.useIterEvent(i);
	}


    @Before(marker = NextInvocationMarker.class)
	public static void method2(final ArgumentProcessorContext pc){
         final Iterator i = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

         	UnsafeMapIteratorRuntimeMonitor.useIterEvent(i);

	}

       @AfterReturning(marker = MapKeyInvocationMarker.class)
	public static void method4(final ArgumentProcessorContext pc,final DynamicContext di ){

        //Collection c = (Collection) di.getStackValue(0,Object.class);
final Set c = (Set) di.getStackValue(0,Object.class);
        final Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

      		UnsafeMapIteratorRuntimeMonitor.createCollEvent(map, c);
	}

	   @AfterReturning(marker = SetInvocationMarker.class)
	public static void method5(final ArgumentProcessorContext pc,final DynamicContext di ){

        final Iterator i = (Iterator) di.getStackValue(0,Object.class);
       // Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final Set c = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      		UnsafeMapIteratorRuntimeMonitor.createIterEvent(c, i);
	}



   //   @AfterReturning(marker = PutInvocationMarker.class, scope = "Main.main")
@After(marker = PutInvocationMarker.class)
	public static void method6(final ArgumentProcessorContext pc,final DynamicContext di ){


        final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        	UnsafeMapIteratorRuntimeMonitor.updateMapEvent(mapSet);

	}



}
