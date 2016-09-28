package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class AddInvocationMarker extends AbstractInvocationMarker {

	//String name;

	public String getMethodDescription(){
		return "* java.util.Vector+.add*(..)";
	}


}
