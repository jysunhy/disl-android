package ch.usi.dag.et2.tools.etracks.remote.trace;


final class ObjectDeathRecord extends ObjectRecord {

    public ObjectDeathRecord (final long time, final long objectId) {
        super (time, objectId);
    }

    //

    @Override
    protected RecordType _recordType () {
        return RecordType.OBJECT_DEATH;
    }

}
