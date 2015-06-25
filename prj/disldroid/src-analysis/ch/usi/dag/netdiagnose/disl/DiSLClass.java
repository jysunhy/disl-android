package ch.usi.dag.netdiagnose.disl;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.netdiagnose.analysis.NetworkAnalysisStub;

public class DiSLClass {

    @AfterReturning(marker=BytecodeMarker.class, guard=Guard.IoBridge_connect.class, args = "invokestatic")
    public static void network_connect(final ArgumentProcessorContext apc, final DynamicContext dc){
        //connect(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs)
        final Object [] args = apc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        final FileDescriptor fd = (FileDescriptor)args[0];
        final InetAddress address = (InetAddress)args[1];
        final int port = (int)args[2];
        final int timeoutMs = (int) args[3];
        final boolean successful = dc.getStackValue (0, boolean.class);
        NetworkAnalysisStub.newConnection (fd, address, port, timeoutMs, successful);
    }

    @AfterReturning(marker=BytecodeMarker.class, guard=Guard.IoBridge_sendto.class, args = "invokestatic")
    public static void sendto(final ArgumentProcessorContext apc, final DynamicContext dc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        //(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port)
        final FileDescriptor fd = (FileDescriptor)args[0];
        final byte[] buffer = (byte[])args[1];
        final int byteOffset = (int)args[2];
        //final int byteCount = (int)args[3];
        final int flags  = (int)args[4];
        final InetAddress address = (InetAddress)args[5];
        final int port = (int)args[6];
        final int sentSize = dc.getStackValue (0, int.class);
        //[byteOffset, byteOffset+sentSize)
        NetworkAnalysisStub.sendMessage (fd, buffer, byteOffset, sentSize, flags, address, port);
    }


    @AfterReturning(marker=BytecodeMarker.class, guard=Guard.IoBridge_sendto_bytebuffer.class, args = "invokestatic")
    public static void sendto_bytebuffer(final ArgumentProcessorContext apc, final DynamicContext dc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        //(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port)
        final FileDescriptor fd = (FileDescriptor)args[0];
        final ByteBuffer buffer = (ByteBuffer)args[1];
        final int flags  = (int)args[2];
        final InetAddress address = (InetAddress)args[3];
        final int port = (int)args[4];
        final int sentSize = dc.getStackValue (0, int.class);
        //[buffer.position()-sentSize, buffer.position())
        NetworkAnalysisStub.sendMessage (fd, buffer.array (), buffer.position () - sentSize, sentSize, flags, address, port);
    }
}