package ch.usi.dag.disldroidreserver.msg.ipc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;


public class IPCHandler implements RequestHandler {

    List <IPCEventRecord> events_time_ordered = new ArrayList <> ();

    DealingThread thread;

    public IPCHandler () {
        thread = new DealingThread (this);
        thread.run ();
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
            insert_into_time_ordered (newEvent);
            thread.newEvent (newEvent);
        } catch (final Exception e) {
            throw new DiSLREServerException ("Error in handle IPC events");
        }
    }


    void insert_into_time_ordered (final IPCEventRecord newrecord) {
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


    public class DealingThread extends Thread {
        IPCHandler handler;

        List <IPCEventRecord> pending_list = new ArrayList <> ();
        List <IPCEventRecord> long_waiting_list = new ArrayList <> ();

        Lock mtx;

        public DealingThread (final IPCHandler _handler) {
            handler = _handler;
            // TODO Auto-generated constructor stub
        }

        public synchronized void  newEvent(final IPCEventRecord event){
            pending_list.add (event);
        }

        boolean isEnd = false;
        boolean finished = false;
        public void endThread(){
            isEnd = true;
            while(!finished) {
                try {
                    System.out.println ("Waiting for the rest events to be dealt");
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run () {
          while(!isEnd){
              for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
                  analysis.ipcEventProcessed (null);
              }
          }
        }
    }


}
