package ch.usi.dag.icc.analysis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.Context;
import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.dislreserver.shadow.ShadowObject;


public class ICCAnalysis extends RemoteAnalysis {

    public static class ApplicationStatus {

        AtomicInteger startServiceReq = new AtomicInteger ();

        AtomicInteger createServiceReq = new AtomicInteger ();

        AtomicInteger createServiceNumber = new AtomicInteger ();

    }


    ConcurrentHashMap <Integer, ApplicationStatus> applicationsStatus = new ConcurrentHashMap <> ();


    private ApplicationStatus get (final Integer caller) {
        ApplicationStatus status;

        if ((status = applicationsStatus.get (caller)) == null) {
            applicationsStatus.putIfAbsent (caller, new ApplicationStatus ());
            status = applicationsStatus.get (caller);
        }

        return status;
    }


    public void onStartService (final int caller) {
        get (caller).startServiceReq.incrementAndGet ();
        System.out.println ("PROCESS-" + caller + " sends request to system_server");
    }


    public void onCreateService (final int callee, final boolean isIsolated) {
        get (callee).createServiceReq.incrementAndGet ();
        System.out.println ("system_server sents request to PROCESS-" + callee);
    }


    public void actualCreateService (final Context context) {
        get (context.pid ()).createServiceNumber.incrementAndGet ();
    }


    public void onSystemReady () {
        for (final int pid : applicationsStatus.keySet ()) {
            final String pname = ShadowAddressSpace.getShadowAddressSpace (pid).getContext ().getPname ();
            final ApplicationStatus status = applicationsStatus.get (pid);

            System.out.println ("PROCESS-" + pid + " " + pname);
            System.out.println ("# of startService request sent to system_server: "
                + status.startServiceReq.get ());
            System.out.println ("# of createService request sent from system_server: "
                + status.createServiceReq.get ());
            System.out.println ("# of service created in this process: "
                + status.createServiceNumber.get ());
        }
    }


    @Override
    public void atExit (final ShadowAddressSpace shadowAddressSpace) {
    }


    @Override
    public void objectFree (
        final ShadowAddressSpace shadowAddressSpace, final ShadowObject netRef) {
    }

}
