package ch.usi.dag.bc.disl;

import ch.usi.dag.bc.analysis.BCAnalysisStub;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;

    @Before (marker = BodyMarker.class, scope="spec.*.*")
    public static void onMethodEntry (final BCContext bcc) {
        AREDispatch.NativeLog ("On method entry:"+bcc.thisClassName ()+"\t"+bcc.thisMethodFullNameWithDesc ()+"\t"+bcc.getTotal ()+"\t"+bcc.getLocal ());
        BCAnalysisStub.sendMeta (
            bcc.thisClassName (), bcc.thisMethodFullNameWithDesc (),
            bcc.getTotal (), bcc.getLocal ());
    }


    @Before (marker = BranchInstrMarker.class, scope="spec.*.*")
    public static void beforeBranchInstr () {
        AREDispatch.NativeLog("before Branch instr setting isBranch to true");
        isBranch = true;
    }


    @AfterReturning (marker = BranchInstrMarker.class, scope="spec.*.*")
    public static void afterBranchInstr (final BCContext bcc) {
        AREDispatch.NativeLog("after Branch instr");
        if (isBranch) {
            BCAnalysisStub.commitBranch (
                bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
            isBranch = false;
        }
    }


    @AfterReturning (marker = BranchLabelMarker.class, scope="spec.*.*")
    public static void afterBranchLabel (final BCContext bcc) {
        AREDispatch.NativeLog("after Branch Label");
        if (isBranch) {
            BCAnalysisStub.commitBranch (
                bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
            isBranch = false;
        }
    }


    @AfterReturning (marker = SwitchLabelMarker.class, scope="spec.*.*")
    public static void afterSwitchLabel (final BCContext bcc) {
        AREDispatch.NativeLog("after Switch Label");
        BCAnalysisStub.commitBranch (
            bcc.thisMethodFullNameWithDesc (), bcc.getIndex ());
    }


    @Before (marker = BodyMarker.class, scope = "*.onTransact")
    public static void test (final MethodStaticContext sc){

        AREDispatch.NativeLog (sc.thisMethodFullName());
    }

    @Before (marker = BodyMarker.class, scope = "*.transact")
    public static void test2 (final MethodStaticContext sc){

        AREDispatch.NativeLog (sc.thisMethodFullName());
    }

}
