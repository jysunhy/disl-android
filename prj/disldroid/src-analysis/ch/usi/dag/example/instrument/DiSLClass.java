package ch.usi.dag.example.instrument;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.example.analysis.AnalysisStub;


public class DiSLClass {
    @Before (marker = BodyMarker.class, scope = "somepackage.*.*")
    public static void enterMethod (final MethodStaticContext msc) {
        AnalysisStub.enter (msc.thisMethodFullName ());
    }

    @After (
        marker = BodyMarker.class,
        scope = "somepackage.*.*",
        guard = Guard.MethodWithoutReturnValue.class)
    public static void leaveMethod (final MethodStaticContext msc) {
        AnalysisStub.leave (msc.thisMethodFullName ());
    }

    @After (
        marker = BodyMarker.class,
        scope = "somepackage.*.*",
        guard = Guard.MethodReturningObject.class)
    public static void leaveMethodWithValue (final MethodStaticContext msc, final DynamicContext ctx) {
        AnalysisStub.leaveWithObject (msc.thisMethodFullName (), ctx.getStackValue (0, Object.class));
    }
}
