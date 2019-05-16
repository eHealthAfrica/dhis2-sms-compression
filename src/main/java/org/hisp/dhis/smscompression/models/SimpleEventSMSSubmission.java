package org.hisp.dhis.smscompression.models;

import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;


public class SimpleEventSMSSubmission extends SMSSubmission {
	protected String eventProgram;
	protected String attributeOptionCombo;
	protected String event;
	protected Date timestamp;
	protected List<DataValue> values;
	
	/* Getters and Setters */
	
	public String getEventProgram() {
		return eventProgram;
	}

	public void setEventProgram(String eventProgram) {
		this.eventProgram = eventProgram;
	}

	public String getAttributeOptionCombo() {
		return attributeOptionCombo;
	}

	public void setAttributeOptionCombo(String attributeOptionCombo) {
		this.attributeOptionCombo = attributeOptionCombo;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<DataValue> getValues() {
		return values;
	}

	public void setValues(List<DataValue> values) {
		this.values = values;
	}
	
	/* Implementation of abstract methods */
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) throws Exception {
		writer.writeNewID(eventProgram);
		writer.writeNewID(attributeOptionCombo);
		writer.writeNewID(event);
		writer.writeDate(timestamp);
		writer.writeDataValues(values);		
	}

	public void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception {
		this.eventProgram = reader.readNewID();
		this.attributeOptionCombo = reader.readNewID();
		this.event = reader.readNewID();
		this.timestamp = reader.readDate();
		this.values = reader.readDataValues(meta);		
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.SIMPLE_EVENT;
	}	
}
