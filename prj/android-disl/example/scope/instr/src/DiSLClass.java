import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;

public class DiSLClass {

	// method only in subpackages of my

	// (..) might be omitted
	@Before(marker = BodyMarker.class, scope = "computeMul(..)", order = 1)
	public static void before1() {
		System.out.println("disl: before every method named 'computeMul'");
	}

	// (..) might be omitted
	@Before(marker = BodyMarker.class, scope = "ClassTest.*(..)", order = 2)
	public static void before2() {
		System.out.println("disl: before every method in class 'ClassTest' in all packages");
	}

	// (..) might be omitted
	@Before(marker = BodyMarker.class, scope = "int my.*(..)", order = 3)
	public static void before3() {
		System.out.println("disl: before every method returning int in 'my' subpackages or package");
	}

	@Before(marker = BodyMarker.class, scope = "my.*(int, int)", order = 4)
	public static void before4() {
		System.out.println("disl: before every method accepting two ints in 'my' subpackages or package");
	}

	@Before(marker = BodyMarker.class, scope = "long *(int, int, int, double, double, double)", order = 5)
	public static void before5() {
		System.out.println("disl: before every method returing long, accepting int int int double double double");
	}

	// (..) might be omitted
	@Before(marker = BodyMarker.class, scope = "[default].*.*(..)", order = 1)
	public static void before6() {
		System.out.println("disl: before every method in class in default package");
	}
}
