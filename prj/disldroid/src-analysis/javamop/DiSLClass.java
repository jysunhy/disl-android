package javamop;
import ch.usi.dag.disl.annotation.After;

import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import java.util.*;
import java.io.*;
public class DiSLClass {

	//HasNext Analysis

	@Before(marker = NextInvocationMarker.class)
		public static void method1(ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
		Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
		      //HasNextRuntimeMonitorDiSL.nextEvent(receiver);
			 CounterClassDiSL.countJoinPoints("HasNext");
	}

	@After(marker = HasNextInvocationMarker.class)
		public static void method2(ArgumentProcessorContext pc,MethodStaticContext ms,MethodInvocationStaticContext1 msc) {
			Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			//HasNextRuntimeMonitorDiSL.hasnextEvent(receiver);
			CounterClassDiSL.countJoinPoints("HasNext");
	}
	//End of HasNext Analysis

	//FileClose Analysis
	//This to handle the file close event
//	@After(marker = BodyMarker.class,scope="TestHarness.main")
//    public static void method4()
//    {
//           Myruntimeanalysis.getInstance();
//
//    }

	@Before(marker = WriteInvocationMarker.class)
	public static void method3(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("FileClose");
			Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	        FileWriter fRef1 = (FileWriter)receiver;
			//FileCloseRuntimeMonitorDiSL.writeEvent(fRef1);
	}

	@After(marker = WCloseInvocationMarker.class)
	public static void method4(ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("FileClose");
//			Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//			FileWriter fRef1 = (FileWriter)receiver;
//			FileCloseRuntimeMonitorDiSL.closeEvent(fRef1);
	}
	//End of FileClose Analysis

	//SafeEnum Analysis
	@Before(marker = NextElementInvocationMarker.class)
		public static void method5(DynamicContext di,ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
		     Enumeration e = (Enumeration) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         //SafeEnumRuntimeMonitorDiSL.nextEvent(e);
			 CounterClassDiSL.countJoinPoints("SafeEnum");
	}


	@AfterReturning(marker = ElementInvocationMarker.class)
		public static void method6(DynamicContext di,ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			  Enumeration e = (Enumeration) di.getStackValue(0, Object.class);
		 	  Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
		 	  //SafeEnumRuntimeMonitorDiSL.createEvent(v, e);
			  CounterClassDiSL.countJoinPoints("SafeEnum");
		}

    @After(marker = ClearInvocationMarker.class)
        public static void method7(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = AddInvocationMarker.class)
    	public static void method8(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = RemoveInvocationMarker.class)
    	public static void method9(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = InsertElementAtInvocationMarker.class)
    	public static void method10(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = SetInvocationMarker.class)
    	public static void method11(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}

    @After(marker = RetainAllInvocationMarker.class)
    	public static void method12(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("SafeEnum");
			Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
   	 		//SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
    	}
    //End of SafeEnum Analysis

    //SafeFile Analysis

//    @Before(marker = BodyMarker.class, scope="* *.*(..)")
//
//    	public static void method13(){
//    		Thread t1 = Thread.currentThread();
//    	//There is a problem here
//    		SafeFileRuntimeMonitorDiSL.beginEvent(t1);
//	}
//
//    @AfterReturning(marker = NewInvocationMarker.class)
//    	public static void method14(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//
//    		Thread t = Thread.currentThread();
//
//    		Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//    		FileReader fRef1 = (FileReader)receiver;
//
//    		  SafeFileRuntimeMonitorDiSL.openEvent(t, fRef1);
//    		//CounterClassDiSL.countJoinPoints("SafeFile");
//    	}
//
//    @After(marker = CloseInvocationMarker.class)
//    	public static void method15(DynamicContext di,ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//
//			Thread t = Thread.currentThread();
//
//			Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//    	 	FileReader fRef2= (FileReader)receiver;
//
//
//    	 	SafeFileRuntimeMonitorDiSL.closeEvent(fRef2, t);
//			//CounterClassDiSL.countJoinPoints("SafeFile");
//
//    	}
//
//	 @After(marker = BodyMarker.class, scope = "* *.*(..)")
//    	public static void method16(){
//		 	Thread t = Thread.currentThread();
//		 	SafeFileRuntimeMonitorDiSL.endEvent(t);
//	}

    //End of SafeFileAnalysis

    //SafeSyncMap Analysis
    @Before(marker = IteratorInvocationMarker.class)
    	public static void method17(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){

    		Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
  	 	   // SafeSyncMapRuntimeMonitorDiSL.accessIterEvent(iter);

			CounterClassDiSL.countJoinPoints("SafeSyncMap");
    	}

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
    	public static void method18(DynamicContext di, MethodStaticContext ms, MethodInvocationStaticContext1 msc){

			Map syncMap = (Map) di.getStackValue(0,Object.class);
   	 		//SafeSyncMapRuntimeMonitorDiSL.syncEvent(syncMap);

			CounterClassDiSL.countJoinPoints("SafeSyncMap");

    	}

