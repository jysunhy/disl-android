package ch.usi.dag.demo.callstack.analysis;

import org.apache.harmony.dalvik.ddmc.DdmVmInternal;

import ch.usi.dag.dislre.AREDispatch;

public class CallStackAnalysisStub {

    public static short BOUNDARY_START = AREDispatch.registerMethod ("ch.usi.dag.demo.callstack.analysis.CallStackAnalysis.boundaryStart");

    public static void boundary_start (final String boundaryName) {
        AREDispatch.NativeLog ("boundary start "+boundaryName);
        AREDispatch.analysisStart (BOUNDARY_START);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendString (boundaryName);
        //AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }
    public static short BOUNDARY_END = AREDispatch.registerMethod ("ch.usi.dag.demo.callstack.analysis.CallStackAnalysis.boundaryEnd");

    public static void boundary_end (final String boundaryName) {
        AREDispatch.NativeLog ("boundary end "+boundaryName);
        AREDispatch.analysisStart (BOUNDARY_END);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendString (boundaryName);
        //AREDispatch.sendObjectPlusData (boundaryName);
        AREDispatch.analysisEnd ();
    }

    public static short STACKTRACE = AREDispatch.registerMethod ("ch.usi.dag.demo.callstack.analysis.CallStackAnalysis.stacktrace");

    public static void sendStackTrace(){
        final int tid = AREDispatch.getThisThreadId ();
        final StackTraceElement [] stackTraceElements = DdmVmInternal.getStackTraceById(tid);
        final StringBuilder st = new StringBuilder ();
        if(stackTraceElements != null) {
            for(int i = 2; i < stackTraceElements.length; i++){
               final StackTraceElement elem = stackTraceElements[i];
               st.append (elem.getClassName ());
               st.append (" ");
               st.append (elem.getMethodName ());
               st.append (" ");
               st.append (elem.getFileName ());
               st.append (" ");
               st.append (elem.getLineNumber ());
               st.append ("\n");
            }
        }
        AREDispatch.analysisStart (STACKTRACE);
        AREDispatch.sendInt (tid);
        AREDispatch.sendString(st.toString ());
        AREDispatch.analysisEnd ();
    }
}