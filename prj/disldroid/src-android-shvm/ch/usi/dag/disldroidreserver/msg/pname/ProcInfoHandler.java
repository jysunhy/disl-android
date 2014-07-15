package ch.usi.dag.disldroidreserver.msg.pname;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;


public class ProcInfoHandler implements RequestHandler {

    @Override
    public void handle (
        final int pid, final DataInputStream is,
        final DataOutputStream os, final boolean debug) throws DiSLREServerException {
        try {
            final String pname = is.readUTF();
            ShadowAddressSpace.getShadowAddressSpace (pid).getContext ().setPname (pname);
        } catch (final IOException e) {
            throw new DiSLREServerException (e);
        }
    }


    @Override
    public void exit () {
    }

}
