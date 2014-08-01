package ch.usi.dag.ipc.analysis;

import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.msg.ipc.IPCTransaction;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.ipc.analysis.lib.DetailedPermissionAlert;
import ch.usi.dag.ipc.analysis.lib.IPCGraph;


public class IPCAnalysis extends RemoteAnalysis {

	public void permission_used(final Context context, final int tid, final long timestamp, final ShadowString info){
	    System.out.println ("Permission "+info.toString ()+" is used in ("+context.pid ()+tid+":)");
	    final IPCTransaction curTransaction = context.getCurrentThreadTransaction (context.pid (), tid, timestamp);
	    if(curTransaction != null) {
            System.out.println ("relative transactoin is ");
        }
	    DetailedPermissionAlert.alert(null, info.toString ());
	}

	public void boundary_start(final Context context, final int tid, final ShadowString boundaryName){
	    DetailedPermissionAlert.boundary_start(context.pid (), tid, boundaryName.toString ());
	}

	public void boundary_end(final Context context, final int tid, final ShadowString boundaryName){
	    DetailedPermissionAlert.boundary_end(context.pid (), tid, boundaryName.toString ());
	}

    @Override
    public void ipcEventProcessed (final IPCEventRecord event) {
        //add node to graphiz via IPCGraph
        if(isEventInterested (event)) {
            IPCGraph.AddEvent (event);
        }
    }

    private boolean isEventInterested(final IPCEventRecord event){
        return isProcInterested (event.frompid) || isProcInterested (event.topid);
    }

    private boolean isProcInterested(final int pid){
        final String pname = ShadowAddressSpace.getShadowAddressSpace (pid).getContext ().getPname ();
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


}
