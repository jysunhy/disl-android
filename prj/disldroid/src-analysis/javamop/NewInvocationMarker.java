package javamop;

/**
 * Sets the region on every method invocation instruction.
 * 
 */
public class NewInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "java.io.FileReader.<init>(..)";
	}
	
}
