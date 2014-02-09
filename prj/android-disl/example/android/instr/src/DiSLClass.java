import java.util.LinkedList;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import android.os.Process;

import android.util.Log;

public class DiSLClass {
	/*
	@After(marker = BasicBlockMarker.class, scope = "MainActivity.*")
		public static void invokedInstr(final CodeLengthSC clsc) {

			CodeExecutedRE.bytecodesExecuted(clsc.codeSize());
		}

	//works Log.v will bring another println
	@After(marker = BodyMarker.class, scope = "Log.v")
		public static void testLog(MethodStaticContext sc, ArgumentProcessorContext pc){
			System.out.println("DISL: IN Log v");
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			if(args[0].getClass().getCanonicalName().equals("java.lang.String")){
				System.out.println("DISLTAG: "+args[0].toString());
			}
		}
	//	works
	@After(marker = BodyMarker.class, scope = "Toast.makeText")
		public static void testToast(MethodStaticContext sc, ArgumentProcessorContext pc){
			System.out.println("DISL: IN Toast.makeText");
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			if(args[1].getClass().getCanonicalName().equals("java.lang.String")){
				System.out.println("TOAST TEXT: "+args[1].toString());
			}
		}

	//it's an App add function
	@After(marker = BodyMarker.class, scope = "MainActivity.add")
		public static void testingadd(MethodStaticContext sc, ArgumentProcessorContext pc) {
			for(int i = 0; i < 1; i++) {
				CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
						100000, 10000000000L, 1.5F, 2.5);

				CodeExecutedRE.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());

				CodeExecutedRE.testingAdvanced2(new LinkedList<String>(),
						new LinkedList<Integer>(), new LinkedList[0], new int[0],
						int[].class, int.class, LinkedList.class,
						LinkedList.class.getClass());

				CodeExecutedRE.testingNull(null, null, null);
			}
		}
	//
	//nothing
	@After(marker = BodyMarker.class, scope = "MainActivity.substraction")
		public static void restartAnalysis() {
			CodeExecutedRE.dislStart();
			CodeExecutedRE.methodsRegister();
		}
	//nothing
	*/
	@After(marker = BodyMarker.class, scope = "MainActivity.multiplication")
		public static void endAnalysis() {
			CodeExecutedRE.dislEnd();
		}
}
