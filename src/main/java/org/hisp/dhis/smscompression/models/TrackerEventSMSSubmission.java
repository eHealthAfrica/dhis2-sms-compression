package org.hisp.dhis.smscompression.models;

import java.util.List;

public class TrackerEventSMSSubmission extends SMSSubmission {
	protected String programStage;
	protected String attributeOptoonCombo;
	protected String trackedEntityInstance;
	protected String event;
	protected String timestamp;
	protected List<DataValue> values;
}
