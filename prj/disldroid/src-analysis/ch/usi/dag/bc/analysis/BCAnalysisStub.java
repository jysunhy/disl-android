package ch.usi.dag.bc.analysis;

import ch.usi.dag.dislre.AREDispatch;


public class BCAnalysisStub {

    public static short SM = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.sendMeta");

    public static short CB = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.commitBranch");

    public static short BB = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.commitBasicBlock");

    public static short PR = AREDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.printResult");


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
