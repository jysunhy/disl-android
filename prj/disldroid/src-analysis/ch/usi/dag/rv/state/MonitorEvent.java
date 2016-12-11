package ch.usi.dag.rv.state;

import ch.usi.dag.rv.logging.DefaultLog;

public abstract class MonitorEvent {
    protected String desc;
    public static String tag = "MONITOREVENT";
    public MonitorEvent(final String desc){
        this.desc = desc;
    }

    public void print(){
        DefaultLog.d(tag, this.toString ());
    }
    public boolean needProcess(){
        return true;
    }
    @Override
    public String toString(){
        return "MonitorEvent "+desc;
    }
}
