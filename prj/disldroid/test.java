public class AnalysisRE {
  static short rpcId = AREDispatch.registerMethod("remote.Analysis.test");

  public static void test (int i, String str, Object obj) {
    AREDispatch.analysisStart(rpcId);
    AREDispatch.sendInteger(i);
    AREDispatch.sendObjectPlusData(str);
    AREDispatch.sendObject(obj);
    AREDispatch.analysisEnd();
  }
}

public class DiSLClass {
  @After(marker = BodyMarker.class, scope = "TargetClass.main")
  public static void test(MethodStaticContext ci, DynamicContext dc) {
    AnalysisRE.test (1, ci.thisMethodName(), dc.getThis().class);
  }
}
