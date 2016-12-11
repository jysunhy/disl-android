package javamop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
public class IdentifyClass {
		//static ArrayList<String> interList = new ArrayList<String>();
	 	static final Map<String, Boolean> isClass = new HashMap<String, Boolean>();
	    static String interfacesToCheck;
	    static Boolean methodExists;
	    static Boolean isPlus=false;

	    static String ClassFromInterface(String Owner, final String name, String methodDescription, final String outerClass, final String outerMethod){

	    	final String noChangeDesc = methodDescription;
	    	methodExists=false;
	    	isPlus = false;
//		//	if(name.equals("<init>")){
//				return methodDescription;
//			}

	    	final String onlyClassName = Owner.substring(Owner.lastIndexOf("/")+1,Owner.length());

	    	if(Owner.equals(outerClass)){
	    		//System.out.println("Hello World");
	    	}

//
	    	//System.out.println("Owner="+Owner +" "+"outClass="+outerClass);

	    		//System.out.println("Method name is="+name +"and outmethodname is"+outerMethod );


	    		if(methodDescription.contains("+")){

	    			isPlus = true;  // if aspectJ contains+ then we need to exclude classes which are extended only allow superinterface.
	    		}

	    		methodDescription = methodDescription.replace("+","");
	    		Owner = Owner.replace("/", ".");
	    		setParameters(methodDescription);
	    		checkMethod(Owner,name, methodDescription,interfacesToCheck);
	    		//System.out.println("methodExists"+methodExists);
	    		if(__checkInterfaces(Owner, isClass, interfacesToCheck,name,outerMethod,outerClass)){


	    				//checkMethod(name, interfacesToCheck);
	    				methodDescription = methodDescription.replace(interfacesToCheck,Owner);

	    		}


	    			//System.out.println("Exiting normally");
	    			//System.out.println("Exit with method Description"+methodDescription);
	    		return methodDescription;
	    }

	    private static void setParameters(final String methodDescription) {

	    	//Only require the interface name for conditional checks.

			String remainingValue=methodDescription;
	    	if(methodDescription.contains(" ")){
	    		remainingValue = methodDescription.split(" ")[1];
	    	}

	    	interfacesToCheck = remainingValue.substring(0,remainingValue.lastIndexOf("("));

	    	if(interfacesToCheck.contains(".")){
	    		interfacesToCheck = interfacesToCheck.substring(0,interfacesToCheck.lastIndexOf("."));
	    	}
		}

		static boolean __checkInterfaces(final String className, final Map<String, Boolean> hashMapCache, final String interfacesToCheck,final String name ,final String outerMethod, final String outerClass){
			//System.out.println("Inside __checkInterfaces="+className+" " +interfacesToCheck);

			final Boolean alreadyMatched = hashMapCache.get(className+"&"+interfacesToCheck);
			if (alreadyMatched == null) { // Class not already processed

				//System.out.println(name+" "+outerMethod);

	    		Class<?> cl = null;
	    		boolean matches = false;

	    		// Check if class implements interfaces specified in the set
	    		try {
	    		cl = Class.forName(className);

	    		} catch (final ClassNotFoundException e) {

	    			try {
	    				final File file = new File("ClassFileList.txt");
	    				final FileWriter fileWriter = new FileWriter(file.getName(),true);
	    				final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	    				bufferedWriter.write("================================="+"\n");
	    				bufferedWriter.write("Keyed class not found="+className+"\n");
	    				bufferedWriter.write("================================="+"\n");

	    				bufferedWriter.close();
	    			} catch(final IOException ex){ex.printStackTrace();}

	    		} catch (final Throwable t) {}
	    		//System.out.println("After Checking conditions for the class existance");


	    		if (cl != null) {

	    			matches = __checkInterfacesOfClass(cl, interfacesToCheck);
	    		}

	    		// Now check also the class' superclasses, traversing the
	    		// hierarchy

	    			while (cl != null && !matches) {
	    				//System.out.println("Checking for isPlus");

	    					cl = cl.getSuperclass();

	    					//System.out.println("Existing from isPlus condition");
	    					matches = __checkInterfacesOfClass(cl, interfacesToCheck);
	    			}

	    		hashMapCache.put(className+"&"+interfacesToCheck, matches);
	    		//System.out.println("Leaving matches on first attempt="+matches);
	    		return matches;

	    		}

	    	else{

	    		return alreadyMatched.booleanValue();
	    	}

	    }

