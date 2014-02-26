package ch.usi.dag.bc.disl;

import ch.usi.dag.bc.analysis.BCAnalysisStub;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;


    @Before (marker = BodyMarker.class, scope="MainActivity.substraction")
    public static void Close () {
        AREDispatch.manuallyClose();
    }

    @Before (marker = BodyMarker.class, scope="MainActivity.foo")
    public static void onMethodEntry (final BCContext bcc) {
        BCAnalysisStub.sendMeta (
            bcc.thisClassName (), bcc.thisMethodFullNameWithDesc (),
            bcc.getTotal (), bcc.getLocal ());
    }


    @Before (marker = BranchInstrMarker.class, scope="MainActivity.foo")
    public static void beforeBranchInstr () {
        isBranch = true;
    }


    @AfterReturning (marker = BranchInstrMarker.class, scope="MainActivity.foo")
    public static void afterBranchInstr (final BCContext bcc) {
        if (isBranch) {
            BCAnalysisStub.commitBranch (
                bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
            isBranch = false;
        }
    }


    @AfterReturning (marker = BranchLabelMarker.class, scope="MainActivity.foo")
    public static void afterBranchLabel (final BCContext bcc) {
        if (isBranch) {
            BCAnalysisStub.commitBranch (
                bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
            isBranch = false;
        }
    }


    @AfterReturning (marker = SwitchLabelMarker.class, scope="MainActivity.foo")
    public static void afterSwitchLabel (final BCContext bcc) {
        BCAnalysisStub.commitBranch (
            bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
    }

}
