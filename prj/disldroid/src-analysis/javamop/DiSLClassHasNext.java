package javamop;
import java.util.Iterator;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassHasNext {

	//HasNext Analysis
	@Before(marker = NextInvocationMarker.class)
		public static void method1(final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
		final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
		HasNextRuntimeMonitorDiSL.nextEvent(receiver);
			//CounterClassDiSL.countJoinPoints("HasNext");
	}

	@After(marker = HasNextInvocationMarker.class)
		public static void method2(final ArgumentProcessorContext pc,final MethodStaticContext ms,final MethodInvocationStaticContext1 msc) {
			final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			HasNextRuntimeMonitorDiSL.hasnextEvent(receiver);
			//CounterClassDiSL.countJoinPoints("HasNext");
	}
	//End of HasNext Analysis
}

