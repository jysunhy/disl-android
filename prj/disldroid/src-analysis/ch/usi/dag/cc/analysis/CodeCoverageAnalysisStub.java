package ch.usi.dag.cc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class CodeCoverageAnalysisStub {

    static short SM = AREDispatch.registerMethod ("ch.usi.dag.cc.analysis.CodeCoverageAnalysis.registerMethod");

    static short CB = AREDispatch.registerMethod ("ch.usi.dag.cc.analysis.CodeCoverageAnalysis.commitBranch");

    static short BB = AREDispatch.registerMethod ("ch.usi.dag.cc.analysis.CodeCoverageAnalysis.commitBasicBlock");

    static short PR = AREDispatch.registerMethod ("ch.usi.dag.cc.analysis.CodeCoverageAnalysis.endAnalysis");


    public static void sendMeta (
        final String className, final String methodID, final int classBranches,
        final int methodBranches, final int classBasicBlocks,
        final int methodBasicBlocks) {
        AREDispatch.analysisStart (SM);
        AREDispatch.sendObjectPlusData (className);
        AREDispatch.sendObjectPlusData (methodID);
        AREDispatch.sendInt (classBranches);
        AREDispatch.sendInt (methodBranches);
        AREDispatch.sendInt (classBasicBlocks);
        AREDispatch.sendInt (methodBasicBlocks);
        AREDispatch.analysisEnd ();
    }


    public static void commitBranch (
        final String methodID, final boolean [] branches) {
        for (int i = 0; i < branches.length; i++) {
            if (branches [i]) {
                AREDispatch.analysisStart (CB);
                AREDispatch.sendObjectPlusData (methodID);
                AREDispatch.sendInt (i);
                AREDispatch.analysisEnd ();
            }
        }
    }


    public static void commitBasicBlock (
        final String methodID, final boolean [] bbs) {
        for (int i = 0; i < bbs.length; i++) {
            if (bbs [i]) {
                AREDispatch.analysisStart (BB);
                AREDispatch.sendObjectPlusData (methodID);
                AREDispatch.sendInt (i);
                AREDispatch.analysisEnd ();
            }
        }
    }


    public static void printResult () {
        AREDispatch.analysisStart (PR);
        AREDispatch.analysisEnd ();
    }

}
