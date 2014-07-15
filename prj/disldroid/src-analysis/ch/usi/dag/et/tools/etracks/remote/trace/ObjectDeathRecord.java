package ch.usi.dag.et.tools.etracks.remote.trace;

import java.util.Formatter;

final class ObjectDeathRecord extends EventRecord {

    private final long __objectId;

    //

    public ObjectDeathRecord (final long time, final long objectId) {
        super (time);
        __objectId = objectId;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        formatter.format ("%x\n", __objectId);
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_DEATH;
    }

}
