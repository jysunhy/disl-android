package ch.usi.dag.taint.disl;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.taint.analysis.TaintAnalysisStub;

public class TaintDiSLClass {


    //propagate from arguments to this
    @Before (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.PotentialPropagationToThis.class)
    public static void potentialProgationToThis (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        //receiver type
        //static this class
        //
        final Object obj = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        for(final Object arg:args) {
            TaintAnalysisStub.taint_propagate (arg, obj, ac.getCallee (), ac.thisMethodFullName ());
        }
    }

  //Propagatoin from this and arguments into the result
    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.PotentialPropagationGuardToResult.class)
    public static void potentialPropagationToResult (final CallContext ac, final DynamicContext dc, final ArgumentProcessorContext pc) {
        AREDispatch.NativeLog ("calling get intent extra");
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        //the result object
        final Object obj = dc.getStackValue(0, Object.class);

        //from argument -> result
        for(final Object arg:args){
            if(arg!=null) {
                TaintAnalysisStub.taint_propagate (arg, obj, ac.getCallee (), ac.thisMethodFullName ());
            }
        }

        //from this -> result
        if(!ac.getCallee ().endsWith ("V")){
            TaintAnalysisStub.taint_propagate (pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS), obj, ac.getCallee (), ac.thisMethodFullName ());
        }
    }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.ReflectionInvokeGuard.class)
    public static void afterReflectionInvoke (final CallContext ac,final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        String arg="";
        for (final Object obj : args) {
            if(obj == null){
                continue;
            }
            final String n = obj.getClass().getCanonicalName();
            if (obj instanceof Object[]){
                continue;
            }else{
                if(n.equals ("java.lang.Integer")) {
                }else if(n.equals ("java.lang.Float")) {
                }else if(n.equals ("java.lang.Double")) {
                }else if(n.equals ("java.lang.String")) {
                    arg = obj.toString ();
                    break;
                }
            }
        }
        final String methodName = ac.getCallee ();
        TaintAnalysisStub.dynamic_alert (methodName, ac.thisMethodFullName(), arg);
    }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.DynamicLoadingGuard.class)
        public static void afterDynamicLoadingInvoke (final CallContext ac,final ArgumentProcessorContext pc) {
            final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
            String arg="";
            for (final Object obj : args) {
                if(obj == null){
                    continue;
                }
                final String n = obj.getClass().getCanonicalName();
                if (obj instanceof Object[]){
                    continue;
                }else{
                    if(n.equals ("java.lang.Integer")) {
                    }else if(n.equals ("java.lang.Float")) {
                    }else if(n.equals ("java.lang.Double")) {
                    }else if(n.equals ("java.lang.String")) {
                        arg = obj.toString ();
                        break;
                    }
                }
            }
            final String methodName = ac.getCallee ();
            TaintAnalysisStub.dynamic_alert (methodName, ac.thisMethodFullName(), arg);
        }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.APISourceGuard.class)
    public static void afterAPISourceInvoke (final DynamicContext dc, final CallContext ac) {
        TaintAnalysisStub.taint_source(dc.getStackValue(0, Object.class),1, ac.getCallee(), ac.thisMethodFullName());
    }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.APISinkGuard.class)
        public static void afterAPISinkInvoke (final CallContext ac, final ArgumentProcessorContext pc) {
            final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
            for (final Object obj : args) {
                TaintAnalysisStub.taint_sink(obj, ac.getCallee(), ac.thisMethodFullName());
            }
        }

//    @AfterReturning (
//            marker = BytecodeMarker.class,
//            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
//            guard = Guard.IOSourceGuard.class)
//        public static void afterIOSourceInvoke (final CallContext ac) {
//            final String methodName = ac.getCallee ();
//            TaintAnalysisStub.source_alert (methodName, ac.thisMethodFullName());
//        }
//
//    @AfterReturning (
//            marker = BytecodeMarker.class,
//            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
//            guard = Guard.IOSinkGuard.class)
//        public static void afterIOSinkInvoke (final CallContext ac) {
//            final String methodName = ac.getCallee ();
//            TaintAnalysisStub.sink_alert (methodName, ac.thisMethodFullName());
//        }
}
