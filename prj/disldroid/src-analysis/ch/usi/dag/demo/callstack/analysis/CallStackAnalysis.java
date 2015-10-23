package ch.usi.dag.demo.callstack.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class CallStackAnalysis extends RemoteAnalysis {

    public void boundaryStart (
        final Context ctx, final int tid, final ShadowString boundaryName) {
        final SVMCallStack state = SVMCallStack.get (ctx, tid);
        state.pushBoundary (boundaryName.toString ());
    }

    public void boundaryEnd (
        final Context ctx, final int tid, final ShadowString boundaryName) {
        final SVMCallStack state = SVMCallStack.get (ctx, tid);
        state.popBoundary (boundaryName.toString ());
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
