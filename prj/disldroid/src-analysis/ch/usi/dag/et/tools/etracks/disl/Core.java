package ch.usi.dag.et.tools.etracks.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.uid.UniqueMethodId;
import ch.usi.dag.et.tools.etracks.local.LocalElephantTracks;

public class Core {
    @Before (marker = BodyMarker.class, guard = Guard.ObjectConstructorOnly.class)
    public static void onObjectConstructorEntry (
        final UniqueMethodId umi, final DynamicContext dc
    ) {
        LocalElephantTracks.onObjectAllocation (
            dc.getThis (), Thread.currentThread ().getId ()
        );
    }
}
