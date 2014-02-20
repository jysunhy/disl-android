package ch.usi.dag.dislreserver.msg.newclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowObject;

public class NewClassHandler implements RequestHandler {

	@Override
    public void handle(final ShadowAddressSpace shadowAddressSpace, final DataInputStream is, final DataOutputStream os, final boolean debug)
			throws DiSLREServerException {

		try {
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
