package ch.usi.dag.disldroidreserver.msg.ipc;

public class IPCEventRecord {
    public IPCEventRecord(){

    }
    public IPCEventRecord(final int _frompid, final int _fromtid,final int _transactionid,  final short _phase, final int _topid, final int _totid, final long _timestamp, final boolean _oneway){

        frompid =_frompid;
        fromtid = _fromtid;
        transactionid = _transactionid;
        phase = _phase;
        topid = _topid;
        totid = _totid;
        timestamp = _timestamp;
        oneway = _oneway;
        System.out.println ("DEBUGGING IPC: "+frompid+" "+fromtid+" "+transactionid+" "+phase+" "+topid+" "+totid+" "+timestamp+" "+oneway);
    }

    public int frompid;
    public int fromtid;
    public int transactionid;
    public short phase;
    public int topid;
    public int totid;
    public long timestamp;
    public boolean oneway;

}
