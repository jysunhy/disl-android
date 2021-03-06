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
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;


public class Context {

    int processID;

    InetAddress address;

    String pname;

    boolean dead = false;

    ConcurrentHashMap <String, Object> states = new ConcurrentHashMap <String, Object> ();

    public <T> T getState (final String key, final Class <T> type) {
        if(states.containsKey (key)) {
            return type.cast (states.get (key));
        } else {
            return null;
        }
    }


    public synchronized void setState (final String key, final Object store) {
        states.put (key, store);
    }


    public synchronized Object setStateIfAbsent (final String key, final Object state) {
        Object res = states.putIfAbsent (key, state);
        if(res == null) {
            res = state;
        }
        return res;
    }

    public int getProcessID () {
        return processID;
    }


    public void setProcessID (final int processID) {
        this.processID = processID;
    }


    public InetAddress getAddress () {
        return address;
    }


    public void setAddress (final InetAddress address) {
        this.address = address;
    }

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


    HashMap<String, ClassNode> clazzCache = new HashMap<String, ClassNode>();

    public ClassNode getClassNodeFor(final String classSignature){
        if(clazzCache.containsKey (classSignature)) {
            return clazzCache.get (classSignature);
        }

        byte[] classCode = null;

        String classFullName = classSignature.replace('/', '.');
        if(classFullName.startsWith("L")) {
            classFullName = classFullName.substring(1);
        }
        if(classFullName.endsWith(";")) {
            classFullName = classFullName.substring(0, classFullName.length()-1);
        }

        try {
            final Socket socket = new Socket(InetAddress.getByName (System.getProperty ("dislserver.ip", "127.0.0.1")), Integer.getInteger("dislserver.port", 6667));
            DataOutputStream os;
            DataInputStream is;

            os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            os.writeInt(classFullName.length ());

            os.write(classFullName.getBytes ());
            os.writeInt(0);
            os.flush();
            final int controlLength = is.readInt();

            final byte[] control = new byte[controlLength];

            is.readFully(control);
            final int classCodeLength = is.readInt();
            classCode = new byte[classCodeLength];
            is.readFully(classCode);
            os.close ();
            is.close ();
            socket.close ();
        } catch (final UnknownHostException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final ClassReader classReader = new ClassReader(classCode);
        final ClassNode classNode = new ClassNode(Opcodes.ASM4);
        classReader.accept(classNode, ClassReader.SKIP_DEBUG
                | ClassReader.EXPAND_FRAMES);
        clazzCache.put (classSignature, classNode);
        return classNode;
    }
}
