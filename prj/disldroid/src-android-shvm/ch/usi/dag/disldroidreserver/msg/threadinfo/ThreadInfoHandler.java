package ch.usi.dag.disldroidreserver.msg.threadinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.Utils;
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

    @Override
    public void handle (final int pid, final ByteBuffer is, final boolean debug) throws Exception {

        try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
            final long net_ref = is.getLong();
            final String name = Utils.readUTF (is);
            final boolean isDaemon = is.getShort () > 0;

            final ShadowClass klass = shadowAddressSpace.getShadowClass (net_ref);
            final ShadowThread sThread = shadowAddressSpace.createShadowThread (
                net_ref, name, isDaemon, klass);

            shadowAddressSpace.registerShadowObject (sThread, debug);
        } catch (final Exception e) {
            throw e;
        }
    }
}
