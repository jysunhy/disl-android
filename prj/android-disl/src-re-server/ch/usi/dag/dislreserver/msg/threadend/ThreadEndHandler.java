package ch.usi.dag.dislreserver.msg.threadend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;

public class ThreadEndHandler implements RequestHandler {

	final AnalysisHandler analysisHandler;

	public ThreadEndHandler(final AnalysisHandler anlHndl) {
		analysisHandler = anlHndl;
	}

	@Override
    public void handle(final ShadowAddressSpace shadowAddressSpace, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

		try {

			final long threadId = is.readLong();

			// announce thread end to the analysis handler
			analysisHandler.threadEnded(shadowAddressSpace, threadId);

		} catch (final IOException e) {
			throw new DiSLREServerException(e);
		}
	}

	public void awaitProcessing() {

	}

	@Override
    public void exit() {

	}
}
