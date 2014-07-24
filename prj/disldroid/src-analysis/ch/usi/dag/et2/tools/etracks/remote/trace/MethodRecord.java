package ch.usi.dag.et2.tools.etracks.remote.trace;

import java.util.Formatter;

abstract class MethodRecord extends EventRecord {

    protected final int _methodId;
    protected final long _receiverId;
    protected final long _threadId;

    //

    public MethodRecord (final long time, final int methodId, final long receiverId, final long threadId) {
        super (time);

        _methodId = methodId;
        _receiverId = receiverId;
        _threadId = threadId;
    }

    @Override
    protected final void _formatTo (final Formatter formatter) {
        super._formatTo (formatter);
        formatter.format (" %x %x %x", _methodId, _receiverId, _threadId);
    }

}
