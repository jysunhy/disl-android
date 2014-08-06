package ch.usi.dag.disldroidreserver.msg.ipc;

public class IPCEventRecord {
    public IPCEventRecord(){

    }
    public IPCEventRecord(final int _frompid, final int _fromtid,final int _transactionid,  final short _phase, final int _topid, final int _totid, final long _timestamp, final boolean _oneway){
        from.pid = _frompid;
        from.tid = _fromtid;
        to.pid = _topid;
        to.tid = _totid;

        transactionid = _transactionid;
        phase = _phase;

        timestamp = _timestamp;
        oneway = _oneway;
        //System.out.println ("DEBUGGING IPC: "+frompid+" "+fromtid+" "+transactionid+" "+phase+" "+topid+" "+totid+" "+timestamp+" "+oneway);
    }

    public DVMThread from = new DVMThread (0, 0);
    public DVMThread to = new DVMThread(0,0);
    public int transactionid;
    public short phase;
    public long timestamp;
    public boolean oneway;

}
