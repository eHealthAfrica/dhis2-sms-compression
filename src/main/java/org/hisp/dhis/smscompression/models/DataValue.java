package org.hisp.dhis.smscompression.models;

public class DataValue {
	protected String categoryOptionCombo;
	protected String dataElement;
	protected String value;

	public DataValue(String categoryOptionCombo, String dataElement, String value) {
		this.categoryOptionCombo = categoryOptionCombo;
		this.dataElement = dataElement;
		this.value = value;
	}
	
	public String getCategoryOptionCombo() {
		return categoryOptionCombo;
	}
	public String getDataElement() {
		return dataElement;
	}
	public String getValue() {
		return value;
	}
}
