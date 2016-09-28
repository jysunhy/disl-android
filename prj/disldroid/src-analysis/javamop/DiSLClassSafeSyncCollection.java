package javamop;
import java.util.Collection;
import java.util.Iterator;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
public class DiSLClassSafeSyncCollection {


    //SafeSyncCollection Analysis
    @Before(marker = IteratorInvocationMarker.class)
	public static void method33(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
    //	SafeSyncCollectionRuntimeMonitorDiSL.setPointCut("SafeSyncCollection - IteratorInvocation"+","+fullMethodName+" "+ms.thisMethodDescriptor());


		//CounterClassDiSL.countJoinPoints("SafeSyncCollection");
		 final Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	 SafeSyncCollectionRuntimeMonitorDiSL.accessIterEvent(iter);
	}

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
	public static void method34(final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
    //	SafeSyncCollectionRuntimeMonitorDiSL.setPointCut("SafeSyncCollection - ConstrSynInvocation"+","+fullMethodName+" "+ms.thisMethodDescriptor());


		  final Collection c = (Collection) di.getStackValue(0,Object.class);
          SafeSyncCollectionRuntimeMonitorDiSL.syncEvent(c);

		//CounterClassDiSL.countJoinPoints("SafeSyncCollection");

	}

    @AfterReturning(marker = ColItrInvocationMarker.class)
	public static void method35(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
    //	SafeSyncCollectionRuntimeMonitorDiSL.setPointCut("SafeSyncCollection - ColItrInvocationunlock"+","+fullMethodName+" "+ms.thisMethodDescriptor());


    	final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	 final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
    	 if(Thread.holdsLock(c)){
    	 	  SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
    		//CounterClassDiSL.countJoinPoints("SafeSyncCollection");
    	}
    }

    @AfterReturning(marker = ColItrInvocationMarker.class)
   	public static void method36(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

    	final String fullMethodName = ms.thisMethodFullName().replace("/",".");
    	//SafeSyncCollectionRuntimeMonitorDiSL.setPointCut("SafeSyncCollection - ColItrInvocationlock"+","+fullMethodName+" "+ms.thisMethodDescriptor());


    	final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
    	if(!Thread.holdsLock(c)){
       		SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
       		//CounterClassDiSL.countJoinPoints("SafeSyncCollection");
       	}
    }

}