    @AfterReturning(marker = MapKeyInvocationMarker.class)
    	public static void method19(ArgumentProcessorContext pc,DynamicContext di , MethodStaticContext ms, MethodInvocationStaticContext1 msc){

		Set mapSet = (Set) di.getStackValue(0,Object.class);
   	 	Map syncMap =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

   	 	//SafeSyncMapRuntimeMonitorDiSL.createSetEvent(syncMap, mapSet);

			CounterClassDiSL.countJoinPoints("SafeSyncMap");

    	}

    @AfterReturning(marker = SetItrInvocationMarker.class)
    	public static void method20(ArgumentProcessorContext pc,DynamicContext di, MethodStaticContext ms, MethodInvocationStaticContext1 msc ){

			Set mapSet = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    		Iterator iter = (Iterator) di.getStackValue(0,Object.class);

    		//SafeSyncMapRuntimeMonitorDiSL.syncCreateIterEvent(mapSet, iter);

    			CounterClassDiSL.countJoinPoints("SafeSyncMap");
    	 	//SafeSyncMap_asyncCreateIter

    	 	//SafeSyncMapRuntimeMonitorDiSL.asyncCreateIterEvent(mapSet, iter);

    	}
	//End of SafeSyncMap


    //UnsafeIterator Analysis
    @Before(marker = NextInvocationMarker.class)
    	public static void method21(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 Iterator i =	(Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    		// UnsafeIteratorRuntimeMonitorDiSL.nextEvent(i);
		}


	@AfterReturning(marker = ItrConstrItInvocationMarker.class)
		public static void method22(DynamicContext di, ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         Iterator i = (Iterator) di.getStackValue(0,Object.class);
	         //UnsafeIteratorRuntimeMonitorDiSL.createEvent(c, i);
    	}

	@After(marker = CRemoveInvocationMarker.class)
		public static void method23(DynamicContext di,ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeIterator");
			 Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         //UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
		}

	@After(marker = CAddInvocationMarker.class)
		public static void method24(DynamicContext di,ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeIterator");
			// Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         //UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);

		}

	//End of UnsafeIterator

	//UnsafeMapIterator Analysis
	@Before(marker = NextInvocationMarker.class)
    public static void method25(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
			 Iterator i = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
	         //UnsafeMapIteratorRuntimeMonitorDiSL.useIterEvent(i);
	}


    @AfterReturning(marker = ValuesInvocationMarker.class)
       public static void method26(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
			 Set c = (Set) di.getStackValue(0,Object.class);
   			 Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      		 //UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }


    @AfterReturning(marker = MapKeyInvocationMarker.class)
    	public static void method27(ArgumentProcessorContext pc,DynamicContext di , MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			 Set c = (Set) di.getStackValue(0,Object.class);
    		 Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

   		    //UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
	}

    @AfterReturning(marker = ColItrInvocationMarker.class)
    	public static void method28(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
				CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			    Iterator i = (Iterator) di.getStackValue(0,Object.class);
    			Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

         	  //UnsafeMapIteratorRuntimeMonitorDiSL.createIterEvent(c, i);
    }

    @After(marker = PutAllInvocationMarker.class)
    	public static void method29(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	    //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = PutInvocationMarker.class)
    	public static void method30(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc ){
			  CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			  Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			  //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = MClearInvocationMarker.class)
    	public static void method31(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc ){
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

			 Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	     //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    @After(marker = MRemoveInvocationMarker.class)
    	public static void method32(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc ){
    		CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

    		 Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
      	     //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    //End of UnsafeMapIterator Analysis

    //SafeSyncCollection Analysis
    @Before(marker = IteratorInvocationMarker.class)
	public static void method33(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){

		CounterClassDiSL.countJoinPoints("SafeSyncCollection");
		// Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	// SafeSyncCollectionRuntimeMonitorDiSL.accessIterEvent(iter);
	}

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
	public static void method34(DynamicContext di, MethodStaticContext ms, MethodInvocationStaticContext1 msc){

		  Collection c = (Collection) di.getStackValue(0,Object.class);
          //SafeSyncCollectionRuntimeMonitorDiSL.syncEvent(c);

		CounterClassDiSL.countJoinPoints("SafeSyncCollection");

	}

    @AfterReturning(marker = ColItrInvocationMarker.class)
	public static void method35(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
    	 Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	 Iterator iter = (Iterator) di.getStackValue(0,Object.class);
    	 if(Thread.holdsLock(c)){
    	 	  //SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
    		    CounterClassDiSL.countJoinPoints("SafeSyncCollection");
    	}
    }

    @AfterReturning(marker = ColItrInvocationMarker.class)
   	public static void method36(ArgumentProcessorContext pc,DynamicContext di,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
    	Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
    	Iterator iter = (Iterator) di.getStackValue(0,Object.class);
    	if(!Thread.holdsLock(c)){
       		//SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
       		CounterClassDiSL.countJoinPoints("SafeSyncCollection");
       	}
    }

}

//Used for Debugging purpose
//String fullMethodName = ms.thisMethodFullName().replace("/",".");
//CounterClassDiSL.countJoinPoints("UnsafeIterator add"+","+fullMethodName+" "+ms.thisMethodDescriptor());
//
