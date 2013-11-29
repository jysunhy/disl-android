package ch.usi.dag.disl.dynamicbypass;

public class DynamicBypassCheck {

	// this version is executed before bootstrap phase
	// it will be after bootstrap replaced by with version in src-dynbypass-a
	// directory
	public static boolean executeUninstrumented() {
		return true;
	}
}
