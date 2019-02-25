package org.hisp.dhis.smscompression.models;

import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public class TrackerEventSMSSubmission extends SMSSubmission {
	protected String programStage;
	protected String attributeOptionCombo;
	protected String trackedEntityInstance;
	protected String event;
	protected Date timestamp;
	protected List<DataValue> values;
	
	/* Getters and Setters */
	
	public String getProgramStage() {
		return programStage;
	}

	public void setProgramStage(String programStage) {
		this.programStage = programStage;
	}

	public String getAttributeOptionCombo() {
		return attributeOptionCombo;
	}

	public void setAttributeOptionCombo(String attributeOptionCombo) {
		this.attributeOptionCombo = attributeOptionCombo;
	}

	public String getTrackedEntityInstance() {
		return trackedEntityInstance;
	}

	public void setTrackedEntityInstance(String trackedEntityInstance) {
		this.trackedEntityInstance = trackedEntityInstance;
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
		writer.writeNewID(programStage);
		writer.writeNewID(attributeOptionCombo);
		writer.writeNewID(trackedEntityInstance);
		writer.writeNewID(event);
		writer.writeDate(timestamp);
		writer.writeDataValues(values);
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception {
		this.programStage = reader.readNewID();
		this.attributeOptionCombo = reader.readNewID();
		this.trackedEntityInstance = reader.readNewID();
		this.event = reader.readNewID();
		this.timestamp = reader.readDate();
		this.values = reader.readDataValues(meta);
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.TRACKER_EVENT;
	}	
}
