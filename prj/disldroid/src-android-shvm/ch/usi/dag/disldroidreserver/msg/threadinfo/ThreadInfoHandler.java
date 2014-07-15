package ch.usi.dag.disldroidreserver.msg.threadinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowThread;

public class ThreadInfoHandler implements RequestHandler {

	@Override
    public void handle(final int pid, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

		try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
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
