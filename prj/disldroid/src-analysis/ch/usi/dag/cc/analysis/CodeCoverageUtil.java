package ch.usi.dag.cc.analysis;

public class CodeCoverageUtil {

    public static float divide (final int a, final int b) {
        return b == 0 ? Float.NaN : (((float) a) / b);
    }

}
