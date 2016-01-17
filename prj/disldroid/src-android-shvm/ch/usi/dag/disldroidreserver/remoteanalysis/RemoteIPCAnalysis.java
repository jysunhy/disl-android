package ch.usi.dag.disldroidreserver.remoteanalysis;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public abstract class RemoteIPCAnalysis extends RemoteAnalysis{
    public void onRequestSent(final TransactionInfo info, final NativeThread client, final Context ctx){}
    public void onRequestReceived(final TransactionInfo info, final NativeThread client,final NativeThread server, final Context ctx){}
    public void onResponseSent(final TransactionInfo info, final NativeThread client, final NativeThread server, final Context ctx){}
    public void onResponseReceived(final TransactionInfo info, final NativeThread client,final NativeThread server, final Context ctx){}
    @Override
    public void atExit (final Context context) {
        // TODO Auto-generated method stub

    }
    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }
}
