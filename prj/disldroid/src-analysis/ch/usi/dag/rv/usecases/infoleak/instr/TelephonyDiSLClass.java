package ch.usi.dag.rv.usecases.infoleak.instr;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.rv.usecases.infoleak.DataLeakMonitorState;
import ch.usi.dag.rv.usecases.infoleak.events.datasource.GetDeviceIdEvent;
import ch.usi.dag.rv.usecases.infoleak.events.datasource.GetSubscriberIdEvent;

public class TelephonyDiSLClass {
    @AfterReturning(marker=BytecodeMarker.class,
    guard=Guard.DeviceIdGuard.class,
    args = "invokevirtual")
    public static void getDeviceId (final DynamicContext dc) {
        final String value = dc.getStackValue (0, String.class);
        DataLeakMonitorState.getInstance ().newGlobalEvent (new GetDeviceIdEvent ("DeviceId", value));
    }

    @AfterReturning(marker=BytecodeMarker.class,
    guard=Guard.SubscriberIdGuard.class,
    args = "invokevirtual")
    public static void getSubscriberId (final DynamicContext dc) {
        final String value = dc.getStackValue (0, String.class);
        DataLeakMonitorState.getInstance ().newGlobalEvent (new GetSubscriberIdEvent ("SubscriberId", value));
    }
}
