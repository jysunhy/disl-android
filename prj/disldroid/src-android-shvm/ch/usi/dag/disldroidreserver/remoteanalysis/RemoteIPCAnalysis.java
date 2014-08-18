package ch.usi.dag.disldroidreserver.remoteanalysis;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public abstract class RemoteIPCAnalysis extends RemoteAnalysis{
    public abstract void onRequestSent(TransactionInfo info, NativeThread client, Context ctx);
    public abstract void onRequestReceived(TransactionInfo info, NativeThread client,NativeThread server, Context ctx);
    public abstract void onResponseSent(TransactionInfo info, NativeThread client, NativeThread server, Context ctx);
    public abstract void onResponseReceived(TransactionInfo info, NativeThread client,NativeThread server, Context ctx);
    @Override
    public void atExit (final Context context) {
        // TODO Auto-generated method stub

    }
    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }
}
