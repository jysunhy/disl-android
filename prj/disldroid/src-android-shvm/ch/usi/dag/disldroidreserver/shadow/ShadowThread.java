package ch.usi.dag.disldroidreserver.shadow;

// TODO ShadowTrhead should better handle if String data are not send
//     over network - throw a runtime exception ??
public class ShadowThread extends ShadowObject {

    private String  name;
    private boolean isDaemon;


    ShadowThread (
        final ShadowAddressSpace currentAddressSpace, final long net_ref,
        final String name, final boolean isDaemon,
        final ShadowClass klass) {
        super (
            currentAddressSpace, net_ref, klass);

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

    public void setName(final String name) {
        this.name = name;
    }

    public void setDaemon(final boolean isDaemon) {
        this.isDaemon = isDaemon;
    }

    @Override
    public boolean equals(final Object obj) {

        if (super.equals(obj)) {

            if (obj instanceof ShadowThread) {

                final ShadowThread t = (ShadowThread) obj;

                if (name != null && name.equals(t.name)
                        && (isDaemon == t.isDaemon)) {
                    return true;
                }
            }
        }

        return false;
    }

    /*@Override
    public int hashCode() {
        throw new UnsupportedOperationException("overriden equals, not overriden hashCode");
    }*/
}
