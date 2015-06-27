package ch.usi.dag.disldroidreserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestDispatcher;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;


public abstract class DiSLREServer {

	private static final String PROP_DEBUG = "debug";
	public static final boolean debug = Boolean.getBoolean (PROP_DEBUG);

	private static final String PROP_PORT = "dislreserver.port";
	private static final int DEFAULT_PORT = 11218;
	private static final int port = Integer.getInteger(PROP_PORT, DEFAULT_PORT);

	private static ServerSocket listenSocket;

	//

	public static void main (final String [] args) {
		if (debug) {
			System.out.println("DiSL-RE: starting analysis server...");
		}

		try {
			listenSocket = new ServerSocket (port);
			if (debug) {
				System.out.printf (
					"DiSL-RE: listening at %s:%d\n",
					listenSocket.getInetAddress ().getHostAddress (),
					listenSocket.getLocalPort ()
				);
			}

			// TODO (YZ) how to terminate
			while (true) {
	            final Socket socket = listenSocket.accept ();
	            if (debug) {
	                System.out.printf (
	                    "DiSL-RE: accepting connection from %s:%d\n",
	                    socket.getInetAddress ().getHostAddress (),
	                    socket.getPort ()
	                );
	            }

	            new Thread () {
	                @Override
                    public void run() {
	                    try {
                            requestLoop (socket);
                            socket.close ();
                        } catch (final Throwable throwable) {
							ShadowAddressSpace.cleanup();
                            reportError (throwable);
                        }
	                };
	            }.start ();
			}


		} catch (final IOException ioe) {
			reportError (new DiSLREServerException (ioe));
		} catch (final Throwable throwable) {
			reportError (throwable);
		}

		if (debug) {
			System.out.println ("DiSL-RE: shutting down analysis server...");
		}

		System.exit(0); // to kill other threads
	}

//	static HashMap <Socket , Integer> sockPid = new HashMap <Socket, Integer> ();
//	static HashMap <Integer , Socket> pidSock = new HashMap <Integer, Socket> ();
	private static void requestLoop(final Socket sock) throws DiSLREServerException {
		try {
			final DataInputStream is = new DataInputStream(
				new BufferedInputStream(sock.getInputStream()));
			final DataOutputStream os = new DataOutputStream(
				new BufferedOutputStream(sock.getOutputStream()));

			final byte[] tmp = new byte[32*1024];
//			if(false){
//
//    			long total = 0;
//    			long bar = 0;
//                while(true && tmp != null){
//                    final int size = is.read (tmp);
//                    if(size > 0) {
//                        total += size;
//                        if(total > bar) {
//                            System.out.println (total);
//                            bar+=1024*1024;
//
//
//                        }
//                    }
//                }
//			}

            //REQUEST_LOOP:
            while (true) {
                final int processID = is.readInt ();
				final byte requestNo = is.readByte();
//				if(pidSock.containsKey (processID)){
//				    if(pidSock.get(processID)!=sock) {
//                        System.err.println ("receive wrong events which is a fake "+ processID);
//                    }
//				}else {
//				    pidSock.put (processID, sock);
//				}
//
//	            if(!sockPid.containsKey (sock)) {
//	                sockPid.put (sock, processID);
//	            }else{
//	                if(sockPid.get (sock) != processID){
//	                    System.err.println ("receive wrong events in "+sockPid.get (sock)+" with wrong pid "+processID);
//	                }
//	            }
                // TODO pass the inetaddress
				try {
    				if (RequestDispatcher.dispatch (processID, requestNo, is, os, debug)) {
    					//break REQUEST_LOOP;
    				}
				}catch(final Exception e){
				    e.printStackTrace ();
				    //final byte[] tmp = new byte[1024];
//				    while(true){
//				        is.read (tmp);
//				    }
				    sock.close ();
				    System.exit (-1);
				    //throw e;
				}
			}

		} catch (final IOException ioe) {
			throw new DiSLREServerException (ioe);
		}
	}


	private static void reportInnerError(final Throwable e) {
		Throwable cause = e.getCause();

		while (cause != null && cause.getMessage() != null) {
			System.err.println("  Inner error: " + cause.getMessage());
			cause = cause.getCause();
		}
	}


	private static void reportError (final Throwable throwable) {
		if (throwable instanceof DiSLREServerException) {
			System.err.print ("DiSL-RE: server error");

			final String message = throwable.getMessage ();
			System.err.println ((message != null) ? ": "+ message : "");

			reportInnerError (throwable);
			if (debug) {
				throwable.printStackTrace ();
			}

		} else {
			// some other exception
			System.err.print ("DiSL-RE: fatal error: ");
			throwable.printStackTrace ();
		}
	}
}
