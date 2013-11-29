package my.pkg;

public class ClassTest {

	static {
		System.out.println("app: my.pkg.ClassTest.clinit()");
	}

	public ClassTest() {
		System.out.println("app: my.pkg.ClassTest.init()");
	}

	public int computeMul(int x, int y) {
		System.out.println("app: my.pkg.ClassTest.computeMul(int, int) int");
		return 0;
	}

	public int computeFastMul(int x, int y) {
		System.out.println("app: my.pkg.ClassTest.computeFastMul(int, int) int");
		return 0;
	}
}