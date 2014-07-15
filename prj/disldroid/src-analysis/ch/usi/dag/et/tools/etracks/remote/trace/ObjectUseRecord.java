package ch.usi.dag.et.tools.etracks.remote.trace;

import java.util.Formatter;

final class ObjectUseRecord extends EventRecord {

    private final long __objectId;
    private final long __threadId;

    //

    public ObjectUseRecord (final long time, final long objectId, final long threadId) {
        super (time);

        __objectId = objectId;
        __threadId = threadId;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        formatter.format ("%x %x\n", __objectId, __threadId);
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_USE;
    }

}
