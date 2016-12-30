package ch.usi.dag.disldroidserver;

import java.io.File;
import java.util.List;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.RemoteFile;

public class TestJADB {
    public static void main(final String args[]){
        try {
            final JadbConnection jadb = new JadbConnection();
            final List<JadbDevice> devices = jadb.getDevices ();
            while(devices.isEmpty ()){
                Thread.sleep (1000);
            }
            final JadbDevice device = devices.get (0);
            final List <RemoteFile> fl = device.list ("/data/data/disl/dex/");
            final int idx = 0;
            for(final RemoteFile f : fl) {
                System.out.println(f.getPath ());
                if(!f.getPath ().startsWith (".")) {
                    final File lf = new File ("testjadb"+idx);
                    device.pull (new RemoteFile ("/data/data/disl/dex/"+f.getPath ()), lf);
                    device.push (lf, new RemoteFile ("/data/data/disl/dex/"+f.getPath ()+"-push"));
                }
            }
        }catch (final Exception e){
            e.printStackTrace ();
        }
    }
}
