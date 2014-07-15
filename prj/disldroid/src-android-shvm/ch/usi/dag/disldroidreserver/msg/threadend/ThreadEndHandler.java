package ch.usi.dag.disldroidreserver.msg.threadend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;


public class ThreadEndHandler implements RequestHandler {

    final AnalysisHandler analysisHandler;


    public ThreadEndHandler (final AnalysisHandler anlHndl) {
        analysisHandler = anlHndl;
    }


    @Override
    public void handle (
        final int pid, final DataInputStream is, final DataOutputStream os,
        final boolean debug)
    throws DiSLREServerException {

        try {

            final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
            final long threadId = is.readLong ();

            // announce thread end to the analysis handler
            analysisHandler.threadEnded (shadowAddressSpace, threadId);

        } catch (final IOException e) {
            throw new DiSLREServerException (e);
        }
    }


    public void awaitProcessing () {

    }


    @Override
    public void exit () {

    }
}
