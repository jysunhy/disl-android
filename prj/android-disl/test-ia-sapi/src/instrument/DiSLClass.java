package instrument;

import observe.ImmutabilityAnalysisRE;
import observe.AndroidRE;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import android.os.Process;
import com.android.internal.os.ZygoteConnection;

//import spec.benchmarks._201_compress.Main;

public class DiSLClass {

	@Before(marker = BodyMarker.class, scope = "MainActivity.add")
		public static void add(MethodStaticContext sc, ArgumentProcessorContext pc){
			AndroidRE.mapPID("testadd",-1);
		}
}
