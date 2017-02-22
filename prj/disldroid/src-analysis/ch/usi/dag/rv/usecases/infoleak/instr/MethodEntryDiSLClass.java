package ch.usi.dag.rv.usecases.infoleak.instr;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.rv.usecases.infoleak.DataLeakMonitorState;
import ch.usi.dag.rv.usecases.infoleak.events.MethodTraceEvent;

/*
 * for generating runtime stack
 * Take effects on app code
 */
public class MethodEntryDiSLClass {
    @Before (
        marker = BodyMarker.class,
//        scope = "com.facebook.*.*",
        order = 1000)
    public static void before_enter (final MethodStaticContext msc) {
        DataLeakMonitorState.getInstance ().newEvent (new MethodTraceEvent (msc.thisMethodFullName (), true));
    }

    @After (
        marker = BodyMarker.class,
//        scope = "com.facebook.*.*",
        order = 1000)
    public static void after_enter (final CallContext msc) {
        DataLeakMonitorState.getInstance ().newEvent (new MethodTraceEvent (msc.thisMethodFullName (), false));
    }
}
