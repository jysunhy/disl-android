package ch.usi.dag.disldroidreserver.shadow;

import java.util.Formattable;
import java.util.Formatter;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;

public class ShadowObject implements Formattable, Cloneable {

    ShadowAddressSpace currentAddressSpace;
    private ShadowClass shadowClass;

	final private long netRef;
	final private long shadowId;

	private Object shadowState;

	//


    ShadowObject (final ShadowAddressSpace currentAddressSpace, final long netReference, final ShadowClass shadowClass) {
        this.currentAddressSpace = currentAddressSpace;
        this.netRef = netReference;
    	this.shadowId = NetReferenceHelper.get_object_id (netReference);
        this.shadowClass = shadowClass;
        this.shadowState = null;
    }

    //

    public long getNetRef () {
		return netRef;
	}

    public long getId () {
		return shadowId;
	}

	public ShadowClass getShadowClass() {

		if (shadowClass != null) {
			return shadowClass;
		} else {

			if (equals(ShadowAddressSpace.BOOTSTRAP_CLASSLOADER)) {
				throw new NullPointerException();
			}

			return currentAddressSpace.JAVA_LANG_CLASS;
		}
	}

    public synchronized Object getState () {
        return shadowState;
    }


    public synchronized <T> T getState (final Class <T> type) {
        return type.cast (shadowState);
    }


    public synchronized void setState (final Object shadowState) {
		this.shadowState = shadowState;
	}

	public synchronized Object setStateIfAbsent(final Object shadowState) {

		final Object retVal = this.shadowState;

		if (retVal == null) {
			this.shadowState = shadowState;
		}

		return retVal;
	}

	// only object id considered
	// TODO consider also the class ID
	@Override
    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (shadowId ^ (shadowId >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ShadowObject) {
			return shadowId == ((ShadowObject) obj).shadowId;
		}

		return false;
	}

    //

    @Override
    public void formatTo (
        final Formatter formatter,
        final int flags, final int width, final int precision
    ) {
        formatter.format ("%s@%x", (shadowClass != null) ? shadowClass.getName () : "<missing>", shadowId);
    }


    @Override
    public Object clone () {
        try {
            return super.clone ();
        } catch (final CloneNotSupportedException e) {
            throw new DiSLREServerFatalException (e);
        }
    }


    void onFork (final ShadowAddressSpace shadowAddressSpace) {
        currentAddressSpace = shadowAddressSpace;
        shadowClass = (ShadowClass) shadowAddressSpace.getClonedShadowObject (shadowClass);
    }

}
