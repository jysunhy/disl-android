package javamop;
import java.util.Enumeration;
import java.util.Vector;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassSafeEnum {



	//SafeEnum Analysis
	@Before(marker = NextElementInvocationMarker.class)
		public static void method5(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
		     final Enumeration e = (Enumeration) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         SafeEnumRuntimeMonitorDiSL.nextEvent(e);
			//CounterClassDiSL.countJoinPoints("SafeEnum");
	}


	@AfterReturning(marker = ElementInvocationMarker.class)
		public static void method6(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			  final Enumeration e = (Enumeration) di.getStackValue(0, Object.class);
		 	  final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
		 	  SafeEnumRuntimeMonitorDiSL.createEvent(v, e);
			//CounterClassDiSL.countJoinPoints("SafeEnum");
		}

    @After(marker = ClearInvocationMarker.class)
        public static void method7(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = AddInvocationMarker.class)
    	public static void method8(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = RemoveInvocationMarker.class)
    	public static void method9(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = InsertElementAtInvocationMarker.class)
    	public static void method10(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = SetInvocationMarker.class)
    	public static void method11(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = RetainAllInvocationMarker.class)
    	public static void method12(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("SafeEnum");
			final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}
    //End of SafeEnum Analysis
}

