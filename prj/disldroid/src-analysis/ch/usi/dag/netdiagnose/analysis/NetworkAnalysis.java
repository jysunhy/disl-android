package ch.usi.dag.netdiagnose.analysis;

import android.util.Base64;
import ch.usi.dag.disldroidreserver.shadow.Context;


public class NetworkAnalysis {
    public static void newConnection (final Context ctx,
        final int fdHash, final String address, final int port, final int timeoutMs, final boolean successful) {
        if(successful) {
            System.out.println(ctx.getProcessID ()+":"+ctx.getPname ()+" new connection to "+address+":"+port);
        }else{
            System.out.println(ctx.getProcessID ()+":"+ctx.getPname ()+" failed connection to "+address+":"+port);
        }
    }

    public static void sendMessage (final Context ctx, final int fdHash, final String dataBase64, final int flags, final String address, final int port){
        final byte data[] = Base64.decode (dataBase64, Base64.DEFAULT);
        System.out.println(ctx.getProcessID ()+":"+ctx.getPname ()+" new data sent to "+address+":"+port+" byte length "+data.length);
    }
}
