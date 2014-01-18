package ch.usi.dag.disl.test.suite.android.instr;

import java.util.LinkedList;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;

public class DiSLClass {

	@After(marker = BasicBlockMarker.class, scope = "MainActivity.*")
	public static void invokedInstr(final CodeLengthSC clsc) {

		ch.usi.dag.disl.test.suite.android.instr.CodeExecutedRE.bytecodesExecuted(clsc.codeSize());
	}

	@After(marker = BodyMarker.class, scope = "MainActivity.add")
	public static void testing() {

		ch.usi.dag.disl.test.suite.android.instr.CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
				100000, 10000000000L, 1.5F, 2.5);

		ch.usi.dag.disl.test.suite.android.instr.CodeExecutedRE.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());

		ch.usi.dag.disl.test.suite.android.instr.CodeExecutedRE.testingAdvanced2(new LinkedList<String>(),
				new LinkedList<Integer>(), new LinkedList[0], new int[0],
				int[].class, int.class, LinkedList.class,
				LinkedList.class.getClass());

		ch.usi.dag.disl.test.suite.android.instr.CodeExecutedRE.testingNull(null, null, null);
	}
}