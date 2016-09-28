package javamop;
import java.util.Collection;
import java.util.Iterator;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassUnsafeIterator {


    //UnsafeIterator Analysis
    @Before(marker = NextInvocationMarker.class)
    	public static void method21(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 final Iterator i =	(Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    		 UnsafeIteratorRuntimeMonitorDiSL.nextEvent(i);
		}


	@AfterReturning(marker = ItrConstrItInvocationMarker.class)
		public static void method22(final DynamicContext di, final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         final Iterator i = (Iterator) di.getStackValue(0,Object.class);
	         UnsafeIteratorRuntimeMonitorDiSL.createEvent(c, i);
    	}

	@After(marker = CRemoveInvocationMarker.class)
		public static void method23(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
		}

	@After(marker = CAddInvocationMarker.class)
		public static void method24(final DynamicContext di,final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
		}

	//End of UnsafeIterator


}

