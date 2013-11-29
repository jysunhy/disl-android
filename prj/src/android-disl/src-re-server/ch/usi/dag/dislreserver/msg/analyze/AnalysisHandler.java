package ch.usi.dag.dislreserver.msg.analyze;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisResolver.AnalysisMethodHolder;
import ch.usi.dag.dislreserver.msg.analyze.mtdispatch.AnalysisDispatcher;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowObjectTable;


public final class AnalysisHandler implements RequestHandler {

	private AnalysisDispatcher dispatcher = new AnalysisDispatcher ();

	public AnalysisDispatcher getDispatcher() {
		return dispatcher;
	}

	public void handle (
		final DataInputStream is, final DataOutputStream os, final boolean debug
	) throws DiSLREServerException {

		try {
			// get net reference for the thread
			long orderingID = is.readLong ();

			// read and create method invocations
			final int invocationCount = is.readInt ();
			if (invocationCount < 0) {
				throw new DiSLREServerException (String.format (
					"invalid number of analysis invocation requests: %d",
					invocationCount
				));
			}

			List <AnalysisInvocation> invocations = __unmarshalInvocations (
				invocationCount, is, debug
			);

			dispatcher.addTask (orderingID, invocations);

		} catch (final IOException ioe) {
			throw new DiSLREServerException(ioe);
		}
	}


	private List <AnalysisInvocation> __unmarshalInvocations (
		final int invocationCount, final DataInputStream is, final boolean debug
	) throws DiSLREServerException {
		final List <AnalysisInvocation> result =
			new LinkedList <AnalysisInvocation> ();

		for (int i = 0; i < invocationCount; ++i) {
			result.add (__unmarshalInvocation (is, debug));
		}

		return result;
	}


	private AnalysisInvocation __unmarshalInvocation (
		final DataInputStream is, final boolean debug
	) throws DiSLREServerException {
		try {
			// *** retrieve method ***

			// read method id from network and retrieve method
			final short methodId = is.readShort ();
			AnalysisMethodHolder amh = AnalysisResolver.getMethod (methodId);

			// *** retrieve method argument values ***

			final Method method = amh.getAnalysisMethod ();

			// read the length of argument data in the request
			final short argsLength = is.readShort ();
			if (argsLength < 0) {
				throw new DiSLREServerException (String.format (
					"invalid value of marshalled argument data length for analysis method %d (%s.%s): %d",
					methodId, method.getDeclaringClass ().getName (),
					method.getName (), argsLength
				));
			}

			// read argument values data according to argument types
			int readLength = 0;
			final List <Object> args = new LinkedList <Object> ();
			for (Class <?> argClass : method.getParameterTypes ()) {
				readLength += unmarshalAndCollectArgument (
					is, argClass, method, args
				);
			}

			if (readLength != argsLength) {
				throw new DiSLREServerException (String.format (
					"received %d, but unmarshalled %d bytes of argument data for analysis method %d (%s.%s)",
					argsLength, readLength, methodId, method.getDeclaringClass ().getName (),
					method.getName ()
				));
			}

			// *** create analysis invocation ***

			if(debug) {
				System.out.printf (
					"DiSL-RE: dispatching analysis method (%d) to %s.%s()\n",
					methodId, amh.getAnalysisInstance().getClass().getSimpleName (),
					method.getName()
				);
			}
			return new AnalysisInvocation (
				method, amh.getAnalysisInstance (), args
			);

		} catch (final IOException ioe) {
			throw new DiSLREServerException (ioe);
		} catch (final IllegalArgumentException iae) {
			throw new DiSLREServerException (iae);
		}
	}


	private int unmarshalAndCollectArgument (
		final DataInputStream is, final Class <?> argClass,
		final Method analysisMethod, List <Object> args
	) throws IOException, DiSLREServerException {

		if (argClass.equals (boolean.class)) {
			args.add (is.readBoolean ());
			return Byte.SIZE / Byte.SIZE;
		}

		if (argClass.equals (char.class)) {
			args.add (is.readChar ());
			return Character.SIZE / Byte.SIZE;
		}

		if (argClass.equals (byte.class)) {
			args.add (is.readByte ());
			return Byte.SIZE / Byte.SIZE;
		}

		if (argClass.equals (short.class)) {
			args.add (is.readShort ());
			return Short.SIZE / Byte.SIZE;
		}

		if (argClass.equals (int.class)) {
			args.add (is.readInt ());
			return Integer.SIZE / Byte.SIZE;
		}

		if (argClass.equals (long.class)) {
			args.add (is.readLong ());
			return Long.SIZE / Byte.SIZE;
		}

		if (argClass.equals (float.class)) {
			args.add (is.readFloat ());
			return Float.SIZE / Byte.SIZE;
		}

		if (argClass.equals (double.class)) {
			args.add (is.readDouble ());
			return Double.SIZE / Byte.SIZE;
		}

		if (ShadowObject.class.isAssignableFrom(argClass)) {
			long net_ref = is.readLong();

			// null handling
			if (net_ref == 0) {
				args.add(null);
			} else {
				args.add(ShadowObjectTable.get(net_ref));
			}

			return Long.SIZE / Byte.SIZE;
		}

		throw new DiSLREServerException (String.format (
			"Unsupported data type %s in analysis method %s.%s",
			argClass.getName (), analysisMethod.getDeclaringClass ().getName (),
			analysisMethod.getName ()
		));
	}

	public void threadEnded(long threadId) {
		dispatcher.threadEndedEvent(threadId);
	}
	
	public void objectsFreed(long[] objFreeIDs) {
		dispatcher.objectsFreedEvent(objFreeIDs);
	}
	
	public void exit() {
		dispatcher.exit();
	}
}
