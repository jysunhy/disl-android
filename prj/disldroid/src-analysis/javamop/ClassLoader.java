package javamop;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;

public class ClassLoader {

    @Before(marker=BodyMarker.class, scope="dalvik.system.BaseDexClassLoader.<init>")
    public static void newClassloader(final MethodStaticContext msc){
        AREDispatch.NativeLog ("find use of "+msc.thisMethodFullName ());
    }
}
