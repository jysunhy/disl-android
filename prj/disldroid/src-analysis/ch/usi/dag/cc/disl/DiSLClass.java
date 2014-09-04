package ch.usi.dag.cc.disl;

import ch.usi.dag.cc.analysis.CodeCoverageAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;


public class DiSLClass {

    @SyntheticLocal
    public static boolean encounterBranch = false;

    @SyntheticLocal
    public static boolean [] branches;

    @SyntheticLocal
    public static boolean [] basicblocks;


    @Before (marker = BodyMarker.class, order = 2)
    public static void onMethodEntry (
        final CodeCoverageContext c) {
        CodeCoverageAnalysisStub.sendMeta (
            c.thisClassName (), c.thisMethodFullNameWithDesc (),
            c.getClassBranchCount (), c.getMethodBranchCount (),
            c.getClassBBCount (), c.getMethodBBCount ());
        branches = new boolean [c.getMethodBranchCount ()];
        basicblocks = new boolean [c.getMethodBBCount ()];
    }


    @Before (marker = BasicBlockMarker.class, order = 1)
    public static void beforeBB (final CodeCoverageContext c) {
        basicblocks [c.getMethodBBindex ()] = true;
    }


    @Before (marker = BranchInstrMarker.class, order = 1)
    public static void beforeBranchInstr () {
        encounterBranch = true;
    }


    @AfterReturning (marker = BranchInstrMarker.class, order = 1)
    public static void afterBranchInstr (final CodeCoverageContext c) {
        if (encounterBranch) {
            branches [c.getIndex ()] = true;
            encounterBranch = false;
        }
    }


    @Before (marker = SwitchInsnMarker.class, order = 1)
    public static void beforeSwitch () {
        encounterBranch = true;
    }


    @AfterReturning (marker = BranchLabelMarker.class, order = 1)
    public static void afterBranchLabel (final CodeCoverageContext c) {
        if (encounterBranch) {
            branches [c.getIndex ()] = true;
            encounterBranch = false;
        }
    }


    @After (marker = BodyMarker.class, order = 2)
    public static void onMethodExit (final CodeCoverageContext c) {
        CodeCoverageAnalysisStub.commitBranch (
            c.thisMethodFullNameWithDesc (), branches);
        CodeCoverageAnalysisStub.commitBasicBlock (
            c.thisMethodFullNameWithDesc (), basicblocks);
    }


    @Before (marker = BodyMarker.class, scope = "foo.bar", order = 2)
    public static void printAnalysisResult () {
        CodeCoverageAnalysisStub.printResult ();
    }

}
