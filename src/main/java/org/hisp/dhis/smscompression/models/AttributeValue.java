package org.hisp.dhis.smscompression.models;

public class AttributeValue {
	protected String attribute;
	protected String value;
	
	public AttributeValue(String attribute, String value) {
		this.attribute = attribute;
		this.value = value;
	}
	
	public String getAttribute() {
		return this.attribute;
	}
	
	public String getValue() {
		return this.value;
	}	
}
