package ch.usi.dag.branchcoverage.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class BCAnalysis extends RemoteAnalysis {

    private static float divide (final int a, final int b) {
        return b == 0 ? Float.NaN : (((float) a) / b);
    }

    public void commitBranch (final Context context, final ShadowString className, final ShadowString methodId, final int index) {
        final byte[] res = context.getByteCodeFor (className.toString ());
        System.out.println (context.getPname ()+" "+className+" "+res==null?0:res.length+" "+methodId+" "+index);
    }

    public void printAllResult () {
        for (final Context context : Context.getAllContext ()) {
            printResult (context);
        }
    }

    public void printResult (final Context context) {
    }

    @Override
    public void atExit (final Context context) {
        printResult (context);
    }

    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }
}
