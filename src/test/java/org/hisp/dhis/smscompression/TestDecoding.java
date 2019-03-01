package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

public class TestDecoding {

	@Test
	public void testDecodeEnrollment() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "GyFcVHTsu3CdBalEvuenk1Ehu9bwXAbeYkAIcFGJvveU7bcYi7OjdClb4VOQyfXzHWBqePUDHgXFR07Dv0hhcm9sZAD+jsrkwtjIAIFNtaXRoAA=";
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
		String sms = "nVFcVHTsLplka63tOuyVlFHBW2bSucap36MJdnEpJNK5Fl5Jy0WghAtNi0PYejm1HWBqePUDH5XFR07GPO1oW5iZGZoamwBAHqggmJiYmIB28ia6ubowszCQIbe3OjK0AGH2IzeyMLyQKbK5sLyAAA==";
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
