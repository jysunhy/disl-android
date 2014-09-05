package ch.usi.dag.cc.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class CodeCoverageAnalysis extends RemoteAnalysis {

    public void registerMethod (
        final ShadowString className,
        final ShadowString methodSig,
        final int classBranch,
        final int methodBranch,
        final int classBasicBlock,
        final int methodBasicBlock) {

        if (methodSig.getState () == null) {
            methodSig.setStateIfAbsent (new CoverageState (
                className.toString (), methodBranch, classBranch, methodBasicBlock,
                classBasicBlock));
        }
    }


    public void commitBranch (final ShadowString methodID, final int index) {
        final CoverageState status = methodID.getState (CoverageState.class);
        status.brancheExecuted [index] = true;
    }


    public void commitBasicBlock (final ShadowString methodID, final int index) {
        final CoverageState status = methodID.getState (CoverageState.class);
        status.basicblockExecuted [index] = true;
    }


    public void endAnalysis () {
        for (final Context context : Context.getAllContext ()) {
            Logger.dump (context);
        }
    }


    @Override
    public void atExit (final Context context) {
        Logger.dump (context);
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }




}
