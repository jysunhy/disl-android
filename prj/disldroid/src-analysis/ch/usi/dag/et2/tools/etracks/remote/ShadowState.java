package ch.usi.dag.et2.tools.etracks.remote;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;
import ch.usi.dag.et2.tools.etracks.util.Outputter;
import ch.usi.dag.util.Maps;
import ch.usi.dag.util.Sets;


abstract class ShadowState {

    private static final Outputter __out__ = ElephantTracksSkeleton.getOutput ();

    //

    private final ShadowObject __shadow;

    //

    private long __lastUseTime = Long.MIN_VALUE;

    private long __deathTime = Long.MIN_VALUE;

    //

    private int __timestampCount = 0;

    private int __lateTimestampCount = 0;

    private int __sameTimestampCount = 0;

    private int __deadTimestampCount = 0;

    //

    private ShadowState (final ShadowObject shadow) {
        // not to be instantiated from outside
        __shadow = shadow;
    }

    //

    public synchronized final boolean updateLastUseTime (final long time) {
        if (! __shadowIsDead ()) {
            __timestampCount++;

            if (time > __lastUseTime) {
                __lastUseTime = time;
                return true;

            } else if (time == __lastUseTime) {
                __sameTimestampCount++;

            } else {
                __lateTimestampCount++;

                __out__.format (
                    "\tignoring timestamp %d (object last used at %d)\n",
                    time, __lastUseTime
                );
            }
        } else {
            //
            // This may happen if we fake GC at the end of the run, because
            // we are forcing release of objects that may be still referenced
            // from other objects. When releasing those, we may try to update
            // a timestamp on a dead object.
            //
            __deadTimestampCount++;

            __out__.format (
                "\tignoring timestamp %d (object died at %d)\n",
                time, __lastUseTime
            );
        }

        return false;
    }

    //

    public synchronized final long handleShadowDeath () {
        __ensureShadowNotDead ();

        __deathTime = __lastUseTime;
        __propagateTimestamp (__deathTime);
        _removeNeighbors ();
        return __deathTime;
    }


    private void __ensureShadowNotDead () {
        if (__shadowIsDead ()) {
            __out__.format (
                "\t%s is already dead (since %08d)\n", __shadow, __deathTime
            );

            throw new IllegalStateException ();
        }
    }

    private boolean __shadowIsDead () {
        return __deathTime != Long.MIN_VALUE;
    }

    //

    public synchronized final void deepPropagateLastUse () {
        //
        // Consider this shadow to be a root, and propagate the last-use
        // timestamp to all its direct and reachable neighbors. Not overly
        // efficient, but it is only performed at the end of the analysis.
        //
        final Set <ShadowObject> visited = Sets.newHashSet ();

        final Queue <ShadowObject> pending = new ArrayDeque <ShadowObject> ();
        __addPending (_neighbors (), visited, pending);

        while (!pending.isEmpty ()) {
            final ShadowObject shadow = pending.remove ();
            final ShadowState state = shadow.getState (ShadowState.class);
            state.updateLastUseTime (__lastUseTime);
            visited.add (shadow);

            __addPending (state._neighbors (), visited, pending);
        }
    }

    private void __addPending (
        final Iterable <ShadowObject> neighbors,
        final Set <ShadowObject> visited, final Queue <ShadowObject> pending
    ) {
        for (final ShadowObject neighbor : neighbors) {
            if (neighbor != null && !visited.contains (neighbor)) {
                pending.add (neighbor);
            }
        }
    }

    //

    /**
     * Propagates a last-use timestamp to neighboring objects, as per the
     * Merlin algorithm.
     */
    private void __propagateTimestamp (final long timestamp) {
        for (final ShadowObject neighbor : _neighbors ()) {
            //
            // The neighbor can be null: if a field or element referencing it
            // may have been explicitly set to null in the client VM.
            //
            if (neighbor != null) {
                final ShadowState state = neighbor.getState (ShadowState.class);
                state.updateLastUseTime (timestamp);
            }
        }
    }


