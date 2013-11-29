public class Main {

	public static void main(String[] args) {
		System.out.println("app: Main.main(..)");

		my.ClassTest mct = new my.ClassTest();
		my.pkg.ClassTest mpct = new my.pkg.ClassTest();
		my.pkg.ClassBigTest mpcbt = new my.pkg.ClassBigTest();

		mct.computeMul(42, 42);
		mct.computeFastMul(42, 42);
		mct.complexComputeMul(42, 42, 42, 42.42, 42.42, 42.42);
		mpct.computeMul(42, 42);
		mpct.computeFastMul(42, 42);
		mpcbt.computeDiv(42, 42);
		mpcbt.computeFastDiv(42, 42);
	}
}
