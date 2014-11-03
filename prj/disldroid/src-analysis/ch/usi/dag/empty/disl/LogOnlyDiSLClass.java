package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;


public class LogOnlyDiSLClass {

    @Before (
        marker = BodyMarker.class,
        scope = "java.lang.String.<clinit>")
    public static void api_194 (final MethodStaticContext msc) {
        AREDispatch.NativeLog ("test abc");
    }
}
