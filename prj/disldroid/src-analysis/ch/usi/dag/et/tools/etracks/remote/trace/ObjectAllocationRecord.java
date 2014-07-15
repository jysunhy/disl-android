package ch.usi.dag.et.tools.etracks.remote.trace;

import java.util.Formatter;

final class ObjectAllocationRecord extends EventRecord {

    private final long __objectId;
    private final long __size;
    private final String __type;
    private final long __threadId;

    //

    public ObjectAllocationRecord (
        final long time, final long objectId, final long size,
        final String type, final long threadId
    ) {
        super (time);

        __objectId = objectId;
        __size = size;
        __type = type;
        __threadId = threadId;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        formatter.format (
            "%x %x %s %x\n", __objectId, __size, __type, __threadId
        );
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_ALLOCATION;
    }

}
