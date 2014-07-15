package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;


public class DiSLClass {

    @Before (
        marker = BodyMarker.class,
        scope = "xxxx.query")
    public static void api_194 (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
    }

}
