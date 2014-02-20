package analyse;

import java.io.PrintStream;

import ch.usi.dag.dislreserver.shadow.ShadowClass;
import ch.usi.dag.dislreserver.shadow.ShadowObject;

// synchronized
public class ImmutabilityShadowObjectState {

	private String allocationSite;
	private final String className;
	private final ImmutabilityShadowClassState iscsForThisObjectsClass;

	private final ImmutabilityShadowFieldState[] fields;
	
	public ImmutabilityShadowObjectState(ShadowObject object,
			String allocationSite) {

		this.allocationSite = allocationSite;

		ShadowClass sClass = object.getShadowClass();
		iscsForThisObjectsClass = ImmutabilityShadowStateHolder.get(sClass).requestClassState(sClass);
		
		this.className = sClass.getName();
		this.fields = new ImmutabilityShadowFieldState[
			iscsForThisObjectsClass.getFieldsMetaInfo().size()];

		for (int i = 0; i < this.fields.length; i++) {
			this.fields[i] = new ImmutabilityShadowFieldState();
		}
	}

	private ImmutabilityShadowFieldState getFieldState(ShadowClass ownerClass, String fieldId) {
		
		ImmutabilityShadowClassState iscs = ImmutabilityShadowStateHolder.get(ownerClass).requestClassState(ownerClass);
		return fields[iscs.getFieldOrdinalNumber(fieldId)];
	}

	public synchronized String getAllocationSite() {
		return allocationSite;
	}

	public synchronized void setAllocationSite(String allocationSite) {
		this.allocationSite = allocationSite;
	}

	public synchronized void onFieldRead(ShadowClass ownerClass, String fieldId, boolean isUnderConstruction) {

		ImmutabilityShadowFieldState field = getFieldState(ownerClass, fieldId);

		if (isUnderConstruction) {
			field.onFieldReadDuringConstruction();
		} else {
			field.onFieldRead();
		}
	}

	public synchronized void onFieldWrite(ShadowClass ownerClass, String fieldId, boolean isUnderConstruction) {

		ImmutabilityShadowFieldState field = getFieldState(ownerClass, fieldId);

		if (isUnderConstruction) {
			field.onFieldWriteDuringConstruction();
		} else {
			field.onFieldWrite();
		}
	}

	public synchronized void dump(PrintStream out) {

		String allocSite = getAllocationSite();
		if(allocSite == null) allocSite = "?";

		// report only allocated objects
		out.append(allocSite).append('\t');
        out.append(className).append('\n');
		
        // do not report fields
        
//		final char SUBSEP = '\034';
//
//		// order of the iteration is according to the ordinal numbers
//		// see the ImmutabilityShadowClassState for more info about the struct
//		for (FieldMetaInfo fmi : iscsForThisObjectsClass.getFieldsMetaInfo()) {
//
//			String fieldName = fmi.getOriginClassName() + SUBSEP + fmi.getFieldId();
//			ImmutabilityShadowFieldState fieldState = fields[fmi.getOrdinalNumber()];
//
//			out.append(allocSite).append('\t');
//	        out.append(className).append('\t');
//			
//			out.append(fieldName).append('\t');
//			out.append(Boolean.toString(fieldState.isMutable())).append('\t');
//			out.append(Boolean.toString(fieldState.zeroInitializationMattered())).append('\n');
//		}
//		
//		out.append('\n');
	}

}
