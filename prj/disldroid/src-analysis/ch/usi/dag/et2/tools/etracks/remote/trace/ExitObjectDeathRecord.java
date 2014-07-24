package ch.usi.dag.et2.tools.etracks.remote.trace;

import java.util.Formatter;


final class ExitObjectDeathRecord extends ObjectRecord {

    private final long __lastUseTime;
    
    public ExitObjectDeathRecord (final long time, final long objectId, final long lastUseTime) {
        super (time, objectId);
        
        __lastUseTime = lastUseTime;
    }

    //

    @Override
    protected final void _formatTo (final Formatter formatter) {
        super._formatTo (formatter);
        formatter.format (" %08d", __lastUseTime);
    }
    
    @Override
    protected RecordType _recordType () {
        return RecordType.EXIT_OBJECT_DEATH;
    }
    
    // INTERFACE: Comparable

    @Override
    public final int compareTo (final EventRecord record) {
        final int result = super.compareTo (record);
        if (result == 0 && record instanceof ExitObjectDeathRecord) {
            final ExitObjectDeathRecord that = (ExitObjectDeathRecord) record;
            
            if (this.__lastUseTime > that.__lastUseTime) {
                return 1;
            } else if (this.__lastUseTime < that.__lastUseTime) {
                return -1;
            } else {
                return 0;
            }

        } else {
            return result;
        }

    }

}
