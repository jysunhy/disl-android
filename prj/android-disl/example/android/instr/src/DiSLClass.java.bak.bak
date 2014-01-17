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

	@Before(marker = BodyMarker.class, scope = "java.lang.Class.*")
		public static void testJavaLang(){ 
			//CodeExecutedRE.mapPID(sc.thisMethodFullName(), -1);
			CodeExecutedRE.testCoverage(-1);
			//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
			//					100000, 10000000000L, 1.5F, 2.5);
		}
	@Before(marker = BodyMarker.class, scope = "ZygoteInit.main")
		public static void testLoadEarlyZygote(MethodStaticContext sc){ 
			CodeExecutedRE.mapPID(sc.thisMethodFullName(), -1);
			//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
			//					100000, 10000000000L, 1.5F, 2.5);
			//System.load("shadowvm");
	}
	@Before(marker = BodyMarker.class, scope = "android.app.ActivityThread.main")
		public static void testLoadEarly(MethodStaticContext sc){ 
			CodeExecutedRE.mapPID(sc.thisMethodFullName(), -1);
			//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
			//					100000, 10000000000L, 1.5F, 2.5);
			System.loadLibrary("shadowvm");
	//		System.load("link");
			//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
			//					100000, 10000000000L, 1.5F, 2.5);
		}


	@AfterReturning(marker = BodyMarker.class, scope = "Process.startViaZygote")
		public static void testprocess(MethodStaticContext sc, ArgumentProcessorContext pc,DynamicContext di)
		{
			//Return value contatins the new pid
			Process.ProcessStartResult ret = di.getStackValue(0, Process.ProcessStartResult.class);
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			System.out.println("*****************************DISL INJECTED*****************************");
			System.out.printf("disl: args for %s %s\n", sc.thisMethodFullName(), sc.thisMethodDescriptor());
			String pname="default";
			//Second argument is the pname
			if(args[1].getClass().getCanonicalName().equals("java.lang.String")){
				pname = args[1].toString();
			}
			System.out.println("disl: \t\tProcess GET PID:\t" + ret.pid+"\t PNAME:"+pname);
			//send to the server the pid:pname
			CodeExecutedRE.mapPID(pname,ret.pid);
		}



	//it's an App add function
	@After(marker = BodyMarker.class, scope = "MainActivity.add")
		public static void testingadd(MethodStaticContext sc, ArgumentProcessorContext pc) {
			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
					100000, 10000000000L, 1.5F, 2.5);

			CodeExecutedRE.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());

			CodeExecutedRE.testingAdvanced2(new LinkedList<String>(),
					new LinkedList<Integer>(), new LinkedList[0], new int[0],
					int[].class, int.class, LinkedList.class,
					LinkedList.class.getClass());

			CodeExecutedRE.testingNull(null, null, null);
		}
	//
	//nothing
	/*
	   @After(marker = BodyMarker.class, scope = "MainActivity.substraction")
	   public static void restartAnalysis() {
	   CodeExecutedRE.dislStart();
	   CodeExecutedRE.methodsRegister();
	   }
	//nothing
	@After(marker = BodyMarker.class, scope = "MainActivity.multiplication")
	public static void endAnalysis() {
	CodeExecutedRE.dislEnd();
	}
	*/



}
