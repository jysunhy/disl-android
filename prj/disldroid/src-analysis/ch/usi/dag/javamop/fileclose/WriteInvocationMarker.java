package ch.usi.dag.javamop.fileclose;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class WriteInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "write";


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
	//Ask Yudi about having receiver or not
	@Override
    public String getReceiver(){
		return "java/io/FileWriter";
	}


}
