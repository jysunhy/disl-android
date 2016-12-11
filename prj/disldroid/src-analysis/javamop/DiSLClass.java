package javamop;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;


public class DiSLClass {

    // HasNext Analysis
    @Before (marker = NextInvocationMarker.class, guard=Guard.HasNext.class)
    public static void method1 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 1;
        AndroidCounter.log (id);
    }


    @After (marker = HasNextInvocationMarker.class, guard=Guard.HasNext.class)
    public static void method2 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 1;
        AndroidCounter.log (id);
    }

    // SafeEnum Analysis
    @Before (marker = NextElementInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method5 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ElementInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method6 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = ClearInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method7 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = AddInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method8 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = RemoveInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method9 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = InsertElementAtInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method10 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = SetInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method11 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @After (marker = RetainAllInvocationMarker.class, guard=Guard.SafeEnum.class)
    public static void method12 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }

    // SafeSyncMap Analysis
    @Before (marker = IteratorInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method17 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ConstrSynInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method18 (
        final DynamicContext di, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = MapKeyInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method19 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = SetItrInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method20 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 2;
        AndroidCounter.log (id);
    }


    // End of SafeSyncMap

    // UnsafeIterator Analysis
    @Before (marker = NextInvocationMarker.class, guard=Guard.UnsafeIterator.class)
    public static void method21 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 3;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ItrConstrItInvocationMarker.class, guard=Guard.UnsafeIterator.class)
    public static void method22 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 3;
        AndroidCounter.log (id);
    }


    @After (marker = CRemoveInvocationMarker.class, guard=Guard.UnsafeIterator.class)
    public static void method23 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 3;
        AndroidCounter.log (id);
    }


    @After (marker = CAddInvocationMarker.class, guard=Guard.UnsafeIterator.class)
    public static void method24 (
        final DynamicContext di, final ArgumentProcessorContext pc,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 3;
        AndroidCounter.log (id);
    }


    // End of UnsafeIterator

    // UnsafeMapIterator Analysis
    @Before (marker = NextInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method25 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ValuesInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method26 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = MapKeyInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method27 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ColItrInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method28 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @After (marker = PutAllInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method29 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @After (marker = PutInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method30 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @After (marker = MClearInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method31 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }


    @After (marker = MRemoveInvocationMarker.class, guard=Guard.UnsafeMap.class)
    public static void method32 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 4;
        AndroidCounter.log (id);
    }

    // End of UnsafeMapIterator Analysis

    // SafeSyncCollection Analysis
    @Before (marker = IteratorInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method33 (
        final ArgumentProcessorContext pc, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 5;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ConstrOSynInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method34 (
        final DynamicContext di, final MethodStaticContext ms,
        final MethodInvocationStaticContext1 msc) {
        final int id = 5;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ColItrInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method35 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 5;
        AndroidCounter.log (id);
    }


    @AfterReturning (marker = ColItrInvocationMarker.class, guard=Guard.SafeSyncMap.class)
    public static void method36 (
        final ArgumentProcessorContext pc, final DynamicContext di,
        final MethodStaticContext ms, final MethodInvocationStaticContext1 msc) {
        final int id = 5;
        AndroidCounter.log (id);
    }

}
