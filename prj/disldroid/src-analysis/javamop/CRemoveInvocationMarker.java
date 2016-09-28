package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class CRemoveInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Collection+.remove*(..)";

	}

}
