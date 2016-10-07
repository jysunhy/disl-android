package ch.usi.dag.disldroidserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;


public class Utils {
    static public byte [] readbytes (final File file) {
        final byte [] res = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        ByteArrayOutputStream bout = null;
        try {
            fis = new FileInputStream (file);
            dis = new DataInputStream (fis);
            bout = new ByteArrayOutputStream ();
            int temp;
            int size = 0;
            final byte [] b = new byte [2048];
            while ((temp = dis.read (b)) != -1) {
                bout.write (b, 0, temp);
                size += temp;
            }
            fis.close ();
            dis.close ();
            if (AndroidInstrumenter.debug) {
                System.out.println (file.getAbsolutePath () + "a" + size);
            }
        } catch (final Exception e) {
            e.printStackTrace ();
        }

        return bout.toByteArray ();
    }
}
