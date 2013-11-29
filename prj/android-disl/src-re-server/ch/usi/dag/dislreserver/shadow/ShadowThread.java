package ch.usi.dag.dislreserver.shadow;

// TODO ShadowTrhead should better handle if String data are not send
//     over network - throw a runtime exception ??
public class ShadowThread extends ShadowObject {

    private String  name;
    private boolean isDaemon;

    public ShadowThread(long net_ref, String name, boolean isDaemon,
            ShadowClass klass) {
        super(net_ref, klass);

        this.name = name;
        this.isDaemon = isDaemon;
    }

    // TODO warn user that it will return null when the ShadowThread is not yet
    // sent.
    public String getName() {
        return name;
    }

    // TODO warn user that it will return false when the ShadowThread is not yet
    // sent.
    public boolean isDaemon() {
        return isDaemon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDaemon(boolean isDaemon) {
        this.isDaemon = isDaemon;
    }

    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj)) {

            if (obj instanceof ShadowThread) {

                ShadowThread t = (ShadowThread) obj;

                if (name != null && name.equals(t.name)
                        && (isDaemon == t.isDaemon)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("overriden equals, not overriden hashCode");
    }
}
