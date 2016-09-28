package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class PutInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Map.put*(..)";
	}

}


