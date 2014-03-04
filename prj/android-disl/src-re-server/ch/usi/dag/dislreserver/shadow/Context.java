package ch.usi.dag.dislreserver.shadow;

import java.net.InetAddress;

public class Context {

    int processID;
    InetAddress address;
    String pname;

    public Context (final int processID, final InetAddress address) {
        this.processID = processID;
        this.address = address;
        this.pname = null;
    }

    public int pid() {
        return processID;
    }

    public InetAddress getInetAddress() {
        return address;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(final String pname) {
        this.pname = pname;
    }

}
