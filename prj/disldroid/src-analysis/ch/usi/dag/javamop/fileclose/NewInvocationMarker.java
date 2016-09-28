package ch.usi.dag.javamop.fileclose;


/**
 * Sets the region on every method invocation instruction.
 *
 */
public class NewInvocationMarker extends AbstractInvocationMarker {

	//String name;

	@Override
    public String getMethodName(){

			return "<init>";


	}

	@Override
    public String testClassName(){

			return "Main";

	}

	@Override
    public String getSignature(){
		    //Have to add ";" for condition to be true, this needs to be improved. Revisit this
			return "(Ljava/io/File;)V";


	}
	//Ask Yudi about having receiver or not
	@Override
    public String getReceiver(){
		return "java/io/FileWriter";
	}


}
