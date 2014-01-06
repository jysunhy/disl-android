import java.util.LinkedList;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import android.os.Process;

public class DiSLClass {
	/*
	   @After(marker = BasicBlockMarker.class, scope = "MainActivity.*")
	   public static void invokedInstr(final CodeLengthSC clsc) {

	   CodeExecutedRE.bytecodesExecuted(clsc.codeSize());
	   }
	   */

/*
	@AfterReturning(marker = BodyMarker.class, scope = "MainActivity.add")
		public static void testreturn(DynamicContext di) {
			int ret = di.getStackValue(0, int.class);
			System.out.println("disl: \t\t\t\treturn with:\t " + ret);
		}   
		*/
	/*
	@AfterReturning(marker = BodyMarker.class, scope = "Process.startViaZygote")
		public static void postcondition2(DynamicContext di) {
			Process.ProcessStartResult ret = di.getStackValue(0, Process.ProcessStartResult.class);
	//		CodeExecutedRE.mapPID(pname, ret.pid);
			pid = ret.pid;
			System.out.println("disl: \t\t\t\tProcess GET PID:\t" + ret.pid+"\t PNAME:"+pname);
		}   
*/

	@AfterReturning(marker = BodyMarker.class, scope = "Process.startViaZygote")
		public static void testprocess(MethodStaticContext sc, ArgumentProcessorContext pc,DynamicContext di){
			Process.ProcessStartResult ret = di.getStackValue(0, Process.ProcessStartResult.class);
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			System.out.println("*****************************DISL INJECTED*****************************");
			System.out.printf("disl: args for %s %s\n", sc.thisMethodFullName(), sc.thisMethodDescriptor());
			String pname="default";
			if(args[1].getClass().getCanonicalName().equals("java.lang.String")){
				pname = args[1].toString();
			}
			System.out.println("disl: \t\tProcess GET PID:\t" + ret.pid+"\t PNAME:"+pname);
	//		if(pname.equals("com.inspur.test"))
	//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
	//				100000, 10000000000L, 1.5F, 2.5);
			for(int i = 0 ; i < args.length; ++i) {
				System.out.printf("disl: \targ[%d]\n", i);

				if(args[i] == null)
					continue;
				if (args[i] instanceof Object[]) {
					Object[] argsarr = (Object[])args[i];
					for (Object arg : argsarr) {
						if(arg == null)
							continue;
						if (arg instanceof Object[]) {
							Object[] argsarr2 = (Object[])arg;
							for (Object arg2 : argsarr2) {
								if(arg2 == null)
									continue;
								Object a = (Object)arg2;		
								String n = a.getClass().getCanonicalName();
								switch (n) {
									case "java.lang.Integer":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Float":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Double":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.String":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									default:
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t== cannot print ==\n");
										break;
								} 
							}

						} else {
							Object a = (Object)arg;		
							String n = a.getClass().getCanonicalName();
							switch (n) {
								case "java.lang.Integer":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Float":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Double":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.String":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								default:
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t== cannot print ==\n");
									break;
							} 
						}			
					}				

				} else {
					Object a = args[i];	
					String n = a.getClass().getCanonicalName();
					switch (n) {
						case "java.lang.Integer":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Float":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Double":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.String":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						default:
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t== cannot print ==\n");
							break;
					} 
				}
			}
		}

/*

	@After(marker = BodyMarker.class, scope = "ActivityManagerService.startProcessLocked")
		public static void getPID(MethodStaticContext sc, ArgumentProcessorContext pc){
			System.out.println("GETTING PID HERE");
			System.out.printf("disl: args for %s %s\n", sc.thisMethodFullName(), sc.thisMethodDescriptor());
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			for(int i = 0 ; i < args.length; ++i) {
				System.out.printf("disl: \targ[%d]\n", i);

				if(args[i] == null)
					continue;
				if (args[i] instanceof Object[]) {
					Object[] argsarr = (Object[])args[i];
					for (Object arg : argsarr) {
						if(arg == null)
							continue;
						if (arg instanceof Object[]) {
							Object[] argsarr2 = (Object[])arg;
							for (Object arg2 : argsarr2) {
								if(arg2 == null)
									continue;
								Object a = (Object)arg2;		
								String n = a.getClass().getCanonicalName();
								switch (n) {
									case "java.lang.Integer":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Float":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Double":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.String":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									default:
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t== cannot print ==\n");
										break;
								} 
							}

						} else {
							Object a = (Object)arg;		
							String n = a.getClass().getCanonicalName();
							switch (n) {
								case "java.lang.Integer":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Float":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Double":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.String":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								default:
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t== cannot print ==\n");
									break;
							} 
						}			
					}				

				} else {
					Object a = args[i];	
					String n = a.getClass().getCanonicalName();
					switch (n) {
						case "java.lang.Integer":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Float":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Double":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.String":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						default:
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t== cannot print ==\n");
							break;
					} 
				}
			}
		}
*/

	
	@After(marker = BodyMarker.class, scope = "MainActivity.add")
		public static void testingadd(MethodStaticContext sc, ArgumentProcessorContext pc) {
			/*
			System.out.printf("disl: args for %s %s\n", sc.thisMethodFullName(), sc.thisMethodDescriptor());
			Object[] args = pc.getArgs(ArgumentProcessorMode.METHOD_ARGS);
			for(int i = 0 ; i < args.length; ++i) {
				System.out.printf("disl: \targ[%d]\n", i);


				if (args[i] instanceof Object[]) {
					Object[] argsarr = (Object[])args[i];
					for (Object arg : argsarr) {

						if (arg instanceof Object[]) {
							Object[] argsarr2 = (Object[])arg;
							for (Object arg2 : argsarr2) {
								Object a = (Object)arg2;		
								String n = a.getClass().getCanonicalName();
								switch (n) {
									case "java.lang.Integer":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Float":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.Double":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									case "java.lang.String":
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t%s\n", a.toString());
										break;
									default:
										System.out.printf("disl: \t\t\t\t%s\n", n);
										System.out.printf("disl: \t\t\t\t== cannot print ==\n");
										break;
								} 
							}

						} else {
							Object a = (Object)arg;		
							String n = a.getClass().getCanonicalName();
							switch (n) {
								case "java.lang.Integer":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Float":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.Double":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								case "java.lang.String":
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t%s\n", a.toString());
									break;
								default:
									System.out.printf("disl: \t\t\t%s\n", n);
									System.out.printf("disl: \t\t\t== cannot print ==\n");
									break;
							} 
						}			
					}				

				} else {
					Object a = args[i];	
					String n = a.getClass().getCanonicalName();
					switch (n) {
						case "java.lang.Integer":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Float":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.Double":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						case "java.lang.String":
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t%s\n", a.toString());
							break;
						default:
							System.out.printf("disl: \t\t%s\n", n);
							System.out.printf("disl: \t\t== cannot print ==\n");
							break;
					} 
				}
			}
*/
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
	@After(marker = BodyMarker.class, scope = "MainActivity.substraction")
		public static void restartAnalysis() {
			CodeExecutedRE.dislStart();
			CodeExecutedRE.methodsRegister();
		}
	@After(marker = BodyMarker.class, scope = "MainActivity.multiplication")
		public static void endAnalysis() {
			CodeExecutedRE.dislEnd();
		}
}
