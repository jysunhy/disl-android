package analyse;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import ch.usi.dag.dislreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowObject;
import ch.usi.dag.dislreserver.shadow.ShadowObjectTable;
import ch.usi.dag.dislreserver.shadow.ShadowString;

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
        } catch(IOException e) {
            e.printStackTrace();
        }

        out = tmp;
    }

    private boolean isObjectUnderConstruction(ShadowObject object) {
		
        for(Object ouc : objectsUnderConstruction.get()) {
            if(ouc == object) {
                return true;
            }
        }

        return false;
	}

	public static void constructorStart(ShadowObject forObj) {
		objectsUnderConstruction.get().push(forObj);
	}

	public static void constructorEnd() {
	    
		ShadowObject obj = objectsUnderConstruction.get().pollFirst();
		
		// sanity checking
		if(obj == null) {
			throw new RuntimeException("De-sync in constructor events");
		}
	}
    
	public void onObjectAllocation(
	        ShadowObject object,
			ShadowString allocationSite
	    ) {

		ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
		state.updateObjectStateAllSite(object, allocationSite.toString());
	}

    public void onFieldRead(
            ShadowObject object,
            ShadowClass ownerClass,
            ShadowString fieldId
        ) {

    	// object cannot be null - checked on the server - analysis problem
        ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
        state.requestObjectState(object).onFieldRead(ownerClass, fieldId.toString(), isObjectUnderConstruction(object));
    }

	public void onFieldWrite(
            ShadowObject object,
            ShadowClass ownerClass,
            ShadowString fieldId
        ) {

		// object cannot be null - checked on the server - analysis problem
		ImmutabilityShadowStateHolder state = ImmutabilityShadowStateHolder.get(object);
        state.requestObjectState(object).onFieldWrite(ownerClass, fieldId.toString(), isObjectUnderConstruction(object));
    }

	private void dumpShadowObjectState(ImmutabilityShadowStateHolder issh) {
		
		if(issh != null) {
			
			ImmutabilityShadowObjectState isos = issh.getObjectState();
			
			if(isos != null) {
				isos.dump(out);
			}
        }
	}
	
	@Override
	public void objectFree(ShadowObject so) {
	    
		ImmutabilityShadowStateHolder issh = so.getState(ImmutabilityShadowStateHolder.class);
		dumpShadowObjectState(issh);
	}

	@Override
	public void atExit() {

		Iterator<Entry<Long, ShadowObject>> iter = ShadowObjectTable.getIterator();
		
        while (iter.hasNext()) {
            
        	ImmutabilityShadowStateHolder issh = iter.next().getValue().getState(ImmutabilityShadowStateHolder.class);
        	dumpShadowObjectState(issh);
        }

        out.close();
	}

}
