package ch.usi.dag.bc.analysis;

import ch.usi.dag.dislre.REDispatch;


public class BCAnalysisStub {

    public static short SM = REDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.sendMeta");

    public static short CB = REDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.commitBranch");

    public static short BB = REDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.commitBasicBlock");

    public static short PR = REDispatch.registerMethod ("ch.usi.dag.bc.analysis.BCAnalysis.printResult");


    public static void sendMeta (
        final String className, final String methodID, final int classBranches,
        final int methodBranches, final int classBasicBlocks,
        final int methodBasicBlocks) {
        REDispatch.analysisStart (SM);
        REDispatch.sendObjectPlusData (className);
        REDispatch.sendObjectPlusData (methodID);
        REDispatch.sendInt (classBranches);
        REDispatch.sendInt (methodBranches);
        REDispatch.sendInt (classBasicBlocks);
        REDispatch.sendInt (methodBasicBlocks);
        REDispatch.analysisEnd ();
    }


    public static void commitBranch (
        final String methodID, final boolean [] branches) {
        for (int i = 0; i < branches.length; i++) {
            if (branches [i]) {
                REDispatch.analysisStart (CB);
                REDispatch.sendObjectPlusData (methodID);
                REDispatch.sendInt (i);
                REDispatch.analysisEnd ();
            }
        }
    }


    public static void commitBasicBlock (
        final String methodID, final boolean [] bbs) {
        for (int i = 0; i < bbs.length; i++) {
            if (bbs [i]) {
                REDispatch.analysisStart (BB);
                REDispatch.sendObjectPlusData (methodID);
                REDispatch.sendInt (i);
                REDispatch.analysisEnd ();
            }
        }
    }


    public static void printResult () {
        REDispatch.analysisStart (PR);
        REDispatch.analysisEnd ();
    }

}
