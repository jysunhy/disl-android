package ch.usi.dag.disldroidreserver.msg.ipc;

public class NativeThread{
    public int pid;
    public int tid;
    public NativeThread(final int _pid, final int _tid){
        pid = _pid;
        tid = _tid;
    }
    @Override
    public  String toString(){
        return "("+String.valueOf (pid)+" "+String.valueOf (tid)+")";
    }
    @Override
    public boolean equals(final Object obj){
        if(this==obj){
            return true ;
        }
        if(!(obj instanceof NativeThread)){
            return false ;
        }
        return ((NativeThread)obj).pid == pid && ((NativeThread)obj).tid == tid;
    }
    @Override
    public int hashCode(){
        return (pid+10000)*(10000+tid);
    }
};