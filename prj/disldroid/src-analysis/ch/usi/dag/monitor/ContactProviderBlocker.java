package ch.usi.dag.monitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import ch.usi.dag.dislre.AREDispatch;

public class ContactProviderBlocker {

    public static Cursor queryGroupFilter(final String[] include, final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor rec = null;
        try {
            if(uri.equals (Uri.parse ("content://com.android.contacts/data"))) {
                String[] rawIdList = getRawIdByGroupId(getGroupId(include, cr), cr);
                String[] newSelectionArgs = new String[selectionArgs.length + rawIdList.length];
                System.arraycopy(selectionArgs, 0, newSelectionArgs, 0, selectionArgs.length);
                System.arraycopy(rawIdList, 0, newSelectionArgs, 0, rawIdList.length);
                rec = cr.query (uri, projection, selection + " and raw_contact_id IN ("+getQuestionMark(rawIdList)+")", newSelectionArgs, sortOrder);
            }
            else if(uri.equals (Uri.parse ("content://com.android.contacts/raw_contacts"))) {
                String[] rawIdList = getRawIdByGroupId(getGroupId(include, cr), cr);
                String[] newSelectionArgs = new String[selectionArgs.length + rawIdList.length];
                System.arraycopy(selectionArgs, 0, newSelectionArgs, 0, selectionArgs.length);
                System.arraycopy(rawIdList, 0, newSelectionArgs, 0, rawIdList.length);
                rec = cr.query (uri, projection, selection + " and _id IN ("+getQuestionMark(rawIdList)+")", newSelectionArgs, sortOrder);
            }
            else if(uri.equals (Uri.parse ("content://com.android.contacts/contacts"))) {
                String[] contactIdList = getContactIdByRawId(getRawIdByGroupId(getGroupId(include, cr), cr), cr);
                String[] newSelectionArgs = new String[selectionArgs.length + contactIdList.length];
                System.arraycopy(selectionArgs, 0, newSelectionArgs, 0, selectionArgs.length);
                System.arraycopy(contactIdList, 0, newSelectionArgs, 0, contactIdList.length);
                rec = cr.query (uri, projection, selection + " and _id IN ("+getQuestionMark(contactIdList)+")", newSelectionArgs, sortOrder);
            }
            else {
//                TODO: need handle more uri
            }
        } catch (Exception e) {
            AREDispatch.NativeLog ("Exception: " + e.toString ());
        }
        return rec;
    }
    
    private static String[] getGroupId(String[] namelist, ContentResolver cr) throws Exception {
        List<String> lst = new ArrayList<String>();
        Cursor cur = cr.query(ContactsContract.Groups.CONTENT_URI,
                new String[]{ContactsContract.Groups._ID},
                ContactsContract.Groups.TITLE+" IN ('"+join(",", namelist)+"')",
                null,
                null);
        if(cur != null && cur.moveToFirst ()) {
            do {
                lst.add (cur.getString (0));
            }while(cur.moveToNext ());
        }
        String[] rec = new String[lst.size()];
        lst.toArray(rec);
        return rec;
    }
    
    private static String[] getRawIdByGroupId(String[] grouplst, ContentResolver cr) throws Exception {
        Set<String> set=new HashSet<String>();
        for (String s : grouplst) {
            Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                    "mimetype=? AND data1=?",
                    new String[]{"vnd.android.cursor.item/group_membership", s},
                    null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    set.add(cur.getString(0));
                } while (cur.moveToNext());
            }
            cur.close();
        }
        String[] rec = new String[set.size()];
        set.toArray(rec);
        return rec;
    }

    private static String join(String connection, String [] namelist) {
        String rec = "";
        for (int i = 0; i < namelist.length- 1; i++) {
            rec = rec + namelist[i] + connection + ' ';
        }
        rec = rec + namelist[namelist.length-1];
        return rec;
    }

    private static String[] getContactIdByRawId(String[] rawlst, ContentResolver cr) throws Exception {
        Set<String> set=new HashSet<String>();
        Cursor cur = cr.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID},
                "_id IN (" + getQuestionMark(rawlst) + ")",
                rawlst,
                null);
        if (cur != null && cur.moveToFirst()) {
            do {
                set.add(cur.getString(0));
            } while (cur.moveToNext());
        }
        cur.close();
        String[] rec = new String[set.size()];
        set.toArray(rec);
        return rec;
    }
    
    private static String getQuestionMark(String[] rawIdList) {
        String str = "";
        for (int i = 0; i < rawIdList.length; i++) {
            if (i == rawIdList.length - 1) {
                str += "?";
            }
            else {
                str += "?, ";
            }
        }
        return str;
    }
}
