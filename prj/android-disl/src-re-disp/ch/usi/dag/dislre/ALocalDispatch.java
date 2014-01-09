package ch.usi.dag.dislre;

public class ALocalDispatch {

	public static native void mapPID(String name,int pid);

	static{
		System.loadLibrary("link");
	}
}
