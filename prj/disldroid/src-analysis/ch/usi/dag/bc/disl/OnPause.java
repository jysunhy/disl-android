package ch.usi.dag.bc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;

public class OnPause {
    @Before (marker = BodyMarker.class, scope="Activity.onPause")
    public static void onMethodEntry (
        ) {
        AREDispatch.manuallyClose ();
    }
}
