package ch.usi.dag.demo.ipc.analysis.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.demo.ipc.analysis.IPCAnalysis;
import ch.usi.dag.demo.ipc.analysis.lib.BinderEvent.EventType;
import ch.usi.dag.demo.ipc.analysis.lib.BinderEvent.RequestRecvdEvent;
import ch.usi.dag.demo.ipc.analysis.lib.BinderEvent.RequestSentEvent;
import ch.usi.dag.demo.ipc.analysis.lib.BinderEvent.ResponseRecvdEvent;
import ch.usi.dag.demo.ipc.analysis.lib.BinderEvent.ResponseSentEvent;
import ch.usi.dag.demo.ipc.analysis.lib.IPCLogger.LoggerType;
import ch.usi.dag.demo.logging.DemoLogger;
import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;

public class ThreadState{

    public static ConcurrentHashMap<NativeThread, ThreadState> stateMap=new ConcurrentHashMap <NativeThread, ThreadState>();

    List<BinderEvent> eventList= new ArrayList<>();

    List<String> permissions = new ArrayList<> ();

    Stack<String> runtimeStack = new Stack <String>();
    NativeThread thd;

    public ThreadState (final NativeThread thd) {
        this.thd=thd;
    }

    @Override
    public String toString(){
        return thd.toString ();
    }

    public synchronized static List<ThreadState> getCallers(final Context ctx, final int tid){
        final List<ThreadState> res = new ArrayList <ThreadState>();
        final ThreadState self = get(ctx, tid);
        final int size = self.eventList.size();
        BinderEvent targetEvent=null;
        IPCLogger.debug ("GETCALLERS","get Callers "+size);
        //self.printEventList ();
        for(int i = size-1; i >= 0; i--){
            final BinderEvent event = self.eventList.get (i);
            if(event.getType () == EventType.RESPONSE_SENT) {
                break;
            }
            if(event.getType() == EventType.REQUEST_RECVD ){
                if(!event.getInfo ().isOneway ()) {
                    targetEvent = event;
                }
                break;
            }
        }
        if(targetEvent == null) {
            return res;
        }
        res.add (get(targetEvent.getClient ()));
        BinderEvent parent;
        while((parent = getParentEvent ((RequestRecvdEvent)targetEvent))!=null){
            targetEvent = parent;
            res.add (get(targetEvent.getClient ()));
        }

        return res;
    }

    private synchronized static BinderEvent getParentEvent(final RequestRecvdEvent event){
        final ThreadState cliState = get(event.getClient ());
        int i;
        for(i = cliState.eventList.size ()-1; i>=0;i--){
            final BinderEvent e = cliState.eventList.get (i);
            if(e.isSameTransaction (event) && e.getType ()==EventType.REQUEST_SENT){
                break;
            }
        }
        if(i < 0) {
            return null;
        }
        while(i > 0){
            i--;
            final BinderEvent tm = cliState.eventList.get (i);
            if(tm.getType () == EventType.RESPONSE_SENT) {
                break;
            }
            if(tm.getType() == EventType.REQUEST_RECVD ){
                if(!tm.getInfo ().isOneway ()) {
                    return tm;
                }
                break;
            }

        }
        return null;
    }

    private synchronized static BinderEvent getOtherEndEvent(final RequestRecvdEvent event){
        final ThreadState cliState = get(event.getClient ());
        BinderEvent e = null;
        for(int i = cliState.eventList.size ()-1; i>=0;i--){
            e = cliState.eventList.get (i);
            if(e.isSameTransaction (event) && e.getType ()==EventType.REQUEST_SENT){
                return e;
            }
        }
        return null;
    }

    public static ThreadState get(final Context ctx, final int tid){
        final NativeThread key = new NativeThread(ctx.pid (), tid);
        return get(key);
    }

    public static ThreadState get(final NativeThread key){
        final ThreadState temp = new ThreadState (key);
        final ThreadState res = stateMap.putIfAbsent (key, temp);
        if(res != null) {
            return res;
        } else {
            return temp;
        }
    }

    public synchronized void addEvent(final BinderEvent event){
        IPCLogger.write (LoggerType.DEBUG, "ADDEVENT", "Add Event "+event.toString ());
        eventList.add (event);
    }

