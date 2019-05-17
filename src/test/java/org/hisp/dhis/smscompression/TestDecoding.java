package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

public class TestDecoding {
	
	@Test
	public void testDecodeDelete() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "NxFceV7su3CdBalEvuenk1Ehu9bwXA1HWBqePUDH5A==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			DeleteSMSSubmission subm = (DeleteSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for Delete is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}
	
	@Test
	public void testDecodeRelationship() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "JzFceV7su3CdBalEvuenk1Ehu9bwXAbeYkAIcFGJv4pD251wzIYtRG4cKWYFgVNkDuQmQNM4uGA=";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			RelationshipSMSSubmission subm = (RelationshipSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for Relationship is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testDecodeSimpleEvent() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "xkFceV7sfD0xJIlU21GNs29zU1iXqIbeYkAIcFGJvRvv5hFAmhMA1HWBqePUDH5XHle7AL1iYmABBOamBgAVnJ7o0Mrk5gHNKK8MbY6ubS7MoAA=";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			SimpleEventSMSSubmission subm = (SimpleEventSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for Aggregate Dataset is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}

	@Test
	public void testDecodeAggregateDataset() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "jAFceV7su3CdBalEvuenk1Ehu9bwXAbeYkAIcFGJvo338wigTQmAGRgYnKuYmAABfnUhhcm9sZACY9HZXJhbGQA6xU21pdGgAA==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			AggregateDatasetSMSSubmission subm = (AggregateDatasetSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for Aggregate Dataset is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}
	
	@Test
	public void testDecodeEnrollment() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "SCFceV7su3CdBalEvuenk1Ehu9bwXAbeYkAIcFGJvveU7bcYi7OjdClb4VOQyfXzHWBqePUDHgXHle7C/SGFyb2xkAPo7K5MLYyAAFNtaXRoAA==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			EnrollmentSMSSubmission subm = (EnrollmentSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for Enrollment is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}

	@Test
	public void testDecodeTrackerEventeHA() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "0VFceV7sLplka63tOuyVlFHBW2bSucap36MJdnEpJNK5Fl5Jy0WghAtNi0PYejm1HWBqePUDH5XHle7GPO1oW5iZGZoamwBAHqggmJiYmIB28ia6ubowszCQIbe3OjK0AGH2IzeyMLyQKbK5sLyAAA==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata_eha.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			TrackerEventSMSSubmission subm = (TrackerEventSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for TrackerEvent is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}	
	
	@Test
	public void testDecodeTrackerEventPlayground() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "lFFceV7sfD0xJIlU21GNs29zU1iXqIKADW5RRyqtbRvv5hFAmhMANkmQQF5UBUG1HWBqePUDH5XHle7AL1iYmABBOamBgAVnJ7o0Mrk5gHNKK8MbY6ubS7MoAA==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata_play.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			TrackerEventSMSSubmission subm = (TrackerEventSMSSubmission) reader.readSubmission(smsBytes, meta);
			System.out.println("Decoded SMS for TrackerEvent is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}
}
