package javamop;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ColItrInvocationMarker extends AbstractInvocationMarker {
	public String getMethodDescription(){
		return "* java.util.Collection.iterator()";
	}
}
