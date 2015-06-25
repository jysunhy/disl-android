package ch.usi.dag.netdiagnose.disl;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class InvocationFilterContext extends MethodStaticContext {
    private final String CLASSNAME = "libcore/io/IoBridge";

	public MethodInsnNode getInvocationNode () {
		final StringBuilder builder = new StringBuilder ();
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof MethodInsnNode) {
		    return (MethodInsnNode)instruction;
		}

		return null;
	}

	public boolean isConnect(){
	    final MethodInsnNode min = getInvocationNode ();
	    if(min == null) {
            return false;
        }
	    if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
	    if(!min.name.equals ("connect")) {
            return false;
        }
        if(!min.desc.equals ("(Ljava/io/FileDescriptor;Ljava/net/InetAddress;II)Z")) {
            return false;
        }
        System.out.println ("Connecting in "+thisMethodFullName ());
        return true;
	}

	public boolean isSendTo(){
        final MethodInsnNode min = getInvocationNode ();
        if(min == null) {
            return false;
        }
        if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
        if(!min.name.equals ("sendto")) {
            return false;
        }
        if(!min.desc.equals ("(Ljava/io/FileDescriptor;[BIIILjava/net/InetAddress;I)I")) {
            return false;
        }
        System.out.println ("Sending in "+thisMethodFullName ());
        return true;
    }

	public boolean isSendToByteBuffer(){
        final MethodInsnNode min = getInvocationNode ();
        if(min == null) {
            return false;
        }
        if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
        if(!min.name.equals ("sendto")) {
            return false;
        }
        if(!min.desc.equals ("(Ljava/io/FileDescriptor;Ljava/nio/ByteBuffer;ILjava/net/InetAddress;I)I")) {
            return false;
        }
        System.out.println ("Sending in "+thisMethodFullName ());
        return true;
    }
}


/*
 * DiSL: skipping unaffected method: libcore/io/IoBridge.<init>(()V)
DiSL: skipping unaffected method: libcore/io/IoBridge.available((Ljava/io/FileDescriptor;)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.bind((Ljava/io/FileDescriptor;Ljava/net/InetAddress;I)V)
DiSL: skipping unaffected method: libcore/io/IoBridge.booleanFromInt((I)Z)
DiSL: skipping unaffected method: libcore/io/IoBridge.booleanToInt((Z)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.closeSocket((Ljava/io/FileDescriptor;)V)
DiSL: skipping unaffected method: libcore/io/IoBridge.connect((Ljava/io/FileDescriptor;Ljava/net/InetAddress;I)Z)
DiSL: skipping unaffected method: libcore/io/IoBridge.connect((Ljava/io/FileDescriptor;Ljava/net/InetAddress;II)Z)
DiSL: skipping unaffected method: libcore/io/IoBridge.connectDetail((Ljava/net/InetAddress;IILlibcore/io/ErrnoException;)Ljava/lang/String;)
DiSL: skipping unaffected method: libcore/io/IoBridge.connectErrno((Ljava/io/FileDescriptor;Ljava/net/InetAddress;II)Z)
DiSL: skipping unaffected method: libcore/io/IoBridge.getSocketLocalAddress((Ljava/io/FileDescriptor;)Ljava/net/InetAddress;)
DiSL: skipping unaffected method: libcore/io/IoBridge.getSocketLocalPort((Ljava/io/FileDescriptor;)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.getSocketOption((Ljava/io/FileDescriptor;I)Ljava/lang/Object;)
DiSL: skipping unaffected method: libcore/io/IoBridge.getSocketOptionErrno((Ljava/io/FileDescriptor;I)Ljava/lang/Object;)
DiSL: skipping unaffected method: libcore/io/IoBridge.isConnected((Ljava/io/FileDescriptor;Ljava/net/InetAddress;III)Z)
DiSL: skipping unaffected method: libcore/io/IoBridge.maybeThrowAfterRecvfrom((ZZLlibcore/io/ErrnoException;)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.maybeThrowAfterSendto((ZLlibcore/io/ErrnoException;)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.open((Ljava/lang/String;I)Ljava/io/FileDescriptor;)
DiSL: skipping unaffected method: libcore/io/IoBridge.postRecvfrom((ZLjava/net/DatagramPacket;ZLjava/net/InetSocketAddress;I)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.read((Ljava/io/FileDescriptor;[BII)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.recvfrom((ZLjava/io/FileDescriptor;Ljava/nio/ByteBuffer;ILjava/net/DatagramPacket;Z)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.recvfrom((ZLjava/io/FileDescriptor;[BIIILjava/net/DatagramPacket;Z)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.sendto((Ljava/io/FileDescriptor;Ljava/nio/ByteBuffer;ILjava/net/InetAddress;I)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.sendto((Ljava/io/FileDescriptor;[BIIILjava/net/InetAddress;I)I)
DiSL: skipping unaffected method: libcore/io/IoBridge.setSocketOption((Ljava/io/FileDescriptor;ILjava/lang/Object;)V)
DiSL: skipping unaffected method: libcore/io/IoBridge.setSocketOptionErrno((Ljava/io/FileDescriptor;ILjava/lang/Object;)V)
DiSL: skipping unaffected method: libcore/io/IoBridge.socket((Z)Ljava/io/FileDescriptor;)
DiSL: skipping unaffected method: libcore/io/IoBridge.write((Ljava/io/FileDescriptor;[BII)V)
 */
