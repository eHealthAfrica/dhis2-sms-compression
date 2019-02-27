package org.hisp.dhis.smscompression.models;

import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public class EnrollmentSMSSubmission extends SMSSubmission {
	protected String trackerProgram;
	protected String trackedEntityType;
	protected String trackedEntityInstance;
	protected String enrollment;
	protected Date timestamp;
	protected List<AttributeValue> values;
	
	public String getTrackerProgram() {
		return trackerProgram;
	}

	public void setTrackerProgram(String trackerProgram) {
		this.trackerProgram = trackerProgram;
	}

	public String getTrackedEntityType() {
		return trackedEntityType;
	}

	public void setTrackedEntityType(String trackedEntityType) {
		this.trackedEntityType = trackedEntityType;
	}

	public String getTrackedEntityInstance() {
		return trackedEntityInstance;
	}

	public void setTrackedEntityInstance(String trackedEntityInstance) {
		this.trackedEntityInstance = trackedEntityInstance;
	}
	
	public String getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(String enrollment) {
		this.enrollment = enrollment;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<AttributeValue> getValues() {
		return values;
	}

	public void setValues(List<AttributeValue> values) {
		this.values = values;
	}
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) throws Exception {				
		writer.writeNewID(trackerProgram);
		writer.writeNewID(trackedEntityType);
		writer.writeNewID(trackedEntityInstance);
		writer.writeNewID(enrollment);
		writer.writeDate(timestamp);
		writer.writeAttributeValues(values);
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception {
		this.trackerProgram = reader.readNewID();
		this.trackedEntityType = reader.readNewID();
		this.trackedEntityInstance = reader.readNewID();
		this.enrollment = reader.readNewID();
		this.timestamp = reader.readDate();
		this.values = reader.readAttributeValues(meta);
	}

	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.ENROLLMENT;
	}
	
}
