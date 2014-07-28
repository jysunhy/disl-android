package ch.usi.dag.dislre;

public class AREDispatch {
	/**
	 * Register method and receive id for this transmission
	 *
	 * @param analysisMethodDesc
	 * @return
	 */
	public static native short registerMethod(String analysisMethodDesc);

	/**
	 * Announce start of an analysis transmission
	 *
	 * @param analysisMethodDesc remote analysis method id
	 */
	public static native void analysisStart(short analysisMethodId);

	/**
	 * Announce start of an analysis transmission with total ordering (among
	 * several threads) under the same orderingId
	 *
	 * @param analysisMethodId remote analysis method id
	 * @param orderingId analyses with the same orderingId are guaranteed to
	 *                   be ordered. Only non-negative values are valid.
	 */
	public static native void analysisStart(short analysisMethodId,
			byte orderingId);

	/**
	 * Announce end of all analysis
	 */
	public static native void manuallyClose();
	/**
	 * Announce start of all analysis
	 */
	public static native void manuallyOpen();
	/**
	 * Announce end of an analysis transmission
	 */
	public static native void analysisEnd();

	// allows transmitting types
	public static native void sendBoolean(boolean booleanToSend);
	public static native void sendByte(byte byteToSend);
	public static native void sendChar(char charToSend);
	public static native void sendShort(short shortToSend);
	public static native void sendInt(int intToSend);
	public static native void sendLong(long longToSend);
	public static native void sendObject(Object objToSend);
	public static native void sendObjectPlusData(Object objToSend);

	// Methods use similar logic as Float.floatToIntBits() and
	// Double.doubleToLongBits() but implemented in the native code
	// to avoid perturbation
	public static native void sendFloat(float floatToSend);
	public static native void sendDouble(double doubleToSend);

	public static native void mapPID(String name,int pid);
	public static native void mapPID(String name);

	public static native void NativeLog(String text);
	public static native void CallAPI(int text);

	//public static native void clearThreadPermission();
	public static native int checkThreadPermission();
	public static native void methodEnter();
	public static native void methodExit();

	public static native long getObjectId(Object obj);
	public static native int getThisThreadId();
	public static native int getThisProcId();

	public static native void test();

	public static native void sendObjectSize(Object obj);

	public static native void sendCurrentThread();

	public static native long getCPUClock();
	//public static native void onFork(int parent);

	/*static{
		System.loadLibrary("shadowvm");
	}*/
	// TODO re - basic type array support
	//  - send length + all values in for cycle - all in native code
	//  PROBLEM: somebody can change the values from the outside
	//   - for example different thread
}
