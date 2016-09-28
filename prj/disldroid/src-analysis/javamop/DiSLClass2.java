package javamop;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class DiSLClass2 {

    //HasNext Analysis
    @Before(marker = NextInvocationMarker.class)
        public static void method1(final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
        final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
              //HasNextRuntimeMonitorDiSL.nextEvent(receiver);
             CounterClassDiSL.countJoinPoints("HasNext");
    }

    @After(marker = HasNextInvocationMarker.class)
        public static void method2(final ArgumentProcessorContext pc,final MethodStaticContext ms,final MethodInvocationStaticContext1 msc) {
            final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //HasNextRuntimeMonitorDiSL.hasnextEvent(receiver);
            CounterClassDiSL.countJoinPoints("HasNext");
    }
    //End of HasNext Analysis

    //FileClose Analysis
    //This to handle the file close event
//  @After(marker = BodyMarker.class,scope="TestHarness.main")
//    public static void method4()
//    {
//           Myruntimeanalysis.getInstance();
//
//    }

//  @Before(marker = WriteInvocationMarker.class)
//  public static void method3(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//          CounterClassDiSL.countJoinPoints("FileClose");
//          Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//          FileWriter fRef1 = (FileWriter)receiver;
//          //FileCloseRuntimeMonitorDiSL.writeEvent(fRef1);
//  }

//  @After(marker = WCloseInvocationMarker.class)
//  public static void method4(ArgumentProcessorContext pc,MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//          CounterClassDiSL.countJoinPoints("FileClose");
//          Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//          FileWriter fRef1 = (FileWriter)receiver;
//          FileCloseRuntimeMonitorDiSL.closeEvent(fRef1);
//  }
    //End of FileClose Analysis

    //SafeEnum Analysis
    @Before(marker = NextElementInvocationMarker.class)
        public static void method5(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
             final Enumeration e = (Enumeration) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //SafeEnumRuntimeMonitorDiSL.nextEvent(e);
            if(e instanceof Enumeration){
             CounterClassDiSL.countJoinPoints("SafeEnum");
            }
        }


    @AfterReturning(marker = ElementInvocationMarker.class)
        public static void method6(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
              final Enumeration e = (Enumeration) di.getStackValue(0, Object.class);
              final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
              //SafeEnumRuntimeMonitorDiSL.createEvent(v, e);
              if(v instanceof Vector){
              CounterClassDiSL.countJoinPoints("SafeEnum");
              }
    }

    @After(marker = ClearInvocationMarker.class)
        public static void method7(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                  CounterClassDiSL.countJoinPoints("SafeEnum");
            }

    }

    @After(marker = AddInvocationMarker.class)
        public static void method8(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                  CounterClassDiSL.countJoinPoints("SafeEnum");
            }
    }

    @After(marker = RemoveInvocationMarker.class)
        public static void method9(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                  CounterClassDiSL.countJoinPoints("SafeEnum");
            }
    }

    @After(marker = InsertElementAtInvocationMarker.class)
        public static void method10(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                CounterClassDiSL.countJoinPoints("SafeEnum");
            }
        }

    @After(marker = SetInvocationMarker.class)
        public static void method11(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                CounterClassDiSL.countJoinPoints("SafeEnum");
            }
    }

    @After(marker = RetainAllInvocationMarker.class)
        public static void method12(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
            if(v instanceof Vector){
                CounterClassDiSL.countJoinPoints("SafeEnum");
            }
        }
    //End of SafeEnum Analysis

    //SafeFile Analysis

