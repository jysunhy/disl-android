package javamop;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;


public class Full {

    // HasNext Analysis
    @Before (marker = NextInvocationMarker.class)
    public static void method1 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        // final Iterator receiver = (Iterator)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        // HasNextRuntimeMonitorDiSL.nextEvent(receiver);
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("HasNext"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    @After (marker = HasNextInvocationMarker.class)
    public static void method2 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        // final Iterator receiver = (Iterator)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        // HasNextRuntimeMonitorDiSL.hasnextEvent(receiver);
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("HasNext"
            + "," + fullMethodName + "," + msc.toLongString ());
    }

    // SafeEnum Analysis
    @Before (marker = NextElementInvocationMarker.class)
    public static void method5 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final Enumeration e = (Enumeration) pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.nextEvent(e);
        if (e instanceof Enumeration) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @AfterReturning (marker = ElementInvocationMarker.class)
    public static void method6 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        // final Enumeration e = (Enumeration) di.getStackValue(0,
        // Object.class);
        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.createEvent(v, e);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = ClearInvocationMarker.class)
    public static void method7 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }

    }


    @After (marker = AddInvocationMarker.class)
    public static void method8 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = RemoveInvocationMarker.class)
    public static void method9 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = InsertElementAtInvocationMarker.class)
    public static void method10 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = SetInvocationMarker.class)
    public static void method11 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = RetainAllInvocationMarker.class)
    public static void method12 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final Object v = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeEnumRuntimeMonitorDiSL.updatesourceEvent(v);
        if (v instanceof Vector) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeEnum"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }

    // SafeSyncMap Analysis
    @Before (marker = IteratorInvocationMarker.class)
    public static void method17 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        // final Iterator iter = (Iterator)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        // SafeSyncMapRuntimeMonitorDiSL.accessIterEvent(iter);

        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("SafeSyncMap"
            + "," + fullMethodName + "," + msc.toLongString ());

    }


    @AfterReturning (marker = ConstrSynInvocationMarker.class)
    public static void method18 (
        final DynamicContext di, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        // final Map syncMap = (Map) di.getStackValue(0,Object.class);
        // SafeSyncMapRuntimeMonitorDiSL.syncEvent(syncMap);

        // CounterClassDiSL.countJoinPoints("SafeSyncMap");
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("SafeSyncMap"
            + "," + fullMethodName + "," + msc.toLongString ());

        // String fullMethodName = ms.thisMethodFullName().replace("/",".");
        // CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);

    }


    @AfterReturning (marker = MapKeyInvocationMarker.class)
    public static void method19 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {

        // final Set mapSet = (Set) di.getStackValue(0,Object.class);
        // final Map syncMap =(Map)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        // SafeSyncMapRuntimeMonitorDiSL.createSetEvent(syncMap, mapSet);

        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("SafeSyncMap"
            + "," + fullMethodName + "," + msc.toLongString ());

        // CounterClassDiSL.countJoinPoints("SafeSyncMap");
        // String fullMethodName = ms.thisMethodFullName().replace("/",".");
        // CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);

    }


    @AfterReturning (marker = SetItrInvocationMarker.class)
    public static void method20 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {

        // Set mapSet = (Set)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final Object mapSet = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // final Iterator iter = (Iterator) di.getStackValue(0,Object.class);

        // SafeSyncMapRuntimeMonitorDiSL.syncCreateIterEvent(mapSet, iter);

        if (mapSet instanceof Set) {
            // String fullMethodName = ms.thisMethodFullName().replace("/",".");
            // CounterClassDiSL.countJoinPoints("SafeSyncMap"+","+fullMethodName);
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeSyncMap"
                + "," + fullMethodName + "," + msc.toLongString ());

        }
        // SafeSyncMap_asyncCreateIter

        // SafeSyncMapRuntimeMonitorDiSL.asyncCreateIterEvent(mapSet, iter);

    }


    // End of SafeSyncMap

    // UnsafeIterator Analysis
    @Before (marker = NextInvocationMarker.class)
    public static void method21 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        // final Iterator i = (Iterator)
        // pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        // UnsafeIteratorRuntimeMonitorDiSL.nextEvent(i);
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeIterator"
            + "," + fullMethodName + "," + msc.toLongString ());

    }


    @AfterReturning (marker = ItrConstrItInvocationMarker.class)
    public static void method22 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {

        final Object c = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // final Iterator i = (Iterator) di.getStackValue(0,Object.class);
        // UnsafeIteratorRuntimeMonitorDiSL.createEvent(c, i);
        if (c instanceof Collection) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("UnsafeIterator"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = CRemoveInvocationMarker.class)
    public static void method23 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {

        final Object c = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
        if (c instanceof Collection) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("UnsafeIterator"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    @After (marker = CAddInvocationMarker.class)
    public static void method24 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {

        final Object c = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // UnsafeIteratorRuntimeMonitorDiSL.updatesourceEvent(c);
        if (c instanceof Collection) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("UnsafeIterator"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }


    // End of UnsafeIterator

    // UnsafeMapIterator Analysis
    @Before (marker = NextInvocationMarker.class)
    public static void method25 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
        // UnsafeMapIteratorRuntimeMonitorDiSL.useIterEvent(i);
    }


    @AfterReturning (marker = ValuesInvocationMarker.class)
    public static void method26 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
        // UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }


    @AfterReturning (marker = MapKeyInvocationMarker.class)
    public static void method27 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());

        // UnsafeMapIteratorRuntimeMonitorDiSL.createCollEvent(map, c);
    }


    @AfterReturning (marker = ColItrInvocationMarker.class)
    public static void method28 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());

        // UnsafeMapIteratorRuntimeMonitorDiSL.createIterEvent(c, i);
    }


    @After (marker = PutAllInvocationMarker.class)
    public static void method29 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    @After (marker = PutInvocationMarker.class)
    public static void method30 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    @After (marker = MClearInvocationMarker.class)
    public static void method31 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    @After (marker = MRemoveInvocationMarker.class)
    public static void method32 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("UnsafeMapIterator"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    // End of UnsafeMapIterator Analysis

    // SafeSyncCollection Analysis
    @Before (marker = IteratorInvocationMarker.class)
    public static void method33 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("SafeSynccollection"
            + "," + fullMethodName + "," + msc.toLongString ());
    }


    @AfterReturning (marker = ConstrOSynInvocationMarker.class)
    public static void method34 (
        final DynamicContext di, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {

        final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
        CounterClassDiSL.countJoinPoints ("SafeSynccollection"
            + "," + fullMethodName + "," + msc.toLongString ());

    }


    @AfterReturning (marker = ColItrInvocationMarker.class)
    public static void method35 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final Object c = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // final Iterator iter = (Iterator) di.getStackValue(0,Object.class);
        // if(Thread.holdsLock(c)){
        // SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
        if (c instanceof Collection) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeSynccollection"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
        // }
    }


    @AfterReturning (marker = ColItrInvocationMarker.class)
    public static void method36 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final Object c = pc.getReceiver (ArgumentProcessorMode.CALLSITE_ARGS);
        // Iterator iter = (Iterator) di.getStackValue(0,Object.class);
        // if(!Thread.holdsLock(c)){
        // SafeSyncCollectionRuntimeMonitorDiSL.syncCreateIterEvent(c, iter);
        if (c instanceof Collection) {
            final String fullMethodName = ms.thisMethodFullName ().replace ("/", ".");
            CounterClassDiSL.countJoinPoints ("SafeSynccollection"
                + "," + fullMethodName + "," + msc.toLongString ());
        }
    }

}
