package ch.usi.dag.demo.ipc.analysis;

import java.util.List;

import ch.usi.dag.demo.ipc.analysis.lib.IPCLogger;
import ch.usi.dag.demo.ipc.analysis.lib.ThreadState;
import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteIPCAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;


public class IPCAnalysis extends RemoteIPCAnalysis {

    public static String analysisTag = "PermissionUsage";

    public void permissionUsed (
        //final Context ctx, final int tid, final ShadowString permissionName) {
        final Context ctx, final int tid, final String permissionName) {
        final List<ThreadState> callers = ThreadState.getCallers (ctx, tid);
        for(final ThreadState caller:callers){
            caller.addPermission(permissionName.toString ());
        }
    }

    @Override
    public void onRequestSent (
        final TransactionInfo info, final NativeThread client, final Context ctx) {
//        System.out.println("Consumed "+client.getPid ()+" "+client.getTid ()+info.getTransactionId ()+" 0");
        final ThreadState clientState = ThreadState.get (client);
        clientState.recordRequestSent(client,info);
    }

    @Override
    public void onRequestReceived (
        final TransactionInfo info, final NativeThread client,
        final NativeThread server, final Context ctx) {
//        System.out.println("Consumed "+client.getPid ()+" "+client.getTid ()+info.getTransactionId ()+" 1");
        final ThreadState clientState = ThreadState.get (client);
        clientState.waitForRequestSent (info, server);
        final ThreadState serverState = ThreadState.get (server);
        serverState.recordRequestReceived(client, server, info);
    }

    @Override
    public void onResponseSent (
        final TransactionInfo info, final NativeThread client, final NativeThread server, final Context ctx) {
//        System.out.println("Consumed "+client.getPid ()+" "+client.getTid ()+info.getTransactionId ()+" 2");
            final ThreadState serverState = ThreadState.get (server);
            serverState.recordResponseSent(client, server, info);
    }

    @Override
    public void onResponseReceived (
        final TransactionInfo info, final NativeThread client,
        final NativeThread server, final Context ctx) {
//        System.out.println("Consumed "+client.getPid ()+" "+client.getTid ()+info.getTransactionId ()+" 3");
            final ThreadState serverState = ThreadState.get (server);
            final ThreadState clientState = ThreadState.get (client);
            if(!clientState.checkResponseReceivedValid(info,server)) {
                return;
            }
            serverState.waitForResponseSent (info, client);
            if(clientState.getPermissionCount()>0){
                IPCLogger.reportPermissionUsage (clientState);
                clientState.clearPermissions ();
            }
            clientState.recordResponseReceived(client, server, info);
        }
}