//    @Before(marker = BodyMarker.class, scope="* *.*(..)")
//
//      public static void method13(){
//          Thread t1 = Thread.currentThread();
//      //There is a problem here
//          SafeFileRuntimeMonitorDiSL.beginEvent(t1);
//  }
//
//    @AfterReturning(marker = NewInvocationMarker.class)
//      public static void method14(ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//
//          Thread t = Thread.currentThread();
//
//          Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//          FileReader fRef1 = (FileReader)receiver;
//
//            SafeFileRuntimeMonitorDiSL.openEvent(t, fRef1);
//          //CounterClassDiSL.countJoinPoints("SafeFile");
//      }
//
//    @After(marker = CloseInvocationMarker.class)
//      public static void method15(DynamicContext di,ArgumentProcessorContext pc, MethodStaticContext ms, MethodInvocationStaticContext1 msc){
//
//          Thread t = Thread.currentThread();
//
//          Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
//          FileReader fRef2= (FileReader)receiver;
//
//
//          SafeFileRuntimeMonitorDiSL.closeEvent(fRef2, t);
//          //CounterClassDiSL.countJoinPoints("SafeFile");
//
//      }
//
//   @After(marker = BodyMarker.class, scope = "* *.*(..)")
//      public static void method16(){
//          Thread t = Thread.currentThread();
//          SafeFileRuntimeMonitorDiSL.endEvent(t);
//  }

    //End of SafeFileAnalysis

    //SafeSyncMap Analysis
    @Before(marker = IteratorInvocationMarker.class)
        public static void method17(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
           // SafeSyncMapRuntimeMonitorDiSL.accessIterEvent(iter);

//          String fullMethodName = ms.thisMethodFullName().replace("/",".");
//          CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);
            CounterClassDiSL.countJoinPoints("SafeSyncMap");
        }

    @AfterReturning(marker = ConstrSynInvocationMarker.class)
        public static void method18(final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Map syncMap = (Map) di.getStackValue(0,Object.class);
            //SafeSyncMapRuntimeMonitorDiSL.syncEvent(syncMap);

            CounterClassDiSL.countJoinPoints("SafeSyncMap");

//          String fullMethodName = ms.thisMethodFullName().replace("/",".");
//          CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);

        }

    @AfterReturning(marker = MapKeyInvocationMarker.class)
        public static void method19(final ArgumentProcessorContext pc,final DynamicContext di , final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

        final Set mapSet = (Set) di.getStackValue(0,Object.class);
        final Map syncMap =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        //SafeSyncMapRuntimeMonitorDiSL.createSetEvent(syncMap, mapSet);

            CounterClassDiSL.countJoinPoints("SafeSyncMap");
//          String fullMethodName = ms.thisMethodFullName().replace("/",".");
//      CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);


        }

    @AfterReturning(marker = SetItrInvocationMarker.class)
        public static void method20(final ArgumentProcessorContext pc,final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){

            //Set mapSet = (Set) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final Collection mapSet = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            final Iterator iter = (Iterator) di.getStackValue(0,Object.class);

            //SafeSyncMapRuntimeMonitorDiSL.syncCreateIterEvent(mapSet, iter);


            if(mapSet instanceof Set){
//              String fullMethodName = ms.thisMethodFullName().replace("/",".");
//              CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);

                CounterClassDiSL.countJoinPoints("SafeSyncMap");
            }
                //SafeSyncMap_asyncCreateIter

            //SafeSyncMapRuntimeMonitorDiSL.asyncCreateIterEvent(mapSet, iter);

        }
    //End of SafeSyncMap


    //UnsafeIterator Analysis
    @Before(marker = NextInvocationMarker.class)
        public static void method21(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
            CounterClassDiSL.countJoinPoints("UnsafeIterator");
             final Iterator i =   (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            // UnsafeIteratorRuntimeMonitorDiSL.nextEvent(i);
        }


    @AfterReturning(marker = ItrConstrItInvocationMarker.class)
        public static void method22(final DynamicContext di, final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

             final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             final Iterator i = (Iterator) di.getStackValue(0,Object.class);
             //UnsafeIteratorRuntimeMonitorDiSL.createEvent(c, i);
             if(c instanceof Collection){
             CounterClassDiSL.countJoinPoints("UnsafeIterator");
             }
         }

    @After(marker = CRemoveInvocationMarker.class)
        public static void method23(final DynamicContext di,final ArgumentProcessorContext pc,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

             final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
             if(c instanceof Collection){
                 CounterClassDiSL.countJoinPoints("UnsafeIterator");
                 }
        }

    @After(marker = CAddInvocationMarker.class)
        public static void method24(final DynamicContext di,final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

            final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
            if(c instanceof Collection){
                 CounterClassDiSL.countJoinPoints("UnsafeIterator");
            }
        }

    //End of UnsafeIterator

    //UnsafeMapIterator Analysis
    @Before(marker = NextInvocationMarker.class)
    public static void method25(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
             final Iterator i = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeMapIteratorRuntimeMonitorDiSL.useIterEvent(i);
    }


    @AfterReturning(marker = ValuesInvocationMarker.class)
       public static void method26(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
             final Set c = (Set) di.getStackValue(0,Object.class);
             final Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }


    @AfterReturning(marker = MapKeyInvocationMarker.class)
        public static void method27(final ArgumentProcessorContext pc,final DynamicContext di , final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

             final Set c = (Set) di.getStackValue(0,Object.class);
             final Map map = (Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

            //UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }

    @AfterReturning(marker = ColItrInvocationMarker.class)
        public static void method28(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
                CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

                final Iterator i = (Iterator) di.getStackValue(0,Object.class);
                final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

              //UnsafeMapIteratorRuntimeMonitorDiSL.createIterEvent(c, i);
    }

    @After(marker = PutAllInvocationMarker.class)
        public static void method29(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

            final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = PutInvocationMarker.class)
        public static void method30(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
              CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
//      String fullMethodName = ms.thisMethodFullName().replace("/",".");
//      CounterClassDiSL.countJoinPoints("UnsafeMapIterator"+","+fullMethodName+" "+msc.toShortString());
//            Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
              //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }

    @After(marker = MClearInvocationMarker.class)
        public static void method31(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

             final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    @After(marker = MRemoveInvocationMarker.class)
        public static void method32(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc ){
            CounterClassDiSL.countJoinPoints("UnsafeMapIterator");

             final Map mapSet =(Map) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
             //UnsafeMapIteratorRuntimeMonitorDiSL.updateMapEvent(mapSet);
    }
    //End of UnsafeMapIterator Analysis

    //SafeSyncCollection Analysis
    @Before(marker = IteratorInvocationMarker.class)
    public static void method33(final ArgumentProcessorContext pc, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

        CounterClassDiSL.countJoinPoints("SafeSyncCollection");
        // Iterator iter = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeSyncCollectionRuntimeMonitorDiSL.accessIterEvent(iter);
    }

    @AfterReturning(marker = ConstrOSynInvocationMarker.class)
    public static void method34(final DynamicContext di, final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){

          final Collection c = (Collection) di.getStackValue(0,Object.class);
          //SafeSyncCollectionRuntimeMonitorDiSL.syncEvent(c);

        CounterClassDiSL.countJoinPoints("SafeSyncCollection");

    }

    @AfterReturning(marker = ColItrInvocationMarker.class)
    public static void method35(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
         final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
         final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
         if(Thread.holdsLock(c)){
              //SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
              if(c instanceof Collection){
                CounterClassDiSL.countJoinPoints("SafeSyncCollection");
              }
        }
    }

    @AfterReturning(marker = ColItrInvocationMarker.class)
    public static void method36(final ArgumentProcessorContext pc,final DynamicContext di,final MethodStaticContext ms, final MethodInvocationStaticContext1 msc){
        final Collection c = (Collection) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
        if(!Thread.holdsLock(c)){
            //SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
            if(c instanceof Collection){
                CounterClassDiSL.countJoinPoints("SafeSyncCollection");
              }
        }
    }

}

//Used for Debugging purpose
//String fullMethodName = ms.thisMethodFullName().replace("/",".");
//CounterClassDiSL.countJoinPoints("UnsafeIterator add"+","+fullMethodName+" "+ms.thisMethodDescriptor());
//
