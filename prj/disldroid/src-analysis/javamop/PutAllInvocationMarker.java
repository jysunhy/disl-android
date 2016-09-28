package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class PutAllInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Map.putAll*(..)";
	}

}


