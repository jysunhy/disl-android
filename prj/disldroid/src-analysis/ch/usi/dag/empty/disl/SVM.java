package ch.usi.dag.empty.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.empty.analysis.TestStub;

public class SVM {

    @Before (
        marker = BodyMarker.class,
        scope = "ch.usi.dag.android.example.Client.onGetDeviceID")
    public static void test () {
      AREDispatch.NativeLog("Device Id");
      TestStub.testingBasic(true, (byte) 125, 's', (short) 50000,
              100000, 10000000000L, 1.5F, 2.5);
    }

    @Before (
        marker = BodyMarker.class,
        scope = "ch.usi.dag.android.example.Client.onGetAndroidID")
    public static void test2 () {
      AREDispatch.NativeLog("Android ID");
      TestStub.testingString ("STRING TEST");
    }
}
