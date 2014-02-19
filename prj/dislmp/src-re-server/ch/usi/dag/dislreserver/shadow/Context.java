package ch.usi.dag.dislreserver.shadow;

import java.net.InetAddress;

public class Context {

    int processID;
    InetAddress address;

    public Context (final int processID, final InetAddress address) {
        this.processID = processID;
        this.address = address;
    }

    public int pid() {
        return processID;
    }

    public InetAddress getInetAddress() {
        return address;
    }

}
