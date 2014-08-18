package ch.usi.dag.disldroidreserver.msg.ipc;

public class NativeThread{
    private final int pid;
    private final long tid;
    public NativeThread(final int _pid, final long _tid){
        pid = _pid;
        tid = _tid;
    }

    public int getPid(){
        return pid;
    }
    public long getTid(){
        return tid;
    }

    @Override
    public  String toString(){
        return "("+String.valueOf (pid)+":"+String.valueOf (tid)+")";
    }
    @Override
    public boolean equals(final Object obj){
        if(this==obj){
            return true ;
        }
        if(!(obj instanceof NativeThread)){
            return false ;
        }
        return (((NativeThread)obj).pid == this.pid) && (((NativeThread)obj).tid == this.tid);
    }
    @Override
    public int hashCode(){
        return (int) ((pid*pid+10000)*(100+tid*tid));
    }
};