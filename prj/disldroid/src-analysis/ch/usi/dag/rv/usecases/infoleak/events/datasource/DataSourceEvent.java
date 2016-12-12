package ch.usi.dag.rv.usecases.infoleak.events.datasource;

import ch.usi.dag.rv.state.MonitorEvent;

public class DataSourceEvent extends MonitorEvent {
    private DataSourceEvent (final String desc) {
        super (desc);
    }
    Object value;
    public Object getValue(){
        return value;
    }
    public DataSourceEvent(final String desc, final Object value){
        super(desc);
        this.value = value;
    }
    @Override
    public String toString(){
        return "RVDataSource with value "+value;
    }
}
