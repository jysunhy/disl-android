package ch.usi.dag.dislreserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.reqdispatch.RequestDispatcher;


public abstract class DiSLREServer {

	private static final String PROP_DEBUG = "debug";
	private static final boolean debug = Boolean.getBoolean(PROP_DEBUG);

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

			Socket socket = listenSocket.accept ();
			if (debug) {
				System.out.printf (
					"DiSL-RE: accepting connection from %s:%d\n",
					socket.getInetAddress ().getHostAddress (),
					socket.getPort ()
				);
			}

			requestLoop (socket);
			socket.close ();

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


	private static void requestLoop(Socket sock) throws DiSLREServerException {
		try {
			final DataInputStream is = new DataInputStream(
				new BufferedInputStream(sock.getInputStream()));
			final DataOutputStream os = new DataOutputStream(
				new BufferedOutputStream(sock.getOutputStream()));

			REQUEST_LOOP: while (true) {
				final byte requestNo = is.readByte();
				if (RequestDispatcher.dispatch (requestNo, is, os, debug)) {
					break REQUEST_LOOP;
				}
			}

		} catch (final IOException ioe) {
			throw new DiSLREServerException (ioe);
		}
	}


	private static void reportInnerError(Throwable e) {
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
