package ch.usi.dag.javamop.hasnext;
/**
 * Sets the region on every method invocation instruction.
 *
 */
public class NextInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "next";


	}

	@Override
    public String testClassName(){

		return "Main";

	}

	@Override
    public String getSignature(){
		// Have to add ";" for condition to be true, this needs to be improved. Revisit this
		return "()Ljava/lang/Object;";
    }
	/*	Ask Yudi about having receiver or not
	public String getReceiver(){
		return "java/util/Iterator";
	}
*/

}
