package ch.usi.dag.disldroidreserver.shadow;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;


public class Context {

    int processID;

    InetAddress address;

    String pname;

    boolean dead = false;


    public boolean isDead () {
        return dead;
    }


    public void setDead (final boolean dead) {
        this.dead = dead;
    }


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


//    public List <NativeThread> getInvovedThreads (final int tid, final long timestamp) {
//        // TODO Auto-generated method stub
//        final List<NativeThread> res = new ArrayList <NativeThread>();
//        final List<IPCEventRecord> list = IPCHandler.getInvolvedEvents (pid(), tid, timestamp);
//        for(final IPCEventRecord event : list) {
//            res.add (event.from);
//        }
//        if(res.size ()==0) {
//            return null;
//        } else {
//            return res;
//        }
//    }

//    public List<IPCEventRecord> getEventsOfSameTransactin(final IPCEventRecord event){
//        final List<IPCEventRecord> res = IPCHandler.getEventsOfSameTransactin (event);
//        return res;
//    }
}
