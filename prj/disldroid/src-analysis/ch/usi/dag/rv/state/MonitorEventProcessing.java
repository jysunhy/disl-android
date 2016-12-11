package ch.usi.dag.rv.state;

import java.util.List;

public interface MonitorEventProcessing {
    public void process(List<MonitorEvent> events);
}
