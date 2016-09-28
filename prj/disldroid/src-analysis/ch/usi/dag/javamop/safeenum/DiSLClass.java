package ch.usi.dag.javamop.safeenum;

import java.util.Enumeration;
import java.util.Vector;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
/**
 * <p>
 * This example shows how to insert snippets at various regions and entries or exits of these regions.
 *
 * <p>
 * It also shows how to implements custom code marker.
 */
public class DiSLClass {

	/**
	 * <p>
	 * This is added before every method call in Main.main method.
	 */

	@Before(marker = NextElementInvocationMarker.class)
	public static void method1(final DynamicContext di,final ArgumentProcessorContext pc){
        final Enumeration e = (Enumeration) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

       SafeEnumRuntimeMonitor.nextEvent(e);

	}


    @AfterReturning(marker = ElementInvocationMarker.class)
	public static void method2(final DynamicContext di,final ArgumentProcessorContext pc){

       final Enumeration e = (Enumeration) di.getStackValue(0, Object.class);
       final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
            SafeEnumRuntimeMonitor.createEvent(v, e);

	}

	@After(marker = ClearInvocationMarker.class)
	public static void method3(final ArgumentProcessorContext pc){


        final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        SafeEnumRuntimeMonitor.updatesourceEvent(v);
	}

    @After(marker = AddInvocationMarker.class)
	public static void method4(final ArgumentProcessorContext pc){


        final Vector v = (Vector) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

        SafeEnumRuntimeMonitor.updatesourceEvent(v);
	}





}
