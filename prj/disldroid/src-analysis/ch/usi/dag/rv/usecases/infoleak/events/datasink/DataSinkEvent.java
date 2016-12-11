package ch.usi.dag.rv.usecases.infoleak.events.datasink;

import java.util.Arrays;

import ch.usi.dag.rv.logging.DefaultLog;
import ch.usi.dag.rv.state.MonitorEvent;
import ch.usi.dag.rv.usecases.infoleak.events.datasource.DataSourceEvent;

public class DataSinkEvent extends MonitorEvent{
    private DataSinkEvent (final String desc) {
        super (desc);
    }

    byte[] content;

    public DataSinkEvent(final String desc, final byte[] value, final int off, final int length){
        super(desc);
        if(value == null){
            content = new byte[0];
            return;
        }
        content = Arrays.copyOfRange (value, off, length+off);
        //this.strValue = Base64.encode (content);//Arrays.toString (content);
        //this.value = value;
        //this.off = off;
        //this.length = length;
    }

    public boolean matches (final DataSourceEvent se) {
        final String source = se.getValue ().toString ();
        return match(this.content, source.getBytes ());
    }
    static boolean match(final byte[] a, final byte[] b){
        DefaultLog.d("MATCHTEST", showBytes (a, 0, a.length)+" to "+showBytes (b, 0, b.length));
        if(a == null || b == null || a.length < b.length) {
            return false;
        }
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < b.length; j++){
                if(b[j] != a[i+j]) {
                    break;
                }
                if(j == b.length - 1){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public String toString(){
        return "DataSink with value "+showBytes (content, 0, content.length);
    }

    public static String showBytes(final byte[] bytes, final int start, final int len){
        String res = "";
        for ( int j = start; j < start+len; j++ ) {
            if(isAsciiPrintable(bytes[j]) && bytes[j] != '\'' && bytes[j] != '"' && bytes[j] != '\\') {
                res += (char)bytes[j];
            }else if(bytes[j]=='\n'){

            }else {
                res += '?';
            }
        }
        return res;
    }

    public static boolean isAsciiPrintable(final byte ch) {
        return ch >= 32 && ch < 127;
    }
    public static void main(final String[] args){
        System.out.println (DataSinkEvent.match ("abcdefg".getBytes (), "df".getBytes ()));
    }
}
