package ch.usi.dag.empty.analysis;
import ch.usi.dag.dislre.AREDispatch;

public class TestStub {
    public static short id = AREDispatch.registerMethod ("ch.usi.dag.empty.analysis.Test.test");
    public static void test(){
        AREDispatch.analysisStart (id);
        AREDispatch.analysisEnd();
    }
}
