package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ItrConstrItInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		return "java.util.Iterator java.util.Collection+.iterator()";
	}

}
