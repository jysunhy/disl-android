package ch.usi.dag.et2.tools.etracks.remote.trace;

import java.util.Formatter;

abstract class ObjectRecord extends EventRecord {

    private final long __objectId;

    //

    public ObjectRecord (final long time, final long objectId) {
        super (time);

        __objectId = objectId;
    }

    //

    @Override
    protected void _formatTo (final Formatter formatter) {
        super._formatTo (formatter);
        formatter.format (" %x", __objectId);
    }

}
