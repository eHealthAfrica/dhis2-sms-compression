package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;

public class DeleteSMSSubmission extends SMSSubmission {
	protected String uid;
	
	/* Getters and Setters */
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/* Implementation of abstract methods */
	
	public void writeSubm(SMSMetadata meta, SMSSubmissionWriter writer) throws Exception {
		writer.writeNewID(uid);
	}
	
	public void readSubm(SMSMetadata meta, SMSSubmissionReader reader) throws Exception {
		this.uid = reader.readNewID();
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return SMSConsts.SubmissionType.DELETE;
	}	
}
