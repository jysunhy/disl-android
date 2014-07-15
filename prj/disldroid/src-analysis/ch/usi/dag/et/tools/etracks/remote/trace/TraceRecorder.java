package ch.usi.dag.et.tools.etracks.remote.trace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import ch.usi.dag.et.tools.etracks.util.Outputter;
import ch.usi.dag.util.Lists;


public final class TraceRecorder {

    private static final Outputter __out__ = Outputter.create ("tracer.log");

    //

    private static final int __DEATH_COUNT_THRESHOLD__ = 100;

    private static final ObjectDeathRecord
        __DUMMY_OBJECT_DEATH__ = new ObjectDeathRecord (0, 0);

    //

    private List <EventRecord> __pendingRecords = Lists.newArrayList ();

    private final ArrayList <EventRecord> __pendingDeaths = Lists.newArrayList ();

    private final LinkedList <EventRecord []> __recordChunks = Lists.newLinkedList ();

    /**
     * The latest death record added to the trace. Updated when adding new death
     * records.
     */
    private ObjectDeathRecord __latestDeath = __DUMMY_OBJECT_DEATH__;

    /**
     * The time before which we should not insert any records, because all the
     * preceding records were already sorted and flushed. This time gets updated
     * when death records are processed and is set to the time of the latest
     * death records.
     */
    private long __minimalTime = 0;


    /**
     * The latest observed time of records in the trace. This time gets updated
     * whenever a records is added to the trace.
     */
    private long __latestTime = 0;

    //

    synchronized public void recordObjectAllocation (
        final long time, final long objectId, final String className,
        final long size, final long threadId
    ) {
        __addRecordIfNotLate (new ObjectAllocationRecord (
            time,  objectId, size, className, threadId
        ));
    }


    synchronized public void recordObjectDeath (final long time, final long objectId) {
        final ObjectDeathRecord death = new ObjectDeathRecord (time, objectId);

        if (__recordCanBeAdded (death)) {
            __pendingDeaths.add (death);
            if (death.compareTo (__latestDeath) > 0) {
                __latestDeath = death;
            }
        }

        if (__pendingDeaths.size () >= __DEATH_COUNT_THRESHOLD__) {
            __flushPendingDeaths ();
        }
    }

    //

    synchronized public void recordObjectUse (final long time, final long objectId, final long threadId) {
        __addRecordIfNotLate (new ObjectUseRecord (
            time,  objectId,  threadId
        ));
    }

    //

    synchronized public void recordPointerUpdate (
        final long time, final long ownerId, final long oldTargetId,
        final long newTargetId, final long threadId
    ) {
        __addRecordIfNotLate (new ReferenceUpdateRecord (
            time, ownerId, oldTargetId, newTargetId, threadId
        ));
    }

    //

    synchronized public void recordMethodEntry (
        final long time, final int methodId, final long receiverId, final long threadId
    ) {
        __addRecordIfNotLate (new MethodEntryRecord (
            time, methodId, receiverId, threadId
        ));
    }


    synchronized public void recordMethodExit (
        final long time, final int methodId, final long receiverId, final long threadId
    ) {
        __addRecordIfNotLate (new MethodExitRecord (
            time, methodId, receiverId, threadId
        ));
    }

    //

    synchronized public void dumpRecords (final String fileName) {
        dumpRecords (new File (fileName));
    }

    synchronized public void dumpRecords (final File file) {
        __flushPendingRecords ();

        try {
            final Formatter out = new Formatter (file);
            for (final EventRecord [] chunk : __recordChunks) {
                __dumpRecords (chunk, out);
            }
            out.close ();

        } catch (final IOException ioe) {
            System.err.printf ("error: failed to dump trace to %s: %s\n",
                file.toString (), ioe.getMessage ()
            );
        }
    }


    private void __flushPendingRecords () {
        __flushPendingDeaths ();

        //
        // Sort the records we currently have and put them in the dump list.
        //
        final EventRecord [] sortedRecords = __sortList (__pendingRecords);

        __recordChunks.add (sortedRecords);
        __pendingRecords = Lists.newArrayList ();

    }


    private void __flushPendingDeaths () {
        __out__.format ("%08d:flushPendingDeaths: pending %d, since %08d\n",
            __latestTime, __pendingDeaths.size (), __minimalTime
        );

        if (__pendingDeaths.isEmpty ()) {
            return;
        }

        //
        // Add all object deaths to the list of records, dump it into an array,
        // and sort it. Then split the array into two parts, with the split
        // point being the latest object death record. Records from the first
        // part are be dumped, the other records (including the currently latest
        // death record) are retained.
        //
        __pendingRecords.addAll (__pendingDeaths);
        __pendingDeaths.clear ();

        //

        final EventRecord [] sortedRecords = __sortList (__pendingRecords);
        final int splitIndex = Arrays.binarySearch (sortedRecords, __latestDeath);
        if (splitIndex < 0) {
            __out__.format (
                "%08d:flushPendingDeaths: failed to find latest death: %s",
                __latestTime, __latestDeath
            );
            throw new RuntimeException ("death eludes me...");
        }

        //

        __pendingRecords = Lists.newArrayList (Arrays.copyOfRange (
            sortedRecords, splitIndex, sortedRecords.length
        ));

        __recordChunks.add (Arrays.copyOf (sortedRecords, splitIndex));

        __minimalTime = __latestDeath.getTime ();
    }

    //

    private boolean __addRecordIfNotLate (final EventRecord record) {
        if (__recordCanBeAdded (record)) {
            return __pendingRecords.add (record);

        } else {
            return false;
        }
    }


    private boolean __recordCanBeAdded (final EventRecord record) {
        final long time = record.getTime ();
        if (time >= __minimalTime) {
            if (time > __latestTime) {
                __latestTime = time;
            }

            return true;

        } else {
            __out__.format (
                "%08d: late record: min %08d, %s",
                __latestTime, __minimalTime, record
            );
            return false;
        }
    }

    //

    private static void __dumpRecords (
        final EventRecord [] records, final Formatter out
    ) {
        for (final EventRecord record : records) {
            out.format ("%s", record);
        }
    }

    private static EventRecord [] __sortList (final List <EventRecord> list) {
        final EventRecord [] result = list.toArray (new EventRecord[0]);
        Arrays.sort (result);
        return result;
    }

}
