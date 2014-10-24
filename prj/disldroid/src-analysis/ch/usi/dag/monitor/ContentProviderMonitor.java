package ch.usi.dag.monitor;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import ch.usi.dag.dislre.AREDispatch;

public class ContentProviderMonitor {
//    private final EnumProviderType providerType = null;

//    public ContentProviderMonitor(final Uri uri, final String[] args) {
//        providerType = findProviderType(uri);
//    }

    public static void newEvent(final Uri uri, final ContentValues values) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: new Event";
        str += " Value: " + values.toString ();
        AREDispatch.NativeLog (str);
    }

    public static void newMulEvent(final Uri uri, final ContentValues[] values) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: new multiple Events";
        str += " Count:" + values.length;
        str += " Value: " + values.toString ();
        AREDispatch.NativeLog (str);
    }

    public static void deleteEvent(final Uri uri, final String where, final String[] selectionArgs) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: delete Event";
        str += " Condition: " + getCondition(where, selectionArgs);
        AREDispatch.NativeLog (str);
    }

    public static void queryEvent(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: query Event";
        str += " Condition: " + getCondition(projection, selection, selectionArgs, sortOrder);
        AREDispatch.NativeLog (str);
    }

    public static void updateEvent(final Uri uri, final ContentValues values, final String where, final String[] selectionArgs) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: update Event";
        str += " Condition: " + getCondition(where, selectionArgs);
        str += " Values: " + values.toString ();
        AREDispatch.NativeLog (str);
    }

    public static void callEvent(final Uri uri, final String method, final String arg, final Bundle extras) {
        EnumProviderType rec = findProviderType(uri);
        String str = "Monitoring " + rec.toString () + " Provider";
        if(uri != null) {
            str += " URI:" + uri.toSafeString ();
        }
        else {
            str += " URI: null";
        }
        str += " Operation: call method Events";
        str += " Method Name:" + method;
        str += " Args: " + arg;
        str += " Bundle: " + extras.toString ();
        AREDispatch.NativeLog (str);
    }

    private static EnumProviderType findProviderType(final Uri uri) {
        EnumProviderType rec = null;
        if (uri == null) {
            return EnumProviderType.Unknow;
        }
        final String str = uri.toSafeString();
        if (str.contains ("content://settings")) {
            rec = EnumProviderType.Settings;
        }
        else if (str.contains ("content://telephony")) {
            rec = EnumProviderType.Telephony;
        }
        else if (str.contains ("content://com.android.deskclock")) {
            rec = EnumProviderType.Deskclock;
        }
        else if (str.contains ("content://downloads")) {
            rec = EnumProviderType.DownloadManager;
        }
        else if (str.contains ("content://browser")) {
            rec = EnumProviderType.Brower;
        }
        else if (str.contains ("content://com.android.calendar")) {
            rec = EnumProviderType.Calendar;
        }
        else if (str.contains ("content://call_log")) {
            rec = EnumProviderType.CallLog;
        }
        else if (str.contains ("content://com.android.contacts")) {
            rec = EnumProviderType.Contacts;
        }
        else if (str.contains ("content://com.android.email.provider")) {
            rec = EnumProviderType.Email;
        }
        else if (str.contains ("content://media")) {
            rec = EnumProviderType.Media;
        }
        else if (str.contains ("content://sms")) {
            rec = EnumProviderType.SMS;
        }
        else if (str.contains ("content://mms")) {
            rec = EnumProviderType.MMS;
        }
        else {
            //TODO: unknow provider
            rec = EnumProviderType.Unknow;
        }
        return rec;
    }

    private static String getCondition(final String where, final String[] args) {
        String rec = where;
        int cnt = 0;
        for (char s: rec.toCharArray ()) {
            if (s == '?') {
                cnt++;
            }
        }
        if(cnt == 1) {
            rec = rec.replace ("\\?", listToString(args));
        }
        for (String obj: args) {
            rec = rec.replaceFirst ("\\?", obj);
        }
        return rec;
    }

    private static String getCondition(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        String rec = null;
        if(projection == null) {
            rec = "select all";
        }
        else {
            rec = "select " + listToString (projection);
        }
        if (selection != null) {
            if(selectionArgs != null) {
                rec += " where " + getCondition(selection, selectionArgs);
            }
            else {
                rec += " where " + selection;
            }
        }
        if (sortOrder != null) {
            rec += " orderby " + sortOrder;
        }
        return rec;
    }

    private static String listToString(final String[] lst) {
        String tmp = "[";
        if(lst.length == 0) {
            tmp += "]";
            return tmp;
        }
        for (String element : lst) {
            tmp += element + ", ";
        }
        tmp = tmp.substring (0, tmp.length ()-2) + "]";
        return tmp;
    }
}
