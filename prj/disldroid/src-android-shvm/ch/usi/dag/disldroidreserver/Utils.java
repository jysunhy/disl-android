package ch.usi.dag.disldroidreserver;

import java.nio.ByteBuffer;

public class Utils {
    public static String readUTF(final ByteBuffer is){
        final int len = is.getShort ();
        final byte[] tmpbuf = new byte[len];
        is.get (tmpbuf);

        return new String(tmpbuf);
    }
}
