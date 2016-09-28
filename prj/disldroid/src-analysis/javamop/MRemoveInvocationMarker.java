package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class MRemoveInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Map.remove*(..)";
	}

}


