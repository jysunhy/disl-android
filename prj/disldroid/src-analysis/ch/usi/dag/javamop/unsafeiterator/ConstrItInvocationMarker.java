package ch.usi.dag.javamop.unsafeiterator;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class ConstrItInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "iterator";
	}

	@Override
    public String testClassName(){

			return "Main";
	}

	@Override
    public String getSignature(){
		    //Have to add ";" for condition to be true, this needs to be improved. Revisit this
			return "()Ljava/util/Iterator;";
	}

	@Override
    public String getReceiver(){
		return "java/util/Set";
	}
}