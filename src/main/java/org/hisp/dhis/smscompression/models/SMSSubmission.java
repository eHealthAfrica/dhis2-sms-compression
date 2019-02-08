package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public abstract class SMSSubmission {
	protected String userID;
	protected String orgUnit;
		
	public abstract int getCurrentVersion();
	public abstract SubmissionType getType();
	public abstract void writeSubm(Metadata meta, SMSSubmissionWriter writer) throws Exception;
	public abstract void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception;

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
	
	public void write(Metadata meta, SMSSubmissionWriter writer) throws Exception {
		writer.writeNewID(userID);
		writer.writeNewID(orgUnit);		
		this.writeSubm(meta, writer);
	}
	
	public void read(Metadata meta, SMSSubmissionReader reader) throws Exception {
		this.userID = reader.readNewID();
		this.orgUnit = reader.readNewID();
		this.readSubm(meta, reader);
	}
} 