    protected abstract Iterable <ShadowObject> _neighbors ();
    protected abstract void _removeNeighbors ();


    //

    public synchronized final ShadowObject putField (
        final ShadowString fieldName, final ShadowObject newReference
    ) {
        __ensureShadowNotDead ();

        return _putField (fieldName, newReference);
    }


    protected abstract ShadowObject _putField (
        final ShadowString fieldName, final ShadowObject newReference
    );

    //

    public synchronized final ShadowObject arrayStore (
        final int elementIndex, final ShadowObject newReference
    ) {
        __ensureShadowNotDead ();

        return _arrayStore (elementIndex, newReference);
    }


    protected abstract ShadowObject _arrayStore (
        final int elementIndex, final ShadowObject newReference
    );

    //

    public static ShadowState forObject (
        final ShadowObject object, final long timestamp
    ) {
        final ShadowState result = __shadowStateFor (object);
        result.updateLastUseTime (timestamp);
        return result;
    }

    private static ShadowState __shadowStateFor (final ShadowObject object) {
        final ShadowClass shadowClass = object.getShadowClass ();
        if (shadowClass == null) {
            // This usually appears to be the Class class.
            return new UnknownShadowState (object);

        } else if (shadowClass.isArray ()) {
            return new ArrayShadowState (object);

        } else {
            return new ObjectShadowState (object);
        }
    }


    //
    // Shadow state for common objects
    //

    static private final class ObjectShadowState extends ShadowState {
        //
        // XXX LB: Temporarily use the field name id() instead of the
        // ShadowString itself as a key for object fields.
        //
        private final Map <Long, ShadowObject> __fields = Maps.newHashMap ();

        //

        private ObjectShadowState (final ShadowObject shadow) {
            super (shadow);
        }

        //

        @Override
        protected ShadowObject _putField (
            final ShadowString fieldName, final ShadowObject newValue
        ) {
            return __fields.put (fieldName.getId (), newValue);
        }

        @Override
        protected Iterable <ShadowObject> _neighbors () {
            return __fields.values ();
        }

        @Override
        protected void _removeNeighbors () {
            __fields.clear ();
        }

        //

        @Override
        protected ShadowObject _arrayStore (
            final int elementIndex, final ShadowObject newReference
        ) {
            throw new UnsupportedOperationException ();
        }

    }


    //
    // Shadow state for arrays
    //

    static private final class ArrayShadowState extends ShadowState {
        private final Map <Integer, ShadowObject> __elements = Maps.newHashMap ();

        //

        private ArrayShadowState (final ShadowObject shadow) {
            super (shadow);
        }

        //

        @Override
        protected ShadowObject _arrayStore (
            final int elementIndex, final ShadowObject newValue
        ) {
            return __elements.put (elementIndex, newValue);
        }

        @Override
        protected Iterable <ShadowObject> _neighbors () {
            return __elements.values ();
        }

        @Override
        protected void _removeNeighbors () {
            __elements.clear ();
        }

        //

        @Override
        protected ShadowObject _putField (
            final ShadowString fieldName, final ShadowObject newReference
        ) {
            throw new UnsupportedOperationException ();
        }

    }


    //
    // Shadow state for unknowns
    //

    static private final class UnknownShadowState extends ShadowState {
        private final Map <Object, ShadowObject> __neighbors = Maps.newHashMap ();

        //

        private UnknownShadowState (final ShadowObject shadow) {
            super (shadow);
        }

        //

        @Override
        protected ShadowObject _arrayStore (
            final int elementIndex, final ShadowObject newValue
        ) {
            return __neighbors.put (Integer.valueOf (elementIndex), newValue);
        }

        @Override
        protected ShadowObject _putField (
            final ShadowString fieldName, final ShadowObject newValue
        ) {
            return __neighbors.put (fieldName, newValue);
        }

        @Override
        protected Iterable <ShadowObject> _neighbors () {
            return __neighbors.values ();
        }

        @Override
        protected void _removeNeighbors () {
            __neighbors.clear ();
        }

    }

}
