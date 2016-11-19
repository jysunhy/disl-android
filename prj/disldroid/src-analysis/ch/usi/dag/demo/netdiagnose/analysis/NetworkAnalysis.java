package ch.usi.dag.demo.netdiagnose.analysis;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Base64;
import ch.usi.dag.demo.callstack.analysis.SVMCallStack;
import ch.usi.dag.demo.logging.WebLogger;
import ch.usi.dag.demo.utils.DemoUtils;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public class NetworkAnalysis extends RemoteAnalysis {

    static class ConnectionStruct{
        public ConnectionStruct (final int fdHash, final String address, final int port) {
            this.fdHash = fdHash;
            this.address = address;
            this.port = port;
            bf = ByteBuffer.allocate (1024);
        }
        public ByteBuffer addNewData(final byte[] data){
            while(bf.remaining () < data.length) {
                bf = bf.duplicate ();
            }
            bf.put (data);
            return bf;
        }
        public ByteBuffer addNewData(final String base64){
            final byte data[] = Base64.decode (base64.toString (), Base64.DEFAULT);
            while(bf.remaining () < data.length) {
                bf = bf.duplicate ();
            }
            bf.put (data);
            return bf;
        }
        public boolean searchInSentData(final String str){
            if(str == null || str.equals ("")) {
                return false;
            }
            return DemoUtils.indexOf(bf.array (), str.getBytes ())  >= 0;
        }
        public static ConnectionStruct initConnectionIfAbsent(
            final ProcessProfiler processProfile, final int fdHash, final String address, final int port){
            ConnectionStruct connection = processProfile.get (fdHash);
            if(connection == null){
                final ConnectionStruct temp = new ConnectionStruct(fdHash, address, port);
                connection = processProfile.putIfAbsent (fdHash, temp);
                if(connection == null) {
                    connection = temp;
                }
            }
            return connection;
        }
        ByteBuffer bf;
        int fdHash;
        String address;
        int port;
    }

    static class ProcessProfiler extends ConcurrentHashMap <Integer, ConnectionStruct> {
        public ProcessProfiler (final int pid) {
            this.pid = pid;
        }
        int pid;
        public static ProcessProfiler initProfilerIfAbsent(final Context ctx){
            ProcessProfiler processProfile = ctx.getState ("Net", ProcessProfiler.class);
            if (processProfile == null) {
                final ProcessProfiler temp = new ProcessProfiler (ctx.getProcessID ());
                processProfile = (ProcessProfiler) ctx.setStateIfAbsent ("Net", temp);
                if(processProfile == null) {
                    processProfile = temp;
                }
            }
            return processProfile;
        }
    }

    private static final String PREDEFINED_KEYWORD = "NONE";

    public static void bind (final Context ctx, final int tid,
        final int fdHash, final String address, final int port) {
        WebLogger.reportNetworkBind (
            ctx.getProcessID (), ctx.getPname (), tid, fdHash,
            address==null?"Unknown":address.toString (), port, SVMCallStack.get (ctx, tid).getRuntimeStack ());
    }


    public static void newConnection (final Context ctx, final int tid,
        final int fdHash, final String address, final int port, final int timeoutMs, final boolean successful) {
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent (ctx);
        ConnectionStruct.initConnectionIfAbsent (processProfile, fdHash, address.toString (), port);
        WebLogger.reportNetworkConnect (
            ctx.getProcessID (), ctx.getPname (), tid, fdHash,
            address==null?"Unknown":address.toString (), port, SVMCallStack.get (ctx, tid).getRuntimeStack ());
    }

    public static void sendMessage (final Context ctx, final int tid, final int fdHash,
        final String dataBase64, final int flags, final String address, final int port){
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent(ctx);
        final ConnectionStruct connection = ConnectionStruct.initConnectionIfAbsent (
            processProfile, fdHash, address==null?"Unknown ":address.toString (), port);
        connection.addNewData (dataBase64.toString ());
        if(connection.searchInSentData(PREDEFINED_KEYWORD)){
            //report
        }
        WebLogger.reportNetworkSend (
            ctx.getProcessID (), ctx.getPname (), tid, fdHash,
            connection.address, connection.port, Base64.decode (dataBase64.toString (), Base64.DEFAULT),
            SVMCallStack.get (ctx, tid).getRuntimeStack ());
    }

    public static void sendMessageFailed (final Context ctx, final int tid, final int fdHash,
        final String dataBase64, final int flags, final String address, final int port){
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent(ctx);
        final ConnectionStruct connection = ConnectionStruct.initConnectionIfAbsent (
            processProfile, fdHash, address==null?"Unknown ":address.toString (), port);
        connection.addNewData (dataBase64.toString ());
    }

    public static void recvMessage (final Context ctx, final int tid, final int fdHash,
        final String dataBase64, final int flags, final String address, final int port){
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent(ctx);
        final ConnectionStruct connection = ConnectionStruct.initConnectionIfAbsent (
            processProfile, fdHash, address==null?"Unknown ":address.toString (), port);
        WebLogger.reportNetworkRecv(ctx.getProcessID (), ctx.getPname (), tid, fdHash,
            connection.address,connection.port, Base64.decode (dataBase64.toString (), Base64.DEFAULT),
            SVMCallStack.get (ctx, tid).getRuntimeStack ());
    }

    public static void recvMessageFailed (final Context ctx, final int tid, final int fdHash, final String dataBase64, final int flags, final String address, final int port){
    }

    @Override
    public void atExit (final Context context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }
}
