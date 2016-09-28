package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class WriteInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.io.FileWriter+.write(..)";
	}


}
