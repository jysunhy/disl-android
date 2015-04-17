package ch.usi.dag.branchcoverage.disl;

import ch.usi.dag.branchcoverage.analysis.CodeCoverageAnalysisProxy;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;


public class DiSLClass {

    @SyntheticLocal
    public static boolean isBranch = false;

    @Before (marker = BranchMarker.class)
    public static void beforeBranchInstr () {
        isBranch = true;
    }

    @AfterReturning (marker = IfThenBranchMarker.class)
    public static void afterBranchInstr (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassName (), c.thisMethodName (), c.getIndex ());
            isBranch = false;
        }
    }

    @AfterReturning (marker = IfElseBranchMarker.class)
    public static void afterJmpInstr (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassName (), c.thisMethodName (), c.getIndex ());
            isBranch = false;
        }
    }

    @Before (marker = SwitchMarker.class)
    public static void beforeSwitch (final CodeCoverageContext c) {
        isBranch = true;
    }

    @AfterReturning (marker = SwitchCaseMarker.class)
    public static void afterBranchLabel (final CodeCoverageContext c) {
        if (isBranch) {
            CodeCoverageAnalysisProxy.commitBranch(c.thisClassName (), c.thisMethodName (), c.getIndex ());
            isBranch = false;
        }
    }
}
