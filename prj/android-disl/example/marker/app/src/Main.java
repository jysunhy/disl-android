public class Main {

	public static void test1() {
		System.out.println("app: Main.test1()");
	}

	public static void test2() {
		System.out.println("app: Main.test2()");
	}

	public static void test3() {
		System.out.println("app: Main.test3()");
	}

	public static void testThrow() {
		System.out.println("app: Main.testThrow()");
		throw new RuntimeException();
	}

	public static void testNoThrow() {
		System.out.println("app: Main.testNoThrow()");
	}

	public static void main(String[] args) {
		test1();
		test2();
		test3();

		try {
			testThrow();
		} catch (Throwable e) {
		}

		testNoThrow();
	}
}
