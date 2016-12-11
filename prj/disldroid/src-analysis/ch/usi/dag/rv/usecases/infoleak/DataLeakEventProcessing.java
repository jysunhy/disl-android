package ch.usi.dag.rv.usecases.infoleak;

import java.util.ArrayList;
import java.util.List;

import ch.usi.dag.rv.logging.DefaultLog;
import ch.usi.dag.rv.state.MonitorEvent;
import ch.usi.dag.rv.state.MonitorEventProcessing;
import ch.usi.dag.rv.usecases.infoleak.events.datasink.DataSinkEvent;
import ch.usi.dag.rv.usecases.infoleak.events.datasource.DataSourceEvent;

public class DataLeakEventProcessing implements MonitorEventProcessing{

    @Override
    public void process (final List <MonitorEvent> events) {
        final boolean violated = false;
        final ArrayList<DataSourceEvent> sources = new ArrayList <DataSourceEvent> ();
        for(final MonitorEvent event: events){
            if(!event.needProcess ()) {
                continue;
            }
            if(event instanceof DataSourceEvent){
                sources.add ((DataSourceEvent)event);
            }
        }
        if(events.get (events.size ()-1) instanceof DataSinkEvent){
            final DataSinkEvent last = (DataSinkEvent)events.get (events.size ()-1);
            for(final DataSourceEvent se : sources){
                if(last.matches(se)){
                    DefaultLog.v ("VIOLATION", last+" found "+se);
                    for(final MonitorEvent event : events){
                        DefaultLog.v ("VIOLATION_TRACE", event.toString ());
                    }
                }
            }
        }
    }

}
