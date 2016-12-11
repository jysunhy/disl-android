package ch.usi.dag.rv.usecases.infoleak.events.datasource;

public class GetSubscriberIdEvent extends DataSourceEvent{
    public GetSubscriberIdEvent (final String desc, final String value) {
        super (desc, value);
    }
}
