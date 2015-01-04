package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;

public class LogOnlyDiSLClass {
    @Before (
        marker = BodyMarker.class,
        scope = "*.*")
    public static void stack_in (final MethodStaticContext msc) {
        //AREDispatch.NativeLog ("Entering: "+msc.thisMethodFullName ()+msc.thisMethodDescriptor ());
        AREDispatch.NativeLog (msc.thisMethodFullName ());
    }
//    @After (
//        marker = BodyMarker.class,
//        scope = "*.*")
//    public static void stack_out (final MethodStaticContext msc) {
//        AREDispatch.NativeLog (msc.thisMethodFullName ());
//        //AREDispatch.NativeLog ("Leaving: "+msc.thisMethodFullName ()+msc.thisMethodDescriptor ());
//    }

//    @After (
//        marker = BodyMarker.class,
//        scope = "org.eembc.grinderbench.CmdlineWrapper.main")
//    public static void api_194 (final MethodStaticContext msc) {
//        AREDispatch.NativeLog ("grinder test end");
//        System.exit (0);
//    }
}
