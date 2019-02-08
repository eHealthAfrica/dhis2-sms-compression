package org.hisp.dhis.smscompression.models;

import java.util.List;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;


public class SimpleEventSMSSubmission extends SMSSubmission {
	protected String eventProgram;
	protected String attributeOptionCombo;
	protected String event;
	protected String timestamp;
	protected List<DataValue> values;
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) {
		
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) {
		
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.SIMPLE_EVENT;
	}	
}
