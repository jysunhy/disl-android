package analyse;

import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowObject;

// synchronized
public class ImmutabilityShadowStateHolder {

	private ImmutabilityShadowClassState classState;
	private ImmutabilityShadowObjectState objectState;

	// Creates new one if not present
	public synchronized ImmutabilityShadowClassState requestClassState(ShadowClass sClass) {
	
		if (classState == null) {
			classState = new ImmutabilityShadowClassState(sClass);
        }
        
        return classState;
	}
	
	public synchronized ImmutabilityShadowObjectState getObjectState() {
		return objectState;
	}
	
	// Creates new one if not present
	public synchronized ImmutabilityShadowObjectState requestObjectState(ShadowObject object) {
	
        if (objectState == null) {
        	objectState = new ImmutabilityShadowObjectState(object, null);
        }
        
        return objectState;
	}

	// updates allocation site
	public synchronized void updateObjectStateAllSite(ShadowObject object, String allocationSite) {
		
		ImmutabilityShadowObjectState isos = requestObjectState(object);
		// update allocation site
		isos.setAllocationSite(allocationSite);
	}
	
	// retrieves ImmutabilityShadowStateHolder for object
	// synchronized would be simpler but introduces one big lock
    public static ImmutabilityShadowStateHolder get(ShadowObject object) {

    	ImmutabilityShadowStateHolder state = object.getState(ImmutabilityShadowStateHolder.class);

    	// we want one ImmutabilityShadowStateHolder instance in the case of
    	// multiple access
        if (state == null) {
            
        	// create new one
        	state = new ImmutabilityShadowStateHolder();

        	// try to assign
            ImmutabilityShadowStateHolder tmp = 
            		(ImmutabilityShadowStateHolder) object.setStateIfAbsent(state);

            // use the one already assigned
            if (tmp != null) {
                state = tmp;
            }
        }
        
        return state;
    }
}
