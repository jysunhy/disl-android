package my;

public class ClassTest {

	static {
		System.out.println("app: my.ClassTest.clinit()");
	}

	public ClassTest() {
		System.out.println("app: my.ClassTest.init()");
	}

	public int computeMul(int x, int y) {
		System.out.println("app: my.ClassTest.computeMul(int, int) int");
		return 0;
	}

	public int computeFastMul(int x, int y) {
		System.out.println("app: my.ClassTest.computeFastMul(int, int) int");
		return 0;
	}

	public long complexComputeMul(int x, int y, int z, double xx, double yy,
			double zz) {
		System.out.println("app: my.ClassTest.complexComputeMul(int, int, int, double, double, double) long");
		return 0L;
	}
}