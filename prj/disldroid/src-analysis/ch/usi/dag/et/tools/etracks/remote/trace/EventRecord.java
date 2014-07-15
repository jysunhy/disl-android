package ch.usi.dag.et.tools.etracks.remote.trace;

import java.util.Formattable;
import java.util.Formatter;

/**
 * Note: this class has a natural ordering that is inconsistent with {@link Object#equals() equals}.
 */
abstract class EventRecord implements Comparable <EventRecord>, Formattable {

    private final long __time;

    //

    public EventRecord (final long time) {
        __time = time;
    }

    //

    public final long getTime () {
        return __time;
    }

    // INTERFACE: Formattable

    @Override
    public final void formatTo (
        final Formatter formatter, final int flags,
        final int width, final int precision
    ) {
        formatter.format ("%08d %c ",  __time, _recordType ().marker ());
        _formatTo (formatter);
    }

    // INTERFACE: Comparable

    @Override
    public final int compareTo (final EventRecord that) {
        //
        // We cannot use Long.signum (this.__time - that.__time), because
        // time can be negative in certain cases.
        //
        if (this.__time > that.__time) {
            return 1;
        } else if (this.__time < that.__time) {
            return -1;
        } else {
            return 0;
        }
    }

    //

    protected abstract RecordType _recordType ();
    protected abstract void _formatTo (final Formatter formatter);
}
