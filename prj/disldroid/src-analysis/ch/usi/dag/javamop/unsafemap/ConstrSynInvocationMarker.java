package ch.usi.dag.javamop.unsafemap;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ConstrSynInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "synchronizedMap";
	}

	@Override
    public String testClassName(){

			return "Main";
	}

	@Override
    public String getSignature(){
		    //Have to add ";" for condition to be true, this needs to be improved. Revisit this
			return "(Ljava/util/Map;)Ljava/util/Map;";
	}

	@Override
    public String getReceiver(){
		return "java/util/Collections";
	}
}
