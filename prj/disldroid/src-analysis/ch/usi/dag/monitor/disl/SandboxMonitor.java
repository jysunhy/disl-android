package ch.usi.dag.monitor.disl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.monitor.ContentProviderMonitor;

public class SandboxMonitor {

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_query_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static Cursor android_content_ContentResolver_query(final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder){
        ContentProviderMonitor.queryEvent(uri, projection, selection, selectionArgs, sortOrder);
        return cr.query (uri, projection, selection, selectionArgs, sortOrder);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_insert_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final Uri android_content_ContentResolver_insert(final ContentResolver cr, final Uri uri, final ContentValues values){
        final Uri res = cr.insert (uri, values);
        ContentProviderMonitor.newEvent (uri, values);
        if(res != null) {
            AREDispatch.NativeLog ("return URI: "+res.toSafeString ());
        }
        return res;
    }

    //bulk insert
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_bulkInsert_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_bulkInsert(final ContentResolver cr, final Uri uri, final ContentValues[] values){
        final int res = cr.bulkInsert (uri, values);
        ContentProviderMonitor.newMulEvent (uri, values);
        if(res != 0) {
            AREDispatch.NativeLog ("return Cursor row: "+res);
        }
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_delete_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_delete(final ContentResolver cr,final Uri uri, final String where, final String[] selectionArgs){
        ContentProviderMonitor.deleteEvent (uri, where, selectionArgs);
        final int res = cr.delete (uri, where, selectionArgs);
        AREDispatch.NativeLog ("return int: "+res);
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_update_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_update(final ContentResolver cr, final Uri uri, final ContentValues values, final String where, final String[] selectionArgs) {
        final int res = cr.update (uri, values, where, selectionArgs);
        ContentProviderMonitor.updateEvent(uri, values, where, selectionArgs);
        AREDispatch.NativeLog ("return int: "+res);
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_call_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final Bundle call(final ContentResolver cr, final Uri uri, final String method, final String arg, final Bundle extras) {
        final Bundle res = cr.call (uri, method, arg, extras);
        ContentProviderMonitor.callEvent (uri, method, arg, extras);
        return res;
    }
    
}
