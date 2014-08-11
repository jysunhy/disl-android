package ch.usi.dag.disldroidreserver.msg.ipc;

import java.util.List;

public class IPCTransaction {
    public IPCTransaction () {
    }
    public IPCTransaction parent = null;
    public List<IPCTransaction> children = null;

    public NativeThread from = new NativeThread (0, 0);
    public NativeThread to = new NativeThread(0,0);
    public int transaction_id;

    public IPCEventRecord[] events;
}
