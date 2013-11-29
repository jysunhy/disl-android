package ch.usi.dag.disl.test.suite.args.instr;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class DiSLClass {
		
	@AfterReturning(marker = BodyMarker.class, scope = "TargetClass.*")
	public static void postcondition(MethodStaticContext sc, ArgumentProcessorContext pc) {
		
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
	}
}
