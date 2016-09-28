package javamop;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassUnsafeMapIterator {


	//UnsafeMapIterator Analysis
	@Before(marker = NextInvocationMarker.class)
    public static void method25(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
			 final Iterator i = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         UnsafeMapIteratorRuntimeMonitorDiSL.useIterEvent(i);
	}


    @AfterReturning(marker = ValuesInvocationMarker.class)
       public static void method26(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
			 final Set c = (Set) di.getStackValue(0,Object.class);
   			 final Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      		 UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }


    @AfterReturning(marker = MapKeyInvocationMarker.class)
    	public static void method27(final ArgumentProcessorContext pc,final DynamicContext di , final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			 final Set c = (Set) di.getStackValue(0,Object.class);
    		 final Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

   		    UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
	}

    @AfterReturning(marker = ColItrInvocationMarker.class)
    	public static void method28(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			    final Iterator i = (Iterator) di.getStackValue(0,Object.class);
    			final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

         	  UnsafeMapIteratorRuntimeMonitorDiSL.createIterEvent(c, i);
    }

    @After(marker = PutAllInvocationMarker.class)
    	public static void method29(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	    UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = PutInvocationMarker.class)
    	public static void method30(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			  final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			  UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = MClearInvocationMarker.class)
    	public static void method31(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
			//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			 final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	     UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    @After(marker = MRemoveInvocationMarker.class)
    	public static void method32(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
    		//CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

    		 final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	     UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    //End of UnsafeMapIterator Analysis

}

