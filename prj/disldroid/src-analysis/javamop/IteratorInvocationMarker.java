package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class IteratorInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Iterator.*(..)";

	}


}
