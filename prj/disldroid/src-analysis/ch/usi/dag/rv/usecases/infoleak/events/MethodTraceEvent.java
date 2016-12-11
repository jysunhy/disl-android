package ch.usi.dag.rv.usecases.infoleak.events;

import ch.usi.dag.rv.state.MonitorEvent;

public class MethodTraceEvent extends MonitorEvent{

    private MethodTraceEvent (final String desc) {
        super (desc);
    }

    boolean isEnter = true;

    public MethodTraceEvent(final String method, final boolean isEnter){
        super (method);
        this.isEnter = isEnter;
    }

    @Override
    public String toString(){
        return "MethodTraceEvent "+(isEnter?" entering ":"leaving ")+this.desc+" ";
    }

    @Override
    public boolean needProcess(){
        return false;
    }
}
