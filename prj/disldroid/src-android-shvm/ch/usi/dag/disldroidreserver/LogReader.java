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
import java.util.Scanner;

import ch.usi.dag.disldroidreserver.reqdispatch.RequestDispatcher;


public class LogReader extends Thread {
    static HashMap <String, LogPos> db = new HashMap <String, LogPos> ();


    public static void main (final String [] args) {
        (new LogReader ()).start ();
    }


    static class AnalysisThread extends Thread {
        LogPos pos;


        public AnalysisThread (final LogPos pos) {
            this.pos = pos;
        }


        @Override
        public void run () {
            while (true) {
                if (pos.containUnAnalyzed ()) {
//                    System.out.println (pos.pid
//                        + " status: " + pos.getBytesAnalyzed () + " / "
//                        + pos.getBytesRead ());
                    try {
                        final ByteBuffer bf = pos.leftToByteBuffer ();
                        final int oldPos = bf.position ();
                        final int processID = bf.getInt ();
                        // System.out.print (processID);
                        final byte requestNo = bf.get ();
                        // System.out.println (" - " + requestNo);

                        try {
                            final boolean ret = RequestDispatcher.dispatch (
                                processID, requestNo, bf, DiSLREServer.debug);
                            pos.addBytesAnalyzed (bf.position () - oldPos);
                            // System.out.println(pos.id+" status: "+pos.bytesAnalyzed+" / "+pos.bytesRead);
                            if (ret) {
                                // db.remove ();
                                return;
                            }
                        } catch (final Exception e) {
                            try {
                                Thread.sleep (5000);
                            } catch (final InterruptedException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace ();
                            }
                            // e.printStackTrace ();
                        }
                    } catch (final BufferUnderflowException e) {
                        try {
                            Thread.sleep (5000);
                        } catch (final InterruptedException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace ();
                        }
                        // e.printStackTrace ();
                    }
                } else {
                    try {
                        Thread.sleep (100);
                    } catch (final InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace ();
                    }
                }

            }
            // while (true) {
            // for (final String key : db.keySet ()) {
            // final LogPos pos = db.get (key);
            // System.out.println(key +
            // " status: "+pos.bytesAnalyzed+" / "+pos.bytesRead);
            // if (pos.bytesAnalyzed < pos.bytesRead) {
            // try{
            // final ByteBuffer bf = ByteBuffer.wrap (
            // pos.content.toByteArray (),
            // pos.bytesAnalyzed,
            // pos.bytesRead-pos.bytesAnalyzed);
            // final int processID = bf.getInt ();
            // System.out.print (processID);
            // final byte requestNo = bf.get ();
            // System.out.println (" - " + requestNo);
            //
            // try {
            // final boolean ret = RequestDispatcher.dispatch (
            // processID, requestNo, bf, DiSLREServer.debug);
            // pos.bytesAnalyzed += bf.position ();
            // if(ret) {
            // db.remove (key);
            // continue;
            // }
            // } catch (final Exception e) {
            // e.printStackTrace ();
            // }
            // }catch(final BufferUnderflowException e){
            // e.printStackTrace ();
            // }
            // }
            // }
            // }
        }
    }
//
//
//    static void test () {
//        final LogPos pos = new LogPos (0, "test");
//        //readFileFrom (new File ("test"), pos);
//        try {
//            Thread.sleep (5000);
//        } catch (final InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace ();
//        }
//        readFileFrom (new File ("test"), pos);
//        // System.out.println (new String (pos.content.toByteArray ()));
//    }


    @Override
    public void start () {
        final File folder0 = new File (
            "/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log");

        if (true) {

            for (final File f : folder0.listFiles ()) {
                if (f.isDirectory ()) {
                    for (final File tablef : f.listFiles ())
                    {
                        tablef.delete ();
                    }
                } else {
                    f.delete ();
                }
            }
        }

        while (true) {
            if (true) {
                try {
                    // System.out.println ("pulling");
                    final List <String> commands = new ArrayList <String> ();
                    commands.add ("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                    commands.add ("pull");
                    commands.add ("/data/data/shadowvmlog/table");
                    commands.add ("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log/table");
                    final ProcessBuilder pb = new ProcessBuilder (commands);
                    pb.start ();
                } catch (final Exception e1) {
                    e1.printStackTrace ();
                }
            }
            try {
                Thread.sleep (1000);
            } catch (final InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace ();
            }
            final File folder1 = new File (
            "/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log/table");
            for (final File fileEntry : folder1.listFiles ()) {
                if (!fileEntry.isDirectory ()) {
                    final String fname = fileEntry.getName ();
                    final String [] arr = fname.split ("_");
                    final int pid = Integer.parseInt (arr [0]);
                    final String pname = arr [1];
                    LogPos pos = null;
                    if (!db.containsKey (arr [0])) {
                        pos = new LogPos (pid, pname);
                        db.put (arr [0], pos);
                        (new AnalysisThread (pos)).start ();
                    }
                    pos = db.get (arr [0]);
                    try {
                        final Scanner s = new Scanner (fileEntry);
                        while (s.hasNextLine ()) {
                            final String thisline = s.nextLine ();
                            final String [] detail = thisline.split (" ");
                            final int idx = Integer.parseInt (detail [0]);
                            final int size = Integer.parseInt (detail [1]);
                            pos.updateIdxInfo (idx, size);
                        }
                    } catch (final Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace ();
                    }
                    final int idx = pos.getCurrentIdx ();
                    final int sizeShould = pos.getSizeOfIdx (idx);
                    final File theFile = new File ("svm-log/svmlog_"
                    + pid + "_" + pname + "_" + idx);
                    if(theFile.length () < sizeShould){
                        try {
                            // System.out.println ("pulling");
                            final List <String> commands = new ArrayList <String> ();
                            commands.add ("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                            commands.add ("pull");
                            commands.add ("/data/data/shadowvmlog/svmlog_"
                                + pid + "_" + pname + "_" + idx);
                            commands.add ("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/svm-log/");
                            final ProcessBuilder pb = new ProcessBuilder (commands);
                            pb.start ();
                        } catch (final Exception e1) {
                            e1.printStackTrace ();
                        }
                        try {
                            Thread.sleep (1000);
                        } catch (final InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace ();
                        }
                    }
                    if(theFile.exists ()){
                        readFileFrom (theFile, pos, idx);
                    }
                }
            }
        }
    }


    static void readFileFrom (final File file, final LogPos pos, final int idx) {
        try {
            final RandomAccessFile raf = new RandomAccessFile (file, "r");
            final FileChannel fc = raf.getChannel ();
            final ByteBuffer buffer = ByteBuffer.allocate (1024); // initialize
                                                                  // somewhere
            while (raf.length () > pos.getBytesRead () - pos.getBytesBefore(idx)) {
                fc.position (pos.getBytesRead () - pos.getBytesBefore(idx));
                buffer.clear ();
                final int numOfBytesRead = fc.read (buffer); // read data into
                                                             // buffer
                final byte [] data = buffer.array ();
                pos.write (data, 0, numOfBytesRead);
                //System.out.println ("Read "+numOfBytesRead+" from "+file.getName ()+" "+pos.getBytesRead ()+" "+pos.getBytesAnalyzed ());
                // if (DiSLREServer.debug) {
                // System.out.println ("Read "
                // + numOfBytesRead + " from " + file.getName ());
                // }
                // pos.bytesRead += numOfBytesRead;
            }
            fc.close ();
            raf.close ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
    }
}
