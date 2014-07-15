package my.pkg;

public class ClassBigTest {

	static {
		System.out.println("app: my.pkg.ClassBigTest.clinit()");
	}

	public ClassBigTest() {
		System.out.println("app: my.pkg.ClassBigTest.init()");
	}

	public double computeDiv(int x, int y) {
		System.out.println("app: my.pkg.ClassBigTest.computeDiv(int, int) double");
		return 0.0;
	}

	public double computeFastDiv(int x, int y) {
		System.out.println("app: my.pkg.ClassBigTest.computeFastDiv(int, int) double");
		return 0.0;
	}
}