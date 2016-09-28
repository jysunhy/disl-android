package ch.usi.dag.example.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteIPCAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.disldroidreserver.shadow.ShadowThread;

public class Analysis extends RemoteIPCAnalysis{
    public void enter (final ShadowThread thd, final ShadowString methodFullName, final Context ctx){
        //...
    }
    public void leave (final ShadowThread thd, final ShadowString methodFullName, final Context ctx){
        //...
    }
    public void leaveWithObject (final ShadowThread thd, final ShadowString methodFullName, final ShadowObject retValue, final Context ctx){
        //...
    }
    @Override
    public void atExit (final Context context) {
        //...
        //method invocation information
    }
}
