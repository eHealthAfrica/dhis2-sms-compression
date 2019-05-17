package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

import org.hisp.dhis.smscompression.Consts.SubmissionType;

public abstract class SMSSubmission {
	protected SMSSubmissionHeader header;
	protected String userID;
	protected String orgUnit;
		
	public abstract int getCurrentVersion();
	public abstract SubmissionType getType();
	public abstract void writeSubm(Metadata meta, SMSSubmissionWriter writer) throws Exception;
	public abstract void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception;

	public SMSSubmission() {
		this.header = new SMSSubmissionHeader();
		header.setType(this.getType());
		header.setVersion(this.getCurrentVersion());
		//Initialise the submission ID so we know if it's been set correctly
		header.setSubmissionID(-1);
	}
	
	public void setSubmissionID(int submissionID) throws Exception {
		if (submissionID < 0 || submissionID > 255) {
			throw new Exception("submission ID must be in the range 0 <= submission_id <= 255");
		}
		header.setSubmissionID(submissionID);
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}
	
	public void validateSubmission() throws Exception {
		header.validateHeaer();
		//TODO: do better validation here
		if (userID.isEmpty() || orgUnit.isEmpty()) {
			throw new Exception("Ensure the UserID and OrgUnitID is set in the submission");
		}
	}
	
	public void write(Metadata meta, SMSSubmissionWriter writer) throws Exception {
		//Ensure we set the lastSyncDate in the subm header
		header.setLastSyncDate(meta.lastSyncDate);

		validateSubmission();
		header.writeHeader(writer);
		writer.writeNewID(userID);
		writer.writeNewID(orgUnit);		
		writeSubm(meta, writer);
	}
	
	public void read(Metadata meta, SMSSubmissionReader reader, SMSSubmissionHeader header) throws Exception {
		this.header = header;
		this.userID = reader.readNewID();
		this.orgUnit = reader.readNewID();
		readSubm(meta, reader);
	}
} 