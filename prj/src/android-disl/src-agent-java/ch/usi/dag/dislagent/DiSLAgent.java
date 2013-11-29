package ch.usi.dag.dislagent;

import java.lang.instrument.Instrumentation;

import ch.usi.dag.disl.dynamicbypass.Bootstrap;

public class DiSLAgent {

	private static volatile Instrumentation myInstrumentation;

	public static void premain(String agentArguments,
			Instrumentation instrumentation) {
		myInstrumentation = instrumentation;
		
		if (!Boolean.getBoolean("dislserver.noBootstrap")) {
			Bootstrap.completed(instrumentation);
		}
	}
	
	public static Instrumentation getInstrumentation() {
		return myInstrumentation;
	}
}
