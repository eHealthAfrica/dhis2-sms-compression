package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

public class TestDecoding {

	@Test
	public void test() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sms = "SiFcVHTsu3CdBalEvuenk1Ehu9bwXAbeYkAIcFGJvveU7bcYi7OjzHWBqePUDHgXFR07Dv0hhcm9sZAD+jsrkwtjIAIFNtaXRoAA";
		byte[] smsBytes = Base64.getDecoder().decode(sms);

		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			SMSSubmissionReader reader = new SMSSubmissionReader();
			SMSSubmissionHeader header = reader.readHeader(smsBytes);
			EnrollmentSMSSubmission subm = (EnrollmentSMSSubmission) reader.readSubmission(header, meta);
			System.out.println("Decoded SMS is: " + gson.toJson(subm));			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}		
		
	}

}
