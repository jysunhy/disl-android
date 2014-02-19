package ch.usi.dag.dislreserver.msg.pname;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;


public class ProcInfoHandler implements RequestHandler {

    @Override
    public void handle (
        final ShadowAddressSpace shadowAddressSpace, final DataInputStream is,
        final DataOutputStream os, final boolean debug) throws DiSLREServerException {
        try {
            // TODO (YZ) receive process data. remove the following dummy code
            is.readInt ();
        } catch (final IOException e) {
            throw new DiSLREServerException (e);
        }
    }


    @Override
    public void exit () {
    }

}
