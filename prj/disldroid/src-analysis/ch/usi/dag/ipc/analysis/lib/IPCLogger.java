package ch.usi.dag.ipc.analysis.lib;

public class IPCLogger {

    static boolean debug = false;

    public enum LoggerType {DEBUG, INFO, ERROR};

    public static void debug(final String tag, final String log){
        write(LoggerType.DEBUG,tag,log);
    }
    public static void info(final String tag, final String log){
        write(LoggerType.INFO, tag,log);
    }
    public static void write(final LoggerType type, final String tag, final String log){
        if(type == LoggerType.DEBUG && !debug){
            return;
        }

        String header=""+Thread.currentThread ().getId ()+" ";
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
        System.out.println (header+log);
    }

    public static void reportPermissionUsage(final ThreadState state){
        state.printPermission();
        state.printStack();

    }
}
