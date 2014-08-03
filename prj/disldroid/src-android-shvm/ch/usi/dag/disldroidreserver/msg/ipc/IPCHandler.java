package ch.usi.dag.disldroidreserver.msg.ipc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;


public class IPCHandler implements RequestHandler {

    public static List <IPCEventRecord> events_time_ordered = new ArrayList <> ();
    public static List <IPCEventRecord> events_receivetime_ordered = new ArrayList <> ();
    //DealingThread thread;
    final AnalysisHandler analysisHandler;

    public IPCHandler (final AnalysisHandler anlHndl) {
        //thread = new DealingThread (this);
        //thread.run ();
        analysisHandler = anlHndl;
    }

    public synchronized static List<IPCEventRecord> getInvolvedEvents(final int pid, final int tid, final long timestamp){
        List<IPCEventRecord> result = new ArrayList <IPCEventRecord>();
        final int size = events_time_ordered.size ();
        for(int i = size-1; i >=0;i--){
            final IPCEventRecord event =  events_time_ordered.get (i);
            if(event.timestamp >= timestamp) {
                continue;
            }
            if(event.to.pid == pid && event.to.tid == tid){

                if(event.phase == 0 || event.phase == 3)
                {
                    //not related to this thread
                    continue;
                }

                if(event.phase == 2) {
                    break;
                }
                if(event.oneway)
                {
                    break;
                }
                result = getInvolvedEvents (event.from.pid, event.from.tid, event.timestamp);
                result.add(event);
            }else if(event.from.pid == pid && event.from.tid == tid){
                //possible children
            }
        }
        return result;
    }

    // pid tid(int) transactionid(int) type(short) pid2 tid2 time(long) boolean(oneway)
    @Override
    public void handle (
        final int pid, final DataInputStream is, final DataOutputStream os,
        final boolean debug)
    throws DiSLREServerException {
        // TODO Auto-generated method stub
        try {
            final int tid = is.readInt ();
            final int transaction_id = is.readInt ();
            final short type = is.readShort ();
            final int pid2 = is.readInt ();
            final int tid2 = is.readInt ();
            final long timestamp = is.readLong ();
            final boolean oneway = is.readBoolean ();
            IPCEventRecord newEvent = new IPCEventRecord();
            switch (type) {
            case 0:
                newEvent= (new IPCEventRecord (
                     pid, tid,transaction_id ,type, -1, -1, timestamp,oneway));
                break;
            case 1:
                newEvent= (new IPCEventRecord (
                    pid2, tid2,transaction_id ,type, pid, tid, timestamp,oneway));
                break;
            case 2:
                newEvent= (new IPCEventRecord (
                    pid2, tid2,transaction_id ,type, pid, tid, timestamp,oneway));
                break;
            case 3:
                newEvent= (new IPCEventRecord (
                    pid, tid,transaction_id ,type, pid2, tid2, timestamp,oneway));
                break;
            default:
                break;
            }
            analysisHandler.ipcOccurred (ShadowAddressSpace.getShadowAddressSpace (pid), tid, newEvent);
            events_receivetime_ordered.add (newEvent);
            insert_into_time_ordered (newEvent);
            //thread.newEvent (newEvent);
        } catch (final Exception e) {
            e.printStackTrace ();
            throw new DiSLREServerException ("Error in handle IPC events");
        }
    }


    synchronized void insert_into_time_ordered (final IPCEventRecord newrecord) {
        // change to half search
        for (int i = events_time_ordered.size () - 1; i >= 0; i--) {
            if (events_time_ordered.get (i).timestamp < newrecord.timestamp) {
                events_time_ordered.add (i + 1, newrecord);
                break;
            }
        }
    }




    @Override
    public void exit () {
        // TODO Auto-generated method stub

    }
//
//
//    public class DealingThread extends Thread {
//        IPCHandler handler;
//
//        List <IPCEventRecord> pending_list = new ArrayList <> ();
//        List <IPCEventRecord> long_waiting_list = new ArrayList <> ();
//
//        Lock mtx;
//
//        public DealingThread (final IPCHandler _handler) {
//            handler = _handler;
//            // TODO Auto-generated constructor stub
//        }
//
//        public synchronized void  newEvent(final IPCEventRecord event){
//            pending_list.add (event);
//        }
//
//        boolean isEnd = false;
//        boolean finished = false;
//        public void endThread(){
//            isEnd = true;
//            while(!finished) {
//                try {
//                    System.out.println ("Waiting for the rest events to be dealt");
//                    Thread.sleep(1000);
//                } catch (final InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void run () {
//          while(!isEnd){
//              for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
//                  analysis.ipcEventProcessed (null);
//              }
//          }
//        }
//    }


}
