package javamop;

/**
 * Sets the region on every method invocation instruction.
 * 
 */
public class ConstrItInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "java.util.Iterator java.util.Collection+.iterator()";

		//return "java.util.Iterator java.util.Set.iterator()";
	}

}