	    private static boolean __checkInterfacesOfClass(final Class<?> cl, final String interfacesToCheck) {
	    	//System.out.println("Inside __checkInterfacesOfClass");
	    	if (cl == null) {
                return false;
            }

	    	if(checkString(cl.getName(),interfacesToCheck)){
	    		//System.out.println("success on first Attempt");
	    		return true;
	    	}

	    	final Class<?>[] interfaces = cl.getInterfaces();

	    		for (final Class <?> interface1 : interfaces) {
	    			String ifName = interface1.getName();
	    			ifName = checkForTopHierarchy(ifName,interfacesToCheck);
	    			//System.out.println("After Checking TopHeirarchy="+ ifName+" "+interfacesToCheck);

	    			if (checkString(ifName, interfacesToCheck)) {
                        return true;
                    }
	    			}

	    	return false;
	    }


	    public static boolean checkString(final String stringToCheck, final String stringsAllowed) {

		    if (stringToCheck.equals(stringsAllowed)) {
		    	return true;
		    }

		    	return false;
	    }


//		public static String checkForTopHierarchy(String ifName,String interfacesToCheck){
//			String topInterface = null;
//			try{
//				Class<?> cl = Class.forName(ifName);
//				Class<?>[] interfaces = cl.getInterfaces();
//
//				for(int i =0;i<interfaces.length;i++){
//					topInterface = interfaces[i].getName();
//
//					if(topInterface!=null){
//
//						if(topInterface.equals(interfacesToCheck)){  //In case top most interface is equal to interfaces to Check
//							ifName = topInterface;
//						}
//					}
//				}
//
//			} catch(Exception ex){}
//
//			return ifName;
//
//		}

//Better way to do is to make this recursive function,
//Idea is to use replaceme method

	    public static String checkForTopHierarchy(String ifName,final String interfacesToCheck){


			String topInterface = null;
			String topInterface_inner = null;
			try{
				final Class<?> cl = Class.forName(ifName);
				final Class<?>[] interfaces = cl.getInterfaces();

				for (final Class <?> interface1 : interfaces) {
					topInterface = interface1.getName();

					if(topInterface!=null){

						if(topInterface.equals(interfacesToCheck)){  //In case top most interface is equal to interfaces to Check
							ifName = topInterface;
						}
						else{
							final Class<?> cl1 = Class.forName(topInterface);
							final Class<?>[] interfaces1 = cl1.getInterfaces();
							topInterface_inner = interfaces1[0].getName();
								if(topInterface_inner.equals(interfacesToCheck)){  //In case top most interface is equal to interfaces to Check
									ifName = topInterface_inner;
								}
							}
					}
				}
			}

			 catch(final Exception ex){}

			return ifName;

		}

//	    public static void checkMethod(String Owner, String methodName, String interfaceName){
//
//	    	try {
//	            Class<?> c = Class.forName(interfaceName);
//	            //Method[] m = c.getDeclaredMethods();
//	            Method[] m = c.getMethods();
//	            for (int i = 0; i < m.length; i++){
//
//	            	String mName = m[i].toString();
//	            	try{
//	            		System.out.println("interfaceName="+interfaceName);
//	            		System.out.println("Method Name="+mName);
//	            		mName = mName.substring(0,mName.indexOf("("));
//	            		mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());
//	            	}catch(Exception ex){
//
//	            		System.err.println("Exception raised");
//	            	}
//	            	System.out.println("Receiver="+ Owner);
//	            	System.out.println("InterfaceName"+interfaceName+" "+"Method names are="+mName+" "+"to be compared with"+methodName);
//	            	if(mName.equals(methodName)){
//	            		System.out.println("Compared="+mName);
//	            		methodExists = true;
//	            	}
//
//	            }
//	        } catch (Throwable e) {
//	            System.err.println(e);
//	        	}
//	    }


	    public static Boolean allowSubInterfaces(final String JoinPointDescription, final AbstractInsnNode instruction, final MethodInsnNode methodInsn){
	    		if(!JoinPointDescription.contains("+")){

	    			final String name = methodInsn.owner.replace("/",".");

	    			System.out.println("JoinPointDescription="+JoinPointDescription);
	    			System.out.println("MethodInsn.owner="+methodInsn.owner);

	    			if((instruction.getOpcode()!=Opcodes.INVOKEINTERFACE) || (JoinPointDescription.contains(name))){
	    				return true;
	    			}
	    			else{
	    				return false;
	    			}
	    		}
	    		else{
	    			return true;
	    		}

	    }


