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
	
	/* Getters and Setters */
	
	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	/* Implementation of abstract methods */
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) throws Exception {
		writer.writeNewID(relationshipType);
		writer.writeNewID(relationship);
		writer.writeNewID(from);
		writer.writeNewID(to);
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) throws Exception {
		this.relationshipType = reader.readNewID();
		this.relationship = reader.readNewID();
		this.from = reader.readNewID();
		this.to = reader.readNewID();
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.RELATIONSHIP;
	}	
}
