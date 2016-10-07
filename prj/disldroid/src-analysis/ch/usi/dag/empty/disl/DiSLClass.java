package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;


public class DiSLClass {


    @Before (
        marker = BodyMarker.class,
        scope = "xxx")
    public static void rpc_test () {
        System.out.println ("");
//        //TestStub.test();
//        TestStub.testingBasic(true, (byte) 125, 's', (short) 50000,
//				100000, 10000000000L, 1.5F, 2.5);
//
//		TestStub.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());
//
//		TestStub.testingAdvanced2(new LinkedList<String>(),
//				new LinkedList<Integer>(), new LinkedList[0], new int[0],
//				int[].class, int.class, LinkedList.class,
//				LinkedList.class.getClass());
    }
}
