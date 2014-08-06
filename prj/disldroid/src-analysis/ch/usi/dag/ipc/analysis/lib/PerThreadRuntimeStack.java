package ch.usi.dag.ipc.analysis.lib;

import java.util.HashMap;
import java.util.Stack;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;


public class PerThreadRuntimeStack{

    static HashMap<Integer, HashMap<Integer,Stack<String>>> runtimeStack = new HashMap <Integer, HashMap<Integer,Stack<String>>>();

//    public static void alert (final IPCTransaction transaction, final String permission) {
//        if(transaction == null) {
//            return;
//        }
//        alert(transaction.to.pid,transaction.to.tid, permission);
//        IPCTransaction outmost = transaction;
//        while(outmost != null) {
//            alert(transaction.from.pid,transaction.from.tid, permission);
//            outmost = outmost.parent;
//        }
//    }


    private static void alert (final int pid, final int tid, final String permission) {
        System.out.println("Detect "+permission+" used in ("+pid+":"+tid+")");
        printStack (pid, tid);
    }



    public static void boundary_start (final int pid, final int tid, final String location) {
        final Stack<String> stk = getStack (pid, tid);
        stk.push (location);
        System.out.println ("entering "+location+" ("+pid+" "+tid+")");
    }

    public static void boundary_end (final int pid, final int tid, final String location) {
        final Stack<String> stk = getStack (pid, tid);
        final String curTop = stk.peek ();
        if(!curTop.equals (location)){
            System.out.println ("stack error");
        }
        System.out.println ("leaving "+location+" ("+pid+" "+tid+")");
        stk.pop();
    }

    public static void printStack(final int pid, final int tid){
        final Stack<String> stk = getStack (pid, tid);
        for(int i = 0; i < stk.size (); i++){
            System.out.println ("("+pid+":"+tid+")#"+i+" "+stk.get (i));
        }
    }

    static Stack<String> getStack(final int pid, final int tid){
        HashMap <Integer, Stack<String>> pl_stk = runtimeStack.get (pid);
        if(pl_stk == null){
            pl_stk = new HashMap <Integer, Stack<String>>();
            runtimeStack.put (pid, pl_stk);
        }
        Stack<String> tl_stk = pl_stk.get (tid);
        if(tl_stk == null){
            tl_stk = new Stack <String>();
            pl_stk.put(tid, tl_stk);
        }
        return tl_stk;
    }



    public static void printStack (final NativeThread client) {
        // TODO Auto-generated method stub

    }
}
