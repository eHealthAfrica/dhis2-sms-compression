package org.hisp.dhis.smscompression.models;

import java.util.List;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.Consts.SubmissionType;


public class AggregateDatasetSMSSubmission extends SMSSubmission {
	protected String dataSet;
	protected boolean complete;
	protected String attributeOptionCombo;
	protected String period;
	protected List<DataValue> values;
	
	public void writeSubm(Metadata meta, SMSSubmissionWriter writer) {
		
	}
	
	public void readSubm(Metadata meta, SMSSubmissionReader reader) {
		
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return Consts.SubmissionType.AGGREGATE_DATASET;
	}	
}
