package org.hisp.dhis.smscompression;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AttributeValue;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.junit.Test;

import com.google.gson.Gson;

import junit.framework.Assert;

public class TestEncoding {

	@Test
	public void test() {
		Gson gson = new Gson();
		try {
			String metadataJson = IOUtils.toString(new FileReader("src/test/resources/metadata.json"));
			Metadata meta = gson.fromJson(metadataJson, Metadata.class);
			EnrollmentSMSSubmission subm = new EnrollmentSMSSubmission();
			
			subm.setUserID("kt2T1Qb4lkU");
			subm.setOrgUnit("dar4XkzRmN0");
			subm.setTrackerProgram("RUOa08S569l");
			subm.setTrackedEntityType("lUKxRSOYxEZ");
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
			System.out.println("subm in Base64 is: " + subm64);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
}
