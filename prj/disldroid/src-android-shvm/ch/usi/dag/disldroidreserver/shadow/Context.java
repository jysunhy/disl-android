package ch.usi.dag.disldroidreserver.shadow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public static byte[] getByteCodeFor(final String classFullName){
        byte[] classCode = null;
        try {
            final Socket socket = new Socket(InetAddress.getByName (System.getProperty ("dislserver.ip", "127.0.0.1")), Integer.getInteger("dislserver.port", 6666));
            DataOutputStream os;
            DataInputStream is;

            os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            os.writeInt(classFullName.length ());

            os.write(classFullName.getBytes ());
            os.writeInt(0);
            //os.write(nm.getClassCode());
            os.flush();
            final int controlLength = is.readInt();

            // allocate buffer for class reading
            final byte[] control = new byte[controlLength];

            // read class
            is.readFully(control);
            final int classCodeLength = is.readInt();
            classCode = new byte[classCodeLength];
            is.readFully(classCode);
            os.close ();
            is.close ();
            socket.close ();
        } catch (final UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return classCode;
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
