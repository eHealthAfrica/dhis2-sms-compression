package org.hisp.dhis.smscompression.models;

import java.util.List;

public class SimpleEventSMSSubmission extends SMSSubmission {
	protected String eventProgram;
	protected String attributeOptionCombo;
	protected String event;
	protected String timestamp;
	protected List<DataValue> values;
}
