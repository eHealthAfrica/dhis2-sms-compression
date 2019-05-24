package org.hisp.dhis.smscompression.models;

import java.util.List;

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;


public class AggregateDatasetSMSSubmission extends SMSSubmission {
	protected String dataSet;
	protected boolean complete;
	protected String attributeOptionCombo;
	protected String period;
	protected List<SMSDataValue> values;
	
	/* Getters and Setters */
	
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getAttributeOptionCombo() {
		return attributeOptionCombo;
	}

	public void setAttributeOptionCombo(String attributeOptionCombo) {
		this.attributeOptionCombo = attributeOptionCombo;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public List<SMSDataValue> getValues() {
		return values;
	}

	public void setValues(List<SMSDataValue> values) {
		this.values = values;
	}
	
	/* Implementation of abstract methods */
	
	public void writeSubm(SMSMetadata meta, SMSSubmissionWriter writer) throws Exception {
		writer.writeNewID(dataSet);
		writer.writeBool(complete);
		writer.writeNewID(attributeOptionCombo);
		writer.writePeriod(period);
		writer.writeDataValues(values);			
	}

	public void readSubm(SMSMetadata meta, SMSSubmissionReader reader) throws Exception {
		this.dataSet = reader.readNewID();
		this.complete = reader.readBool();
		this.attributeOptionCombo = reader.readNewID();
		this.period = reader.readPeriod();
		this.values = reader.readDataValues(meta);		
	}
	
	public int getCurrentVersion() {
		return 1;
	}
	
	public SubmissionType getType() {
		return SMSConsts.SubmissionType.AGGREGATE_DATASET;
	}	
}
