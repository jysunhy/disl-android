package ch.usi.dag.disldroidserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class USBForwarder {

    public static void combineAnalysis(final String analysisServerIP, final int analysisPort, final int localPort){
        try{
            final Socket sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);
            final DataInputStream isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));;


            final Socket sockToAnalysis = new Socket(InetAddress.getByName (analysisServerIP), analysisPort);
            final DataOutputStream osAnalysis = new DataOutputStream(new BufferedOutputStream(sockToAnalysis.getOutputStream()));;


                final byte []buf = new byte[1024];
                //int total = 0;
                while(true){
                    final int len = isAndroid.read (buf);
                    //total+=len;
                    if(len < 0) {
                        break;
                    }
                    osAnalysis.write (buf, 0, len);
                }

        }catch(final Exception e){
            e.printStackTrace ();
        }
    }

    public static void combineInstrumentation(final String instrServerIP, final int instrPort, final int localPort){
        try{
            final Socket sockToAndroid = new Socket(InetAddress.getByName ("127.0.0.1"), localPort);
            final DataOutputStream osAndroid = new DataOutputStream(new BufferedOutputStream(sockToAndroid.getOutputStream()));;
            final DataInputStream isAndroid = new DataInputStream(new BufferedInputStream(sockToAndroid.getInputStream()));;



            while(true){
                final Socket sockToInstr = new Socket(InetAddress.getByName (instrServerIP), instrPort);
                final DataOutputStream osInstr = new DataOutputStream(new BufferedOutputStream(sockToInstr.getOutputStream()));;
                final DataInputStream isInstr = new DataInputStream(new BufferedInputStream(sockToInstr.getInputStream()));;

                int dexNameLength = isAndroid.readInt();
                osInstr.writeInt(dexNameLength);
                byte[] dexName = new byte[dexNameLength];
                isAndroid.readFully (dexName);
                osInstr.write(dexName);
                int dexLength = isAndroid.readInt ();
                osInstr.writeInt(dexLength);
                byte[] bytecode = new byte[dexLength];
                isAndroid.readFully (bytecode);
                osInstr.write(bytecode);
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

                sockToInstr.close ();
                osInstr.close ();
                isInstr.close ();
            }
        } catch(final Exception e){
            e.printStackTrace ();

        }
    }

    public static void main (final String [] args) {
        // TODO Auto-generated method stub
        (new Thread(new Thread1())).start ();
        (new Thread(new Thread2())).start ();
        (new Thread(new Thread3())).start ();
    }
    static class Thread1 implements Runnable{
        @Override
        public void run () {
            // TODO Auto-generated method stub
            USBForwarder.combineInstrumentation ("127.0.0.1", 6667, 6100);
        }
    }
    static class Thread2 implements Runnable{
        @Override
        public void run () {
            // TODO Auto-generated method stub
            USBForwarder.combineAnalysis ("127.0.0.1", 6668, 6101);
        }
    }
    static class Thread3 implements Runnable{
        @Override
        public void run () {
            // TODO Auto-generated method stub
            USBForwarder.combineAnalysis ("127.0.0.1", 6668, 6102);
        }
    }
}