    static long Waiting_Time=50;
    static long Max_Waiting_Time=500;
    public void waitForRequestSent(final TransactionInfo info, final NativeThread server){
        final ShadowAddressSpace space = ShadowAddressSpace.getShadowAddressSpaceNoCreate(thd.getPid ());
        if(space == null || space.getContext ().getPname () == null){
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "Proc "+thd.getPid ()+" is not observed");
            return;
        }

        if(eventList.size ()==0){
            IPCLogger.write(LoggerType.DEBUG, "START", "Proc "+thd.getPid ()+" just starts");
            return;
        }

        BinderEvent res = null;
        IPCLogger.write (LoggerType.DEBUG, "WAIT",server+" is waiting for request sent event for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        final long waitingTime = Waiting_Time;

        int cnt = 0;
        while(((res=findEvent(info))==null)
        ){
            if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().isDead ()){
                IPCLogger.debug("WAIT","don't wait for a dead process "+thd.getPid ());
                break;
            }
            cnt++;
            try {

                Thread.sleep (waitingTime);
                if(cnt > 49 && cnt % 50 == 0)
                {
                   IPCLogger.write(LoggerType.DEBUG, "PENDING", server+" timeout "+thd+" transaction "+info);
                   IPCLogger.write(LoggerType.DEBUG, "PENDING", server+" is waiting for "+thd+" transaction "+info);
                }
                //waitingTime*=2;
                //waitingTime = waitingTime>Max_Waiting_Time?Max_Waiting_Time:waitingTime;
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(res != null) {
            IPCLogger.write (LoggerType.DEBUG, "WAIT","request sent event ready for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        } else {
            IPCLogger.write (LoggerType.DEBUG,"WAIT", "request sent event lost for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        }
    }

    private void printEventList(){
        final Iterator<BinderEvent> iter = eventList.iterator ();
        String log = "event list for "+thd.toString ()+ "\n";

        while(iter.hasNext ()){
            final BinderEvent event = iter.next ();
            log = log + event.toString () + "\n";
        }
        IPCLogger.debug ("PRINTEVENTLIST",log);
    }

    public void waitForResponseSent(final TransactionInfo info, final NativeThread client){
        final ShadowAddressSpace space = ShadowAddressSpace.getShadowAddressSpaceNoCreate(thd.getPid ());
        if(space == null ||space.getShadowAddressSpace (thd.getPid ()).getContext ().getPname () == null){
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "Proc "+thd.getPid ()+" is not observed");
            return;
        }
        if(eventList.size ()==0){
            IPCLogger.write(LoggerType.DEBUG, "START", "Proc "+thd.getPid ()+" just starts");
            return;
        }
        BinderEvent res = null;
        IPCLogger.write (LoggerType.DEBUG, "WAIT", "waiting for response sent event for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid() +" from "+client.getPid ()+" "+client.getTid ());
        final long waitingTime = Waiting_Time;
        int cnt = 0;
        while(((res=findEvent(info,client))==null)
        ){
            cnt++;
            if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().isDead ()){
                IPCLogger.debug("WAIT", "don't wait for a dead process "+thd.getPid ());
                break;
            }

            try {
                Thread.sleep (waitingTime);
                if(cnt > 49 && cnt % 50 == 0) {
                    IPCLogger.write(LoggerType.DEBUG, "PENDING", client + " timeout for "+thd+" transaction "+info);
                    IPCLogger.write(LoggerType.DEBUG, "PENDING", client + " is waiting for "+thd+" transaction "+info);
                }
                //waitingTime*=2;
                //waitingTime = waitingTime>Max_Waiting_Time?Max_Waiting_Time:waitingTime;
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(res != null) {
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "response sent event ready for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        } else{
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "response sent event lost for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid()+" from "+client.getPid ()+" "+client.getTid ());
        }
    }

    private synchronized BinderEvent findEvent(final TransactionInfo info){
        final Iterator<BinderEvent> iter = eventList.iterator ();
        BinderEvent res = null;

        while(iter.hasNext ()){
            final BinderEvent event = iter.next ();
            if(event.getType () != EventType.REQUEST_SENT) {
                continue;
            }
            if(event.info.getTransactionId () == info.getTransactionId () && event.getClient().equals (thd)){
                res = event;
                break;
            }
        }
        return res;
    }
    private synchronized BinderEvent findEvent(final TransactionInfo info, final NativeThread client){
        final Iterator<BinderEvent> iter = eventList.iterator ();
        BinderEvent res = null;
        while(iter.hasNext ()){
            final BinderEvent event = iter.next ();
            if(event.getType () != EventType.RESPONSE_SENT) {
                continue;
            }
            if(event.info.getTransactionId () == info.getTransactionId () && event.getClient().equals (client)){
                res = event;
                break;
            }
        }
        return res;
    }


    public synchronized void pushBoundary(final String boundaryName){
        DemoLogger.debug (IPCAnalysis.analysisTag, this.thd+" enter "+boundaryName);
        IPCLogger.debug (IPCAnalysis.analysisTag, this.thd+" enter "+boundaryName);
        runtimeStack.push (boundaryName);
    }
    public synchronized  void popBoundary(final String boundaryName){
        DemoLogger.debug (IPCAnalysis.analysisTag, this.thd+" leave "+boundaryName);
        IPCLogger.debug (IPCAnalysis.analysisTag, this.thd+" leave "+boundaryName);
        runtimeStack.pop ();
    }
    public synchronized String peekBoundary(){
        return runtimeStack.peek ();
    }

    public synchronized void addPermission (final String permissionName) {
        permissions.add (permissionName);
    }
    public synchronized void clearPermissions () {
        permissions = new ArrayList<> ();
    }

    public synchronized int getPermissionCount () {
        return permissions.size();
    }

    public void printStack (final String tag) {
        int cnt = 0;
        final String pname = ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().getPname ();
        if(runtimeStack.size()>0){
            IPCLogger.info("PERMISSION_USAGE","Calling Stack in proc "+pname+"("+thd.getPid ()+":"+thd.getTid ()+")");
            DemoLogger.info (IPCAnalysis.analysisTag, "Calling Stack in proc "+pname+"("+thd.getPid ()+":"+thd.getTid ()+")");
            for(int i = runtimeStack.size()-1; i>=0; i--){
                IPCLogger.info (tag,"#"+cnt+":"+runtimeStack.get (i));
                DemoLogger.info (tag,"#"+cnt+":"+runtimeStack.get (i));
                cnt++;
            }
        }
    }

    public void printPermission () {
        if(runtimeStack.size () > 0){
            DemoLogger.info (IPCAnalysis.analysisTag, "**************************************************");
            final String pname = ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().getPname ();
            String res="Detect use of permission(s):";
            for(int i = 0; i < permissions.size(); i++){
                res+=" #"+permissions.get(i);
            }
            res =  res + " in proc "+pname+"("+thd.getPid ()+":"+thd.getTid ()+")";
            IPCLogger.info("PERMISSION_USAGE", res);
            DemoLogger.info(IPCAnalysis.analysisTag, res);
            this.printStack(IPCAnalysis.analysisTag);
            DemoLogger.info (IPCAnalysis.analysisTag, "**************************************************");
        }
    }
    public void recordRequestSent (final NativeThread client, final TransactionInfo info) {
        final BinderEvent event = new RequestSentEvent (client, info);
        addEvent (event);
    }
    public void recordRequestReceived (
        final NativeThread client, final NativeThread server, final TransactionInfo info) {
        final BinderEvent event = new RequestRecvdEvent (client, server, info);
        addEvent (event);
    }
    public void recordResponseSent (
        final NativeThread client, final NativeThread server, final TransactionInfo info) {
        final BinderEvent event = new ResponseSentEvent(client, server, info);
        addEvent (event);
    }
    public void recordResponseReceived (
        final NativeThread client, final NativeThread server, final TransactionInfo info) {

        /*
         * We choose to keep the event history instead of discard events at the moment
         */

        final BinderEvent event = new ResponseRecvdEvent(client, server, info);
        addEvent (event);
    }

    public boolean checkResponseReceivedValid (
        final TransactionInfo info, final NativeThread server) {
        if (eventList.size () == 0) {
            return true;
        }
        final BinderEvent top = eventList.get (eventList.size ()-1);
        if(top.getType () == EventType.REQUEST_SENT && top.getClient ().equals (thd) && top.getInfo ().equals (info)) {
            return true;
        }
        IPCLogger.debug ("INVALID", "Invalid response received found "+ thd + info+server+" last event "+top);
        return false;
    }
}
