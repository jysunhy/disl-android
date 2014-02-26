package ch.usi.dag.dislreserver.msg.onfork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.usi.dag.dislreserver.exception.DiSLREServerException;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisHandler;
import ch.usi.dag.dislreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.Forkable;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;


public class OnForkHandler implements RequestHandler {

    final AnalysisHandler analysisHandler;


    public OnForkHandler (final AnalysisHandler anlHndl) {
        analysisHandler = anlHndl;
    }


    @Override
    public void handle (
        final ShadowAddressSpace shadowAddressSpace, final DataInputStream is,
        final DataOutputStream os, final boolean debug) throws DiSLREServerException {
        try {
            final int childPID = is.readInt ();
            System.out.println("on fork for "+childPID);
            shadowAddressSpace.onFork (childPID);

            for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
                if (analysis instanceof Forkable) {
                    ((Forkable) analysis).onFork (shadowAddressSpace.getContext (), childPID);
                }
            }
        } catch (final IOException e) {
            //HAIYANG
            //throw new DiSLREServerException (e);
            (new DiSLREServerException (e)).printStackTrace ();
        }
    }


    @Override
    public void exit () {
    }

}
