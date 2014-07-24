package ch.usi.dag.et2.tools.etracks.remote.trace;

import java.util.Formatter;

final class ObjectAllocationRecord extends ObjectRecord {

    private final long __size;
    private final String __type;
    private final long __threadId;

    //

    public ObjectAllocationRecord (
        final long time, final long objectId, final long size,
        final String type, final long threadId
    ) {
        super (time, objectId);

        __size = size;
        __type = type;
        __threadId = threadId;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        super._formatTo (formatter);
        formatter.format (" %x %s %x", __size, __type, __threadId);
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_ALLOCATION;
    }

}
