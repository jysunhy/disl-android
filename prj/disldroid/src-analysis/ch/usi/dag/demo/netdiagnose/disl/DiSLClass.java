package ch.usi.dag.demo.netdiagnose.disl;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysisStub;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class DiSLClass {
//
//    @Before(marker=BodyMarker.class, scope="com.squareup.okhttp.*.*")
//    public static void okhttp_before(final MethodStaticContext msc){
//        AREDispatch.NativeLog ("Entering"+msc.thisMethodFullName ());
//    }
//
//    @After(marker=BodyMarker.class, scope="com.squareup.okhttp.*.*")
//    public static void okhttp_after(final MethodStaticContext msc){
//        AREDispatch.NativeLog ("Leaving"+msc.thisMethodFullName ());
//    }

    @AfterReturning(marker=BodyMarker.class,
    order = 1,
    scope="libcore.io.IoBridge.connect(java.io.FileDescriptor,java.net.InetAddress,int,int)")
    public static void network_connect(final ArgumentProcessorContext apc, final DynamicContext dc){
        //connect(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs)
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        final FileDescriptor fd = (FileDescriptor)args[0];
        final InetAddress address = (InetAddress)args[1];
        final int port = (int)args[2];
        final int timeoutMs = (int) args[3];
        final boolean successful = dc.getStackValue (0, boolean.class);
        NetworkAnalysisStub.newConnection (fd, address, port, timeoutMs, successful);
    }

    @AfterReturning(marker=BodyMarker.class,
    order = 1,
    scope="libcore.io.IoBridge.sendto(java.io.FileDescriptor,byte[],int,int,int,java.net.InetAddress,int)")
    public static void sendto(final ArgumentProcessorContext apc, final DynamicContext dc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        //(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port)
        final FileDescriptor fd = (FileDescriptor)args[0];
        final byte[] buffer = (byte[])args[1];
        final int byteOffset = (int)args[2];
        final int byteCount = (int)args[3];
        final int flags  = (int)args[4];
        final InetAddress address = (InetAddress)args[5];
        final int port = (int)args[6];
        final int sentSize = dc.getStackValue (0, int.class);
        //AREDispatch.NativeLog ("Num to send: "+byteCount+"; Num sent: "+sentSize);
        //[byteOffset, byteOffset+sentSize)
        if(sentSize > 0) {
            NetworkAnalysisStub.sendMessage (fd, buffer, byteOffset, sentSize, flags, address, port);
        }else {
            NetworkAnalysisStub.sendMessageFailed(fd, buffer, 0, buffer.length, flags, address, port);
        }
    }

//    @SyntheticLocal
//    static int oldPosition;

    @Before(marker=BodyMarker.class, scope="libcore.io.IoBridge.sendto(java.io.FileDescriptor,java.nio.ByteBuffer,int,java.net.InetAddress,int)")
    public static void sendto_bytebuffer_before(final ArgumentProcessorContext apc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        //(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port)
        final ByteBuffer buffer = (ByteBuffer)args[1];
        //oldPosition = buffer.position ();
    }

    @AfterReturning(marker=BodyMarker.class,
    order = 1,
    scope="libcore.io.IoBridge.sendto(java.io.FileDescriptor,java.nio.ByteBuffer,int,java.net.InetAddress,int)")
    public static void sendto_bytebuffer(final ArgumentProcessorContext apc, final DynamicContext dc){
        final Object [] args = apc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        //(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port)
        final FileDescriptor fd = (FileDescriptor)args[0];
        final ByteBuffer buffer = (ByteBuffer)args[1];
        final int flags  = (int)args[2];
        final InetAddress address = (InetAddress)args[3];
        final int port = (int)args[4];
        final int sentSize = dc.getStackValue (0, int.class);
        //[buffer.position()-sentSize, buffer.position())
        //AREDispatch.NativeLog ("old position:"+oldPosition+" new position:"+buffer.position ()+" ret val:"+sentSize);
        if(sentSize > 0) {
            NetworkAnalysisStub.sendMessage (fd, buffer.array (), buffer.position () - sentSize, sentSize, flags, address, port);
        }else {
            NetworkAnalysisStub.sendMessageFailed (fd, buffer.array (), 0, buffer.position (), flags, address, port);
        }
    }
}
