package ch.usi.dag.dislre;

public class AREDispatch {
	public static void test(){
		add(1,2);
		registerMethod("ch.usi.dag.disl.test.dispatch.CodeExecuted.bytecodesExecuted");
		short s = 0;
		byte b = 127;
		short cnt = 0;
		String tmp = "def";
		AREDispatch tmp2 = new AREDispatch();
		
		while(cnt < 2) {
			try {
			Thread.sleep(100);
			}catch(Exception e){
			}
			cnt++;

			analysisStart((short)29, b);
			sendObject(new Thread());
			sendObject(new AREDispatch());
			sendObject(tmp);
			sendObject(tmp2);
			sendObject(new String("adb"));
			sendByte((byte)cnt);
			analysisEnd();
		}
		//analysisStart(s,b);
		sendBoolean(true);
		sendByte(b);
		sendChar('a');
		sendShort(s);
		sendInt(0);
		sendLong(0);
		sendObject(new Object());
		sendObjectPlusData(new Object());
	}
	public static native int add(int a, int b);
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
	

	static{
		System.loadLibrary("shadowvm");
	}
	// TODO re - basic type array support
	//  - send length + all values in for cycle - all in native code
	//  PROBLEM: somebody can change the values from the outside
	//   - for example different thread
}
