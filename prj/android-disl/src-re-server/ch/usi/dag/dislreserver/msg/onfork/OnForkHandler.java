package ch.usi.dag.dislreserver.msg.onfork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;


public class OnForkHandler implements RequestHandler {

    final AnalysisHandler analysisHandler;


    public OnForkHandler (final AnalysisHandler anlHndl) {
        analysisHandler = anlHndl;
    }


    @Override
    public void handle (
        final int pid, final DataInputStream is,
        final DataOutputStream os, final boolean debug) throws DiSLREServerException {
        try {
            final int childPID = is.readInt ();

            if (childPID != 0) {
                ShadowAddressSpace.getShadowAddressSpace (pid).onFork (childPID);
            } else {
                // Blocked the child process
                ShadowAddressSpace.getShadowAddressSpaceBlocked (pid);
            }
        } catch (final IOException e) {
            throw new DiSLREServerException (e);
        }
    }


    @Override
    public void exit () {
    }

}
