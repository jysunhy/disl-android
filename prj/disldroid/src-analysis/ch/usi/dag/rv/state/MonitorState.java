package ch.usi.dag.rv.state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.rv.logging.DefaultLog;


public abstract class MonitorState{
    ConcurrentHashMap <Long, ThreadState> threadStates = new ConcurrentHashMap <Long, MonitorState.ThreadState> ();
    static class ThreadState {
        int threadId;
        List<MonitorEvent> eventList = new ArrayList <MonitorEvent> ();
    }

    List<MonitorEventProcessing> processings = new ArrayList<MonitorEventProcessing>();

    public void addProcessing(final MonitorEventProcessing processing){
        processings.add (processing);
    }

    public void newEvent(final MonitorEvent e){
        e.print ();
        final long tid = DefaultLog.getTID();
        final ThreadState tmp = new ThreadState ();
        ThreadState cur = threadStates.putIfAbsent (tid, tmp);
        if(cur == null){
            cur = tmp;
        }
        cur.eventList.add (e);
        if(e.needProcess ()) {
            for(final MonitorEventProcessing p : processings){
                p.process (cur.eventList);
            }
        }
    }
}