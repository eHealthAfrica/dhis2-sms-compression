package org.hisp.dhis.smscompression.models;

import java.util.List;

public class AggregateDatasetSMSSubmission extends SMSSubmission {
	protected String dataSet;
	protected boolean complete;
	protected String attributeOptionCombo;
	protected String period;
	protected List<DataValue> values;
}
