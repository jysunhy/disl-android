package ch.usi.dag.javamop.hasnext;

import java.util.Iterator;

import javamop.HasNextInvocationMarker;
import javamop.NextInvocationMarker;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
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
	@Before(marker = NextInvocationMarker.class)
	public static void HasNext_next(final ArgumentProcessorContext pc){
			final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			HasNextRuntimeMonitor.nextEvent(receiver);
	}
	@After(marker = HasNextInvocationMarker.class)
	public static void HasNext_hasnext(final ArgumentProcessorContext pc) {
			final Iterator receiver = (Iterator) pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
			HasNextRuntimeMonitor.hasnextEvent(receiver);
	}
}


