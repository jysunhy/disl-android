package ch.usi.dag.javamop.fileclose;

import java.io.FileWriter;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
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
    @SyntheticLocal
    static boolean disConst=true;

@Before(marker = WriteInvocationMarker.class)
	public static void method1(final ArgumentProcessorContext pc){

        //if(disConst){
        //    Runtime.getRuntime().addShutdownHook(new FileClose_DummyHookThread());
        //    disConst=false;
        //}

        final Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

       final FileWriter fRef1 = (FileWriter)receiver;
		FileCloseRuntimeMonitor.writeEvent(fRef1);

	}

//@AfterReturning(marker = NewInvocationMarker.class, scope = "Main.main")
//	public static void method2(){
//        System.out.println(fRef1);
//       // SafeFileWriterRuntimeMonitor.openEvent(fRef1);
//
//	}

@After(marker = CloseInvocationMarker.class)
	public static void method3(final ArgumentProcessorContext pc){

         final Object receiver = pc.getReceiver(ArgumentProcessorMode.CALLSITE_ARGS);

           final FileWriter fRef1 = (FileWriter)receiver;
	       FileCloseRuntimeMonitor.closeEvent(fRef1);
	}

    @After(marker = BodyMarker.class,scope="Main.main")
    public static void method4(final ArgumentProcessorContext pc)
    {
           Myruntimeanalysis.getInstance();

    }
}
