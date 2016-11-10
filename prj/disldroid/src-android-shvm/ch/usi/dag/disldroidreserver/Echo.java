package ch.usi.dag.disldroidreserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Echo {
    private static final String PROP_DEBUG = "debug";
    public static final boolean debug = Boolean.getBoolean (PROP_DEBUG);

    private static final String PROP_PORT = "dislreserver.port";
    private static final int DEFAULT_PORT = 11218;
    private static final int port = Integer.getInteger(PROP_PORT, DEFAULT_PORT);

    public static void main(final String args[]){
        try {
            final ServerSocket listenSocket = new ServerSocket (port);
            if (debug) {
                System.out.printf (
                    "DiSL-RE: listening at %s:%d\n",
                    listenSocket.getInetAddress ().getHostAddress (),
                    listenSocket.getLocalPort ()
                );
            }
            final int total = 0;

            while (true) {
                final Socket socket = listenSocket.accept ();
                (new EchoThread (socket)).start ();
            }
        }catch(final Exception e){
            e.printStackTrace ();
        }
    }
    public static AtomicInteger total = new AtomicInteger (0);
    public static class EchoThread extends Thread{
        Socket sock;
        EchoThread(final Socket s){
            this.sock = s;
        }
        @Override
        public
        void run(){
            try{
            final DataInputStream is = new DataInputStream(
                new BufferedInputStream(sock.getInputStream()));
            final byte[] buffer = new byte[1024];
            while(true){
                final int len = is.read (buffer, 0, buffer.length);
                total.addAndGet (len);
                System.out.println("Total "+total.get ());
            }
            }catch (final Exception e){
                e.printStackTrace ();
            }
        }
    }
}
