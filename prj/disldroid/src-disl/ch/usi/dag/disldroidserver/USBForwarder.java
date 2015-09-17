package ch.usi.dag.disldroidserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class USBForwarder {

    public static void combineAnalysis(final String analysisServerIP, final int analysisPort, final int localPort){
        try{
            DataInputStream isAndroid = null;

            final HashMap<Integer, DataOutputStream> connectionMap = new HashMap <Integer, DataOutputStream> ();
            while(true){
//                Socket sockToAndroid = null;
//                if(isAndroid == null){
//                    sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);
//                    sockToAndroid.setSoTimeout (10000);
//                    isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));
//                }

                final Socket sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);
                sockToAndroid.setSoTimeout (20000);
                isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));
                int pid;
                try {
                    pid = isAndroid.readInt ();
                }catch(final SocketTimeoutException e){
                    if(sockToAndroid != null) {
                        sockToAndroid.close ();
                    }
                    isAndroid.close ();
                    isAndroid = null;
                    System.out.println("Wait analysis connection starts");
                    continue;
                }catch(final Exception e){
                    e.printStackTrace ();
                    if(sockToAndroid != null) {
                        sockToAndroid.close ();
                    }
                    isAndroid.close ();
                    isAndroid = null;
                    return;
                }
                final FileOutputStream fos = new FileOutputStream (new File("debug_events."+pid), true);
                if(!connectionMap.containsKey (pid)){
                    try{
                        final Socket sockToAnalysis = new Socket(InetAddress.getByName (analysisServerIP), analysisPort);
                        final DataOutputStream osAnalysis = new DataOutputStream(new BufferedOutputStream(sockToAnalysis.getOutputStream()));
                        connectionMap.put (pid, osAnalysis);
                    }catch(final Exception e){
                        connectionMap.put(pid, null);
                    }
                }
                DataOutputStream osAnalysis = connectionMap.get (pid);

                final byte []buf = new byte[BUFFER_SIZE];
                final int total = isAndroid.readInt ();
                int readLen = 0;
                try {

                    while(readLen < total){
                        final int len = isAndroid.read (buf);
                        readLen += len;
                        try {
                            if(osAnalysis != null){
                                osAnalysis.write (buf, 0, len);
                                osAnalysis.flush ();
                                fos.write (buf, 0, len);
                                fos.flush ();
                            }
                        }catch (final Exception e){
                            System.err.println (pid+" errors");
                            connectionMap.put (pid, null);
                            osAnalysis = null;
                        }
                        //System.out.println ("Transmitting "+len+" bytes of data from pid "+pid + "of "+total);
                    }
                }catch(final Exception e){
                    e.printStackTrace ();
                }
                fos.close ();
            }
        }catch(final Exception e){
            e.printStackTrace ();
        }
    }

    public static void echoDex(final int localPort){
        String info = "";
        try{
            while(true){
                try{
                    final Socket sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);

                    sockToAndroid.setSoTimeout (20000);

                    final DataOutputStream osAndroid = new DataOutputStream(new BufferedOutputStream(sockToAndroid.getOutputStream()));
                    final DataInputStream isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));

                    int dexNameLength;
                    System.out.println ("New dex");
                    try {
                        dexNameLength = isAndroid.readInt();
                    }catch (final SocketTimeoutException e){
                        sockToAndroid.close ();
                        osAndroid.close ();
                        isAndroid.close ();
                        System.out.println ("timeout reconnect");
                        continue;
                    }
                    System.out.println ("Name Length "+dexNameLength);
                    final byte[] dexName = new byte[dexNameLength];
                    isAndroid.readFully (dexName);

                    info = new String(dexName);
                    System.out.println ("Name "+ info);
                    //final String name = new String(dexName);

                    final int dexLength = isAndroid.readInt ();

                    System.out.println ("Dex length "+ dexLength);

                    final byte[] bytecode = new byte[dexLength];
                    isAndroid.readFully (bytecode);

                    System.out.println ("Dex all read");

                    //dexNameLength = 0;
                    osAndroid.writeInt(dexNameLength);
                    //dexName = new byte[dexNameLength];
                    osAndroid.write(dexName);

                    osAndroid.writeInt(dexLength);
                    osAndroid.write(bytecode);
                    osAndroid.flush ();
                    try{
                        sockToAndroid.close ();
                        osAndroid.close ();
                        isAndroid.close ();
                    }catch(final Exception e ){

                    }
                }catch(final Exception e){
                    e.printStackTrace ();
                }
            }

        } catch(final Exception e){
            System.err.println (info);
            e.printStackTrace ();
        }

    }


    public static void combineInstrumentation(final String instrServerIP, final int instrPort, final int localPort){
        String info = "";
        try{
            while(true){
                try{
                final Socket sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);
                sockToAndroid.setSoTimeout (20000);
                final DataOutputStream osAndroid = new DataOutputStream(new BufferedOutputStream(sockToAndroid.getOutputStream()));
                final DataInputStream isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));


                int dexNameLength;
                System.out.println ("New dex");
                try {
                    dexNameLength = isAndroid.readInt();
                }catch (final SocketTimeoutException e){
                    sockToAndroid.close ();
                    osAndroid.close ();
                    isAndroid.close ();
                    System.out.println ("timeout reconnect");
                    Thread.sleep(1000);
                    continue;
                }catch (final Exception e){
                    e.printStackTrace ();
                    sockToAndroid.close ();
                    osAndroid.close ();
                    isAndroid.close ();
                    Thread.sleep(1000);
                    continue;
                }
                final Socket sockToInstr = new Socket(InetAddress.getByName (instrServerIP), instrPort);
                final DataOutputStream osInstr = new DataOutputStream(new BufferedOutputStream(sockToInstr.getOutputStream()));;
                final DataInputStream isInstr = new DataInputStream(new BufferedInputStream(sockToInstr.getInputStream()));;


                System.out.println ("Name Length "+dexNameLength);
                osInstr.writeInt(dexNameLength);
                byte[] dexName = new byte[dexNameLength];
                isAndroid.readFully (dexName);

                info = new String(dexName);
                System.out.println ("Name "+ info);

                osInstr.write(dexName);
                int dexLength = isAndroid.readInt ();

                System.out.println ("Dex length "+ dexLength);

                osInstr.writeInt(dexLength);
                byte[] bytecode = new byte[dexLength];
                isAndroid.readFully (bytecode);

                System.out.println ("Dex all read");

                osInstr.write(bytecode);

                osInstr.flush ();


                dexNameLength = isInstr.readInt();
                osAndroid.writeInt(dexNameLength);
                dexName = new byte[dexNameLength];
                isInstr.readFully (dexName);
                osAndroid.write(dexName);
                dexLength = isInstr.readInt ();
                osAndroid.writeInt(dexLength);
                bytecode = new byte[dexLength];
                isInstr.readFully (bytecode);
                osAndroid.write(bytecode);

                osAndroid.flush ();
                System.out.println ("Dex all written");
                sockToAndroid.close ();
                osAndroid.close ();
                isAndroid.close ();
                sockToInstr.close ();
                osInstr.close();
                isInstr.close ();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        } catch(final Exception e){
            System.err.println (info);
            e.printStackTrace ();
        }
    }

    public static void main (final String [] args) {
        // TODO Auto-generated method stub
        (new Thread(new InstrThread())).start ();
        if(args.length == 0 || !args[0].equals ("--instrument-only")){
            (new Thread(new AnalysisThread())).start ();
        }

    }
    static class InstrThread implements Runnable{
        @Override
        public void run () {
            // TODO Auto-generated method stub
            USBForwarder.combineInstrumentation ("127.0.0.1", 6667, 6100);
            //USBForwarder.echoDex (6100);
        }
    }
    static class AnalysisThread implements Runnable{
        @Override
        public void run () {
            // TODO Auto-generated method stub
            USBForwarder.combineAnalysis ("127.0.0.1", 6668, 6101);
        }
    }

    static int BUFFER_SIZE = 32*1024;
}

