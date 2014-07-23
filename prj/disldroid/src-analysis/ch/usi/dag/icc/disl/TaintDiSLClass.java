package ch.usi.dag.icc.disl;

import android.app.Activity;
import android.content.Intent;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class TaintDiSLClass {
    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.IntentPutExtraGuard.class)
    public static void updateIntentExtra (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("put field into intent");
        final android.content.Intent intent = (Intent) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        ICCAnalysisStub.taint_propagate (args[0], intent, ac.getCallee (), ac.thisMethodFullName ());
        ICCAnalysisStub.taint_propagate (args[1], intent, ac.getCallee (), ac.thisMethodFullName ());
    }

    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.StartActivityForResultGuard.class)
    public static void startActivityForResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in start activity for result test");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[0];
        intent.putExtra ("svm_specialtag", "123");
        intent.putExtra ("svm_intentid", AREDispatch.getObjectId (intent));
        intent.putExtra ("svm_pid", AREDispatch.getThisProcId ());
        intent.putExtra ("svm_tid", AREDispatch.getThisThreadId ());
    }

    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.ActivityGetIntentGuard.class)
    public static void getIntent (final CallContext ac, final DynamicContext dc) {
        AREDispatch.NativeLog ("calling get intent");
        final android.content.Intent intent = dc.getStackValue(0, Intent.class);
        if(intent.hasExtra("svm_specialtag")) {
            ICCAnalysisStub.taint_propagate2 ((long)intent.getExtra ("svm_intentid"), (int)intent.getExtra ("svm_pid"), intent, ac.getCallee (), ac.thisMethodFullName ());
        }
    }

    @Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.SetActivityResultGuard.class)
    public static void setActivityResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in set activity result");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[1];
        intent.putExtra ("svm_specialtag", "456");
        intent.putExtra ("svm_intentid", AREDispatch.getObjectId (intent));
        intent.putExtra ("svm_pid", AREDispatch.getThisProcId ());
        intent.putExtra ("svm_tid", AREDispatch.getThisThreadId ());
    }

    @After (
        marker = BodyMarker.class,
        scope = "*.onCreate")
    public static void onActivityCreate (final DynamicContext dc, final CallContext ac) {
        AREDispatch.NativeLog ("in activity on create");
        final Intent intent = dc.getLocalVariableValue (0, Activity.class).getIntent ();
        if(intent.hasExtra("svm_specialtag")) {
            final String tag = intent.getExtras().getString("specialtag");
            if(tag !=null){
                AREDispatch.NativeLog ("HAHA get the special tag "+tag);
            }
        }
    }

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
                    ICCAnalysisStub.taint_propagate2 ((long)intent.getExtra ("svm_intentid"), (int)intent.getExtra ("svm_pid"), intent, ac.getCallee (), ac.thisMethodFullName ());
                }
            }
        }
    }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
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
        ICCAnalysisStub.dynamic_alert (methodName, ac.thisMethodFullName(), arg);
    }
    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
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
            ICCAnalysisStub.dynamic_alert (methodName, ac.thisMethodFullName(), arg);
        }

    @AfterReturning (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = Guard.APISourceGuard.class)
    public static void afterAPISourceInvoke (final DynamicContext dc, final CallContext ac) {
        ICCAnalysisStub.taint_source(dc.getStackValue(0, String.class),1, ac.getCallee(), ac.thisMethodFullName());
    }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.APISinkGuard.class)
        public static void afterAPISinkInvoke (final CallContext ac, final ArgumentProcessorContext pc) {
            final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
            ICCAnalysisStub.taint_sink(args[2], ac.getCallee(), ac.thisMethodFullName());
        }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.IOSourceGuard.class)
        public static void afterIOSourceInvoke (final CallContext ac) {
            final String methodName = ac.getCallee ();
            ICCAnalysisStub.source_alert (methodName, ac.thisMethodFullName());
        }

    @AfterReturning (
            marker = BytecodeMarker.class,
            args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
            guard = Guard.IOSinkGuard.class)
        public static void afterIOSinkInvoke (final CallContext ac) {
            final String methodName = ac.getCallee ();
            ICCAnalysisStub.sink_alert (methodName, ac.thisMethodFullName());
        }
}
