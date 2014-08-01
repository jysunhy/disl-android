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
import ch.usi.dag.icc.disl.CallContext;
import ch.usi.dag.taint.analysis.TaintAnalysisStub;

public class IntentBasedDiSLClass {
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
        AREDispatch.NativeLog ("before start activity for result test");
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
        guard = Guard.StartActivityForResultGuard.class)
    public static void afterStartActivityForResult (final CallContext ac, final ArgumentProcessorContext pc) {

        AREDispatch.NativeLog ("after start activity for result test");

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



//  @After (
//      marker = BodyMarker.class,
//      scope = "*.onCreate")
//  public static void onActivityCreate (final DynamicContext dc, final CallContext ac) {
//      AREDispatch.NativeLog ("in activity on create");
//      final Intent intent = dc.getLocalVariableValue (0, Activity.class).getIntent ();
//      if(intent!=null){
//          if(intent.hasExtra("svm_specialtag")) {
//              final String tag = intent.getExtras().getString("specialtag");
//              if(tag !=null){
//                  AREDispatch.NativeLog ("HAHA get the special tag "+tag);
//              }
//          }
//      }
//  }
}
