package ch.usi.dag.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class ProviderUtil {
    
    public static String getQuestionMark(String[] rawIdList) {
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

    
    public static String Join(String con, String[] lst) {
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
    
    public static String[] getAppBlockList() {
        String url = "http://tcloud.sjtu.edu.cn:31080/appBlockList";
        String[] rec = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            InputStream fin = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            StringBuffer buffer = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            rec = jsonToArray(buffer.toString ());
//            AREDispatch.NativeLog ();
        } catch (Exception e) {
            
        }
        return rec;
    }
    
    public static String[] getFileBlockList() {
        String url = "http://tcloud.sjtu.edu.cn:31080/fileBlockList";
        String[] rec = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            InputStream fin = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            StringBuffer buffer = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            rec = jsonToArray(buffer.toString ());
//            AREDispatch.NativeLog ();
        } catch (Exception e) {
            
        }
        return rec;
    }
    
    public static String[] getUrlBlockList() {
        String url = "http://tcloud.sjtu.edu.cn:31080/urlBlockList";
        String[] rec = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            InputStream fin = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            StringBuffer buffer = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            rec = jsonToArray(buffer.toString ());
//            AREDispatch.NativeLog ();
        } catch (Exception e) {
            
        }
        return rec;
    }
    
    public static String[] getUrlRedirectList() {
        String url = "http://tcloud.sjtu.edu.cn:31080/urlBlockList";
        String[] rec = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            InputStream fin = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            StringBuffer buffer = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            rec = jsonToArray(buffer.toString ());
//            AREDispatch.NativeLog ();
        } catch (Exception e) {
            
        }
        return rec;
    }
    
    public static String[] jsonToArray(String str) {
        str = str.substring(1);
        str = str.substring(0, str.length()-1);
        String[] lst = str.split(",");
        for (int i = 0; i < lst.length; i++) {
            lst[i] = lst[i].substring(1);
            lst[i] = lst[i].substring(0, lst[i].length()-1);
        }
        return lst;
    }
    
    public static String getMainActivityNameFromManifest(String filePath) {
        String rec = null;
        File file = new File(filePath);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
            DocumentBuilder builder = factory.newDocumentBuilder();   
            Document doc = builder.parse(file);   
            NodeList nl = doc.getElementsByTagName("VALUE");
        } catch (Exception e) {
            System.out.println(e.toString ());
        }
        return rec;
    }

}
