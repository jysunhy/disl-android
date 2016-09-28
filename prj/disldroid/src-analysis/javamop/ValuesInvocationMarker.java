package javamop;

/**
 * Sets the region on every method invocation instruction.
 * 
 */
public class ValuesInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Map.values()";
	}	
}
