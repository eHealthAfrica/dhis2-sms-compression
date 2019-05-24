package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

public class TestEncodeDecode {

	public void printDecoded(String subm64, SMSMetadata meta) throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		SMSSubmissionReader reader = new SMSSubmissionReader();
		byte[] smsBytes = Base64.getDecoder().decode(subm64);
		SMSSubmissionHeader header = reader.readHeader(smsBytes);
		System.out.println("Decoded header is: " + gson.toJson(header));
		
		SMSSubmission decodedSubm = reader.readSubmission(smsBytes, meta);
		System.out.println("Decoded submission is: " + gson.toJson(decodedSubm));
	}
	
	@Test
	public void testEncodeDelete() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
			DeleteSMSSubmission subm = new DeleteSMSSubmission();
			
			subm.setUserID("kt2T1Qb4lkU");
			subm.setOrgUnit("dar4XkzRmN0");
			subm.setUid("r7M1gUFK37v");
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Delete submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeRelationship() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
			RelationshipSMSSubmission subm = new RelationshipSMSSubmission();
			
			subm.setUserID("kt2T1Qb4lkU");
			subm.setOrgUnit("dar4XkzRmN0");
			subm.setRelationshipType("RUOa08S569l");
			subm.setRelationship("uf3svrmp8Oj");
			subm.setFrom("H6uSAMO5WLD");
			subm.setTo("a3kGcGDCuk6");
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Relationship submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeSimpleEvent() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
			SimpleEventSMSSubmission subm = new SimpleEventSMSSubmission();
			
			//Tom Wakiki (system)
			subm.setUserID("GOLswS44mh8");
			//Ngelehun CHC
			subm.setOrgUnit("DiszpKrYNg8");
			//Antenatal Care Visit
			subm.setEventProgram("lxAQ7Zs9VYR");
			// Default catOptionCombo
			subm.setAttributeOptionCombo("HllvX50cXC0");
			// New UID
			subm.setEvent("l7M1gUFK37v");
			subm.setTimestamp(meta.lastSyncDate);
			ArrayList<SMSDataValue> values = new ArrayList<>();
			// WHOMCH Smoking
			values.add(new SMSDataValue("HllvX50cXC0", "sWoqcoByYmD", "true"));
			// WHOMCH Smoking cessation counselling provided
			values.add(new SMSDataValue("HllvX50cXC0", "Ok9OQpitjQr", "false"));
			// WHOMCH Hemoglobin value
			values.add(new SMSDataValue("HllvX50cXC0", "vANAXwtLwcT", "14"));
			subm.setValues(values);
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Simple Event submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeAggregateDataset() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
			AggregateDatasetSMSSubmission subm = new AggregateDatasetSMSSubmission();
			
			subm.setUserID("kt2T1Qb4lkU");
			subm.setOrgUnit("dar4XkzRmN0");
			subm.setDataSet("RUOa08S569l");
			subm.setComplete(true);
			subm.setAttributeOptionCombo("HllvX50cXC0");
			subm.setPeriod("2019W10");
			ArrayList<SMSDataValue> values = new ArrayList<>();
			values.add(new SMSDataValue("HllvX50cXC0", "uf3svrmp8Oj", "Harold"));
			values.add(new SMSDataValue("HllvX50cXC0", "H6uSAMO5WLD", "Gerald"));
			values.add(new SMSDataValue("HllvX50cXC0", "a3kGcGDCuk6", "Smith"));
			subm.setValues(values);
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Aggregate Dataset submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeEnrollment() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata_play.json"));
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
			EnrollmentSMSSubmission subm = new EnrollmentSMSSubmission();
			
			//Tom Wakiki (system)
			subm.setUserID("GOLswS44mh8");
			//Ngelehun CHC
			subm.setOrgUnit("DiszpKrYNg8");
			//Child Programme
			subm.setTrackerProgram("IpHINAT79UW");
			//Person
			subm.setTrackedEntityType("nEenWmSyUEp");
			//Newly generated UID
			subm.setTrackedEntityInstance("T2bRuLEGoVN");
			//Newly generated UID
			subm.setEnrollment("p7M1gUFK37W");
			subm.setTimestamp(meta.lastSyncDate);
			
			ArrayList<SMSAttributeValue> values = new ArrayList<>();
			//First Name
			values.add(new SMSAttributeValue("w75KJ2mc4zz", "Harold"));
			//Last Name
			values.add(new SMSAttributeValue("zDhUuAYrxNC", "Smith"));
			//City
			values.add(new SMSAttributeValue("FO4sWYJ64LQ", "Sydney"));
			//Address
			values.add(new SMSAttributeValue("VqEFza8wbwA", "The Opera House"));
			//Unique ID
			values.add(new SMSAttributeValue("lZGmxYbs97q", "987123"));
			subm.setValues(values);
			
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Enrollment submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
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
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
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
			subm.setTrackedEntityInstance("ArQlnv8qbih");
			// New UID
			subm.setEvent("r7M1gUFK37v");
			subm.setTimestamp(meta.lastSyncDate);
			ArrayList<SMSDataValue> values = new ArrayList<>();
			// LR IDSR ID
			values.add(new SMSDataValue("DAv5bv9oqMW", "YvAtvSRhME2", "123456"));
			// LR Patient Record ID
			values.add(new SMSDataValue("DAv5bv9oqMW", "oJ5sQEzA2Ot", "PA1111"));
			// LR Person Collecting Specimen Name
			values.add(new SMSDataValue("DAv5bv9oqMW", "uPKpHhvtmWD", "Mustafa Conteh"));
			// LR Reporting Person Name"
			values.add(new SMSDataValue("DAv5bv9oqMW", "nxmNRfqRCYj", "Foday Sesay"));			
			subm.setValues(values);
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Tracker Event submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
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
			SMSMetadata meta = gson.fromJson(metadataJson, SMSMetadata.class);
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
			ArrayList<SMSDataValue> values = new ArrayList<>();
			// Apgar score
			values.add(new SMSDataValue("HllvX50cXC0", "a3kGcGDCuk6", "10"));
			// Weight (g)
			values.add(new SMSDataValue("HllvX50cXC0", "UXz7xuGCEhU", "500"));
			// ARV at birth"
			values.add(new SMSDataValue("HllvX50cXC0", "wQLfBvPrXqq", "Others"));
			// Infant feeding
			values.add(new SMSDataValue("HllvX50cXC0", "X8zyunlgUfM", "Exclusive"));			
			subm.setValues(values);
			subm.setSubmissionID(1);
			
			SMSSubmissionWriter writer = new SMSSubmissionWriter(meta);
			byte[] compressSubm = writer.compress(subm);
			String subm64 = Base64.getEncoder().encodeToString(compressSubm);			
			System.out.println("Tracker Event submission in Base64 is: " + subm64);
			
			printDecoded(subm64, meta);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
