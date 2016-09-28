package ch.usi.dag.javamop.unsafemap;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class PutInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "put";
	}

	@Override
    public String testClassName(){

			return "Main";

	}

	@Override
    public String getSignature(){
		    //Have to add ";" for condition to be true, this needs to be improved. Revisit this
			return "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";


	}

	@Override
    public String getReceiver(){
		return "java/util/Map.put";
	}
}
