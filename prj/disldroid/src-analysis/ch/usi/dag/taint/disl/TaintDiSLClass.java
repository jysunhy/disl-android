package ch.usi.dag.taint.disl;

import android.content.Intent;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.taint.analysis.TaintAnalysisStub;

public class TaintDiSLClass {


    //propagte from arguments to this
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


    //Track the intent to be sent to other activities
    //attach information into the intent object with the putExtra method
    //conflict chance is very low even happens, the key can be adapted
    //the extra information is used to propagate taint in multiple processes
    @Before (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.StartActivityForResultGuard.class)
    public static void startActivityForResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in start activity for result test");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[0];
        final long time = AREDispatch.getCPUClock ();
        TaintAnalysisStub.taint_prepare (intent, time ,ac.getCallee (),  ac.thisMethodFullName ());
        if(intent != null){
            intent.putExtra ("svm_specialtag", "svmonly");
            intent.putExtra ("svm_intentid", AREDispatch.getObjectId (intent));
            intent.putExtra ("svm_pid", AREDispatch.getThisProcId ());
            intent.putExtra ("svm_tid", AREDispatch.getThisThreadId ());
            intent.putExtra ("svm_time", time);
        }
    }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.ActivityGetIntentGuard.class)
    public static void getIntent (final CallContext ac, final DynamicContext dc) {
        AREDispatch.NativeLog ("calling get intent");
        final android.content.Intent intent = dc.getStackValue(0, Intent.class);
        if(intent != null){
            if(intent.hasExtra("svm_specialtag")) {
                TaintAnalysisStub.taint_propagate2 ((long)intent.getExtra ("svm_intentid"), (int)intent.getExtra ("svm_pid"), (long)intent.getExtra ("svm_time"),intent, ac.getCallee (), ac.thisMethodFullName ());
            }
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
            TaintAnalysisStub.taint_propagate (arg, obj, ac.getCallee (), ac.thisMethodFullName ());
        }

        //from this -> result
        if(!ac.getCallee ().endsWith ("V")){
            TaintAnalysisStub.taint_propagate (pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS), obj, ac.getCallee (), ac.thisMethodFullName ());
        }
    }


    @Before (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.SetActivityResultGuard.class)
    public static void setActivityResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in set activity result");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[1];
        final long time = AREDispatch.getCPUClock ();
        TaintAnalysisStub.taint_prepare (intent, time, ac.getCallee (), ac.thisMethodFullName ());
        intent.putExtra ("svm_specialtag", "svmonly");
        intent.putExtra ("svm_intentid", AREDispatch.getObjectId (intent));
        intent.putExtra ("svm_pid", AREDispatch.getThisProcId ());
        intent.putExtra ("svm_tid", AREDispatch.getThisThreadId ());
        intent.putExtra ("svm_time", time);
    }

//    @After (
//        marker = BodyMarker.class,
//        scope = "*.onCreate")
//    public static void onActivityCreate (final DynamicContext dc, final CallContext ac) {
//        AREDispatch.NativeLog ("in activity on create");
//        final Intent intent = dc.getLocalVariableValue (0, Activity.class).getIntent ();
//        if(intent!=null){
//            if(intent.hasExtra("svm_specialtag")) {
//                final String tag = intent.getExtras().getString("specialtag");
//                if(tag !=null){
//                    AREDispatch.NativeLog ("HAHA get the special tag "+tag);
//                }
//            }
//        }
//    }

    @Before (
        marker = BodyMarker.class,
        scope = "*.onActivityResult")
    public static void onActivityResult (final CallContext ac, final ArgumentProcessorContext pc) {
        AREDispatch.NativeLog ("in on activity result");
        final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        if(args.length==3){
            final Intent intent = (Intent)args[2];
            if(intent == null){

            }else{
                if(intent.hasExtra("svm_specialtag")) {
                    final String tag = intent.getExtras().getString("specialtag");
                    if(tag !=null){
                        AREDispatch.NativeLog ("HAHA get the special tag "+tag);
                    }
                    TaintAnalysisStub.taint_propagate2 ((long)intent.getExtra ("svm_intentid"), (int)intent.getExtra ("svm_pid"),(long)intent.getExtra ("svm_time") , intent, ac.getCallee (), ac.thisMethodFullName ());
                }
            }
        }
    }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.ReflectionGuard.class)
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
        TaintAnalysisStub.taint_source(dc.getStackValue(0, String.class),1, ac.getCallee(), ac.thisMethodFullName());
    }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.APISinkGuard.class)
        public static void afterAPISinkInvoke (final CallContext ac, final ArgumentProcessorContext pc) {
            final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
            if(args.length>2){
                TaintAnalysisStub.taint_sink(args[2], ac.getCallee(), ac.thisMethodFullName());
            }
        }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.IOSourceGuard.class)
        public static void afterIOSourceInvoke (final CallContext ac) {
            final String methodName = ac.getCallee ();
            TaintAnalysisStub.source_alert (methodName, ac.thisMethodFullName());
        }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.IOSinkGuard.class)
        public static void afterIOSinkInvoke (final CallContext ac) {
            final String methodName = ac.getCallee ();
            TaintAnalysisStub.sink_alert (methodName, ac.thisMethodFullName());
        }
}
