package ch.usi.dag.disldroidreserver.msg.stringinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;

public class StringInfoHandler implements RequestHandler {

	@Override
    public void handle(final int pid, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

		try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
			final long net_ref = is.readLong();
			final String str = is.readUTF();

            final ShadowClass klass = shadowAddressSpace.getShadowClass (net_ref);
            final ShadowString sString = shadowAddressSpace.createShadowString (
                net_ref, str, klass);
            shadowAddressSpace.registerShadowObject (sString, debug);
		} catch (final IOException e) {
			throw new DiSLREServerException(e);
		}
	}

	@Override
    public void exit() {

	}

}
