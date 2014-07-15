package ch.usi.dag.et.tools.etracks.remote.trace;

import java.util.Formatter;

final class ReferenceUpdateRecord extends EventRecord {

    private final long __oldTargetId;
    private final long __newTargetId;
    private final long __objectId;
    private final long __threadId;

    //

    public ReferenceUpdateRecord (
        final long time, final long objectId,
        final long oldTargetId, final long newTargetId
    ) {
        this (time, oldTargetId, newTargetId, objectId, -1);
    }

    public ReferenceUpdateRecord (
        final long time, final long objectId,
        final long oldTargetId, final long newTargetId, final long threadId
    ) {
        super (time);

        __objectId = objectId;
        __threadId = threadId;
        __oldTargetId = oldTargetId;
        __newTargetId = newTargetId;
    }

    //

    @Override
    protected void _formatTo (final Formatter formatter) {
        formatter.format (
            "%x %x %x %x\n", __oldTargetId, __objectId, __newTargetId, __threadId
        );
    }

    @Override
    protected RecordType _recordType () {
        return RecordType.REFERENCE_UPDATE;
    }

}
