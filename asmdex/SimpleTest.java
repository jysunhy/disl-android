import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ow2.asmdex.*;
import org.ow2.asmdex.specificAnnotationVisitors.*;

public class SimpleTest {
	
		public static void main(String args[]) {
		FileOutputStream os = null;
		try {
		int api = Opcodes.ASM4;
		File inFile = new File("test.dex");
		File outFile = new File("output.dex");
		//... // Argument validation
		//AnnotRulesManager rm = ...; // Rules to apply
		ApplicationReader ar = new ApplicationReader(api, inFile);
		ApplicationWriter aw = new ApplicationWriter();
		//ApplicationVisitor aa = new ApplicationAdapterAnnotateCalls(api, rm, aw);
		//ar.accept(aa, 0);
		byte [] b = aw.toByteArray();
		os = new FileOutputStream(outFile);
		os.write(b);
		} catch (IOException e) { // recovery
		} finally { // cleanup }
		}
		}

}

