package ch.usi.dag.icc.analysis;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowObject;


public class ICCAnalysis extends RemoteAnalysis {

    public void onStartService (final int caller, final Context context) {
        System.out.println ("PROCESS-"
            + context.pid () + ": receives start service request from " + caller);
    }


    public void onScheduleCreateService (final int caller, final Context context) {
        System.out.println ("PROCESS-"
            + context.pid () + ": sends create service request to " + caller);
    }


    public void actualCreateService (final Context context) {
        System.out.println ("PROCESS-" + context.pid () + ": create a service");
    }


    @Override
    public void atExit (final ShadowAddressSpace shadowAddressSpace) {
    }


    @Override
    public void objectFree (
        final ShadowAddressSpace shadowAddressSpace, final ShadowObject netRef) {
    }

}
