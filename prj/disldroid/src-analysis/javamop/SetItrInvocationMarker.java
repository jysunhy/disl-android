package javamop;

public class SetItrInvocationMarker extends AbstractInvocationMarker {

	public String getMethodDescription(){
		//return "* java.util.Collection+.iterator()";
		return "java.util.Iterator java.util.Set.iterator()";
	}
}
