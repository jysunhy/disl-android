package ch.usi.dag.dislserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.exception.DiSLInMethodException;

public abstract class DiSLServer {

	public static final String PROP_DEBUG = "debug";
	private static final boolean debug = Boolean.getBoolean(PROP_DEBUG);

	private static final String PROP_PORT = "dislserver.port";
	private static final int DEFAULT_PORT = 11217;
	private static final int port = Integer.getInteger(PROP_PORT, DEFAULT_PORT);

	private static final String PROP_TIME_STAT = "dislserver.timestat";
	private static final boolean timeStat = Boolean.getBoolean(PROP_TIME_STAT);

	private static final String PROP_CONT = "dislserver.continuous";
	private static final boolean continuous = Boolean.getBoolean(PROP_CONT);
	
	private static final String PROP_BYPASS = "dislserver.disablebypass";
	private static final boolean bypass = ! Boolean.getBoolean(PROP_BYPASS);

	private static final AtomicInteger aliveWorkers = new AtomicInteger();
	private static final AtomicLong instrumentationTime = new AtomicLong();

	private static DiSL disl;
	private static ServerSocket listenSocket;

	//

	public static void main (final String [] args) {
		try {
			// use dynamic bypass
			disl = new DiSL (bypass);

			if (debug) {
				System.out.println ("DiSL: starting instrumentation server...");
			}

			listenSocket = new ServerSocket (port);
			if (debug) {
				System.out.printf (
					"DiSL: listening at %s:%d\n",
					listenSocket.getInetAddress ().getHostAddress (),
					listenSocket.getLocalPort ()
				);
			}

			while (true) {
				final Socket newClient = listenSocket.accept ();
				if (debug) {
					System.out.printf (
						"DiSL: accepting connection from %s:%d\n",
						newClient.getInetAddress ().getHostAddress (),
						newClient.getPort ()
					);
				}

				NetMessageReader sc = new NetMessageReader (newClient);
				aliveWorkers.incrementAndGet ();
				new Worker (sc, disl).start ();
			}

		} catch (final IOException ioe) {
			reportError (new DiSLServerException (ioe));
		} catch (final Throwable throwable) {
			reportError (throwable);
		}
	}


	private static void reportInnerError (final Throwable throwable) {
		Throwable cause = throwable.getCause ();
		while (cause != null && cause.getMessage () != null) {
			System.err.println ("  Inner error: " + cause.getMessage ());
			cause = cause.getCause ();
		}
	}


	public static void reportError (Throwable throwable) {
		if (throwable instanceof DiSLException) {
			System.err.print ("DiSL: error");

			// report during which method it happened
			if (throwable instanceof DiSLInMethodException) {
				System.err.printf (
					" (while instrumenting method \"%s\")", throwable.getMessage ()
				);

				// set inner error
				throwable = throwable.getCause ();
			}

			reportOptionalMessage (throwable);
			reportInnerError (throwable);
			if (debug) {
				throwable.printStackTrace ();
			}

		} else if (throwable instanceof DiSLServerException) {
			System.err.print ("DiSL: server error");

			reportOptionalMessage (throwable);
			reportInnerError (throwable);
			if (debug) {
				throwable.printStackTrace ();
			}

		} else {
			// some other exception
			System.err.print ("DiSL: fatal error: ");
			throwable.printStackTrace ();
		}
	}


	private static void reportOptionalMessage (final Throwable throwable) {
		final String message = throwable.getMessage ();
		System.err.println ((message != null) ? ": "+ message : "");
	}


	public static void workerDone (final long instrTime) {
		instrumentationTime.addAndGet (instrTime);

		if (aliveWorkers.decrementAndGet () == 0) {
			if (timeStat) {
				System.out.printf (
					"DiSL: instrumentation took %d milliseconds\n",
					instrumentationTime.get () / 1000000
				);
			}

			// no workers - shutdown
			if (!continuous) {
				disl.terminate ();

				if (debug) {
					System.out.println ("DiSL: shutting down instrumentation server...");
				}

				System.exit(0);
			}
		}
	}
}
