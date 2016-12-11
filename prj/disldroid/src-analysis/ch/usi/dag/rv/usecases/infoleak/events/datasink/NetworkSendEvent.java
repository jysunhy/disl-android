package ch.usi.dag.rv.usecases.infoleak.events.datasink;

import java.io.FileDescriptor;
import java.net.InetAddress;

public class NetworkSendEvent extends DataSinkEvent{
    public NetworkSendEvent (
        final FileDescriptor fd, final byte [] buffer, final int byteOffset,
        final int sentSize, final int flags, final InetAddress address, final int port) {
        super(
            "Network Send "+(fd==null?"":fd.toString ())+"-"
            +(address==null?"":address.toString ())
            +":"+port, buffer, byteOffset, sentSize);
    }
}
