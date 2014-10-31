package ch.usi.dag.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import ch.usi.dag.dislre.AREDispatch;

public class ContactProviderBlocker {

    public static Cursor queryGroupFilter(final String[] include, final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor rec = null;
        String newSelection = null;
        String[] newSelectionArgs = null;
        String[] lst = null;
        try {
            if(uri.equals (Uri.parse ("content://com.android.contacts/data")) || uri.equals (Uri.parse ("content://com.android.contacts/raw_contacts"))) {
                lst = getRawIdByGroupId(getGroupId(include, cr), cr);
            }
            else if(uri.equals (Uri.parse ("content://com.android.contacts/contacts"))) {
                lst = getContactIdByRawId(getRawIdByGroupId(getGroupId(include, cr), cr), cr);
            }
            else if(uri.equals (Uri.parse ("content://com.android.contacts/groups"))) {
                lst = getGroupId(include, cr);
            }
            else {
//                TODO: need handle more uri
            }
            newSelection = getSelection(uri, selection, lst);
            newSelectionArgs = getSelectionArgs(selectionArgs, lst);
            AREDispatch.NativeLog("original: " + selection + " " + Join(",", selectionArgs));
            AREDispatch.NativeLog("data uri change-> selection:" + newSelection + " args:" + Join(",", newSelectionArgs));
            rec = cr.query (uri, projection, newSelection, newSelectionArgs, sortOrder);
        } catch (Exception e) {
            AREDispatch.NativeLog ("Exception: " + e.toString ());
        }
        return rec;
    }
    
    public static int deleteGroupFilter(final String[] include, final ContentResolver cr, final Uri uri, final String selection, final String[] selectionArgs) {
        String newSelection = null;
        String[] newSelectionArgs = null;
        String[] lst = null;
        try {
            if(uri.equals (Uri.parse ("content://com.android.contacts/data")) || uri.equals (Uri.parse ("content://com.android.contacts/raw_contacts"))) {
                lst = getRawIdByGroupId(getGroupId(include, cr), cr);
            }
            else if(uri.equals (Uri.parse ("content://com.android.contacts/contacts"))) {
                lst = getContactIdByRawId(getRawIdByGroupId(getGroupId(include, cr), cr), cr);
            }
            else {
//                TODO: need handle more uri
            }
            newSelection = getSelection(uri, selection, lst);
            newSelectionArgs = getSelectionArgs(selectionArgs, lst);
        } catch (Exception e) {
            AREDispatch.NativeLog ("Exception: " + e.toString ());
        }
        return cr.delete (uri, newSelection, newSelectionArgs);
    }
    
    public static Uri insertGroupFilter(final String[] include, final ContentResolver cr, final Uri uri, final ContentValues values) {
        Set<String> set = new HashSet<String>(Arrays.asList(include)); 
        if(values.get ("ContactsContract.Data.MIMETYPE").equals ("vnd.android.cursor.item/group")) {
            if(set.contains(values.get("title"))) {
                return null;
            }
        }
        return cr.insert (uri, values);
    }
    
    private static String[] getGroupId(String[] namelist, ContentResolver cr) throws Exception {
        List<String> lst = new ArrayList<String>();
        Cursor cur = cr.query(ContactsContract.Groups.CONTENT_URI,
                new String[]{ContactsContract.Groups._ID},
                ContactsContract.Groups.TITLE+" IN (" + getQuestionMark(namelist) + ")",
                namelist,
                null);
        if(cur != null && cur.moveToFirst ()) {
            do {
                lst.add (cur.getString (0));
            }while(cur.moveToNext ());
        }
        if(lst.size () == 0) {
            return null;
        }
        String[] rec = new String[lst.size()];
        lst.toArray(rec);
//        AREDispatch.NativeLog ("getGroupId:" + Join(",", rec));
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
//        AREDispatch.NativeLog ("getRawIdByGroupId:" + Join(",", rec));
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
//        AREDispatch.NativeLog ("getContactIdByRawId:" + Join(",", rec));
        return rec;
    }
    
    private static String getSelection(Uri uri, String selection, String[] lst) {
        String rec = null;
        if(uri.equals (Uri.parse ("content://com.android.contacts/data"))) {
            if(selection != null) {
                rec = selection + " and raw_contact_id IN ("+getQuestionMark(lst)+")";
            }
            else {
                rec = "raw_contact_id IN ("+getQuestionMark(lst)+")";
            }
        }
        else if(uri.equals (Uri.parse ("content://com.android.contacts/raw_contacts")) 
        || uri.equals (Uri.parse ("content://com.android.contacts/contacts"))
        || uri.equals (Uri.parse ("content://com.android.contacts/groups"))) {
            if(selection != null) {
                rec = selection + " and _id IN ("+getQuestionMark(lst)+")";
            }
            else {
                rec = "_id IN ("+getQuestionMark(lst)+")";
            }
        }
        else {
//          TODO: need handle more uri
        }
        return rec;
    }
    
    private static String[] getSelectionArgs(String[] SelectionArgs, String[] lst) {
        if(SelectionArgs == null) {
            if(lst == null || lst.length == 0) {
                return null;
            }
            return lst;
        }
        String[] rec = new String[SelectionArgs.length + lst.length];
        System.arraycopy (SelectionArgs, 0, rec, 0, SelectionArgs.length);
        System.arraycopy (lst, 0, rec, SelectionArgs.length, lst.length);
        if(rec.length == 0) {
            return null;
        }
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

    
    private static String Join(String con, String[] lst) {
        String rec = "";
        if(lst == null) {
            return null;
        }
        for (int i = 0; i < lst.length; i++) {
            if(i == lst.length - 1) {
                rec += lst[i];
            }
            else {
                rec += lst[i] + con + " ";
            }
        }
        return rec;
    }

}
