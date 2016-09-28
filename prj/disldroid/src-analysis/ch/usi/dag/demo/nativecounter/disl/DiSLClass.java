package ch.usi.dag.demo.nativecounter.disl;

import ch.usi.dag.demo.nativecounter.analysis.NativeAnalysisStub;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class DiSLClass {
    @AfterReturning(marker=BytecodeMarker.class, guard=NativeGuard.class, args = "invokestatic, invokespecial, invokeinterface, invokevirtual")
    public static void nativeCounter(final NativeStaticContext msc){
        NativeAnalysisStub.report (msc.getNativeInfoFrom(), msc.getNativeInfoTo());
    }
}
