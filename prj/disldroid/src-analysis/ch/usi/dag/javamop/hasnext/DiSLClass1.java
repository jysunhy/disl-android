package ch.usi.dag.javamop.hasnext;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
/**
 * <p>
 * This example shows how to insert snippets at various regions and entries or exits of these regions.
 *
 * <p>
 * It also shows how to implements custom code marker.
 */
public class DiSLClass1 {
	/**
	 * <p>
	 * This is added before every method call in Main.main method.
	 */
    @Before(marker = BodyMarker.class, scope = "HasNextRuntimeMonitor.hasnextEvent")
	   public static void countNext_next(){
           Myruntimeanalysis.setCountNum(1);

	}
     @Before(marker = BodyMarker.class, scope = "HasNextRuntimeMonitor.nextEvent")
	   public static void countHasNext_next(){
            Myruntimeanalysis.setCountNum(1);
    }
//	@After(marker = BodyMarker.class, scope = "Main.main")
//	   public static void returnCountNum(){
//            System.out.println("Total number of Pointcuts executed are = "+ Myruntimeanalysis.getCountNum());
//    }
}
