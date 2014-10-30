package ch.usi.dag.monitor.disl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.monitor.ContactProviderBlocker;

public class SandboxBlocker {
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_query_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static Cursor android_content_ContentResolver_query(final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder){
        return ContactProviderBlocker.queryGroupFilter(new String[] {"Friends"}, cr, uri, projection, selection, selectionArgs, sortOrder);
    }

    
}
