package ch.usi.dag.disldroidreserver.msg.close;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.reqdispatch.RequestHandler;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;


public final class CloseHandler implements RequestHandler {

    @Override
    public void handle (
        final int pid, final DataInputStream is, final DataOutputStream os,
        final boolean debug
    ) {
        final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);

//        if (ShadowAddressSpace.removeShadowAddressSpace (shadowAddressSpace)) {
//            // call exit on all request handlers - waits for all uncompleted
//            // actions
//            // TODO shall we call handler.exit()
//            for (final RequestHandler handler : RequestDispatcher.getAllHandlers ()) {
//                handler.exit ();
//            }
//        }

        // invoke atExit on all analyses
        for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
            analysis.atExit (shadowAddressSpace.getContext ());
        }

        ShadowAddressSpace.removeShadowAddressSpace (shadowAddressSpace);
    }


    @Override
    public void exit () {

    }


    @Override
    public void handle (final int pid, final ByteBuffer is, final boolean debug) throws Exception {
        final ShadowAddressSpace shadowAddressSpace = ShadowAddressSpace.getShadowAddressSpace (pid);
      for (final RemoteAnalysis analysis : AnalysisResolver.getAllAnalyses ()) {
          analysis.atExit (shadowAddressSpace.getContext ());
      }

      ShadowAddressSpace.removeShadowAddressSpace (shadowAddressSpace);
    }

}
