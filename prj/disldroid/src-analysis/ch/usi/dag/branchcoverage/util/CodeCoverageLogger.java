package ch.usi.dag.branchcoverage.util;

public class CodeCoverageLogger {

    public static void printCoverage(final String className, final String methodName, final int cnt, final int sum){
        System.out.println ("class: "+className);
        System.out.println ("\t method: "+methodName+" "+cnt+" / "+sum);
    }
}
