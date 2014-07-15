package ch.usi.dag.disldroidreserver.msg.newclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public class NewClassHandler implements RequestHandler {

	@Override
    public void handle(final int pid, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

        try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
			final String className = is.readUTF();
			final long oid = is.readLong();

			final ShadowObject classLoader = shadowAddressSpace.getShadowObject (oid);
			final int classCodeLength = is.readInt();
			final byte[] classCode = new byte[classCodeLength];
			is.readFully(classCode);

			shadowAddressSpace.loadBytecode(classLoader, className, classCode, debug);
		} catch (final IOException e) {
			throw new DiSLREServerException(e);
		}
	}

	@Override
    public void exit() {

	}

}
