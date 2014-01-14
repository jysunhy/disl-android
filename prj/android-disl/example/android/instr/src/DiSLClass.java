import java.util.LinkedList;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import android.os.Process;

import android.util.Log;
import ch.usi.dag.dislre.ALocalDispatch;

public class DiSLClass {

	//@Before(marker = BodyMarker.class, scope = "java.lang.Class.*")
	@Before(marker = BodyMarker.class)
		public static void testJavaLang(){ 
			//CodeExecutedRE.mapPID(sc.thisMethodFullName(), -1);
			ALocalDispatch.testCoverage(-1);
			//			CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
			//					100000, 10000000000L, 1.5F, 2.5);
		}
}
