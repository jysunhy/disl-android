package ch.usi.dag.disldroidreserver.msg.classinfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.Utils;
import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;


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


    @Override
    public void handle (final int pid, final ByteBuffer is, final boolean debug) throws Exception {
        try {
            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
            final long net_ref = is.getLong ();
            final String classSignature = Utils.readUTF (is);
            final String classGenericStr = Utils.readUTF (is);
            final ShadowObject classLoader = shadowAddressSpace.getShadowObject (is.getLong ());

            final ShadowClass superClass = (ShadowClass) shadowAddressSpace.getShadowObject (is.getLong ());
            shadowAddressSpace.createAndRegisterShadowClass (
                net_ref, superClass, classLoader,
                classSignature, classGenericStr, debug);
        } catch (final Exception e) {
            throw e;
        }
    }

}
