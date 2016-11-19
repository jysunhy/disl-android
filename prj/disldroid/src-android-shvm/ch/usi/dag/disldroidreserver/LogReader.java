package ch.usi.dag.disldroidreserver;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.usi.dag.disldroidreserver.reqdispatch.RequestDispatcher;

public class LogReader extends Thread{
    static HashMap <String, LogPos> db = new HashMap <String, LogPos> ();

    public static void main (final String [] args) {
        (new LogReader ()).start ();
    }


    static class AnalysisThread extends Thread {
        LogPos pos;
        public AnalysisThread(final LogPos pos){
            this.pos = pos;
        }
        @Override
        public void run () {
            while(true){
                if (pos.containUnAnalyzed ()) {
                    //System.out.println(pos.id+" status: "+pos.bytesAnalyzed+" / "+pos.bytesRead);
                    try{
                        final ByteBuffer bf = pos.leftToByteBuffer ();
                        final int processID = bf.getInt ();
                        //System.out.print (processID);
                        final byte requestNo = bf.get ();
                        //System.out.println (" - " + requestNo);

                        try {
                            final boolean ret = RequestDispatcher.dispatch (
                                processID, requestNo, bf, DiSLREServer.debug);
                                pos.setBytesAnalyzed (bf.position ());
//                                System.out.println(pos.id+" status: "+pos.bytesAnalyzed+" / "+pos.bytesRead);
                            if(ret) {
                                //db.remove ();
                                return;
                            }
                        } catch (final Exception e) {
                            e.printStackTrace ();
                        }
                    }catch(final BufferUnderflowException e){
                        e.printStackTrace ();
                    }
                }else {
                    try {
                        Thread.sleep (1000);
                    } catch (final InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
//            while (true) {
//                for (final String key : db.keySet ()) {
//                    final LogPos pos = db.get (key);
//                    System.out.println(key + " status: "+pos.bytesAnalyzed+" / "+pos.bytesRead);
//                    if (pos.bytesAnalyzed < pos.bytesRead) {
//                        try{
//                            final ByteBuffer bf = ByteBuffer.wrap (
//                                pos.content.toByteArray (),
//                                pos.bytesAnalyzed,
//                                pos.bytesRead-pos.bytesAnalyzed);
//                            final int processID = bf.getInt ();
//                            System.out.print (processID);
//                            final byte requestNo = bf.get ();
//                            System.out.println (" - " + requestNo);
//
//                            try {
//                                final boolean ret = RequestDispatcher.dispatch (
//                                    processID, requestNo, bf, DiSLREServer.debug);
//                                    pos.bytesAnalyzed += bf.position ();
//                                if(ret) {
//                                    db.remove (key);
//                                    continue;
//                                }
//                            } catch (final Exception e) {
//                                e.printStackTrace ();
//                            }
//                        }catch(final BufferUnderflowException e){
//                            e.printStackTrace ();
//                        }
//                    }
//                }
//            }
        }
    }


    static void test () {
        final LogPos pos = new LogPos ("test");
        readFileFrom (new File ("test"), pos);
        try {
            Thread.sleep (5000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
        readFileFrom (new File ("test"), pos);
        //System.out.println (new String (pos.content.toByteArray ()));
    }


    @Override
    public
    void start () {
        final File folder0 = new File (
            "/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log");
        if(true) {
            for (final File f : folder0.listFiles ()) {
                f.delete ();
            }
        }
        while (true) {
            if(true) {
                try {
                    //System.out.println ("pulling");
                    final List <String> commands = new ArrayList <String> ();
                    commands.add ("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                    commands.add ("pull");
                    commands.add ("/data/data/shadowvmlog");
                    commands.add ("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log");
                    final ProcessBuilder pb = new ProcessBuilder (commands);
                    pb.start ();
                } catch (final Exception e1) {
                    e1.printStackTrace ();
                }
            }
            try {
                Thread.sleep (2000);
            } catch (final InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace ();
            }
            for (final File fileEntry : folder0.listFiles ()) {
                if (!fileEntry.isDirectory ()) {
                    final String fname = fileEntry.getName ();
                    final String [] arr = fname.split ("_");
                    if (!db.containsKey (fname)) {
                        final LogPos pos = new LogPos (arr[1]);
                        db.put (fname, pos);
                        (new AnalysisThread (pos)).start();
                    }
                    final LogPos pos = db.get (fname);
                    readFileFrom (fileEntry, pos);
                }
            }
        }
    }


    static void readFileFrom (final File file, final LogPos pos) {
        try {
            final RandomAccessFile raf = new RandomAccessFile (file, "r");
            final FileChannel fc = raf.getChannel ();
            final ByteBuffer buffer = ByteBuffer.allocate (1024); // initialize
                                                                  // somewhere
            while (raf.length () > pos.getBytesRead ()) {
                fc.position (pos.getBytesRead ());
                buffer.clear ();
                final int numOfBytesRead = fc.read (buffer); // read data into
                                                             // buffer
                final byte [] data = buffer.array ();
                pos.write (data, 0, numOfBytesRead);
//                if (DiSLREServer.debug) {
//                    System.out.println ("Read "
//                        + numOfBytesRead + " from " + file.getName ());
//                }
                //pos.bytesRead += numOfBytesRead;
            }
            fc.close ();
            raf.close ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
    }
}
