package ch.usi.dag.ipc.analysis;

import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.msg.ipc.IPCTransaction;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.ipc.analysis.lib.DetailedPermissionAlert;
import ch.usi.dag.ipc.analysis.lib.IPCGraph;


public class ICCAnalysis extends RemoteAnalysis {

	public void permission_used(final Context context, final int tid, final ShadowString info){
	    System.out.println ("Permission "+info.toString ()+" is used in ("+context.pid ()+tid+":)");
	    final IPCTransaction curTransaction = context.getCurrentThreadTransaction (context.pid (), tid);
	    if(curTransaction != null) {
            System.out.println ("relative transactoin is ");
        }
	    //
	    DetailedPermissionAlert.update(null, info.toString ());
	}

	public void boundary_start(final Context context, final int tid, final ShadowString boundaryName){
	    DetailedPermissionAlert.push(context.pid (), tid, boundaryName.toString ());
	}

	public void boundary_end(final Context context, final int tid, final ShadowString boundaryName){
	    DetailedPermissionAlert.pop(context.pid (), tid, boundaryName.toString ());
	}

    @Override
    public void ipcEventProcessed (final IPCEventRecord event) {
        //add node to graphiz via IPCGraph
        if(isEventInterested (event)) {
            IPCGraph.AddEvent (event);
        }
    }

    private boolean isEventInterested(final IPCEventRecord event){
        return true;
    }


    @Override
    public void atExit (final Context context) {
    }


    @Override
    public void objectFree (
            final Context context, final ShadowObject netRef) {
    }


}
