package ch.usi.dag.et.tools.etracks.remote;

import java.util.Iterator;
import java.util.Map;

import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowString;
import ch.usi.dag.et.tools.etracks.util.Outputter;
import ch.usi.dag.util.Maps;


abstract class ShadowState {

    private static final Outputter __out__ = ElephantTracksSkeleton.getOutput ();

    //

    private final ShadowObject __shadow;

    private long __lastUseTime = Long.MIN_VALUE;

    private long __deathTime = Long.MIN_VALUE;

    int timestampCount = 0;

    int redundantTimestampCount = 0;

    //

    private ShadowState (final ShadowObject shadow) {
        // not to be instantiated from outside
        __shadow = shadow;
    }

    //

    public final void updateLastUseTime (final long time) {
        timestampCount++;
        if (time > __lastUseTime) {
            __lastUseTime = time;

        } else {
            redundantTimestampCount++;
            if (time < __lastUseTime) {
                __out__.format (
                    "\tignoring usage timestamp %d, latest use was at %d\n",
                    time, __lastUseTime
                );
            }
        }
    }

    public final long getLastUseTime () {
        return __lastUseTime;
    }

    public final long getDeathTime () {
        return __deathTime;
    }

    //

    public final boolean isDead () {
        return __deathTime != Long.MIN_VALUE;
    }

    public final long handleShadowDeath () {
        _ensureShadowNotDead ();
        __deathTime = __lastUseTime;

        __releaseNeighbors (__deathTime);
        return __deathTime;
    }

    private void __releaseNeighbors (final long deathTime) {
        final Iterator <ShadowObject> neighbors = _neighborsIterator ();
        while (neighbors.hasNext ()) {
            final ShadowObject neighbor = neighbors.next ();
            if (neighbor != null) {
            	final ShadowState neighborState = neighbor.getState (ShadowState.class);
            	neighborState.updateLastUseTime (deathTime);
            }

            neighbors.remove ();
        }
    }

    //

    protected abstract Iterator <ShadowObject> _neighborsIterator ();

    protected final void _ensureShadowNotDead () {
        if (isDead ()) {
            __out__.format ("\t%s is already dead, died at %08d",
                __shadow, __deathTime
            );
        }
    }

    //

    public final boolean isArray () {
        return __shadow.getShadowClass ().isArray ();
    }

    //

    public ShadowObject putField (
        final ShadowString fieldName, final ShadowObject newReference
    ) {
        throw new UnsupportedOperationException ();
    }

    public ShadowObject arrayStore (
        final int elementIndex, final ShadowObject newReference
    ) {
        throw new UnsupportedOperationException ();
    }

    //

    public static ShadowState forObject (
        final ShadowObject object, final long timestamp
    ) {
        final ShadowClass shadowClass = object.getShadowClass ();
        if (shadowClass == null) {
            return __timestampShadowState (timestamp, new UnknownShadowState (object));
        } else if (shadowClass.isArray ()) {
            return __timestampShadowState (timestamp, new ArrayShadowState (object));
        } else {
            return __timestampShadowState (timestamp, new ObjectShadowState (object));
        }
    }

    private static ShadowState __timestampShadowState (
        final long timestamp, final ShadowState shadowState
    ) {
        shadowState.updateLastUseTime (timestamp);
        return shadowState;
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
        public ShadowObject putField (
            final ShadowString fieldName, final ShadowObject newValue
        ) {
            _ensureShadowNotDead ();
            return __fields.put (fieldName.getId (), newValue);
        }

        @Override
        protected Iterator <ShadowObject> _neighborsIterator () {
            return __fields.values ().iterator ();
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
        public ShadowObject arrayStore (
            final int elementIndex, final ShadowObject newValue
        ) {
            _ensureShadowNotDead ();
            return __elements.put (elementIndex, newValue);
        }

        @Override
        protected Iterator <ShadowObject> _neighborsIterator () {
            return __elements.values ().iterator ();
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
        public ShadowObject arrayStore (
            final int elementIndex, final ShadowObject newValue
        ) {
            _ensureShadowNotDead ();
            return __neighbors.put (Integer.valueOf (elementIndex), newValue);
        }

        @Override
        public ShadowObject putField (
            final ShadowString fieldName, final ShadowObject newValue
        ) {
            _ensureShadowNotDead ();
            return __neighbors.put (fieldName, newValue);
        }

        @Override
        protected Iterator <ShadowObject> _neighborsIterator () {
            return __neighbors.values ().iterator ();
        }

    }

}
