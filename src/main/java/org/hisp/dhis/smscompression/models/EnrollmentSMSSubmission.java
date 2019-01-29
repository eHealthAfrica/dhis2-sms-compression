package org.hisp.dhis.smscompression.models;

import java.util.List;

public class EnrollmentSMSSubmission extends SMSSubmission {
	protected String trackerProgram;
	protected String trackedEntityInstance;
	protected String enrollment;
	protected String timestamp;
	protected List<AttributeValue> values;
}
