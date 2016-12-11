package ch.usi.dag.demo.callstack.disl;

import ch.usi.dag.demo.callstack.analysis.CallStackAnalysisStub;
import ch.usi.dag.demo.dynamicloader.disl.Guard;
import ch.usi.dag.demo.ipc.disl.CallContext;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.dislre.AREDispatch;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class MethodEntryDiSLClass {

    /* every time entering a method */
    @Before (
        marker = BodyMarker.class,
        //scope = "ch.usi.dag.android.example.*.*",
        order = 1000)
    public static void before_enter (final CallContext msc) {
        CallStackAnalysisStub.boundary_start (msc.thisMethodFullName ());
    }

    /* every time leaving a method */
    @After (
        marker = BodyMarker.class,
        //scope = "ch.usi.dag.android.example.*.*",
        order = 1000)
    public static void after_enter (final CallContext msc) {
        CallStackAnalysisStub.boundary_end (msc.thisMethodFullName()+msc.thisMethodDescriptor ());
    }
//
//    @Before(marker=BytecodeMarker.class,guard=Guard.ReflectionGuard.class, args = "invokevirtual")
//    public static void reflect(final ArgumentProcessorContext apc){
//        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
//
//        if(args.length > 0 && args[0] != null) {
//            AREDispatch.NativeLog ("REFLECTION "+args[0].getClass ().toString ());
//            if(args[0] instanceof java.lang.Class){
//                final Class clz = (Class)args[0];
//                AREDispatch.NativeLog ("REFLECTION real class "+clz.getName ());
//            }
//        }else {
//            AREDispatch.NativeLog ("REFLECTION empty");
//        }
//    }
    @Before(marker=BytecodeMarker.class,
    order = 2,
    guard=Guard.DeviceIdGuard.class,
    args = "invokevirtual")
    public static void getDeviceId () {
        AREDispatch.NativeLog ("GET DEVICE ID");
        CallStackAnalysisStub.boundary_start ("GET DEVICE ID");
        //NetworkAnalysisStub.fileopen ("DeviceId");
    }
}
