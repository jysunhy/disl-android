package ch.usi.dag.cc.analysis;

import java.util.Arrays;
import java.util.Formatter;

import ch.usi.dag.disldroidreserver.shadow.Replicable;


public class CoverageState implements Replicable {

    String className;

    boolean [] brancheExecuted;

    boolean [] basicblockExecuted;

    // for dumping
    int coveredBranches;

    int coveredBasicBlocks;

    int classBranch;

    int classBasicBlock;


    public CoverageState (
        final String className,
        final int methodBranch, final int classBranch, final int methodBasicBlock,
        final int classBasicBlock) {
        this.className = className;
        this.brancheExecuted = new boolean [methodBranch];
        this.basicblockExecuted = new boolean [methodBasicBlock];

        this.coveredBranches = 0;
        this.coveredBasicBlocks = 0;
        this.classBranch = classBranch;
        this.classBasicBlock = classBasicBlock;
    }


    private CoverageState (
        final String className, final boolean [] branches,
        final boolean [] basicblocks) {
        this.className = className;
        this.brancheExecuted = Arrays.copyOf (branches, branches.length);
        this.basicblockExecuted = Arrays.copyOf (basicblocks, basicblocks.length);

        this.coveredBranches = 0;
        this.coveredBasicBlocks = 0;
    }


    @Override
    public Replicable replicate () {
        return new CoverageState (className, brancheExecuted, basicblockExecuted);
    }


    public void updateCoverageData () {
        coveredBranches = 0;
        coveredBasicBlocks = 0;

        for (final boolean element : brancheExecuted) {
            if (element) {
                coveredBranches++;
            }
        }

        for (final boolean element : basicblockExecuted) {
            if (element) {
                coveredBasicBlocks++;
            }
        }
    }


    @Override
    public String toString () {
        final Formatter formatter = new Formatter ();

        final StringBuilder branchBitmap = new StringBuilder ();

        for (final boolean element : brancheExecuted) {
            if (element) {
                branchBitmap.append ('1');
            } else {
                branchBitmap.append ('0');
            }
        }

        final StringBuilder basicblockBitmap = new StringBuilder ();

        for (final boolean element : basicblockExecuted) {
            if (element) {
                basicblockBitmap.append ('1');
            } else {
                basicblockBitmap.append ('0');
            }
        }

        formatter.format ("Total Branches: %d; Covered Branches: %d; "
            + "Total Basicblocks: %d; Covered Basicblocks: %d; ",
            brancheExecuted.length,
            coveredBranches,
            basicblockExecuted.length,
            coveredBasicBlocks);

        final String res = formatter.toString ();
        formatter.close ();

        return res;
    }

}
