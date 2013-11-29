package ch.usi.dag.disl.utilinstr.tlvinserter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import ch.usi.dag.disl.localvar.ThreadLocalVar;
import ch.usi.dag.disl.util.Constants;

/**
 * This is just a utility class for disl compilation 
 */
public final class ExtendThread {

	private static final String THREAD_BIN_DIR = "./bin-thread/"; 
	
	public static void main(String[] args) throws Exception {
		
		Class<?> tc = Thread.class;

		// get thread class as resource
		InputStream tis = tc.getResourceAsStream("Thread.class");

		// prepare dynamic bypass variable
		ThreadLocalVar tlv = new ThreadLocalVar(null, "bypass",
				Type.getType(boolean.class), false);
		tlv.setDefaultValue(0);
		
		// prepare Set with dynamic bypass
		Set<ThreadLocalVar> tlvs = new HashSet<ThreadLocalVar>();
		tlvs.add(tlv);
		
		// parse Thread in ASM
		ClassReader cr = new ClassReader(tis);
		ClassWriter cw = new ClassWriter(cr, 0);
		
		// put dynamic bypass into Thread using TLVInserter
		cr.accept(new TLVInserter(cw, tlvs), 0);
		
		// prepare Thread file name
		String threadFileName = tc.getName();
		threadFileName = threadFileName.replace(
				Constants.PACKAGE_STD_DELIM, Constants.PACKAGE_INTERN_DELIM);
		threadFileName += Constants.CLASS_EXT;
		
		// output Thread code into special thread bin directory
		write(THREAD_BIN_DIR + threadFileName, cw.toByteArray());
	}
		

	private static void write(String outputFile, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		fos.write(data);
		fos.close();
	}
}
