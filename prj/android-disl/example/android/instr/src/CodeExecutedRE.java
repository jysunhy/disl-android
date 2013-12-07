import ch.usi.dag.dislre.AREDispatch;

// Optimally, this class is automatically created on analysis machine
// and redefines during loading the CodeExecuted class on the client vm

// Even more optimally, this is automatically generated native class with same
// functionality
public class CodeExecutedRE {

	private static short beId = AREDispatch.registerMethod(
			"ch.usi.dag.disl.test.suite.android.instr.CodeExecuted.bytecodesExecuted");

	private static short tbId = AREDispatch.registerMethod(
			"ch.usi.dag.disl.test.suite.android.instr.CodeExecuted.testingBasic");

	private static short taId = AREDispatch.registerMethod(
			"ch.usi.dag.disl.test.suite.android.instr.CodeExecuted.testingAdvanced");

	private static short ta2Id = AREDispatch.registerMethod(
			"ch.usi.dag.disl.test.suite.android.instr.CodeExecuted.testingAdvanced2");

	private static short tnId = AREDispatch.registerMethod(
			"ch.usi.dag.disl.test.suite.android.instr.CodeExecuted.testingNull");

	public static void bytecodesExecuted(final int count) {

		final byte orderingid = 1;
		AREDispatch.analysisStart(beId, orderingid);

		AREDispatch.sendInt(count);

		AREDispatch.analysisEnd();
	}
	public static void testingBasic(final boolean b, final byte by, final char c, final short s, final int i,
			final long l, final float f, final double d) {
		AREDispatch.analysisStart(tbId);

		AREDispatch.sendBoolean(b);
		AREDispatch.sendByte(by);
		AREDispatch.sendChar(c);
		AREDispatch.sendShort(s);
		AREDispatch.sendInt(i);
		AREDispatch.sendLong(l);
		AREDispatch.sendFloat(f);
		AREDispatch.sendDouble(d);

		AREDispatch.analysisEnd();
	}

	public static void testingAdvanced(final String s, final Object o, final Class<?> c,
			final Thread t) {

		AREDispatch.analysisStart(taId);

		AREDispatch.sendObjectPlusData(s);
		AREDispatch.sendObject(o);
		AREDispatch.sendObject(c);
		AREDispatch.sendObjectPlusData(t);

		AREDispatch.analysisEnd();
	}

	public static void testingAdvanced2(final Object o1, final Object o2, final Object o3,
			final Object o4, final Class<?> class1, final Class<?> class2,
			final Class<?> class3, final Class<?> class4) {

		AREDispatch.analysisStart(ta2Id);

		AREDispatch.sendObject(o1);
		AREDispatch.sendObject(o2);
		AREDispatch.sendObject(o3);
		AREDispatch.sendObject(o4);
		AREDispatch.sendObject(class1);
		AREDispatch.sendObject(class2);
		AREDispatch.sendObject(class3);
		AREDispatch.sendObject(class4);

		AREDispatch.analysisEnd();
	}

	public static void testingNull(final String s, final Object o, final Class<?> c) {

		AREDispatch.analysisStart(tnId);

		AREDispatch.sendObjectPlusData(s);
		AREDispatch.sendObject(o);
		AREDispatch.sendObject(c);

		AREDispatch.analysisEnd();
	}
}
