package ch.usi.dag.dislreserver.msg.close;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ch.usi.dag.dislreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.reqdispatch.RequestDispatcher;
import ch.usi.dag.dislreserver.reqdispatch.RequestHandler;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;


public final class CloseHandler implements RequestHandler {

    @Override
    public void handle (
        final int pid, final DataInputStream is, final DataOutputStream os,
        final boolean debug
    ) {
        final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpaceBlocked (pid);

        if (ShadowAddressSpace.removeShadowAddressSpace (shadowAddressSpace)) {
            // call exit on all request handlers - waits for all uncompleted
            // actions
            for (final RequestHandler handler : RequestDispatcher.getAllHandlers ()) {
                handler.exit ();
            }

            // invoke atExit on all analyses
            for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
                analysis.atExit (shadowAddressSpace);
            }
        }
    }


    @Override
    public void exit () {

    }

}
