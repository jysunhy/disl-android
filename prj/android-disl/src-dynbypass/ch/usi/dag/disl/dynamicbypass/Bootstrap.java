package ch.usi.dag.disl.dynamicbypass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class Bootstrap {

	public static void completed(Instrumentation instr) {
		
		try {
			// get system class loader
			// this class is loaded by bootstrap classloader which does not help
			// system classloader should be able to locate the class because of
			// disl agent is there
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			
			// find our class in resources
			InputStream dbcIS = cl.getResourceAsStream(
					"DynamicBypassCheck-AfterBootstrap.class");
			
			byte[] newDBCCode = loadAsBytes(dbcIS);
			instr.redefineClasses(new ClassDefinition(DynamicBypassCheck.class,
					newDBCCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// thx: http://www.java2s.com/Tutorial/Java/0180__File/Loadfiletobytearray.htm
	public final static byte[] loadAsBytes(InputStream is) throws IOException {

		byte readBuf[] = new byte[512 * 1024];

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		int readCnt = is.read(readBuf);
		while (0 < readCnt) {
			bout.write(readBuf, 0, readCnt);
			readCnt = is.read(readBuf);
		}

		is.close();

		return bout.toByteArray();
	}
}
