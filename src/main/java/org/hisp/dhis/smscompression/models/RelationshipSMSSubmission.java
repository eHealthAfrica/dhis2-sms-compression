package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public class RelationshipSMSSubmission extends SMSSubmission {
	protected String relationshipType;
	protected String relationship;
	protected String from;
	protected String to;
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) {
		
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) {
		
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.RELATIONSHIP;
	}	
}
