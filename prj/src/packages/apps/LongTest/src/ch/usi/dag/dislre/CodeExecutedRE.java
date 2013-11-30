package ch.usi.dag.dislre;

import ch.usi.dag.dislre.REDispatch;

// Optimally, this class is automatically created on analysis machine
// and redefines during loading the CodeExecuted class on the client vm

// Even more optimally, this is automatically generated native class with same
// functionality
public class CodeExecutedRE {

	private static short beId = REDispatch.registerMethod(
			"ch.usi.dag.disl.test.dispatch.CodeExecuted.bytecodesExecuted");

	private static short tbId = REDispatch.registerMethod(
			"ch.usi.dag.disl.test.dispatch.CodeExecuted.testingBasic");
	
	private static short taId = REDispatch.registerMethod(
			"ch.usi.dag.disl.test.dispatch.CodeExecuted.testingAdvanced");
	
	private static short ta2Id = REDispatch.registerMethod(
			"ch.usi.dag.disl.test.dispatch.CodeExecuted.testingAdvanced2");
	
	private static short tnId = REDispatch.registerMethod(
			"ch.usi.dag.disl.test.dispatch.CodeExecuted.testingNull");
	
	public static void bytecodesExecuted(int count) {
		
		final byte orderingid = 1;
		REDispatch.analysisStart(beId, orderingid);
		
		REDispatch.sendInt(count);
		
		REDispatch.analysisEnd();
	}
	
	public static void testingBasic(boolean b, byte by, char c, short s, int i,
			long l, float f, double d) {
		
		REDispatch.analysisStart(tbId);
		
		REDispatch.sendBoolean(b);
		REDispatch.sendByte(by);
		REDispatch.sendChar(c);
		REDispatch.sendShort(s);
		REDispatch.sendInt(i);
		REDispatch.sendLong(l);
		REDispatch.sendFloat(f);
		REDispatch.sendDouble(d);
		
		REDispatch.analysisEnd();
	}
	
	public static void testingAdvanced(String s, Object o, Class<?> c,
			Thread t) {
		
		REDispatch.analysisStart(taId);
		
		REDispatch.sendObjectPlusData(s);
		REDispatch.sendObject(o);
		REDispatch.sendObject(c);
		REDispatch.sendObjectPlusData(t);
		
		REDispatch.analysisEnd();
	}

	public static void testingAdvanced2(Object o1, Object o2, Object o3,
			Object o4, Class<?> class1, Class<?> class2, 
			Class<?> class3, Class<?> class4) {

		REDispatch.analysisStart(ta2Id);
		
		REDispatch.sendObject(o1);
		REDispatch.sendObject(o2);
		REDispatch.sendObject(o3);
		REDispatch.sendObject(o4);
		REDispatch.sendObject(class1);
		REDispatch.sendObject(class2);
		REDispatch.sendObject(class3);
		REDispatch.sendObject(class4);
		
		REDispatch.analysisEnd();		
	}
	
	public static void testingNull(String s, Object o, Class<?> c) {
		
		REDispatch.analysisStart(tnId);
		
		REDispatch.sendObjectPlusData(s);
		REDispatch.sendObject(o);
		REDispatch.sendObject(c);
		
		REDispatch.analysisEnd();
	}
}
