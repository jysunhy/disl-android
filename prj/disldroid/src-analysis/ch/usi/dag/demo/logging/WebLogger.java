package ch.usi.dag.demo.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;


public class WebLogger {


    static String path = "/Library/WebServer/Documents/Websites/splashdemo/data/";
    static FileWriter writer0 = null;

    static int counter = 0;

    static String removeForHtml(final String input){
        String res = input.replace ("<init>", "()");
        res = res.replace ("<clinit>", "()");
        return res;
    }

    public synchronized static void branchTaken(final int pid, final String pname, final String classname, String methodsig, final int idx, final int total, final int times){
        methodsig = sig2name (methodsig);
        methodsig = removeForHtml (methodsig);
        counter++;
        if(counter > 50){
            counter = 0;
            if(writer0 != null){
                try {
                    writer0.close ();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                writer0 = null;
            }
        }
        if(writer0 == null) {
            try {
                writer0 = new FileWriter (new File(path+"branch-takens.log"));
                writer0.write ("<tr><th>process</th><th>class name</th><th>method signature</th><th>total branches</th><th>branch index</th><th>taken times</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        final String line = "<tr><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+classname+"</td>"
            +"<td>"+methodsig+"</td>"
            +"<td>"+total+"</td>"
            +"<td>"+idx+"</td>"
            +"<td>"+times+"</td></tr>\n";
        try {
            writer0.write (line);
            writer0.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer0 = null;
        }
    }

    static FileWriter writer1 = null;

    static String sig2name(final String methodsig){
        String res = "";
        int l = -1;
        String tmp = "";

        for(int i = 0; i < methodsig.length (); i++){
            final char c = methodsig.charAt (i);
            if(c == 'L' && l == -1) {
                l = i;
                tmp = "";
            }
            if(l == -1) {
                res = res + c;
            }else {
                if(c=='/') {
                    tmp = "";
                } else if(c==';') {
                    res = res + tmp+";";
                    l = -1;
                }else{
                    tmp += c;
                }
            }
        }
        return res;
    }
    static int counter2 = 0;
    public static void reportBranchCoverage(final int pid, final String pname, final String classname, String methodsig, final int covered, final int total){
        methodsig = sig2name (methodsig);
        methodsig = removeForHtml (methodsig);
        counter2++;
        if(counter2 > 500){
            counter2 = 0;
            if(writer1 != null){
                try {
                    writer1.close ();
                } catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                writer1 = null;
            }
        }
        if(writer1 == null) {
            try {
                writer1 = new FileWriter (new File(path+"branch-overall.log"));
                writer1.write ("<tr><th>process</th><th>class name</th><th>method signature</th><th>total branches</th><th>covered branches</th><th>coverage</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }

        double perc = 0;
        if(total != 0 ) {
            final int tmp = (10000*covered/total);
            perc = tmp / 100.0;
        }

        final String line = "<tr><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+classname+"</td>"
            +"<td>"+methodsig+"</td>"
            +"<td>"+total+"</td>"
            +"<td>"+covered+"</td>"
            +"<td>"+(total==0?"NA":perc)+"%</td></tr>\n";
        try {
            writer1.write (line);
            writer1.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer1 = null;
        }
    }

    static FileWriter writer2 = null;

    public static void reportPermission(final int pid, final String pname, final long l, final List <String> permissions, final Stack <String> runtimeStack){
        if(writer2 == null) {
            try {
                writer2 = new FileWriter (new File(path+"permission.log"));
                writer2.write ("<tr><th>process</th><th>thread id</th><th>permissions</th><th>call stack</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        String res = "<td>"+l+"</td><td>";

        for(int i = 0; i < permissions.size (); i++){
            if(i!=0) {
                res = res + "<br/>";
            }
            res = res + permissions.get (i);
        }
        String param = "";

        String msgType = "success";
        if(res.contains ("SMS")){
            msgType = "danger";
        }else if(res.contains ("READ_PHONE_STATE") || res.contains ("ACCOUNT")) {
            msgType = "warning";
        }else if(res.contains ("CALL_LOG") || res.contains ("CONTACTS")){
            msgType = "info";
        }
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                String cur = runtimeStack.get (i);
                cur = removeForHtml (cur);
                param += cur;
            }
        }
        res = "<tr class=\""+ msgType +"\"><td>"+pname+"("+pid+")"+"</td>"+res;
        res += "</td><td><a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+param+"')\">open</a></td></tr>\n";
        try {
            writer2.write (res);
            writer2.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer2 = null;
        }
    }

    static FileWriter writer3 = null;
    public static void reportNetworkBind(final int pid, final String pname, final long tid, final int fd, final String address, final int port,  final Stack <String> runtimeStack ){
        if(writer3 == null) {
            try {
                writer3 = new FileWriter (new File(path+"network.log"));
                writer3.write ("<tr><th>process</th><th>thread id</th><th>event type</th><th>file descriptor hash</th><th>address</th><th>data</th><th>call stack</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        String param = "";
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                String cur = runtimeStack.get (i);
                cur = removeForHtml (cur);
                param += cur;
            }
        }
        final String column = "<td><a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+param+"')\">open</a></td>";

        final String line = "<tr class=\"success\"><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+tid+"</td>"
            +"<td>bind</td>"
            +"<td>"+fd+"</td>"
            +"<td>"+address+" : "+port +"</td>"
            +"<td> N/A </td>"+column+"</tr>\n";
        try {
            writer3.write (line);
            writer3.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer3 = null;
        }
    }
    public static void reportNetworkConnect(final int pid, final String pname, final long tid, final int fd, final String address, final int port,  final Stack <String> runtimeStack){
        if(writer3 == null) {
            try {
                writer3 = new FileWriter (new File(path+"network.log"));
                writer3.write ("<tr><th>process</th><th>thread id</th><th>event type</th><th>file descriptor hash</th><th>address</th><th>data</th><th>call stack</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        String param = "";
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                String cur = runtimeStack.get (i);
                cur = removeForHtml (cur);
                param += cur;
            }
        }
        final String column = "<td><a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+param+"')\">open</a></td>";

        final String line = "<tr class=\"info\"><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+tid+"</td>"
            +"<td>connect</td>"
            +"<td>"+fd+"</td>"
            +"<td>"+address+" : "+port +"</td>"
            +"<td> N/A </td>"+column+"</tr>\n";

        try {
            writer3.write (line);
            writer3.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer3= null;
        }
    }
    public static void reportNetworkSend(final int pid, final String pname, final long tid, final int fd, final String address, final int port, final byte[] data,  final Stack <String> runtimeStack){
        final int start = 0;
        final int length = data.length;
        if(writer3 == null) {
            try {
                writer3 = new FileWriter (new File(path+"network.log"));
                writer3.write ("<tr><th>process</th><th>thread id</th><th>event type</th><th>file descriptor hash</th><th>address</th><th>data</th><th>call stack</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        String param = "";
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                String cur = runtimeStack.get (i);
                cur = removeForHtml (cur);
                param += cur;
            }
        }
        final String column = "<td><a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+param+"')\">open</a></td>";

        final String packet = bytesToHex (data, start, length) +"</br>"+ bytesToHtml (data, start, length);
        final String line = "<tr class=\"danger\"><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+tid+"</td>"
            +"<td>send</td>"
            +"<td>"+fd+"</td>"
            +"<td>"+address+" : "+port +"</td>"
            +"<td> <a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+packet+"')\">"+length+" bytes</a> </td>"+column+"</tr>\n";
        try {
            writer3.write (line);
            writer3.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer3= null;
        }
    }
    public static void reportNetworkRecv(final int pid, final String pname, final long tid, final int fd, final String address, final int port, final byte[] data,  final Stack <String> runtimeStack){
        final int start = 0;
        final int length = data.length;
        if(writer3 == null) {
            try {
                writer3 = new FileWriter (new File(path+"network.log"));
                writer3.write ("<tr><th>process</th><th>thread id</th><th>event type</th><th>file descriptor hash</th><th>address</th><th>data</th><th>call stack</th></tr>\n");
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        String param = "";
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                String cur = runtimeStack.get (i);
                cur = removeForHtml (cur);
                param += cur;
            }
        }
        final String column = "<td><a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+param+"')\">open</a></td>";

        final String packet = bytesToHex (data, start, length) +"</br>"+ bytesToHtml (data, start, length);
        final String line = "<tr class=\"warning\"><td>"+pname+"("+pid+")"+"</td>"
            +"<td>"+tid+"</td>"
            +"<td>recv</td>"
            +"<td>"+fd+"</td>"
            +"<td>"+address+" : "+port +"</td>"
            +"<td> <a data-toggle=\"modal\" data-target=\"#packet\" onclick=\"setPacket('"+packet+"')\">"+length+" bytes</a> </td>"+column+"</tr>\n";
        try {
            writer3.write (line);
            writer3.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer3= null;
        }
    }
    public static void main(final String args[]){

        System.out.println (sig2name ("(La/b/c;I)V"));

        branchTaken (1, "com.android.phone", "class", "method", 0, 10, 1);
        branchTaken (1, "com.android.phone", "class", "method", 0, 10, 1);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 1);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 0);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 3);
        final Stack<String> a = new Stack <String> ();
        a.add ("a");
        a.add ("b");
        a.add ("c");
        reportPermission (2, "af", 3, a, a);
        reportPermission (2, "af", 3, a, a);

        final int pid = 1;
        final String pname = "d";
        final int tid = 0;
        final int fd = -1;
        final String address = "address";
        final int port = -1;
        final byte[] data = new byte[32];
        for(int i = 0; i < 32; i++) {
            data[i] = (byte)('a'+(i%26));
        }
        reportNetworkBind (1, "a", 0, fd, "0.0", 666, a);
        reportNetworkConnect (1, "a", 0, fd, "0.0", 666, a);
        reportNetworkSend (pid, pname, tid, fd, address, port, data, a);
        reportNetworkRecv (pid, pname, tid, fd, address, port, data, a);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    static int bytesPerLine = 16;
    public static String bytesToHex(final byte[] bytes, final int start, final int len) {
        String res = "";

        int cnt = 0;
        for ( int j = start; j < start+len; j++ ) {
            if(cnt % bytesPerLine == 0 && j != start){
                res += ('<');
                res += ('/');
                res += ('b');
                res += ('r');
                res += ('>');
            }
            cnt++;

            final int v = bytes[j] & 0xFF;
            res += (hexArray[v >>> 4]);
            res += (hexArray[v & 0x0F]);
            res += ' ';
        }
        return res;
    }

    public static boolean isAsciiPrintable(final byte ch) {
        return ch >= 32 && ch < 127;
    }

    public static String bytesToHtml(final byte[] bytes, final int start, final int len) {
        String res = "";

        for ( int j = start; j < start+len; j++ ) {
            if(isAsciiPrintable(bytes[j]) && bytes[j] != '\'' && bytes[j] != '"' && bytes[j] != '\\') {
                res += (char)bytes[j];
            }else if(bytes[j]=='\n'){
                res += ('<');
                res += ('/');
                res += ('b');
                res += ('r');
                res += ('>');
            }else {
                res += '?';
            }
        }
        return res;
    }
}
