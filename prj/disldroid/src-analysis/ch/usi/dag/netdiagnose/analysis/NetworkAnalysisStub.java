package ch.usi.dag.netdiagnose.analysis;

import java.io.FileDescriptor;
import java.net.InetAddress;

import android.util.Base64;
import ch.usi.dag.dislre.AREDispatch;

public class NetworkAnalysisStub {

    public static short NEW_CONNECTION = AREDispatch.registerMethod ("ch.usi.dag.netdiagnose.analysis.NetworkAnalysis.newConnection");
    public static short SEND_MESSAGE = AREDispatch.registerMethod ("ch.usi.dag.netdiagnose.analysis.NetworkAnalysis.sendMessage");
    public static void newConnection (
        final FileDescriptor fd, final InetAddress inetAddress, final int port, final int timeoutMs, final boolean successful) {
        AREDispatch.analysisStart (NEW_CONNECTION);
        AREDispatch.sendInt(fd.hashCode ());
        if(inetAddress != null) {
            AREDispatch.sendObjectPlusData (inetAddress.getHostAddress ());
        } else {
            AREDispatch.sendObjectPlusData (null);
        }
        AREDispatch.sendInt(port);
        AREDispatch.sendInt(timeoutMs);
        AREDispatch.sendBoolean (successful);
        AREDispatch.analysisEnd ();
        AREDispatch.NativeLog ("new connection - fd:"+fd.hashCode ()+" "+inetAddress.getHostAddress ()+":"+port+" successful?"+successful);
    }

    public static void sendMessage (final FileDescriptor fd, final byte[] buffer, final int start, final int length, final int flags, final InetAddress inetAddress, final int port) {
        if(length <= 0) {
            return;
        }
        AREDispatch.analysisStart (SEND_MESSAGE);
        AREDispatch.sendInt(fd.hashCode ());
        AREDispatch.sendObjectPlusData (Base64.encodeToString (buffer, start, length, Base64.DEFAULT));
        AREDispatch.sendInt(flags);
        if(inetAddress != null) {
            AREDispatch.sendObjectPlusData (inetAddress.getHostAddress ());
        } else {
            AREDispatch.sendObjectPlusData (null);
        }
        AREDispatch.sendInt(port);
        AREDispatch.analysisEnd ();
        AREDispatch.NativeLog ("send data - fd:"+fd.hashCode ()+" "+(inetAddress==null?"":inetAddress.getHostAddress ())+":"+port+" data-length:"+length);
    }

}
