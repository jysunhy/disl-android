package ch.usi.dag.et2.tools.etracks.remote.trace;

final class MethodEntryRecord extends MethodRecord {

    public MethodEntryRecord (
        final long time, final int methodId,
        final long receiverId, final long threadId
    ) {
        super (time, methodId, receiverId, threadId);
    }

    //

    @Override
    protected RecordType _recordType () {
        return RecordType.METHOD_ENTRY;
    }

}
