package ch.usi.dag.cc.analysis;

import java.util.Formatter;

import ch.usi.dag.disldroidreserver.shadow.Replicable;

public class StatisticSummary implements Replicable {
    int classBranch;

    int classBasicBlock;

    int coveredClass;

    int coveredMethod;

    int coveredBranches;

    int coveredBasicBlocks;

    public StatisticSummary (final int classBranch, final int classBasicBlock) {
        this.classBranch = classBranch;
        this.classBasicBlock = classBasicBlock;

        this.coveredClass = 0;
        this.coveredMethod = 0;
        this.coveredBranches = 0;
        this.coveredBasicBlocks = 0;
    }


    @Override
    public Replicable replicate () {
        return new StatisticSummary (classBranch, classBasicBlock);
    }


    @Override
    public String toString () {
        final Formatter formatter = new Formatter ();

        formatter.format ("Total Branches: %d; Covered Branches: %d; "
            + "Total Basicblocks: %d; Covered Basicblocks: %d",
            classBranch,
            coveredBranches,
            classBasicBlock,
            coveredBasicBlocks);

        final String res = formatter.toString ();
        formatter.close ();

        return res;
    }


    public String toTexString () {
        final Formatter formatter = new Formatter ();

        formatter.format (" &  %.2f  &  %.2f  &  %d  &  %d",
            CodeCoverageUtil.divide (coveredBranches, classBranch),
            CodeCoverageUtil.divide (coveredBasicBlocks, classBasicBlock),
            coveredMethod,
            coveredClass);

        final String res = formatter.toString ();
        formatter.close ();

        return res;
    }


    public void merge (final StatisticSummary classStatistic) {
        classBranch += classStatistic.classBranch;
        coveredBranches += classStatistic.coveredBranches;

        classBasicBlock += classStatistic.classBasicBlock;
        coveredBasicBlocks += classStatistic.coveredBasicBlocks;

        coveredClass++;
        coveredMethod += classStatistic.coveredMethod;
    }

}
