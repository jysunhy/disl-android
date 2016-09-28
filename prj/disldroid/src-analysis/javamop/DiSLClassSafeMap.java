package javamop;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassSafeMap {


    //SafeSyncMap Analysis
    @Before(marker = IteratorInvocationMarker.class)
    	public static void method17(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

	    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
			//SafeSyncMapMonitorDiSL.setPointCut("SafeSyncMap - Iterator"+","+fullMethodName+" "+ms.thisMethodDescriptor());

			final Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    		SafeSyncMapRuntimeMonitorDiSL.accessIterEvent(iter);



			//CounterClassDiSL.countJoinPoints("SafeSyncMap");
    	}

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
    	public static void method18(final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
		//SafeSyncMapMonitorDiSL.setPointCut("SafeSyncMap - ConstrSynInvocation"+","+fullMethodName+" "+ms.thisMethodDescriptor());


			final Map syncMap = (Map) di.getStackValue(0,Object.class);
   	 		SafeSyncMapRuntimeMonitorDiSL.syncEvent(syncMap);

			//CounterClassDiSL.countJoinPoints("SafeSyncMap");

    	}

    @AfterReturning(marker = MapKeyInvocationMarker.class)
    	public static void method19(final ArgumentProcessorContext pc,final DynamicContext di , final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
		//SafeSyncMapMonitorDiSL.setPointCut("SafeSyncMap - MapKeyInvocation"+","+fullMethodName+" "+ms.thisMethodDescriptor());


		final Set mapSet = (Set) di.getStackValue(0,Object.class);
   	 	final Map syncMap =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

   	 	SafeSyncMapRuntimeMonitorDiSL.createSetEvent(syncMap, mapSet);

			//CounterClassDiSL.countJoinPoints("SafeSyncMap");

    	}

    @AfterReturning(marker = SetItrInvocationMarker.class)
    	public static void method20(final ArgumentProcessorContext pc,final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
		//SafeSyncMapMonitorDiSL.setPointCut("SafeSyncMap - SetItrInvocation"+","+fullMethodName+" "+ms.thisMethodDescriptor());



			final Set mapSet = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    		final Iterator iter = (Iterator) di.getStackValue(0,Object.class);

    		SafeSyncMapRuntimeMonitorDiSL.syncCreateIterEvent(mapSet, iter);

    	//		CounterClassDiSL.countJoinPoints("SafeSyncMap");
    	 	//SafeSyncMap_asyncCreateIter

    	 	SafeSyncMapRuntimeMonitorDiSL.asyncCreateIterEvent(mapSet, iter);


			//CounterClassDiSL.countJoinPoints("SafeSyncMap");
    	}
	//End of SafeSyncMap

}