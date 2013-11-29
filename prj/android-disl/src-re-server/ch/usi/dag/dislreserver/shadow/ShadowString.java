package ch.usi.dag.dislreserver.shadow;

// TODO ShadowString should better handle if String data are not send
//      over network - throw a runtime exception ??
public class ShadowString extends ShadowObject {

    private String value;

    public ShadowString(long net_ref, String value, ShadowClass klass) {
        super(net_ref, klass);
        this.value = value;
    }

    // TODO warn user that it will return null when the ShadowString is not yet
    // sent.
    @Override
    public String toString() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj)) {

            if (obj instanceof ShadowString) {

                if (value != null && value.equals(((ShadowString) obj).value)) {
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
