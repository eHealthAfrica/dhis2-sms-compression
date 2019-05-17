package org.hisp.dhis.smscompression.models;

import java.util.Date;

import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public class SMSSubmissionHeader {
	protected SubmissionType type;
	protected int version;
	protected Date lastSyncDate;
	protected int submissionID;
	
	public int getSubmissionID() {
		return submissionID;
	}

	public void setSubmissionID(int submissionID) {
		this.submissionID = submissionID;
	}

	public SubmissionType getType() {
		return type;
	}

	public void setType(SubmissionType type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	
	public void validateHeaer() throws Exception {
		//TODO: More validation here
		if (submissionID < 0 || submissionID > 255) {
			throw new Exception("Ensure the Submission ID has been set for this submission");
		}
	}
	
	public void writeHeader(SMSSubmissionWriter writer) throws Exception {
		writer.writeType(type);
		writer.writeVersion(version);
		writer.writeDate(lastSyncDate);
		writer.writeSubmissionID(submissionID);
	}
	
	public void readHeader(SMSSubmissionReader reader) throws Exception {
		this.type = reader.readType();
		this.version = reader.readVersion();
		this.lastSyncDate = reader.readDate();
		this.submissionID = reader.readSubmissionID();
	}	
}
