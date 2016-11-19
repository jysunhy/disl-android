package ch.usi.dag.disldroidreserver.msg.pname;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.Utils;
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
            System.out.println ("New Process "+pname+"-"+pid);
            for(final ShadowAddressSpace space : ShadowAddressSpace.getAllShadowAddressSpace ()){
                if(space.getContext ().pid() == pid ) {
                    continue;
                }
                if((space.getContext ().getPname ()!= null) && (space.getContext ().getPname ().equals (pname))) {
                    System.out.println ("DEBUGGING:"+pname+" pid "+space.getContext ().pid ()+" stopped");
                    space.getContext ().setDead (true);
                }
            }

            ShadowAddressSpace.getShadowAddressSpace (pid).getContext ().setPname (pname);
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
            final String pname = Utils.readUTF (is);
            System.out.println ("New Process "+pname+"-"+pid);
            for(final ShadowAddressSpace space : ShadowAddressSpace.getAllShadowAddressSpace ()){
                if(space.getContext ().pid() == pid ) {
                    continue;
                }
                if((space.getContext ().getPname ()!= null) && (space.getContext ().getPname ().equals (pname))) {
                    System.out.println ("DEBUGGING:"+pname+" pid "+space.getContext ().pid ()+" stopped");
                    space.getContext ().setDead (true);
                }
            }

            ShadowAddressSpace.getShadowAddressSpace (pid).getContext ().setPname (pname);
        } catch (final Exception e) {
            throw e;
        }
    }

}
