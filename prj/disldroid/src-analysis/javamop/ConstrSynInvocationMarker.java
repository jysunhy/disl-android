package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ConstrSynInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		//return "* java.util.Collections.synchr*(..)";
        return "java.util.Map java.util.Collections.synchr*(..)";
	}

}
