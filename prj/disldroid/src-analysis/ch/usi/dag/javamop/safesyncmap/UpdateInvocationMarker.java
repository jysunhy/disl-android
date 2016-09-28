package ch.usi.dag.javamop.safesyncmap;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class UpdateInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "update";


	}

	@Override
    public String testClassName(){

			return "Main";

	}

	@Override
    public String getSignature(){
		    //Have to add ";" for condition to be true, this needs to be improved. Revisit this
			return "(Ljava/lang/String;)V";


	}

	@Override
    public String getReceiver(){
		return "Main$Item";
	}


}
