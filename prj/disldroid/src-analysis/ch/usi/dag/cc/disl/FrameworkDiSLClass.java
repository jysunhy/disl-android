package ch.usi.dag.cc.disl;

import ch.usi.dag.cc.analysis.CodeCoverageAnalysisStub;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;

public class FrameworkDiSLClass {
    /*
     * Android App process doesn't end as normal jvm process
     * So user can define some points where they want to get a result
     * Of course, the user can also get result on the fly at the analysis end
     *
     * Here for convenience, we instrument Activity.onPause
     * print result at the analysis end
     * when user click 'Home' to return to the desktop page
     *
     * This takes effect on "framework.jar"
     */
    @Before (marker = BodyMarker.class,
        scope="Activity.onPause")
    public static void onPause () {

        CodeCoverageAnalysisStub.printResult ();
    }
}
