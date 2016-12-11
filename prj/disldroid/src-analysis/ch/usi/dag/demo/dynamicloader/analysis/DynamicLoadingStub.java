package ch.usi.dag.demo.dynamicloader.analysis;

import ch.usi.dag.dislre.AREDispatch;

public class DynamicLoadingStub {

    public static short CL = AREDispatch.registerMethod ("ch.usi.dag.demo.dynamicloader.analysis.DynamicLoadingAnalysis.newBaseDexClassLoader");

    public static void newBaseDexClassLoader (final String path) {
        AREDispatch.NativeLog ("DYNAMIC LOADING "+path);
        AREDispatch.analysisStart (CL);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendString(path);
        AREDispatch.analysisEnd ();
    }
}
