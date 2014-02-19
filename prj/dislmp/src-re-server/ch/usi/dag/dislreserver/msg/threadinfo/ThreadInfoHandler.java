package ch.usi.dag.dislreserver.msg.threadinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowThread;

public class ThreadInfoHandler implements RequestHandler {

	@Override
    public void handle(final ShadowAddressSpace shadowAddressSpace, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

		try {
			final long net_ref = is.readLong();
			final String name = is.readUTF();
			final boolean isDaemon = is.readBoolean();

			final ShadowClass klass = shadowAddressSpace.getShadowClass (net_ref);
            final ShadowThread sThread = shadowAddressSpace.createShadowThread (
                net_ref, name, isDaemon, klass);

            shadowAddressSpace.registerShadowObject (sThread, debug);
		} catch (final IOException e) {
			throw new DiSLREServerException(e);
		}
	}

	@Override
    public void exit() {

	}
}
