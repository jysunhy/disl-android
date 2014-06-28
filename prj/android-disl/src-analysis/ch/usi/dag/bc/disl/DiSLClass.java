package ch.usi.dag.bc.disl;

import ch.usi.dag.bc.analysis.BCAnalysisStub;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;

    @SyntheticLocal
    public static boolean [] branches;


    @Before (marker = BodyMarker.class, order = 2)
    public static void onMethodEntry (final BCContext bcc) {
        BCAnalysisStub.sendMeta (
            bcc.thisClassName (), bcc.thisMethodFullNameWithDesc (),
            bcc.getTotal (), bcc.getLocal ());
        branches = new boolean [bcc.getLocal ()];
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
    }

//    @Before (marker = BodyMarker.class, scope="System.exit")
//    public static void printAnalysisResult_0 () {
//        AREDispatch.NativeLog ("EXIT from System.exit");
//        BCAnalysisStub.printResult ();
//    }
//
//    @Before (marker = BodyMarker.class, scope="MainActivity.substraction", order = 2)
//    public static void printAnalysisResult () {
//        BCAnalysisStub.printResult ();
//    }

}
