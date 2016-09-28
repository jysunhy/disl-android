package ch.usi.dag.javamop.safefile;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class CloseInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "close";


	}

	@Override
    public String testClassName(){

		return "Main";

	}

	@Override
    public String getSignature(){
		// Have to add ";" for condition to be true, this needs to be improved. Revisit this
		return "()V";
    }
//	Ask Yudi about having receiver or not
	@Override
    public String getReceiver(){
		return "java/io/FileReader";
	}


}
