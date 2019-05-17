package org.hisp.dhis.smscompression;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.Consts.SubmissionType;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.AttributeValue;
import org.hisp.dhis.smscompression.models.DataValue;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.hisp.dhis.smscompression.utils.BitInputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class SMSSubmissionReader {
	protected BitInputStream inStream;
		
	public SMSSubmissionHeader readHeader(byte[] smsBytes) throws Exception {
		if (!checkCRC(smsBytes)) throw new Exception("Invalid CRC");
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(smsBytes);
		this.inStream = new BitInputStream(byteStream);
		inStream.read(Consts.CRC_BITLEN); // skip CRC
		
		SMSSubmissionHeader header = new SMSSubmissionHeader();
		header.readHeader(this);
		
		return header;
	}
	
	public SMSSubmission readSubmission(byte[] smsBytes, Metadata meta) throws Exception {
		SMSSubmissionHeader header = readHeader(smsBytes);
		SMSSubmission subm = null;
		
		switch(header.getType()) {
		case AGGREGATE_DATASET:
			subm = new AggregateDatasetSMSSubmission();
			break;
		case DELETE:
			subm = new DeleteSMSSubmission();
			break;
		case ENROLLMENT:
			subm = new EnrollmentSMSSubmission();
			break;
		case RELATIONSHIP:
			subm = new RelationshipSMSSubmission();
			break;
		case SIMPLE_EVENT:
			subm = new SimpleEventSMSSubmission();
			break;
		case TRACKER_EVENT:
			subm = new TrackerEventSMSSubmission();
			break;
		default:
			throw new Exception("Unknown SMS Submission Type");
		}
		
		subm.read(meta, this, header);
		inStream.close();
		return subm;
	}
	
	private boolean checkCRC(byte[] smsBytes) {
		byte crc = smsBytes[0];
		byte[] submBytes = Arrays.copyOfRange(smsBytes, 1, smsBytes.length);
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] calcCRC = digest.digest(submBytes);
			return (calcCRC[0] == crc);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public SubmissionType readType() throws IOException {
		int submType = inStream.read(Consts.SUBM_TYPE_BITLEN);
		return Consts.SubmissionType.values()[submType];
	}
	
	public int readVersion() throws IOException {
		return inStream.read(Consts.VERSION_BITLEN);		
	}
	
	public Date readDate() throws IOException {
		long epochSecs = inStream.read(Consts.EPOCH_DATE_BITLEN);
		Date dateVal = new Date(epochSecs * 1000);
		return dateVal;
	}
	
	public String readNewID() throws Exception {
		return IDUtil.readNewID(inStream);
	}
	
	public List<AttributeValue> readAttributeValues(Metadata meta) throws IOException {
		return ValueUtil.readAttributeValues(meta, inStream); 
	}
	
	public List<DataValue> readDataValues(Metadata meta) throws IOException {
		return ValueUtil.readDataValues(meta, inStream); 
	}
	
	public boolean readBool() throws IOException {
		int intVal = inStream.read(1);
		return intVal == 1;
	}
	
	//TODO: Update this once we have a better impl of period
	public String readPeriod() throws IOException {
		return ValueUtil.readString(inStream);
	}
	
	public int readSubmissionID() throws IOException {
		return inStream.read(Consts.SUBM_ID_BITLEN);
	}
}
