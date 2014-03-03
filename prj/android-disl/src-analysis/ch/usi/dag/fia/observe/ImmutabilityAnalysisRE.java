package ch.usi.dag.fia.observe;

import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.dislre.REDispatch;

public class ImmutabilityAnalysisRE {

    public static short cs = AREDispatch.registerMethod(
            "ch.usi.dag.fia.analysis.ImmutabilityAnalysis.constructorStart");

    public static short ce = AREDispatch.registerMethod(
            "ch.usi.dag.fia.analysis.ImmutabilityAnalysis.constructorEnd");

    public static short oa = AREDispatch.registerMethod(
            "ch.usi.dag.fia.analysis.ImmutabilityAnalysis.onObjectAllocation");

    public static short fr = AREDispatch.registerMethod(
            "ch.usi.dag.fia.analysis.ImmutabilityAnalysis.onFieldRead");

    public static short fw = AREDispatch.registerMethod(
            "ch.usi.dag.fia.analysis.ImmutabilityAnalysis.onFieldWrite");

	public static void constructorStart(final Object forObject) {

        AREDispatch.analysisStart(cs);

        AREDispatch.sendObject(forObject);

        AREDispatch.analysisEnd();
	}

	public static void constructorEnd() {

        AREDispatch.analysisStart(ce);

        AREDispatch.analysisEnd();
	}

    public static void onObjectAllocation(final Object object, final String allocationSite) {

        AREDispatch.analysisStart(oa);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(allocationSite);

        AREDispatch.analysisEnd();

    }

    public static void onFieldRead(final Object object, final Class<?> ownerClass, final String fieldId) {

        AREDispatch.analysisStart(fr);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(ownerClass);
        AREDispatch.sendObjectPlusData(fieldId);

        AREDispatch.analysisEnd();
    }

    public static void onFieldWrite(final Object object, final Class<?> ownerClass, final String fieldId) {

        AREDispatch.analysisStart(fw);

        AREDispatch.sendObject(object);
        AREDispatch.sendObjectPlusData(ownerClass);
        AREDispatch.sendObjectPlusData(fieldId);

        REDispatch.analysisEnd();
    }
}
