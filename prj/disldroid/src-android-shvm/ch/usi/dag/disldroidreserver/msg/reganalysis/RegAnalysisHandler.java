package ch.usi.dag.disldroidreserver.msg.reganalysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.Utils;
import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;

public final class RegAnalysisHandler implements RequestHandler {

	@Override
    public void handle(final int pid, final DataInputStream is, final DataOutputStream os,
			final boolean debug) throws DiSLREServerException {
		try {
			final short methodId = is.readShort();
			final String methodString = is.readUTF();

			// register method
			AnalysisResolver.registerMethodId(pid, methodId, methodString);

			if (debug) {
				System.out.printf(
						"DiSL-RE: registered %s as analysis method %d\n",
						methodString.toString(), methodId);
			}

		} catch (final IOException ioe) {
			throw new DiSLREServerException(ioe);
		}
	}

	@Override
    public void exit() {

	}

    @Override
    public void handle (final int pid, final ByteBuffer is, final boolean debug) throws Exception {
        try {
            final short methodId = is.getShort();
            final String methodString = Utils.readUTF (is);

            // register method
            AnalysisResolver.registerMethodId(pid, methodId, methodString);

            if (debug) {
                System.out.printf(
                        "DiSL-RE: registered %s as analysis method %d\n",
                        methodString.toString(), methodId);
            }

        } catch (final Exception e) {
            throw e;
        }
    }

}
