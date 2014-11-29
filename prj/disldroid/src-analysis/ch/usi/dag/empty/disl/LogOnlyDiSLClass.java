package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;

public class LogOnlyDiSLClass {

    @After (
        marker = BodyMarker.class,
        scope = "org.eembc.grinderbench.CmdlineWrapper.main")
    public static void api_194 (final MethodStaticContext msc) {
        AREDispatch.NativeLog ("grinder test end");
        System.exit (0);
    }
}
