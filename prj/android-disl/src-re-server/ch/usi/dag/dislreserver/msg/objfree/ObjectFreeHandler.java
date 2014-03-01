package ch.usi.dag.dislreserver.msg.objfree;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;

public class ObjectFreeHandler implements RequestHandler {

    final AnalysisHandler analysisHandler;

	public ObjectFreeHandler(final AnalysisHandler anlHndl) {
		analysisHandler = anlHndl;
	}

	@Override
    public void handle(final int pid, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {


		try {

			final int freeCount = is.readInt();

			final long[] objFreeIDs = new long[freeCount];

			for(int i = 0; i < freeCount; ++i) {

				final long netref = is.readLong();

				objFreeIDs[i] = netref;
			}

            analysisHandler.objectsFreed (
                ShadowAddressSpace.getShadowAddressSpaceBlocked (pid), objFreeIDs);

		} catch (final IOException e) {
			throw new DiSLREServerException(e);
		}
	}

	@Override
    public void exit() {

	}

}
