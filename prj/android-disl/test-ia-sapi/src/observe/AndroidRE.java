package observe;

import ch.usi.dag.dislre.ALocalDispatch;

public class AndroidRE {

	public static void testCoverage(int arg){
		ALocalDispatch.testCoverage(arg);
	}

	public static void mapPID(String pname, int pid){
		ALocalDispatch.mapPID(pname,pid);
	}
}
