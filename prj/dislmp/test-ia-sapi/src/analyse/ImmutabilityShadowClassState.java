package analyse;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;

import ch.usi.dag.dislreserver.shadow.FieldInfo;
import ch.usi.dag.dislreserver.shadow.ShadowClass;

// synchronized
public class ImmutabilityShadowClassState {

	private static final char SUBSEP = '\034';

	// *** BIG NOTE ****
	// We need to keep ordinal numbers in sync with super classes. That means,
	// if the fieldsMetaInfo for some super class has some ordinal number here,
	// it will have same ordinal number even in the ImmutabilityShadowClassState
	// in the super class. See construction of fieldsMetaInfo.
	
	// This is because we need to mark field accesses in the object of some
	// class but the field can be accessed in super class. Example: Some class
	// declares field private. Other class declares field with the same name
	// private again. Two different fields with different id but same name
	// in one object. And we need to distinguish between them but match them,
	// in the classes and subclasses.
	
	// On the other hand, we need fast access to the fields, by name.
	// fieldsMetaInfoLookup is right for this. And note that for each class,
	// we need only visible fields for this class. So the map is ok.
	
	private final LinkedList<FieldMetaInfo> fieldsMetaInfo = new LinkedList<FieldMetaInfo>();
	
	// TODO replaced with version similar to orig.
	// so the performance comparison is more fair
	
//	private final Map<String, FieldMetaInfo> fieldsMetaInfoLookup =
//			new HashMap<String, FieldMetaInfo>();
	
	private final FieldMetaInfo[] fieldsMetaInfoA;
	
//	---

	/**
	 * Creates numbering for all fields in the class and all its super classes.
	 * Id is just an ordinal number where fields with lower ids are from super
	 * classes.  
	 */
	public ImmutabilityShadowClassState(ShadowClass cls) {

		// go through whole hierarchy and create filed meta infos
		for (ShadowClass current = cls; current != null; current = current
				.getSuperclass()) {

			// get all declared fields
			for (FieldInfo field : current.getDeclaredFields()) {

				// if the field is not static, add it at the beginning of the
				// list -> lower id number
				if ((field.getModifiers() & Opcodes.ACC_STATIC) == 0) {

					fieldsMetaInfo.addFirst(new FieldMetaInfo(current.getName(),
							getFieldId(field.getFieldNode())));
				}
			}
		}

		// put them to the map
		for (int i = 0; i < fieldsMetaInfo.size(); ++i) {

			// get item from list - super classes first
			// NOTE: access is bit slower here, it can be exported to array if
			// needed
			FieldMetaInfo fmi = fieldsMetaInfo.get(i);

			// add ordering number
			fmi.setOrdinalNumber(i);

			// add to the map
			// NOTE that items from subclasses override items form superclasses
			// this is ok because for lookup, we need only visible fields
			
			// TODO replaced with version similar to orig.
			// so the performance comparison is more fair
//			fieldsMetaInfoLookup.put(fmi.getFieldId(), fmi);
		}
		
		fieldsMetaInfoA = fieldsMetaInfo.toArray(new FieldMetaInfo[0]);
		
//		---
	}

	
    /**
     * Gets the canonical ID for a fields. All canonical field IDs are intern;
     * they can be compared for identity.
     * 
     * @see ch.usi.dag.disl.example.sharing.instrument.FieldAccessStaticContext#getFieldId()
     */
    private static String getFieldId(FieldNode field) {
        return field.name + SUBSEP + field.desc;
    }

	public Collection<FieldMetaInfo> getFieldsMetaInfo() {
		return Collections.unmodifiableCollection(fieldsMetaInfo);
	}
	
	public int getFieldOrdinalNumber(String fieldId) {
		
		// TODO replaced with version similar to orig.
		// so the performance comparison is more fair
//		FieldMetaInfo fmi = fieldsMetaInfoLookup.get(fieldId);
		
		FieldMetaInfo fmi = null;
		
		for(int i = fieldsMetaInfoA.length - 1; i >= 0; --i) {
			if(fieldsMetaInfoA[i].getFieldId().equals(fieldId)) {
				fmi = fieldsMetaInfoA[i];
				break;
			}
		}

//		---
		
		if(fmi == null) {
			throw new RuntimeException("Field not found");
		}
		
		return fmi.getOrdinalNumber();
	}
}
