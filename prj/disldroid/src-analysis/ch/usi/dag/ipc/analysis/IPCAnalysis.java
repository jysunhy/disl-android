package ch.usi.dag.ipc.analysis;

import java.util.HashMap;
import java.util.List;

import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.ipc.analysis.lib.IPCGraph;
import ch.usi.dag.ipc.analysis.lib.PerThreadRuntimeStack;


public class IPCAnalysis extends RemoteAnalysis {

    class Endpoint {
        int binderid;

        int transaction_id; // used to distinguish different transactions via a
                            // single binder

        boolean oneway;
    }


    static class Logger {
        public static void log (final String a) {

        }

    }


    abstract class IPCEvent {
        int type;

        Endpoint endpoint;

        NativeThread from;

        NativeThread to;
    }


    class RequestSentEvent extends IPCEvent {

        public RequestSentEvent (
            final Endpoint target, final NativeThread client, final Context ctx) {
            // TODO Auto-generated constructor stub
        }

    }


    class RequestReceivedEvent extends IPCEvent {

        public RequestReceivedEvent (
            final Endpoint target, final NativeThread client,
            final NativeThread server, final Context ctx) {
            // TODO Auto-generated constructor stub
        }

    }


    class ResponseSentEvent extends IPCEvent {

        public ResponseSentEvent (
            final Endpoint target, final NativeThread server, final Context ctx) {
            // TODO Auto-generated constructor stub
        }

    }


    class ResponseReceivedEvent extends IPCEvent {

        public ResponseReceivedEvent (
            final Endpoint target, final NativeThread server,
            final NativeThread client, final Context ctx) {
            // TODO Auto-generated constructor stub
        }

    }

    static class PermissionUsage {
        private static final long WAITING_TIME = 1000;

        static HashMap <NativeThread, List <String>> permission_usage = new HashMap <NativeThread, List <String>> ();

        static HashMap <NativeThread, List <IPCEvent>> event_map = new HashMap <NativeThread, List <IPCEvent>> ();

        public static void addEvent (final IPCEvent event) {
            waitEventBefore(event);
            if(event instanceof RequestSentEvent || event instanceof ResponseReceivedEvent) {
                event_map.get (event.from).add (event);
            } else {
                event_map.get (event.to).add (event);
            }
        }

        public static void notifyAllCaller (final NativeThread curThd, final String permission) {
            //get top event of the map
            final List<IPCEvent> list = event_map.get (curThd);
            final IPCEvent event = list.get (list.size()-1);
            notifyAllCaller(event, permission);
        }
        public static int getNumberOfPermissionUsage(final NativeThread thd){
            return permission_usage.get (thd).size ();
        }
        public static void printPermission(final NativeThread thd){
            for(final String permission : permission_usage.get (thd)) {
                Logger.log(permission);
            }
        }
        public static void clearPermissionUsage (final NativeThread thd) {
            permission_usage.remove (thd);
        }


        private static void waitEventBefore (final IPCEvent event) {
            waitAndGetLastEvent (event);
            return;
        }
        private static IPCEvent getLastEventNoWait(final IPCEvent event){
            if(event instanceof RequestSentEvent) {
                return null;
            }else if(event instanceof ResponseSentEvent){
                return getLastEventOfThread (event.to, event);
            }else {
                return getOtherEndEvent (event);
            }
        }

        private static IPCEvent waitAndGetLastEvent(final IPCEvent event){
            IPCEvent res=null;
            if(event instanceof RequestSentEvent) {
                return null;
            }
            while((res=getLastEventNoWait(event))==null){
                try {
                    Thread.sleep (WAITING_TIME);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return res;
        }

        private static void notifyCaller(final NativeThread thd, final String permission){
            permission_usage.get (thd).add (permission);
        }

        private static void notifyAllCaller(final IPCEvent event, final String permission){
            if(!(event instanceof RequestReceivedEvent) || event.endpoint.oneway) {
                return;
            }
            notifyCaller (event.from, permission);
            final IPCEvent cli_event =waitAndGetLastEvent (event);
            notifyAllCaller (getLastEventOfThread(event.from, cli_event), permission);
        }

        private static IPCEvent getOtherEndEvent (
            final IPCEvent event) {
            final NativeThread thd;
            if(event instanceof RequestSentEvent || event instanceof ResponseSentEvent) {
                thd = event.to;
            } else {
                thd = event.from;
            }
            final List<IPCEvent> event_list = event_map.get (thd);
            for(int i = 0; i < event_list.size (); i++)
            {
                if(event_list.get (i).endpoint.equals (event.endpoint)){
                    return event;
                }
            }
            return null;
        }

        private static IPCEvent getLastEventOfThread (
            final NativeThread thd, final IPCEvent event) {
            final List<IPCEvent> event_list = event_map.get (thd);
            for(int i = 0; i < event_list.size (); i++)
            {
                if(event_list.get (i) == event){
                    if(i>0) {
                        return event_list.get (i-1);
                    } else {
                        return null;
                    }
                }
            }
            return null;
        }


    }


    public void boundary_start (
        final NativeThread thd, final ShadowString boundaryName) {
        PerThreadRuntimeStack.boundary_start (
            thd, boundaryName.toString ());
    }


    public void boundary_end (
        final NativeThread thd,final ShadowString boundaryName) {
        PerThreadRuntimeStack.boundary_end (
            thd, boundaryName.toString ());
    }

    public void permission_used (
        final NativeThread thd, final ShadowString permissionName) {
        Logger.log (permissionName.toString ());
        PermissionUsage.notifyAllCaller (thd, permissionName.toString ());
    }

    public void onRequestSent (final Endpoint target,
        final NativeThread client, final Context ctx) {
        PermissionUsage.addEvent (new RequestSentEvent (target, client, ctx));
    }


    public void onRequestReceived (final Endpoint target,
        final NativeThread client, final NativeThread server, final Context ctx) {
        PermissionUsage.addEvent (new RequestReceivedEvent (
            target, client, server, ctx));
    }


    public void onResponseSent (final Endpoint target,
        final NativeThread server, final Context ctx) {
        PermissionUsage.addEvent (new ResponseSentEvent (target, server, ctx));
    }

    public void onResponseReceived (final Endpoint target,
        final NativeThread server, final NativeThread client, final Context ctx) {
        final ResponseReceivedEvent event = new ResponseReceivedEvent (
            target, server, client, ctx);
        if(PermissionUsage.getNumberOfPermissionUsage(client) > 0) {
            //Print all permission usage in client
            PermissionUsage.printPermission (client);
            //Print stack
            PerThreadRuntimeStack.printStack (client);
            PermissionUsage.clearPermissionUsage(client);
        }
        PermissionUsage.addEvent (event);
    }


    @Override
    public void ipcEventProcessed (
        final Context context, final long threadid, final IPCEventRecord event) {
    }

    @Override
    public void atExit (final Context context) {
        IPCGraph.GenerateGraphizScript ();
    }

    @Override
    public void objectFree (
        final Context context, final ShadowObject netRef) {
    }

}
