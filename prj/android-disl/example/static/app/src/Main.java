import java.util.Random;

public class Main {

	public static void testLoop() {
		Random rnd = new Random(42);
		int[] arr = new int[10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rnd.nextInt();
		}
	}

	public static void testNoLoop() {
		Random rnd = new Random(42);
		int[] arr = new int[3];
		arr[0] = rnd.nextInt();
		arr[1] = rnd.nextInt();
		arr[2] = rnd.nextInt();
	}

	public static void main(String[] args) {
		testLoop();
		testNoLoop();
	}
}
