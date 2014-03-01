package ch.usi.dag.icc.analysis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
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

    ConcurrentSkipListSet <ShadowObject> callers = new ConcurrentSkipListSet <> ();


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
        System.out.println("MARK: IN START SERVICE");
    }


    public void onScheduleCreateService (final int caller) {
        get (caller).createServiceReq.incrementAndGet ();
        System.out.println("MARK: IN SCHEDULE SERVICE");
    }


    public void actualCreateService (final Context context) {
        get (context.pid ()).createServiceNumber.incrementAndGet ();
        System.out.println("MARK: IN ACTUAL CREATE SERVICE");
    }


    @Override
    public void atExit (final ShadowAddressSpace shadowAddressSpace) {
        final int pid = shadowAddressSpace.getContext ().pid ();
        final ApplicationStatus status = applicationsStatus.get (shadowAddressSpace.getContext ().pid ());
        System.out.println ("PROCESS-" + pid);
        System.out.println ("# of startService request sent to system_server: "
            + status.startServiceReq.get ());
        System.out.println ("# of createService request received from system_server: "
            + status.createServiceReq.get ());
        System.out.println ("# of service created in this process: "
            + status.createServiceNumber.get ());
    }


    @Override
    public void objectFree (
        final ShadowAddressSpace shadowAddressSpace, final ShadowObject netRef) {
    }

}
