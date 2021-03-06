package ch.usi.dag.dislreserver.msg.reganalysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;

public final class RegAnalysisHandler implements RequestHandler {

	@Override
    public void handle(final ShadowAddressSpace shadowAddressSpace, final DataInputStream is, final DataOutputStream os,
			final boolean debug) throws DiSLREServerException {
		try {
			final short methodId = is.readShort();
			final String methodString = is.readUTF();

			// register method
			AnalysisResolver.registerMethodId(methodId, methodString);

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

}
