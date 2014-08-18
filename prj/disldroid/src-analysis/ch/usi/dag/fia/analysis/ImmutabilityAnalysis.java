package ch.usi.dag.fia.analysis;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.zip.GZIPOutputStream;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowClass;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;

public class ImmutabilityAnalysis extends RemoteAnalysis {

    private final String dumpFile = System.getProperty(getClass().getPackage()
            .getName() + ".Profile", "profile.tsv.gz");

    private final PrintStream out;

    // keeps track of all allocated objects for particular thread
    private static ThreadLocal<Deque<ShadowObject>> objectsUnderConstruction = new ThreadLocal<Deque<ShadowObject>>() {
    	@Override
    	protected Deque<ShadowObject> initialValue() {

    		return new ArrayDeque<ShadowObject>();
    	}
    };

    public ImmutabilityAnalysis() {
        PrintStream tmp = null;

        try {
            tmp = new PrintStream(new GZIPOutputStream(
                new BufferedOutputStream(new FileOutputStream(dumpFile)))
            );
        } catch(final IOException e) {
            e.printStackTrace();
        }

        out = tmp;
    }

    private boolean isObjectUnderConstruction(final ShadowObject object) {

        for(final Object ouc : objectsUnderConstruction.get()) {
            if(ouc == object) {
                return true;
            }
        }

        return false;
	}

	public static void constructorStart(final ShadowObject forObj) {
		objectsUnderConstruction.get().push(forObj);
	}

	public static void constructorEnd() {

		final ShadowObject obj = objectsUnderConstruction.get().pollFirst();

		// sanity checking
		if(obj == null) {
			throw new RuntimeException("De-sync in constructor events");
		}
	}

	public void onObjectAllocation(
	        final ShadowObject object,
			final ShadowString allocationSite
	    ) {

		final ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
		state.updateObjectStateAllSite(object, allocationSite.toString());
	}

    public void onFieldRead(
            final ShadowObject object,
            final ShadowClass ownerClass,
            final ShadowString fieldId
        ) {

    	// object cannot be null - checked on the server - analysis problem
        final ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
        state.requestObjectState(object).onFieldRead(ownerClass, fieldId.toString(), isObjectUnderConstruction(object));
    }

	public void onFieldWrite(
            final ShadowObject object,
            final ShadowClass ownerClass,
            final ShadowString fieldId
        ) {

		// object cannot be null - checked on the server - analysis problem
		final ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
        state.requestObjectState(object).onFieldWrite(ownerClass, fieldId.toString(), isObjectUnderConstruction(object));
    }

	private void dumpShadowObjectState(final ImmutabilityShadowStateHolder issh) {

		if(issh != null) {

			final ImmutabilityShadowObjectState isos = issh.getObjectState();

			if(isos != null) {
				isos.dump(out);
			}
        }
	}

	@Override
	public void objectFree(final Context context, final ShadowObject so) {

		final ImmutabilityShadowStateHolder issh = so.getState(ImmutabilityShadowStateHolder.class);
		dumpShadowObjectState(issh);
	}

	@Override
    public void atExit (final Context context) {

        for (final ShadowObject value : context.getShadowObjectIterator ()) {
            final ImmutabilityShadowStateHolder issh = value.getState (ImmutabilityShadowStateHolder.class);
            dumpShadowObjectState (issh);
        }

        out.close ();
    }

}
