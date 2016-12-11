package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ConstrOSynInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "* java.util.Collections.synchr*(..)";
       
	}

}
