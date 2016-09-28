package javamop;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import java.lang.reflect.Method;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
public class IdentifyClass {
		//static ArrayList<String> interList = new ArrayList<String>();
	 	static final Map<String, Boolean> isClass = new HashMap<String, Boolean>();
	    static String interfacesToCheck;
	    static Boolean methodExists;
	    static Boolean isPlus=false;

	    static String ClassFromInterface(String Owner, String name, String methodDescription, String outerClass, String outerMethod){

	    	String noChangeDesc = methodDescription;
	    	methodExists=false;
	    	isPlus = false;
//		//	if(name.equals("<init>")){
//				return methodDescription;
//			}

	    	String onlyClassName = Owner.substring(Owner.lastIndexOf("/")+1,Owner.length());



//




	    		if(methodDescription.contains("+")){

	    			isPlus = true;  // if aspectJ contains+ then we need to exclude classes which are extended only allow superinterface.
	    		}

	    		methodDescription = methodDescription.replace("+","");
	    		Owner = Owner.replace("/", ".");
	    		setParameters(methodDescription);
	    		checkMethod(Owner,name, methodDescription,interfacesToCheck);

	    		if(__checkInterfaces(Owner, isClass, interfacesToCheck,name,outerMethod,outerClass)){


	    				//checkMethod(name, interfacesToCheck);
	    				methodDescription = methodDescription.replace(interfacesToCheck,Owner);

	    		}



	    		return methodDescription;
	    }

	    private static void setParameters(String methodDescription) {

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

		static boolean __checkInterfaces(final String className, Map<String, Boolean> hashMapCache, final String interfacesToCheck,String name ,String outerMethod, String outerClass){


			final Boolean alreadyMatched = hashMapCache.get(className+"&"+interfacesToCheck);
			if (alreadyMatched == null) { // Class not already processed


	    		Class<?> cl = null;
	    		boolean matches = false;

	    		// Check if class implements interfaces specified in the set
	    		try {
	    		cl = Class.forName(className);

	    		} catch (ClassNotFoundException e) {

	    			try {
	    				File file = new File("ClassFileList.txt");
	    				FileWriter fileWriter = new FileWriter(file.getName(),true);
	    				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

	    				bufferedWriter.write("================================="+"\n");
	    				bufferedWriter.write("Keyed class not found="+className+"\n");
	    				bufferedWriter.write("================================="+"\n");

	    				bufferedWriter.close();
	    			} catch(IOException ex){ex.printStackTrace();}

	    		} catch (Throwable t) {}


	    		if (cl != null) {

	    			matches = __checkInterfacesOfClass(cl, interfacesToCheck);
	    		}

	    		// Now check also the class' superclasses, traversing the
	    		// hierarchy

	    			while (cl != null && !matches) {

	    					cl = cl.getSuperclass();

	    					matches = __checkInterfacesOfClass(cl, interfacesToCheck);
	    			}

	    		hashMapCache.put(className+"&"+interfacesToCheck, matches);
	    		return matches;

	    		}

	    	else{

	    		return alreadyMatched.booleanValue();
	    	}

	    }

	    private static boolean __checkInterfacesOfClass(final Class<?> cl, final String interfacesToCheck) {
	    	if (cl == null)
	    		return false;

	    	if(checkString(cl.getName(),interfacesToCheck)){
	    		return true;
	    	}

	    	final Class<?>[] interfaces = cl.getInterfaces();

	    		for (int i = 0; i < interfaces.length; i++) {
	    			String ifName = interfaces[i].getName();
	    			ifName = checkForTopHierarchy(ifName,interfacesToCheck);

	    			if (checkString(ifName, interfacesToCheck))
	    				return true;
	    			}

	    	return false;
	    }


	    public static boolean checkString(String stringToCheck, String stringsAllowed) {

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

	    public static String checkForTopHierarchy(String ifName,String interfacesToCheck){


			String topInterface = null;
			String topInterface_inner = null;
			try{
				Class<?> cl = Class.forName(ifName);
				Class<?>[] interfaces = cl.getInterfaces();

				for(int i =0;i<interfaces.length;i++){
					topInterface = interfaces[i].getName();

					if(topInterface!=null){

						if(topInterface.equals(interfacesToCheck)){  //In case top most interface is equal to interfaces to Check
							ifName = topInterface;
						}
						else{
							Class<?> cl1 = Class.forName(topInterface);
							Class<?>[] interfaces1 = cl1.getInterfaces();
							topInterface_inner = interfaces1[0].getName();
								if(topInterface_inner.equals(interfacesToCheck)){  //In case top most interface is equal to interfaces to Check
									ifName = topInterface_inner;
								}
							}
					}
				}
			}

			 catch(Exception ex){}

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


	    public static Boolean allowSubInterfaces(String JoinPointDescription, AbstractInsnNode instruction, MethodInsnNode methodInsn){
	    		if(!JoinPointDescription.contains("+")){

	    			String name = methodInsn.owner.replace("/",".");


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


	    public static  void checkMethod(String Owner, String mName, String mTest, String interfaceName){

	    	//mName = mName.substring(0,mName.indexOf("("));
     		//mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());

     		mTest = mTest.substring(0,mTest.indexOf("("));
    		mTest = mTest.substring(mTest.lastIndexOf(".")+1,mTest.length());



    		if(mTest.endsWith("*")){
    			String onlyMethod = mTest.substring(0,mTest.indexOf("*"));
    			if(!onlyMethod.isEmpty()){
    				if(mName.startsWith(onlyMethod)){
   // 					methodExists = isPlus?true:getMethods(mName,Owner);
    					methodExists = true;
    					//System.out.println("What is the value of methodExists="+methodExists);
    				}
    			}
    		}

    		if(mTest.startsWith("*")){
    			String onlyMethod = mTest.substring(mTest.indexOf("*")+1,mTest.length());
    			if(!onlyMethod.isEmpty()){
    				if(mTest.endsWith(onlyMethod)){

    					methodExists = true;
    				}
    			}
    		}
    		if(mTest.equals("*")){
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

	    public static Boolean getMethods(String methodTest,String interfaceName){
	    	Boolean testMethod = false;
	    	try {
	            Class<?> c = Class.forName(interfaceName);

	            Method[] m = c.getDeclaredMethods();
	            for (int i = 0; i < m.length; i++){
	            	String mName = m[i].toString();
            		mName = mName.substring(0,mName.indexOf("("));
            		mName = mName.substring(mName.lastIndexOf(".")+1,mName.length());

            		if(methodTest.equals(mName)){
            			testMethod=true;
            			break;
            		}


            	}


	    	}catch(Exception ex){}
	    	catch(Throwable t){}

	    	return testMethod;
	    }


	    static Boolean allowExtends(String Owner, String interfaceName){

	    	try{
	    		Class<?> superClass = Class.forName(interfaceName);
	    		Class<?> classToTest = Class.forName(Owner);
	    		if(classToTest.isAssignableFrom(superClass)){
	    			//return true;
	    		}

	    	}catch(Exception ex){
	    	}
	    	catch(Throwable t){}
	    	return false;

	    }

	    public static Boolean isInterfaceMethod(){
	    		return methodExists;
	    }



}
