package ch.usi.dag.rv.usecases.infoleak.events.datasource;

public class GetDeviceIdEvent extends DataSourceEvent{
    public GetDeviceIdEvent (final String desc, final String value) {
        super (desc, value);
    }
}
