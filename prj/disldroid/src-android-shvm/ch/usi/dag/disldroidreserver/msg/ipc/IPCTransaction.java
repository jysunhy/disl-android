package ch.usi.dag.disldroidreserver.msg.ipc;

import java.util.List;

public class IPCTransaction {
    public IPCTransaction () {
        // TODO Auto-generated constructor stub
    }
    public IPCTransaction parent = null;
    public List<IPCTransaction> children = null;

    public int frompid;
    public int fromtid;
    public int transaction_id;
    public int topid;
    public int totid;
    public IPCEventRecord[] events;
}
