package ch.usi.dag.disldroidreserver.msg.ipc;

public class DVMThread{
    public int pid;
    public int tid;
    public DVMThread(final int _pid, final int _tid){
        pid = _pid;
        tid = _tid;
    }
    @Override
    public  String toString(){
        return String.valueOf (pid)+String.valueOf (tid);
    }
    @Override
    public boolean equals(final Object obj){
        if(this==obj){
            return true ;
        }
        if(!(obj instanceof DVMThread)){
            return false ;
        }
        return ((DVMThread)obj).pid == pid && ((DVMThread)obj).tid == tid;
    }
    @Override
    public int hashCode(){
        return (pid+10000)*(10000+tid);
    }
};