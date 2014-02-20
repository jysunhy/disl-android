package analyse;

//synchronized
public final class ImmutabilityShadowFieldState {

    private static final byte FRESH = 1;
    private static final byte DID_MATTER = 2;
    private static final byte DID_NOT_MATTER = 3;

    private byte zeroInitializationState = FRESH;

    private boolean mutable = false;

    public synchronized void onFieldReadDuringConstruction() {
        switch (zeroInitializationState) {
        case FRESH:
            zeroInitializationState = DID_MATTER;
            break;
        }
    }

    public synchronized void onFieldWriteDuringConstruction() {
        switch (zeroInitializationState) {
        case FRESH:
            zeroInitializationState = DID_NOT_MATTER;
            break;
        }
    }

    public synchronized void onFieldRead() { }

    public synchronized void onFieldWrite() {
        mutable = true;
    }

    public synchronized boolean zeroInitializationMattered() {
        return zeroInitializationState == FRESH || zeroInitializationState == DID_MATTER;
    }

    public synchronized boolean isMutable() {
        return mutable;
    }
}
