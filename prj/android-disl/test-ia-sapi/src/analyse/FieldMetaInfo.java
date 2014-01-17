package analyse;

public class FieldMetaInfo {

	private String originClassName;
	private String fieldId;
	private int ordinalNumber;

	public FieldMetaInfo(String originClassName, String fieldId) {
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

	public void setOrdinalNumber(int ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}
}
