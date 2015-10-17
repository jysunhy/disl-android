package ch.usi.dag.demo.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;


public class WebLogger {


    static String path = "/Library/WebServer/Documents/Websites/splashdemo/data/";
    static FileWriter writer0 = null;

    public static void branchTaken(final int pid, final String pname, final String classname, final String methodsig, final int idx, final int total, final int times){
        if(writer0 == null) {
            try {
                writer0 = new FileWriter (new File(path+"branch-takens.log"));
                writer0.write ("<tr><th>process</th><th>class name</th><th>method signature</th><th>total branches</th><th>token index</th><th>token times</th></tr>\n");
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
        }
    }

    static FileWriter writer1 = null;

    public static void reportBranchCoverage(final int pid, final String pname, final String classname, final String methodsig, final int covered, final int total, final boolean clean){
        if(clean) {
            try {
                if(writer1 != null) {
                    writer1.close ();
                    writer1 = null;
                }

            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }
        if(writer1 == null) {
            try {
                writer1 = new FileWriter (new File(path+"branch-overall.log"), false);
                writer1.write ("<tr><th>process</th><th>class name</th><th>method signature</th><th>total branches</th><th>covered branches</th><th>coverage</th></tr>\n");
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
            +"<td>"+covered+"</td>"
            +"<td>"+(total==0?"NA":covered*100.0/total)+"%</td></tr>\n";
        try {
            writer1.write (line);
            writer1.flush ();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        if(res.contains ("SEND_SMS")){
            msgType = "warning";
        }else if(res.contains ("READ_PHONE_STATE")) {
            msgType = "danger";
        }else if(res.contains ("CALL_LOG") || res.contains ("CONTACTS")){
            msgType = "info";
        }
        if(runtimeStack!=null){
            for(int i = 0; i < runtimeStack.size (); i++)
            {
                if(i!=0) {
                    param = param + "<br/>";
                }
                param += runtimeStack.get (i);
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
        }
    }

    public static void main(final String args[]){
        branchTaken (1, "com.android.phone", "class", "method", 0, 10, 1);
        branchTaken (1, "com.android.phone", "class", "method", 0, 10, 1);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 1, true);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 0, true);
        reportBranchCoverage (1, "phone", "cls", "mtd", 0, 3, false);
        final Stack<String> a = new Stack <String> ();
        a.add ("a");
        a.add ("b");
        a.add ("c");
        reportPermission (2, "af", 3, a, a);
        reportPermission (2, "af", 3, a, a);
    }

}
