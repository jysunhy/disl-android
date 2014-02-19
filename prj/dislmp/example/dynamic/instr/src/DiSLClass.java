import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;

public class DiSLClass {

	@After(marker = BodyMarker.class, scope = "Main.testMul")
	public static void afterMul(DynamicContext di) {
		int a = di.getLocalVariableValue(0, int.class);
		int b = di.getLocalVariableValue(1, int.class);
		System.out.println("disl: a=" + a);
		System.out.println("disl: b=" + b);
	}

	@After(marker = BodyMarker.class, scope = "Main.testInstance")
	public static void afterInstance(DynamicContext di) {
		Object o = di.getThis();
		System.out.println("disl: this=" + o.toString());
	}

	@After(marker = BodyMarker.class, scope = "Main.testException")
	public static void afterException(DynamicContext di) {
		Throwable o = di.getException();
		System.out.println("disl: exception=" + o.getMessage());
	}
}
