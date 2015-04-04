package ch.usi.dag.empty.disl;

import ch.usi.dag.empty.analysis.TestStub;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;


public class DiSLClass {


    @Before (
        marker = BodyMarker.class,
        scope = "Activity.onCreate")
    public static void rpc_test () {
        TestStub.test();
    }
}