	    public static  void checkMethod(final String Owner, final String mName, String mTest, final String interfaceName){

	    	//mName = mName.substring(0,mName.indexOf("("));
     		//mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());

     		mTest = mTest.substring(0,mTest.indexOf("("));
    		mTest = mTest.substring(mTest.lastIndexOf(".")+1,mTest.length());


    			//System.out.println("mTest="+mTest+" "+"mName="+mName);

    		if(mTest.endsWith("*")){
    			//System.out.println("entering endsWith");
    			final String onlyMethod = mTest.substring(0,mTest.indexOf("*"));
    			if(!onlyMethod.isEmpty()){
    				//System.out.println("Not Empty String");
    				if(mName.startsWith(onlyMethod)){
    					//System.out.println("Inside condition");
   // 					methodExists = isPlus?true:getMethods(mName,Owner);
    					methodExists = true;
    					//System.out.println("What is the value of methodExists="+methodExists);
    				}
    			}
    		}

    		//System.out.println("Leaving endsWith");
    		if(mTest.startsWith("*")){
    			//System.out.println("entering StartsWith");
    			final String onlyMethod = mTest.substring(mTest.indexOf("*")+1,mTest.length());
    			if(!onlyMethod.isEmpty()){
    				if(mTest.endsWith(onlyMethod)){

    					methodExists = true;
    				}
    			}
    		}
    		if(mTest.equals("*")){
    			//System.out.println("MethodTest is equal");
    			methodExists = getMethods(mName,interfaceName);

    		}

    		if(mTest.equals(mName)){

    			methodExists = true;
}

//	    	try {
//	            Class<?> c = Class.forName(interfaceName);
//	            //Method[] m = c.getDeclaredMethods();
//	            Method[] m = c.getMethods();
//	            for (int i = 0; i < m.length; i++){
//
//	            	String mName = m[i].toString();
//	            	try{
//	            		System.out.println("interfaceName="+interfaceName);
//	            		System.out.println("Method Name="+mName);
//	            		mName = mName.substring(0,mName.indexOf("("));
//	            		mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());
//	            	}catch(Exception ex){
//
//	            		System.err.println("Exception raised");
//	            	}
//	            	System.out.println("Receiver="+ Owner);
//	            	System.out.println("InterfaceName"+interfaceName+" "+"Method names are="+mName+" "+"to be compared with"+methodName);
//	            	if(mName.equals(methodName)){
//	            		System.out.println("Compared="+mName);
//	            		methodExists = true;
//	            	}
//
//	            }
//	        } catch (Throwable e) {
//	            System.err.println(e);
//	        	}
	    }

	    public static Boolean getMethods(final String methodTest,final String interfaceName){
	    	Boolean testMethod = false;
	    	try {
	            final Class<?> c = Class.forName(interfaceName);

	            final Method[] m = c.getDeclaredMethods();
	            for (final Method element : m) {
	            	String mName = element.toString();
            		mName = mName.substring(0,mName.indexOf("("));
            		mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());
            		//System.out.println("MethodTest="+methodTest);
            		//System.out.println("mName"+mName);
            		if(methodTest.equals(mName)){
            			testMethod=true;
            			//System.out.println("Value of testMethod="+testMethod);
            			break;
            		}


            	}


	    	}catch(final Exception ex){}
	    	catch(final Throwable t){}

	    	return testMethod;
	    }


	    static Boolean allowExtends(final String Owner, final String interfaceName){
	    	//System.out.println("Inside allowExtends");
	    	//System.out.println("Owner="+Owner);
	    	//System.out.println("interfaceName="+interfaceName);
	    	try{
	    		final Class<?> superClass = Class.forName(interfaceName);
	    		final Class<?> classToTest = Class.forName(Owner);
	    		//System.out.println("Testing condition is about to start");
	    		if(classToTest.isAssignableFrom(superClass)){
	    			//System.out.println(Owner+"is a sub class of"+interfaceName);
	    			//return true;
	    		}

	    		//System.out.println("Test condition went well, I think");
	    	}catch(final Exception ex){
	    	}
	    	catch(final Throwable t){}
	    	return false;

	    }

	    public static Boolean isInterfaceMethod(){
	    		//System.out.println("Inside isInterfaceMethod="+methodExists);
	    		return methodExists;
	    }



}
