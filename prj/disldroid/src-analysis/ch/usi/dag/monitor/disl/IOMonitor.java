package ch.usi.dag.monitor.disl;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import libcore.io.IoBridge;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.dislre.AREDispatch;


public class IOMonitor {
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_bind.class, args = "invokestatic")
    public static void libcore_io_IoBridge_bind (
        final FileDescriptor fd, final InetAddress address, final int port)
    throws SocketException {
        AREDispatch.NativeLog ("Detection of #bind# fd: "+fd.toString ()+ " address "+address.getHostAddress ()+ " port: "+port);
        libcore.io.IoBridge.bind (fd, address, port);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_connect.class, args = "invokestatic")
    public static boolean libcore_io_IoBridge_connect (
        final FileDescriptor fd, final InetAddress inetAddress, final int port,
        final int timeoutMs) throws SocketException, SocketTimeoutException {
        AREDispatch.NativeLog ("Detection of #connect#"+inetAddress.toString ()+":"+port);
        return IoBridge.connect (fd, inetAddress, port, timeoutMs);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_open.class, args = "invokestatic")
    public static FileDescriptor libcore_io_IoBridge_open (
        final String path, final int flags) throws FileNotFoundException {
        final FileDescriptor res = IoBridge.open (path, flags);
        AREDispatch.NativeLog ("Detection of #open# path:"+path+" opened Fd: "+res.toString ());
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_read.class, args = "invokestatic")
    public static int libcore_io_IoBridge_read (
        final FileDescriptor fd, final byte [] bytes, final int byteOffset,
        final int byteCount) throws IOException {
        final int res = IoBridge.read (fd,  bytes,  byteOffset, byteCount);
        AREDispatch.NativeLog ("Detection of #read# fd:"+fd.toString ()+" read size:"+res);
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_write.class, args = "invokestatic")
    public static void libcore_io_IoBridge_write (
        final FileDescriptor fd, final byte [] bytes, final int byteOffset,
        final int byteCount) throws IOException {
        AREDispatch.NativeLog ("Detection of #write# fd:"+fd.toString ()+" to write size:"+ byteCount);
        IoBridge.write (fd,  bytes,  byteOffset, byteCount);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_sendto.class, args = "invokestatic")
    public static int libcore_io_IoBridge_sendto (
        final FileDescriptor fd, final byte [] bytes, final int byteOffset,
        final int byteCount, final int flags, final InetAddress inetAddress,
        final int port) throws IOException {
        AREDispatch.NativeLog ("Detection of #sendto# fd:"+fd.toString ()+" address:"+inetAddress+" port:"+port);
        return IoBridge.sendto (fd, bytes, byteOffset, byteCount, flags, inetAddress, port);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_sendto_2.class, args = "invokestatic")
    public static int libcore_io_IoBridge_sendto (
        final FileDescriptor fd, final ByteBuffer buffer, final int flags,
        final InetAddress inetAddress, final int port) throws IOException {
        AREDispatch.NativeLog ("Detection of #sendto# fd:"+fd.toString ()+" address:"+inetAddress+" port:"+port);
        return IoBridge.sendto (fd, buffer, flags, inetAddress, port);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_recvfrom.class, args = "invokestatic")
    public static int libcore_io_IoBridge_recvfrom (
        final boolean isRead, final FileDescriptor fd, final byte [] bytes,
        final int byteOffset, final int byteCount, final int flags,
        final DatagramPacket packet, final boolean isConnected) throws IOException {
        AREDispatch.NativeLog ("Detection of #recvfrom# fd:"+fd.toString ());
        return IoBridge.recvfrom (isRead, fd, bytes, byteOffset, byteCount, flags, packet, isConnected);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.IOGuard.IoBridge_recvfrom_2.class, args = "invokestatic")
    public static int libcore_io_IoBridge_recvfrom (
        final boolean isRead, final FileDescriptor fd, final ByteBuffer buffer,
        final int flags, final DatagramPacket packet, final boolean isConnected)
    throws IOException {
        AREDispatch.NativeLog ("Detection of #recvfrom# fd:"+fd.toString ());
        return IoBridge.recvfrom (isRead, fd, buffer, flags,  packet, isConnected);
    }

    /*public static InetAddress libcore_io_IoBridge_getSocketLocalAddress (
        final FileDescriptor fd) {
        return null;
    }

    public static int libcore_io_IoBridge_getSocketLocalPort (final FileDescriptor fd) {
        return 0;
    }*/

}
