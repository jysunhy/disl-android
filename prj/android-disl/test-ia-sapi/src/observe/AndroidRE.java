package observe;

import ch.usi.dag.dislre.ALocalDispatch;
import ch.usi.dag.dislre.AREDispatch;

public class AndroidRE {

	//public static void testCoverage(int arg){
		//ALocalDispatch.testCoverage(arg);
		//AREDispatch.testCoverage(arg);
	//}

	public static void mapPID(String pname, int pid){
		AREDispatch.mapPID(pname,pid);
	}
}
