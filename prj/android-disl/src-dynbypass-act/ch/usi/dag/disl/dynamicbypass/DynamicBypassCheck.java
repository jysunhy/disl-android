package ch.usi.dag.disl.dynamicbypass;


public class DynamicBypassCheck {

	// this version is executed after bootstrap phase
	// and is it the replacement for version in src-dynbypass directory
	public static boolean executeUninstrumented() {
		return DynamicBypass.isActivated();
	}
}
