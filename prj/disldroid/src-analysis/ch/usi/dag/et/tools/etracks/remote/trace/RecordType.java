package ch.usi.dag.et.tools.etracks.remote.trace;

// TODO: Is this enum necessary?
enum RecordType {

    OBJECT_ALLOCATION ('A'),
    OBJECT_DEATH ('D'),
    OBJECT_USE ('R'),
    REFERENCE_UPDATE ('U'),
    METHOD_ENTRY ('M'),
    METHOD_EXIT ('E');

    //

    private final char __marker;

    //

    private RecordType (final char marker) {
        __marker = marker;
    }

    //

    public char marker () {
        return __marker;
    }

    //

    public static final RecordType valueOf (final char marker) {
        for (RecordType type : values ()) {
            if (marker == type.__marker) {
                return type;
            }
        }

        throw new IllegalArgumentException ("no record type for marker '"+ marker +"'");
    }

}
