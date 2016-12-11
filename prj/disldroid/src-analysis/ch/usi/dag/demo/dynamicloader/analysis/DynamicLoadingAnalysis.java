package ch.usi.dag.demo.dynamicloader.analysis;

import ch.usi.dag.demo.callstack.analysis.SVMCallStack;
import ch.usi.dag.demo.logging.WebLogger;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public class DynamicLoadingAnalysis extends RemoteAnalysis {
    public static void newBaseDexClassLoader (final Context ctx, final int tid, final String path){
        System.out.println("found "+ctx.getPname ()+" "+ctx.getProcessID ()+" "+tid+" load from "+path);
        WebLogger.reportNetworkRecv(ctx.getProcessID (), ctx.getPname (), tid, 1234, "1234",
            1234, path.getBytes (),
            SVMCallStack.get (ctx, tid).getRuntimeStack ());
    }

    @Override
    public void atExit (final Context context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }
}
