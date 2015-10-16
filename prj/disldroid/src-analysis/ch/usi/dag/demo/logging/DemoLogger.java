package ch.usi.dag.demo.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class DemoLogger {

    static boolean debug = false;

    public enum LoggerType {DEBUG, INFO, ERROR};

    static ConcurrentHashMap <String, FileWriter> logWriters = new ConcurrentHashMap <String, FileWriter> ();

    static FileWriter getWriter(final String tag){
        FileWriter temp = null;
        try {
            final File file = new File(tag+".log");
            temp = new FileWriter (file, true);

            FileWriter res = logWriters.putIfAbsent (tag, temp);
            if(res == null) {
                final PrintWriter writer = new PrintWriter(tag+".log");
                writer.write ("");
                res = temp;
            }else {
                temp.close ();
            }
            return res;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void debug(final String tag, final String log){
        write(LoggerType.DEBUG,tag,log);
    }
    public static void info(final String tag, final String log){
        write(LoggerType.INFO, tag,log);
    }
    public static void error(final String tag, final String log){
        write(LoggerType.ERROR, tag,log);
    }
    private static void write(final LoggerType type, final String tag, final String log){
        if(type == LoggerType.DEBUG && !debug){
            return;
        }


        String header="";
        switch(type){
        case DEBUG:
            header+="D/"+tag+"\t\t: ";
            break;
        case INFO:
            header+="I/"+tag+"\t\t: ";
            break;
        case ERROR:
            header+="E/"+tag+"\t\t: ";
            break;
        }
        try {
            getWriter(tag).write (header+log+"\n");
            getWriter(tag).flush ();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args){
        DemoLogger.info ("A", "test");
        DemoLogger.info ("A", "test");
    }

    public static void info (final String tag, final byte [] data) {
        write (LoggerType.INFO, tag, data);
    }

    private static void write (final LoggerType type, final String tag, final byte [] data) {
        if(type == LoggerType.DEBUG && !debug){
            return;
        }


        String header="";
        switch(type){
        case DEBUG:
            header+="D/"+tag+"\t\t: ";
            break;
        case INFO:
            header+="I/"+tag+"\t\t: ";
            break;
        case ERROR:
            header+="E/"+tag+"\t\t: ";
            break;
        }
        try {
            getWriter(tag).write (header);
            for(final byte b: data){
                getWriter(tag).write (Integer.toBinaryString(b & 255 | 256).substring(1));
                getWriter(tag).write ('(');
                getWriter(tag).write (b);
                getWriter(tag).write (')');
                getWriter(tag).write (' ');
            }
            getWriter(tag).write ("\n");
            for(final byte b: data){
                getWriter(tag).write (b);
            }
            getWriter(tag).write ("\n");
            getWriter(tag).flush ();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
