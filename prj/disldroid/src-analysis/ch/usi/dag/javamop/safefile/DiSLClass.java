package ch.usi.dag.javamop.safefile;

import java.io.FileReader;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
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


 @AfterReturning(marker = NewInvocationMarker.class)
    public static void method0a(final ArgumentProcessorContext pc){
   final Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final FileReader fRef1 = (FileReader)receiver;
        final Thread t = Thread.currentThread();
		SafeFileRuntimeMonitor.openEvent(t, fRef1);
}

//	@Before(marker = BodyMarker.class, scope = "* *.*(..)")
	@Before(marker = BodyMarker.class,scope = "* *.*(..)")
	public static void method1(){
		final Thread t = Thread.currentThread();
		SafeFileRuntimeMonitor.beginEvent(t);
	}


      @After(marker = CloseInvocationMarker.class)
	public static void method3(final DynamicContext di,final ArgumentProcessorContext pc){
        final Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);
        final FileReader fRef2= (FileReader)receiver;
		final Thread t = Thread.currentThread();
		SafeFileRuntimeMonitor.closeEvent(fRef2, t);
	}

  //  @After(marker = BodyMarker.class, scope = "* *.*(..)")
 @After(marker = BodyMarker.class,scope = "* *.*(..)")
	public static void method4(){
		final Thread t = Thread.currentThread();
		SafeFileRuntimeMonitor.endEvent(t);
	}


}
