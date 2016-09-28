package ch.usi.dag.javamop.unsafeiterator;

import java.util.Iterator;
import java.util.Set;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class DiSLClass {


@Before(marker = NextInvocationMarker.class)

    public static void HasNext_pass(final ArgumentProcessorContext pc){
	final Iterator i =	(Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	UnsafeIteratorRuntimeMonitor.nextEvent(i);

}

 @AfterReturning(marker = ConstrItInvocationMarker.class)
	public static void method3(final DynamicContext di, final ArgumentProcessorContext pc){
       //Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final Set c = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
          final Iterator i = (Iterator) di.getStackValue(0,Object.class);
          UnsafeIteratorRuntimeMonitor.createEvent(c, i);

    }


@After(marker = AddInvocationMarker.class)
		public static void method5(final DynamicContext di,final ArgumentProcessorContext pc){
	       	//	Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	final Set c = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
		        UnsafeIteratorRuntimeMonitor.updatesourceEvent(c);
		}






}
