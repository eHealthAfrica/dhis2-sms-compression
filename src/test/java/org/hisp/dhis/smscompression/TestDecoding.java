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
			SMSSubmissionHeader header = reader.readHeader(smsBytes);
			EnrollmentSMSSubmission subm = (EnrollmentSMSSubmission) reader.readSubmission(header, meta);
			System.out.println("Decoded SMS for Enrollment is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}

	@Test
	public void testDecodeTrackerEvent() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "z1FcVHTsu3CdBalEvuenk1Ehu9bwXAhG5U1xDSpKKnR8ejc1TvbIdClb4VOQyfXzHWBqePUDHgXFR07HUAgeuaZKaW1teQCTiHpTC2srmATWpVKb2huAAA==";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			SMSSubmissionHeader header = reader.readHeader(smsBytes);
			TrackerEventSMSSubmission subm = (TrackerEventSMSSubmission) reader.readSubmission(header, meta);
			System.out.println("Decoded SMS for TrackerEvent is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
	}	
	
}
