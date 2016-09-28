package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ElementInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
			return "java.util.Enumeration java.util.Vector+.elements()";
	}


}
