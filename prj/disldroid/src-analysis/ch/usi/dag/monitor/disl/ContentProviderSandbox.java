package ch.usi.dag.monitor.disl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.marker.BytecodeMarker;

public class ContentProviderSandbox {
    @Monitor(marker=BytecodeMarker.class, guard=SandboxGuard.ContentResolver_query_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static Cursor android_content_ContentResolver_query(final ContentResolver cr, final Uri uri, final String[] projection,
        String selection, String[] selectionArgs, final String sortOrder){
        //restrict scope to group Sandbox only
        if(selection == null) {
            selection = "";
        }
        if(selectionArgs == null){
            selectionArgs = new String[0];
        }
        DBHelper.addSandboxGroupFilter(uri, selection, selectionArgs);
        return cr.query (uri, projection, selection, selectionArgs, sortOrder);
    }

    @Monitor(marker=BytecodeMarker.class, guard=SandboxGuard.ContentResolver_insert_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final Uri android_content_ContentResolver_insert(final ContentResolver cr, final Uri uri, final ContentValues values){
        //each time add a new contact, add it into sandbox group
        if(uri.equals (ContactsContract.RawContacts.CONTENT_URI)){
            final Uri res = cr.insert (uri, values);
            DBHelper.addContactToSandboxGroup(ContentUris.parseId (res));
            return res;
        }
        //restrict moving contacts to any other group
        if(uri.equals (ContactsContract.Data.CONTENT_URI)){
            if(values !=null && values.containsKey (ContactsContract.Data.MIMETYPE)){
                if(values.get (ContactsContract.Data.MIMETYPE) == ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE){
                    return Uri.parse (ContactsContract.Data.CONTENT_URI+"/" + DBHelper.getGroupDataIdForPerson(values.get(ContactsContract.Data.CONTACT_ID)));
                }
            }
        }
        return cr.insert (uri, values);
    }


    @Monitor(marker=BytecodeMarker.class, guard=SandboxGuard.ContentResolver_delete_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_delete(final ContentResolver cr,final Uri uri, String where, String[] selectionArgs){
        //restrict scope to group Sandbox only
        if(where == null) {
            where = "";
        }
        if(selectionArgs == null){
            selectionArgs = new String[0];
        }
        DBHelper.addSandboxGroupFilter(uri, where, selectionArgs);
        final int res = cr.delete (uri, where, selectionArgs);
        return res;
    }

    @Monitor(marker=BytecodeMarker.class, guard=SandboxGuard.ContentResolver_update_Guard.class, args = "invokevirtual,invokestatic,invokeinterface")
    public static final int android_content_ContentResolver_update(final ContentResolver cr, final Uri uri, final ContentValues values,  String where,  String[] selectionArgs) {
        //restrict scope to group Sandbox only
        if(where == null) {
            where = "";
        }
        if(selectionArgs == null){
            selectionArgs = new String[0];
        }
        DBHelper.addSandboxGroupFilter(uri, where, selectionArgs);

        //restrict updating group, directly return 0
        if(uri.equals (ContactsContract.Data.CONTENT_URI)){
            if(values !=null && values.containsKey (ContactsContract.Data.MIMETYPE)){
                if(values.get (ContactsContract.Data.MIMETYPE) == ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE){
                    return 0;
                }
            }
        }

        final int res = cr.update (uri, values, where, selectionArgs);
        return res;
    }

}
