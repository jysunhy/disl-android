package ch.usi.dag.disldroidreserver.msg.ipc;

public class IPCEventRecord {
    public IPCEventRecord(){
        from = new NativeThread (0, 0);
        to = new NativeThread (0, 0);
    }
    public IPCEventRecord(final int _frompid, final int _fromtid,final int _transactionid,  final short _phase, final int _topid, final int _totid, final long _timestamp, final boolean _oneway){
        from = new NativeThread (_frompid, _fromtid);
        to = new NativeThread(_topid, _totid);

        transactionid = _transactionid;
        phase = _phase;

        timestamp = _timestamp;
        oneway = _oneway;
        //System.out.println ("DEBUGGING IPC: "+frompid+" "+fromtid+" "+transactionid+" "+phase+" "+topid+" "+totid+" "+timestamp+" "+oneway);
    }

    @Override
    public String toString(){
        return "from ("+from.getPid ()+":"+from.getTid ()+") ("+transactionid+(oneway?"oneway":"twoway")+phase+") to ("+to.getPid ()+":"+to.getTid ()+")";
    }

    public NativeThread from = null;
    public NativeThread to = null;
    public int transactionid;
    public short phase;
    public long timestamp;
    public boolean oneway;

}
