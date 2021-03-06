package ch.usi.dag.bc.disl;

import ch.usi.dag.bc.analysis.BCAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;

    @SyntheticLocal
    public static boolean [] branches;

    @SyntheticLocal
    public static boolean [] basicblocks;


    @Before (marker = BodyMarker.class, order = 2)
    public static void onMethodEntry (
        final BCContext bcc, final BBCContext bbcc) {
        BCAnalysisStub.sendMeta (
            bcc.thisClassName (), bcc.thisMethodFullNameWithDesc (),
            bcc.getClassBranchCount (), bcc.getMethodBranchCount (),
            bbcc.getClassBBCount (), bbcc.getMethodBBCount ());
        branches = new boolean [bcc.getMethodBranchCount ()];
        basicblocks = new boolean [bbcc.getMethodBBCount ()];
    }


    @Before (marker = BasicBlockMarker.class, order = 1)
    public static void beforeBB (final BBCContext bbcc) {
        basicblocks [bbcc.getMethodBBindex ()] = true;
    }


    @Before (marker = BranchInstrMarker.class, order = 1)
    public static void beforeBranchInstr () {
        isBranch = true;
    }


    @AfterReturning (marker = BranchInstrMarker.class, order = 1)
    public static void afterBranchInstr (final BCContext bcc) {
        if (isBranch) {
            branches [bcc.getIndex ()] = true;
            isBranch = false;
        }
    }


    @Before (marker = SwitchInsnMarker.class, order = 1)
    public static void beforeSwitch (final BCContext bcc) {
        isBranch = true;
    }


    @AfterReturning (marker = BranchLabelMarker.class, order = 1)
    public static void afterBranchLabel (final BCContext bcc) {
        if (isBranch) {
            branches [bcc.getIndex ()] = true;
            isBranch = false;
        }
    }


    @After (marker = BodyMarker.class, order = 2)
    public static void onMethodExit (final BCContext bcc) {
        BCAnalysisStub.commitBranch (
            bcc.thisMethodFullNameWithDesc (), branches);
        BCAnalysisStub.commitBasicBlock (
            bcc.thisMethodFullNameWithDesc (), basicblocks);
    }


    @Before (marker = BodyMarker.class, scope = "foo.bar", order = 2)
    public static void printAnalysisResult () {
        BCAnalysisStub.printResult ();
    }

}
