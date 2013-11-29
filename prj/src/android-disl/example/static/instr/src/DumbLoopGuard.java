import ch.usi.dag.disl.annotation.GuardMethod;

public class DumbLoopGuard {

    @GuardMethod
    public static boolean isApplicable(DumbLoopContext dlc) {
        if (dlc.hasLoop()) {
            return true;
        } else {
            return false;
        }
    }
}
