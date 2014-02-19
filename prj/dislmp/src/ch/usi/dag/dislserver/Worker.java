package ch.usi.dag.dislserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.util.Constants;

public class Worker extends Thread {

	private static final boolean debug = Boolean
			.getBoolean(DiSLServer.PROP_DEBUG);

	private static final String PROP_UNINSTR = "dislserver.uninstrumented";
	private static final String uninstrPath = 
			System.getProperty(PROP_UNINSTR, null);

	private static final String PROP_INSTR = "dislserver.instrumented";
	private static final String instrPath = 
			System.getProperty(PROP_INSTR, null);

	// used for replays
	private static final byte[] emptyByteArray = new byte[0];
	
	private final NetMessageReader sc;
	private final DiSL disl;
	
	private final AtomicLong instrumentationTime = new AtomicLong();

	Worker(NetMessageReader sc, DiSL disl) {
		this.sc = sc;
		this.disl = disl;
	}

	public void run() {

		try {

			instrumentationLoop();

			sc.close();
		}
		catch (Throwable e) {
			DiSLServer.reportError(e);
		}
		finally {
			DiSLServer.workerDone(instrumentationTime.get());
		}
	}

	private void instrumentationLoop() throws Exception {

		try {
		
		while (true) {

			NetMessage nm = sc.readMessage();

			// communication closed by the client
			if (nm.getControl().length == 0 && nm.getClassCode().length == 0) {
				return;
			}

			byte[] instrClass;

			try {
				
				long startTime = System.nanoTime();
				
				instrClass = instrument(new String(nm.getControl()),
						nm.getClassCode());
				
				instrumentationTime.addAndGet(System.nanoTime() - startTime);
			}
			catch (Exception e) {

				// instrumentation error
				// send the client a description of the server-side error

				String errToReport = e.getMessage();
				
				// during debug send the whole message
				if(debug) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					errToReport = sw.toString();
				}

				// error protocol:
				// control contains the description of the server-side error
				// class code is an array of size zero
				String errMsg = "Instrumentation error for class "
						+ new String(nm.getControl()) + ": " + errToReport;

				sc.sendMessage(new NetMessage(errMsg.getBytes(),
						emptyByteArray));

				throw e;
			}

			NetMessage replyData = null;
			
			if(instrClass != null) {
				// class was modified - send modified data
				replyData = new NetMessage(emptyByteArray, instrClass);
			}
			else {
				// zero length means no modification
				replyData = new NetMessage(emptyByteArray, emptyByteArray);
			}
			
			sc.sendMessage(replyData);
		}
		
		}
		catch (IOException e) {
			throw new DiSLServerException(e);
		}
	}
	
	private byte[] instrument(String className, byte[] origCode)
			throws DiSLServerException, DiSLException {
		
		// backup for empty class name
		if(className == null || className.isEmpty()) {
			className = UUID.randomUUID().toString();
		}
		
		// dump uninstrumented
		if (uninstrPath != null) {
			dump(className, origCode, uninstrPath);
		}

		// instrument
		byte[] instrCode = disl.instrument(origCode);

		// dump instrumented
		if (instrPath != null && instrCode != null) {
			dump(className, instrCode, instrPath);
		}

		return instrCode;
	}

	private void dump(String className, byte[] codeAsBytes, String path)
			throws DiSLServerException {
		
		try {
		
			// extract the class name and package name
			int i = className.lastIndexOf(Constants.PACKAGE_INTERN_DELIM);
			String onlyClassName = className.substring(i + 1);
			String packageName = className.substring(0, i + 1);
			
			// construct path to the class
			String pathWithPkg = path + File.separator + packageName;

			// create directories
			new File(pathWithPkg).mkdirs();

			// dump the class code
			FileOutputStream fo = new FileOutputStream(pathWithPkg
					+ onlyClassName + Constants.CLASS_EXT);
			fo.write(codeAsBytes);
			fo.close();
		}
		catch (FileNotFoundException e) {
			throw new DiSLServerException(e);
		}
		catch (IOException e) {
			throw new DiSLServerException(e);
		}
	}
}
