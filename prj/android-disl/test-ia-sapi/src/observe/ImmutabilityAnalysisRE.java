package observe;

import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.dislre.ALocalDispatch;

public class ImmutabilityAnalysisRE {

    public static short cs = AREDispatch.registerMethod(
            "analyse.ImmutabilityAnalysis.constructorStart");
    
    public static short ce = AREDispatch.registerMethod(
            "analyse.ImmutabilityAnalysis.constructorEnd");
    
    public static short oa = AREDispatch.registerMethod(
            "analyse.ImmutabilityAnalysis.onObjectAllocation");

    public static short fr = AREDispatch.registerMethod(
            "analyse.ImmutabilityAnalysis.onFieldRead");

    public static short fw = AREDispatch.registerMethod(
            "analyse.ImmutabilityAnalysis.onFieldWrite");

	public static void constructorStart(Object forObject) {

        AREDispatch.analysisStart(cs);

        AREDispatch.sendObject(forObject);

        AREDispatch.analysisEnd();
	}

	public static void constructorEnd() {
		
        AREDispatch.analysisStart(ce);

        AREDispatch.analysisEnd();
	}
    
    public static void onObjectAllocation(Object object, String allocationSite) {

        AREDispatch.analysisStart(oa);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(allocationSite);

        AREDispatch.analysisEnd();

    }

    public static void onFieldRead(Object object, Class<?> ownerClass, String fieldId) {

        AREDispatch.analysisStart(fr);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(ownerClass);
        AREDispatch.sendObjectPlusData(fieldId);

        AREDispatch.analysisEnd();
    }

    public static void onFieldWrite(Object object, Class<?> ownerClass, String fieldId) {

        AREDispatch.analysisStart(fw);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(ownerClass);
        AREDispatch.sendObjectPlusData(fieldId);

        AREDispatch.analysisEnd();
    }

}