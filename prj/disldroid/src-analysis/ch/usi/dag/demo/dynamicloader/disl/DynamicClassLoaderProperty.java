package ch.usi.dag.demo.dynamicloader.disl;

import java.util.HashMap;
import java.util.Stack;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class DynamicClassLoaderProperty {
    @Before(marker=BodyMarker.class, scope="dalvik.system.BaseDexClassLoader.<init>")
    public static void newClassloader(final ArgumentProcessorContext apc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        DynamicClassLoaderMonitor.onNewDexClassLoader(args[0].toString ());
    }
    @Before (marker = BodyMarker.class)
    public static void beforeMethod (final MethodStaticContext msc) {
        DynamicClassLoaderMonitor.methodEntry (msc.thisMethodFullName ());
    }
    @After (marker = BodyMarker.class)
    public static void afterMethod (final MethodStaticContext msc) {
        DynamicClassLoaderMonitor.methodExit ();
    }
    static class DynamicClassLoaderMonitor {
        public static void methodEntry (final String thisMethodFullName) {
            stacks.get(Thread.currentThread ().getId ()).push (thisMethodFullName);
        }

        public static void methodExit () {
            stacks.get(Thread.currentThread ().getId ()).pop ();
        }
        public static void onNewDexClassLoader (final String path) {
            printViolation(path, stacks.get (Thread.currentThread ().getId ()));
        }
        static HashMap<Long, Stack<String>> stacks = new HashMap <Long, Stack<String>> ();
        private static void printViolation (final String path, final Stack <String> stack) {
        }
    }
}
