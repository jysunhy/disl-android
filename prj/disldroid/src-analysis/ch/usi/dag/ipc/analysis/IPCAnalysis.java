package ch.usi.dag.ipc.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.usi.dag.disldroidreserver.msg.ipc.DVMThread;
import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.ipc.analysis.lib.IPCGraph;
import ch.usi.dag.ipc.analysis.lib.PerThreadRuntimeStack;


public class IPCAnalysis extends RemoteAnalysis {

    HashMap <DVMThread, List<String>> permission_usage = new HashMap <DVMThread, List<String>>();

	public void permission_used(final Context context, final int tid, final long timestamp, final ShadowString info){
	    System.out.println ("Permission "+info.toString ()+" is used in ("+context.pid ()+tid+":)");
	    final List<DVMThread> involvedThreads = context.getInvovedThreads(tid, timestamp);
	    if(involvedThreads != null ){
    	    for(final DVMThread thd : involvedThreads)
    	    {
    	        //notify thread of the permission usage
    	        List<String> permission_strs = permission_usage.get (thd);
    	        if(permission_strs == null) {
                    permission_strs = new ArrayList <String>();
                    permission_usage.put (thd, permission_strs);
                }
    	        permission_strs.add (info.toString ());
    	    }
	    }
	}

	public void boundary_start(final Context context, final int tid, final ShadowString boundaryName){
	    PerThreadRuntimeStack.boundary_start(context.pid (), tid, boundaryName.toString ());
	}

	public void boundary_end(final Context context, final int tid, final ShadowString boundaryName){
	    PerThreadRuntimeStack.boundary_end(context.pid (), tid, boundaryName.toString ());
	}

    @Override
    public void ipcEventProcessed (final Context context, final long threadid, final IPCEventRecord event) {
        //add node to graphiz via IPCGraph
        System.out.println ("event received in analysis");
        if(isEventInterested (context, threadid, event)) {
            IPCGraph.AddEvent (event);//Select interested IPC event for drawing IPC graph
        }
        if(event.phase == 3) {//the last event
            //check any permission leakage
            //if any
            //print stack of thread (pid,tid)
            final List<String> permissions = permission_usage.get (event.from);
            if(permissions!=null && permissions.size()>0){
                String permissoin_str="";
                for(final String p:permissions) {
                    permissoin_str = p+" "+permissoin_str;
                }
                System.out.println (permissoin_str);
                PerThreadRuntimeStack.printStack (context.pid (), (int)threadid);
                permission_usage.remove (event.from);
            }
        }
    }

    private boolean isEventInterested(final Context context, final long tid, final IPCEventRecord event){
        return isProcInterested (context);
    }

    private boolean isProcInterested(final Context context){
        String pname = context.getPname ();
        while(pname == null){
            pname = context.getPname ();
            System.out.println ("name info is not ready yet");
            try {
                Thread.sleep (1000);
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
            final String []interestedProcesses = {"com.android.contacts"};
            for(final String item:interestedProcesses){
                if(pname.equals (item)) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public void atExit (final Context context) {
        IPCGraph.GenerateGraphizScript ();
    }


    @Override
    public void objectFree (
            final Context context, final ShadowObject netRef) {
    }

    public static void main(final String args[]){
        final RemoteAnalysis a = new IPCAnalysis();
        try {
            a.getClass ().getMethod ("ipcEventProcessed", new Class[]{IPCEventRecord.class} );
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
