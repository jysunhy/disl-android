package ch.usi.dag.disl.dynamicbypass;

public class DynamicBypass {

	private static final String PROP_DEBUG = "debug";
	private static final boolean debug = Boolean.getBoolean(PROP_DEBUG);
	
	public static boolean isActivated() {
		return Thread.currentThread().bypass;
	}

	public static void activate() {
		
		// bypass should be 0 in this state
		if(debug && Thread.currentThread().bypass) {
			throw new RuntimeException(
					"Dynamic bypass fatal error: activated twice");
		}
		
		Thread.currentThread().bypass = true;
	}

	public static void deactivate() {
		
		// bypass should be 1 in this state
		if(debug && ! Thread.currentThread().bypass) {
			throw new RuntimeException(
					"Dynamic bypass fatal error: deactivated twice");
		}
		
		Thread.currentThread().bypass = false;
	}
}
