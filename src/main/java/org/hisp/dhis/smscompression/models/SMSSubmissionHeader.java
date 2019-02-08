package org.hisp.dhis.smscompression.models;

import java.util.Date;

import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;

public class SMSSubmissionHeader {
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

	protected SubmissionType type;
	protected int version;
	protected Date lastSyncDate;
	
	public void writeHeader(SMSSubmissionWriter writer) throws Exception {
		writer.writeType(type);
		writer.writeVersion(version);
		writer.writeDate(lastSyncDate);
	}
	
	public void readHeader(SMSSubmissionReader reader) throws Exception {
		this.type = reader.readType();
		this.version = reader.readVersion();
		this.lastSyncDate = reader.readDate();
	}	
}
