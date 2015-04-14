package ch.usi.dag.branchcoverage.disl;

import ch.usi.dag.branchcoverage.analysis.CodeCoverageAnalysisProxy;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;

    @Before (marker = BranchMarker.class, order = 1)
    public static void beforeBranchInstr () {
        isBranch = true;
    }

    @AfterReturning (marker = IfThenBranchMarker.class, order = 1)
    public static void afterBranchInstr (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassSignature (), c.thisMethodSignature (), c.getIndex ());
            isBranch = false;
        }
    }

    @AfterReturning (marker = IfElseBranchMarker.class, order = 1)
    public static void afterJmpInstr (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassSignature (), c.thisMethodSignature (), c.getIndex ());
            isBranch = false;
        }
    }

    @Before (marker = SwitchMarker.class, order = 1)
    public static void beforeSwitch (final CodeCoverageContext c) {
        isBranch = true;
    }

    @AfterReturning (marker = SwitchCaseMarker.class, order = 1)
    public static void afterBranchLabel (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassSignature (), c.thisMethodSignature (), c.getIndex ());
            isBranch = false;
        }
    }
}
