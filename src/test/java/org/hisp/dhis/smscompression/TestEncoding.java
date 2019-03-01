package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AttributeValue;
import org.hisp.dhis.smscompression.models.DataValue;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Test;

import com.google.gson.Gson;

import junit.framework.Assert;

public class TestEncoding {

	@Test
	public void testEncodeEnrollment() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			EnrollmentSMSSubmission subm = new EnrollmentSMSSubmission();
			
			subm.setUserID("kt2T1Qb4lkU");
			subm.setOrgUnit("dar4XkzRmN0");
			subm.setTrackerProgram("RUOa08S569l");
			subm.setTrackedEntityType("lUKxRSOYxEZ");
			subm.setTrackedEntityInstance("T2bRuLEGoVN");
			subm.setEnrollment("p7M1gUFK37W");
			subm.setTimestamp(meta.lastSyncDate);
			ArrayList<AttributeValue> values = new ArrayList<>();
			values.add(new AttributeValue("X6vKrn3IfAA", "Harold"));
			values.add(new AttributeValue("dHyUZSrJlR8", "Gerald"));
			values.add(new AttributeValue("T2bRuLEGoVN", "Smith"));
			subm.setValues(values);			
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Enrollment submission in Base64 is: " + subm64);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeTrackerEventeHA() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata_eha.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			TrackerEventSMSSubmission subm = new TrackerEventSMSSubmission();
			
			// Jasper Timm
			subm.setUserID("BfbaQwtjEko");
			// Gbo Chiefdom
			subm.setOrgUnit("Lb571MsRIkS");
			// Liberia Case Details
			subm.setProgramStage("QftwC9Td4f9");
			// Default catOptionCombo
			subm.setAttributeOptionCombo("DAv5bv9oqMW");
			// Test Person
			subm.setTrackedEntityInstance("X0jDYqFOUZc");
			// New UID
			subm.setEvent("r7M1gUFK37v");
			subm.setTimestamp(meta.lastSyncDate);
			ArrayList<DataValue> values = new ArrayList<>();
			// LR IDSR ID
			values.add(new DataValue("DAv5bv9oqMW", "YvAtvSRhME2", "123456"));
			// LR Patient Record ID
			values.add(new DataValue("DAv5bv9oqMW", "oJ5sQEzA2Ot", "PA1111"));
			// LR Person Collecting Specimen Name
			values.add(new DataValue("DAv5bv9oqMW", "uPKpHhvtmWD", "Mustafa Conteh"));
			// LR Reporting Person Name"
			values.add(new DataValue("DAv5bv9oqMW", "nxmNRfqRCYj", "Foday Sesay"));			
			subm.setValues(values);			
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Tracker Event submission in Base64 is: " + subm64);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeTrackerEventPlayground() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata_play.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			TrackerEventSMSSubmission subm = new TrackerEventSMSSubmission();
			
			// Jasper Timm
			subm.setUserID("V3qn98bKsr6");
			// Ngelehun CHC
			subm.setOrgUnit("DiszpKrYNg8");
			// Birth
			subm.setProgramStage("A03MvHHogjR");
			// Default catOptionCombo
			subm.setAttributeOptionCombo("HllvX50cXC0");
			// Test Person
			subm.setTrackedEntityInstance("DacGG5vK1K6");
			// New UID
			subm.setEvent("r7M1gUFK37v");
			subm.setTimestamp(meta.lastSyncDate);
			ArrayList<DataValue> values = new ArrayList<>();
			// Apgar score
			values.add(new DataValue("HllvX50cXC0", "a3kGcGDCuk6", "10"));
			// Weight (g)
			values.add(new DataValue("HllvX50cXC0", "UXz7xuGCEhU", "500"));
			// ARV at birth"
			values.add(new DataValue("HllvX50cXC0", "wQLfBvPrXqq", "Others"));
			// Infant feeding
			values.add(new DataValue("HllvX50cXC0", "X8zyunlgUfM", "Exclusive"));			
			subm.setValues(values);			
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Tracker Event submission in Base64 is: " + subm64);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
