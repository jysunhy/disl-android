package ch.usi.dag.monitor.disl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.monitor.ContentProviderMonitor;

public class SandboxMonitor {
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_query_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static Cursor android_content_ContentResolver_query(final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder){

        if(uri!=null) {
            AREDispatch.NativeLog ("Monitoring "+cr.getClass ().getCanonicalName ()+" query URI:"+uri.toSafeString ());
        }
        ContentProviderMonitor.queryEvent (uri, projection, selection, selectionArgs, sortOrder);
        String args="";
        if(projection != null){
            for (final String element : projection) {
                args += element+" ";
            }
        }
        args += ";";
        if(selection!=null) {
            args += selection;
        }
        args += ";";
        if(selectionArgs != null)
        {
            for (final String selectionArg : selectionArgs) {
                args+=selectionArg+" ";
            }
        }
        args+=";";
        if(sortOrder!=null) {
            args+=sortOrder;
        }
        AREDispatch.NativeLog ("Args:"+args);
        return cr.query (uri, projection, selection, selectionArgs, sortOrder);
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_insert_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final Uri android_content_ContentResolver_insert(final ContentResolver cr, final Uri url, final ContentValues values){
        AREDispatch.NativeLog ("Monitoring "+cr.getClass ().getCanonicalName ()+" insert URI:"+url.toSafeString ());
        final Uri res = cr.insert (url, values);
        if(res != null) {
            AREDispatch.NativeLog ("return URI: "+res.toSafeString ());
        }
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_delete_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_delete(final ContentResolver cr,final Uri url, final String where, final String[] selectionArgs){
        AREDispatch.NativeLog ("Monitoring "+cr.getClass ().getCanonicalName ()+" delete URI:"+url.toSafeString ());
        final int res = cr.delete (url, where, selectionArgs);
        AREDispatch.NativeLog ("return int: "+res);
        return res;
    }
    @Monitor(marker=BytecodeMarker.class, scope="*.*", guard=SandboxGuard.ContentResolver_update_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_update(final ContentResolver cr, final Uri uri, final ContentValues values, final String where, final String[] selectionArgs) {
        AREDispatch.NativeLog ("Monitoring "+cr.getClass ().getCanonicalName ()+" update URI:"+uri.toSafeString ());
        final int res = cr.update (uri, values, where, selectionArgs);
        AREDispatch.NativeLog ("return int: "+res);
        return res;
    }
    //public final Bundle call(final Uri uri, final String method, final String arg, final Bundle extras)
}
