package ch.usi.dag.demo.netdiagnose.analysis;

import java.io.FileDescriptor;
import java.net.InetAddress;

import android.util.Base64;
import ch.usi.dag.dislre.AREDispatch;

public class NetworkAnalysisStub {

    public static short BIND = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.bind");
    public static short NEW_CONNECTION = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.newConnection");
    public static short SEND_MESSAGE = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.sendMessage");
    public static short SEND_MESSAGE_FAILED = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.sendMessageFailed");
    public static short RECV_MESSAGE = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.recvMessage");
    public static short RECV_MESSAGE_FAILED = AREDispatch.registerMethod ("ch.usi.dag.demo.netdiagnose.analysis.NetworkAnalysis.recvMessageFailed");


    public static void bind (final FileDescriptor fd, final InetAddress inetAddress, final int port) {
        AREDispatch.analysisStart (BIND);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.sendInt(port);
        AREDispatch.analysisEnd ();
    }

    public static void fileopen(final String path){
        AREDispatch.analysisStart (BIND);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.sendInt(-1);
        AREDispatch.sendString (path);
        AREDispatch.sendInt(-1);
        AREDispatch.analysisEnd ();
    }

    public static void newConnection (
        final FileDescriptor fd, final InetAddress inetAddress, final int port, final int timeoutMs, final boolean successful) {
        AREDispatch.analysisStart (NEW_CONNECTION);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.sendInt(port);
        AREDispatch.sendInt(timeoutMs);
        AREDispatch.sendBoolean (successful);
        AREDispatch.analysisEnd ();
    }

    public static void sendMessage (final FileDescriptor fd, final byte[] buffer, final int start, final int length, final int flags, final InetAddress inetAddress, final int port) {
        if(length <= 0) {
            return;
        }
        AREDispatch.analysisStart (SEND_MESSAGE);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        if(buffer == null) {
            AREDispatch.sendString("");
        } else{
            AREDispatch.sendString (Base64.encodeToString (buffer, start, length, Base64.DEFAULT));
        }
        AREDispatch.sendInt(flags);
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.sendInt(port);
        AREDispatch.analysisEnd ();
    }

    public static void sendMessageFailed (final FileDescriptor fd, final byte[] buffer, final int start, final int length, final int flags, final InetAddress inetAddress, final int port) {
        if(length <= 0) {
            return;
        }
        AREDispatch.analysisStart (SEND_MESSAGE_FAILED);
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        if(buffer == null) {
            AREDispatch.sendString("");
        } else{
            AREDispatch.sendString (Base64.encodeToString (buffer, start, length, Base64.DEFAULT));
        }
        AREDispatch.sendInt(flags);
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.sendInt(port);
        AREDispatch.analysisEnd ();
    }

    public static void recvMessage (final FileDescriptor fd, final byte[] buffer, final int start, final int length, final int flags, final InetAddress inetAddress, final int port) {
        if(length <= 0) {
            return;
        }
        AREDispatch.NativeLog ("still fine here 0");
        AREDispatch.analysisStart (RECV_MESSAGE);
        AREDispatch.NativeLog ("still fine here");
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        AREDispatch.NativeLog ("still fine here 2");
        if(buffer == null) {
            AREDispatch.sendString("");
        } else{
            AREDispatch.sendString (Base64.encodeToString (buffer, start, length, Base64.DEFAULT));
        }
        AREDispatch.NativeLog ("still fine here 3");
        AREDispatch.sendInt(flags);
        AREDispatch.NativeLog ("still fine here 4");
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.NativeLog ("still fine here 5");
        AREDispatch.sendInt(port);
        AREDispatch.analysisEnd ();
    }
    public static void recvMessageFailed (final FileDescriptor fd, final byte[] buffer, final int start, final int length, final int flags, final InetAddress inetAddress, final int port) {
        if(length <= 0) {
            return;
        }
        AREDispatch.NativeLog ("still fine here 0");
        AREDispatch.analysisStart (RECV_MESSAGE_FAILED);
        AREDispatch.NativeLog ("still fine here 1");
        AREDispatch.sendInt (AREDispatch.getThisThreadId ());
        AREDispatch.NativeLog ("still fine here 2");
        if(fd == null){
            AREDispatch.sendInt(0);
        }else {
            AREDispatch.sendInt(fd.hashCode ());
        }
        AREDispatch.NativeLog ("still fine here 3");
        AREDispatch.sendString("");
        AREDispatch.NativeLog ("still fine here 4");
        AREDispatch.sendInt(flags);
        AREDispatch.NativeLog ("still fine here 5");
        if(inetAddress != null) {
            final String a = inetAddress.getHostAddress ();
            AREDispatch.sendString (a==null?"":a);
        } else {
            AREDispatch.sendString ("");
        }
        AREDispatch.NativeLog ("still fine here 6");
        AREDispatch.sendInt(port);
        AREDispatch.NativeLog ("still fine here 7");
        AREDispatch.analysisEnd ();
        AREDispatch.NativeLog ("still fine here 8");
    }

}
