import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;

/**
 * <p>
 * This example shows how to insert snippets at various regions and entries or exits of these regions.
 * 
 * <p>
 * It also shows how to implements custom code marker.
 */
public class DiSLClass {

	/**
	 * <p>
	 * This is added before every method call in Main.main method.
	 */
	@Before(marker = MethodInvocationMarker.class, scope = "Main.main")
	public static void beforeInvocation() {
		System.out.println("disl: before invocation");
	}

	/**
	 * <p>
	 * This is added after every method call in Main.main method.
	 */
	@After(marker = MethodInvocationMarker.class, scope = "Main.main")
	public static void afterInvocation() {
		System.out.println("disl: after invocation");
	}

	/**
	 * <p>
	 * This is added after every method matching Main.test* no matter how it
	 * ends (throw or no throw).
	 */
	@After(marker = BodyMarker.class, scope = "Main.test*")
	public static void afterMethod() {
		System.out.println("disl: after method Main.test*");
	}

	/**
	 * <p>
	 * This is added after every method matching Main.test* that exists via
	 * throwing exception.
	 */
	@AfterThrowing(marker = BodyMarker.class, scope = "Main.test*")
	public static void afterThrowing() {
		System.out.println("disl: after throwing Main.test*");
	}

	/**
	 * <p>
	 * This is added after every method matching Main.test* that returns
	 * normally.
	 */
	@AfterReturning(marker = BodyMarker.class, scope = "Main.test*")
	public static void afterReturning() {
		System.out.println("disl: after returning Main.test*");
	}
}
