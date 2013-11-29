public class Main {

	public String name = "app-name";

	public void testInstance() {
		System.out.println("app: Main.testInstance()");
	}

	@Override
	public String toString() {
		return name;
	}

	public static int testMul(int x, int y) {
		System.out.println("app: Main.testMul()");
		int a = x;
		int b = y;
		int mul = a * b;
		System.out.println("app: a=" + a);
		System.out.println("app: b=" + b);
		System.out.println("app: mul=" + mul);
		return mul;
	}

	public static void testException() {
		System.out.println("app: Main.testException()");
		throw new RuntimeException("app-exception");
	}

	public static void main(String[] args) {
		testMul(3, 5);

		try {
			testException();
		} catch (Throwable e) {
		}

		Main m = new Main();
		m.testInstance();
	}
}