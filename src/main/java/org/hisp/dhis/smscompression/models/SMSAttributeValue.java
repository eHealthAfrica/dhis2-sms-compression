package org.hisp.dhis.smscompression.models;

public class SMSAttributeValue {
	protected String attribute;
	protected String value;
	
	public SMSAttributeValue(String attribute, String value) {
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
