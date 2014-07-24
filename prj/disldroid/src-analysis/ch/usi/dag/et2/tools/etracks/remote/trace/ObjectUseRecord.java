package ch.usi.dag.et2.tools.etracks.remote.trace;

import java.util.Formatter;

final class ObjectUseRecord extends ObjectRecord {

    private final long __threadId;

    //

    public ObjectUseRecord (final long time, final long objectId, final long threadId) {
        super (time, objectId);

        __threadId = threadId;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        super._formatTo (formatter);
        formatter.format (" %x", __threadId);
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_USE;
    }

}
