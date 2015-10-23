package ch.usi.dag.demo.callstack.analysis;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.shadow.Context;

public class SVMCallStack {

    public static ConcurrentHashMap<NativeThread, SVMCallStack> stateMap=new ConcurrentHashMap <NativeThread, SVMCallStack>();
    Stack<String> runtimeStack = new Stack<String>();
    public Stack <String> getRuntimeStack () {
        return runtimeStack;
    }

    NativeThread thd;

    public SVMCallStack (final NativeThread key) {
        thd = key;
    }

    public static SVMCallStack get(final Context ctx, final int tid){
        final NativeThread key = new NativeThread(ctx.pid (), tid);
        return get(key);
    }

    public static SVMCallStack get(final NativeThread key){
        final SVMCallStack temp = new SVMCallStack (key);
        final SVMCallStack res = stateMap.putIfAbsent (key, temp);
        if(res != null) {
            return res;
        } else {
            return temp;
        }
    }
    public synchronized void pushBoundary(final String boundaryName){
        runtimeStack.push (boundaryName);
    }
    public synchronized  void popBoundary(final String boundaryName){
        runtimeStack.pop ();
    }
    public synchronized String peekBoundary(){
        return runtimeStack.peek ();
    }
}
