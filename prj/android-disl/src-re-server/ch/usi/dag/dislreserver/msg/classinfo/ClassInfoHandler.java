package ch.usi.dag.dislreserver.msg.classinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowObject;


public class ClassInfoHandler implements RequestHandler {

    @Override
    public void handle (final int pid, final DataInputStream is, final DataOutputStream os,
        final boolean debug)
    throws DiSLREServerException {

        try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
            final long net_ref = is.readLong ();
            final String classSignature = is.readUTF ();
            final String classGenericStr = is.readUTF ();
            final ShadowObject classLoader = shadowAddressSpace.getShadowObject (is.readLong ());

            final ShadowClass superClass = (ShadowClass) shadowAddressSpace.getShadowObject (is.readLong ());
            shadowAddressSpace.createAndRegisterShadowClass (
                net_ref, superClass, classLoader,
                classSignature, classGenericStr, debug);
        } catch (final IOException e) {
            throw new DiSLREServerException (e);
        }
    }


    @Override
    public void exit () {

    }
}
