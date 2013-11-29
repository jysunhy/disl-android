import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class DiSLClass {

	@Before(marker = BodyMarker.class, scope = "Main.test*", guard = DumbLoopGuard.class)
	public static void before(MethodStaticContext msc) {
		System.out.printf("disl: %s has loop\n", msc.thisMethodName());
	}

	@After(marker = BodyMarker.class, scope = "Main.test*")
	public static void after(DumbLoopContext dlc, MethodStaticContext msc) {
		if (dlc.hasLoop()) {
			System.out.printf("disl: %s had loop\n", msc.thisMethodName());
		} else {
			System.out.printf("disl: %s had no loop\n", msc.thisMethodName());
		}
	}
}
