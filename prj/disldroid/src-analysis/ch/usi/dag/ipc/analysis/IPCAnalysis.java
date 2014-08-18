package ch.usi.dag.ipc.analysis;

import java.util.List;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteIPCAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.ipc.analysis.lib.BinderEvent;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.RequestRecvdEvent;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.RequestSentEvent;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.ResponseRecvdEvent;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.ResponseSentEvent;
import ch.usi.dag.ipc.analysis.lib.IPCLogger;
import ch.usi.dag.ipc.analysis.lib.ThreadState;


public class IPCAnalysis extends RemoteIPCAnalysis {

    public void boundaryStart (
        final Context ctx, final int tid, final ShadowString boundaryName) {
        final ThreadState state = ThreadState.get (ctx, tid);
        state.pushBoundary (boundaryName.toString ());
    }

    public void boundaryEnd (
        final Context ctx, final int tid, final ShadowString boundaryName) {
        final ThreadState state = ThreadState.get (ctx, tid);
        state.popBoundary (boundaryName.toString ());
    }

    public void permissionUsed (
        final Context ctx, final int tid, final ShadowString permissionName) {

        if(permissionName.equals ("android.permission.WAKE_LOCK")) {
            return;
        }

        IPCLogger.debug ("PERMISSION", permissionName.toString ()+ " in "+ctx.pid ()+":"+tid);
        final List<ThreadState> callers = ThreadState.getCallers (ctx, tid);
        IPCLogger.debug ("PERMISSION","callers "+callers.size ());
        int cnt = 0;
        for(final ThreadState caller:callers){
            IPCLogger.debug ("PERMISSION","caller "+(cnt++)+caller.toString ());
            caller.addPermission(permissionName.toString ());
        }
    }

    @Override
    public void onRequestSent (
        final TransactionInfo info, final NativeThread client, final Context ctx) {
        final BinderEvent event = new RequestSentEvent (client, info);
        final ThreadState clientState = ThreadState.get (client);
        clientState.addEvent (event);
    }

    @Override
    public void onRequestReceived (
        final TransactionInfo info, final NativeThread client,
        final NativeThread server, final Context ctx) {
        final ThreadState clientState = ThreadState.get (client);
        //IPCLogger.write (LoggerType.DEBUG, client.toString ()+" "+info.toString ()+" "+server.toString ()+" onRequestReceived");
        clientState.waitForEvent (info);
        final BinderEvent event = new RequestRecvdEvent (client, server, info);
        final ThreadState serverState = ThreadState.get (server);
        serverState.addEvent (event);
    }

    @Override
    public void onResponseSent (
        final TransactionInfo info, final NativeThread client, final NativeThread server, final Context ctx) {
            final BinderEvent event = new ResponseSentEvent(client, server, info);
            final ThreadState serverState = ThreadState.get (server);
            serverState.addEvent (event);
    }

    @Override
    public void onResponseReceived (
        final TransactionInfo info, final NativeThread client,
        final NativeThread server, final Context ctx) {
            final ThreadState serverState = ThreadState.get (server);
            //IPCLogger.write (LoggerType.DEBUG, client.toString ()+" "+info.toString ()+" "+server.toString ()+" onResponseReceived");
            serverState.waitForEvent (info, client);
            final BinderEvent event = new ResponseRecvdEvent(client, server, info);
            final ThreadState clientState = ThreadState.get (client);
            if(clientState.getPermissionCount()>0){
                //IPCLogger.info ("Result", "Report Event:"+event.toString ());
                clientState.printPermission();
                clientState.printStack();
                clientState.clearPermissions ();
            }
            clientState.addEvent (event);
        }


}
