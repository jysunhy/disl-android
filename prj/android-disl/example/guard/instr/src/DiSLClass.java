import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;

public class DiSLClass {

	/**
	 * <p>
	 * This is added before every method call in Main.main method. And is always
	 * used thanks to guard.
	 */
	@Before(marker = MethodInvocationMarker.class, scope = "Main.main", guard = GuardYes.class)
	public static void beforeInvocation() {
		System.out.println("disl: before invocation");
	}

	/**
	 * <p>
	 * This snippet is never invoked due to guard.
	 */
	@After(marker = MethodInvocationMarker.class, scope = "Main.main", guard = GuardNo.class)
	public static void afterInvocation() {
		System.out.println("disl: after invocation");
	}
}
