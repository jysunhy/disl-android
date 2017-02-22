package ch.usi.dag.rv.usecases.infoleak.instr;

import android.util.Log;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class EmptyDiSLClass {
    @Before (
        marker = BodyMarker.class,
        order = 1000)
    public static void before_enter (final MethodStaticContext msc) {
        Log.d ("HAIYANG", msc.thisMethodFullName ());
    }
}
