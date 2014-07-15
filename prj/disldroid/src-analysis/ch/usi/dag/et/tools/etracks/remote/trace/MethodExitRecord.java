package ch.usi.dag.et.tools.etracks.remote.trace;

final class MethodExitRecord extends MethodRecord {

    public MethodExitRecord (
        final long time, final int methodId,
        final long receiverId, final long threadId
    ) {
        super (time, methodId, receiverId, threadId);
    }

    //

    @Override
    protected RecordType _recordType () {
        return RecordType.METHOD_EXIT;
    }

}
