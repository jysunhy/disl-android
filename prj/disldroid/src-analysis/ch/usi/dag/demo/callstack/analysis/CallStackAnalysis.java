package ch.usi.dag.demo.callstack.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;


public class CallStackAnalysis extends RemoteAnalysis {

    public void boundaryStart (
        //final Context ctx, final int tid, final ShadowString boundaryName) {
        final Context ctx, final int tid, final String boundaryName) {
        final SVMCallStack state = SVMCallStack.get (ctx, tid);
        state.pushBoundary (boundaryName.toString ());
        //System.out.println(ctx.getPname ()+" "+tid+" entering "+boundaryName);
    }

    public void boundaryEnd (
        //final Context ctx, final int tid, final ShadowString boundaryName) {
        final Context ctx, final int tid, final String boundaryName) {
        final SVMCallStack state = SVMCallStack.get (ctx, tid);
        state.popBoundary (boundaryName.toString ());
        //System.out.println(ctx.getPname ()+" leaving "+boundaryName);
    }
    public void stacktrace(final Context ctx, final int tid, final String st){
        System.out.println(ctx.getPname ()+" "+tid+" thread "+tid+"\n"+st);
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
