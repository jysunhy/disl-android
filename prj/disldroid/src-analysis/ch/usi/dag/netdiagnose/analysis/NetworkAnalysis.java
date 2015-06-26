package ch.usi.dag.netdiagnose.analysis;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Base64;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;


public class NetworkAnalysis extends RemoteAnalysis {

    static String PREDEFINED_USERNAME = "username";
    static String PREDEFINED_PASSWORD = "password";

    static class ConnectionStruct{
        public ConnectionStruct (final int fdHash, final String address, final int port) {
            this.fdHash = fdHash;
            this.address = address;
            this.port = port;
            bf = ByteBuffer.allocate (1024);
        }
        public void dumpConnectionInfo(){
            System.out.println (new StringBuilder(address).append(":").append (port).append ("-").append (fdHash));
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

        public boolean matchPlainUsernamePassword(final String str){
            if(str == null || str.equals ("")) {
                return false;
            }
            return indexOf(bf.array (), str.getBytes ())  >= 0;
        }
        public static ConnectionStruct initConnectionIfAbsent(final ProcessProfiler processProfile, final int fdHash, final String address, final int port){
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
            ProcessProfiler processProfile = ctx.getState (ProcessProfiler.class);
            if (processProfile == null) {
                final ProcessProfiler temp = new ProcessProfiler (ctx.getProcessID ());
                processProfile = (ProcessProfiler) ctx.setStateIfAbsent (temp);
                if(processProfile == null) {
                    processProfile = temp;
                }
            }
            return processProfile;
        }
    }

    public static void newConnection (final Context ctx,
        final int fdHash, final ShadowString address, final int port, final int timeoutMs, final boolean successful) {
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent (ctx);
        final ConnectionStruct connection = ConnectionStruct.initConnectionIfAbsent (processProfile, fdHash, address.toString (), port);
    }

    public static void sendMessage (final Context ctx, final int fdHash, final ShadowString dataBase64, final int flags, final ShadowString address, final int port){
        final ProcessProfiler processProfile = ProcessProfiler.initProfilerIfAbsent(ctx);
        final ConnectionStruct connection = ConnectionStruct.initConnectionIfAbsent (processProfile, fdHash, address.toString (), port);
        connection.addNewData (dataBase64.toString ());
        if(connection.matchPlainUsernamePassword(PREDEFINED_PASSWORD)){
            connection.dumpConnectionInfo ();
        }
    }


    public static int indexOf(final byte[] outerArray, final byte[] smallerArray) {
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
               if (outerArray[i+j] != smallerArray[j]) {
                   found = false;
                   break;
               }
            }
            if (found) {
                return i;
            }
         }
       return -1;
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
