package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class CAddInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Collection+.add*(..)";
	}

}
