package ch.usi.dag.ipc.analysis.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.EventType;
import ch.usi.dag.ipc.analysis.lib.BinderEvent.RequestRecvdEvent;
import ch.usi.dag.ipc.analysis.lib.IPCLogger.LoggerType;


public class ThreadState{


    static HashMap<NativeThread, ThreadState> stateMap=new HashMap <NativeThread, ThreadState>();

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
        ThreadState res = stateMap.get (key);
        if(res != null) {
            return res;
        }
        res = new ThreadState (key);
        stateMap.put (key, res);
        return res;
    }

    public synchronized void addEvent(final BinderEvent event){
        IPCLogger.write (LoggerType.DEBUG, "ADDEVENT", "Add Event "+event.toString ());

//        try {
//            final File logfile = new File("debug.added"+thd.getPid ()+"_"+thd.getTid ());
//            if(!logfile.exists()) {
//                logfile.createNewFile();
//            }
//            FileOutputStream fout;
//            fout = new FileOutputStream (logfile, true);
//            fout.write (event.toString ().getBytes ());
//            fout.write ('\n');
//        } catch (final Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


        eventList.add (event);
        //printEventList ();
    }

    static long Waiting_Time=50;
    static long Max_Waiting_Time=500;
    public void waitForEvent(final TransactionInfo info){
        if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().getPname () == null){
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "Proc "+thd.getPid ()+" is not observed");
            return;
        }
//        if(info.isOneway ()) {
//            return;
//        }
        BinderEvent res = null;
        IPCLogger.write (LoggerType.DEBUG, "WAIT","waiting for request sent event for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        long waitingTime = Waiting_Time;
        while(((res=findEvent(info))==null)
//        && (waitingTime < Max_Waiting_Time)
        ){
            if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().isDead ()){
                IPCLogger.debug("WAIT","don't wait for a dead process "+thd.getPid ());
                break;
            }

            try {

                Thread.sleep (waitingTime);
                waitingTime*=2;
                waitingTime = waitingTime>Max_Waiting_Time?Max_Waiting_Time:waitingTime;
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(res != null) {
            IPCLogger.write (LoggerType.DEBUG, "WAIT","request sent event ready for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        } else {

            IPCLogger.write (LoggerType.DEBUG,"WAIT", "request sent event lost for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
            //printEventList ();

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

    public void waitForEvent(final TransactionInfo info, final NativeThread client){
        if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().getPname () == null){
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "Proc "+thd.getPid ()+" is not observed");
            return;
        }
        //final ThreadState state = get(client);
        BinderEvent res = null;
        IPCLogger.write (LoggerType.DEBUG, "WAIT", "waiting for response sent event for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid() +" from "+client.getPid ()+" "+client.getTid ());
        long waitingTime = Waiting_Time;
        while(((res=findEvent(info,client))==null)
//        && (waitingTime < Max_Waiting_Time)
        ){
            if(ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().isDead ()){
                IPCLogger.debug("WAIT", "don't wait for a dead process "+thd.getPid ());
                break;
            }

            try {
                Thread.sleep (waitingTime);
                waitingTime*=2;
                waitingTime = waitingTime>Max_Waiting_Time?Max_Waiting_Time:waitingTime;
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(res != null) {
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "response sent event ready for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid());
        } else{
            //printEventList ();
            IPCLogger.write (LoggerType.DEBUG, "WAIT", "response sent event lost for transaction "+info.getTransactionId ()+" in "+thd.getPid ()+" "+thd.getTid()+" from "+client.getPid ()+" "+client.getTid ());
        }
    }

    private synchronized BinderEvent findEvent(final TransactionInfo info){
        final Iterator<BinderEvent> iter = eventList.iterator ();
        BinderEvent res = null;
        //String log = "try to find request sent for "+thd.toString ()+info.toString ()+"\n";

        while(iter.hasNext ()){
            final BinderEvent event = iter.next ();
            //log = log + event.toString () + "\n";
            if(event.getType () != EventType.REQUEST_SENT) {
                continue;
            }
            if(event.info.getTransactionId () == info.getTransactionId () && event.getClient().equals (thd)){
                res = event;
                break;
            }
        }
        //IPCLogger.debug (log);
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
        runtimeStack.push (boundaryName);
    }
    public synchronized  void popBoundary(final String boundaryName){
        //ASSERT top == boundaryName
        if(!runtimeStack.peek ().equals (boundaryName)) {
            IPCLogger.write (LoggerType.ERROR,"POPBOUNDARY", "not match when poping boundary "+boundaryName+ ":"+runtimeStack.peek ());
        }
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

    public void printStack () {
        int cnt = 0;
        final String pname = ShadowAddressSpace.getShadowAddressSpace (thd.getPid ()).getContext ().getPname ();
        IPCLogger.info("PERMISSION_USAGE","Calling Stack in proc "+pname+"("+thd.getPid ()+":"+thd.getTid ()+")");
        for(int i = runtimeStack.size()-1; i>=0; i--){
            IPCLogger.info ("PRINTSTACK","#"+cnt+":"+runtimeStack.get (i));
            cnt++;
        }
    }

    public void printPermission () {
        String res="Detect use of permission(s):";
        for(int i = 0; i < permissions.size(); i++){
            res+=" #"+permissions.get(i);
        }
        IPCLogger.info("PERMISSION_USAGE", res);
    }
}
