package ch.usi.dag.disldroidreserver.shadow;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.usi.dag.disldroidreserver.msg.ipc.DVMThread;
import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.msg.ipc.IPCHandler;


public class Context {

    int processID;

    InetAddress address;

    String pname;


    public Context (final int processID, final InetAddress address) {
        this.processID = processID;
        this.address = address;
        this.pname = null;
    }


    public int pid () {
        return processID;
    }


    public InetAddress getInetAddress () {
        return address;
    }


    public String getPname () {
        return pname;
    }


    public void setPname (final String pname) {
        this.pname = pname;
    }


    public Iterable <ShadowObject> getShadowObjectIterator () {
        return ShadowAddressSpace.getShadowAddressSpace (processID).getShadowObjectIterator ();
    }


    public static Context getContext (final int pid) {
        return ShadowAddressSpace.getShadowAddressSpace (pid).getContext ();
    }


    public static Collection <Context> getAllContext () {
        final Collection <ShadowAddressSpace> shadowAddressSpaces = ShadowAddressSpace.getAllShadowAddressSpace ();
        final ArrayList <Context> contexts = new ArrayList <Context> (
            shadowAddressSpaces.size ());

        for (final ShadowAddressSpace shadowAddressSpace : shadowAddressSpaces) {
            contexts.add (shadowAddressSpace.context);
        }

        return contexts;
    }


    public List <DVMThread> getInvovedThreads (final int tid, final long timestamp) {
        // TODO Auto-generated method stub
        final List<DVMThread> res = new ArrayList <DVMThread>();
        final List<IPCEventRecord> list = IPCHandler.getInvolvedEvents (pid(), tid, timestamp);
        for(final IPCEventRecord event : list) {
            res.add (event.from);
        }
        if(res.size ()==0) {
            return null;
        } else {
            return res;
        }
    }


//    public List <DVMThread> getInvovedThreads (final int tid, final long timestamp) {
//        // TODO Auto-generated method stub
//        return null;
//    }

//    public static List<IPCEventRecord> getLastEvents(final int pid, final int tid, final long timestamp){
//        return null;
//    }
}
