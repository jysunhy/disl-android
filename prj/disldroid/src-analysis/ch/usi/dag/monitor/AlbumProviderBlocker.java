package ch.usi.dag.monitor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class AlbumProviderBlocker {
    
    public static Cursor queryFileNameFilter(final String[] include, final ContentResolver cr, final Uri uri, final String[] projection,
        final String selection, final String[] selectionArgs, final String sortOrder) {
//        uri should be "content://media/external/images/media"
        String newSelection = getSelection(include, selection);
        String[] newSelectionArgs = getSelectionArgs(include, selectionArgs);
        return cr.query (uri, projection, newSelection, newSelectionArgs, sortOrder);
    }
    
    private static String getSelection(String[] include, String selection) {
        String rec = null;
        if(selection == null) {
            rec = "_display_name IN (" + ProviderUtil.getQuestionMark (include) + ")";
        }
        else {
            rec = selection + " and _display_name IN (" + ProviderUtil.getQuestionMark (include) + ")";
        }
        return rec;
    }
    
    private static String[] getSelectionArgs(String[] include, String[] selectionArgs) {
        if(selectionArgs == null) {
            if(include == null || include.length == 0) {
                return null;
            }
            return include;
        }
        String[] rec = new String[selectionArgs.length + include.length];
        System.arraycopy (selectionArgs, 0, rec, 0, selectionArgs.length);
        System.arraycopy (include, 0, rec, selectionArgs.length, include.length);
        if(rec.length == 0) {
            return null;
        }
        return rec;
    }
    
}
