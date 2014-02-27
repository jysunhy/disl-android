package ch.usi.dag.fia.analysis;

public class FieldMetaInfo {

	private final String originClassName;
	private final String fieldId;
	private int ordinalNumber;

	public FieldMetaInfo(final String originClassName, final String fieldId) {
		super();
		this.originClassName = originClassName;
		this.fieldId = fieldId;
	}

	public String getOriginClassName() {
		return originClassName;
	}

	public String getFieldId() {
		return fieldId;
	}

	public int getOrdinalNumber() {
		return ordinalNumber;
	}

	public void setOrdinalNumber(final int ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}
}
