package org.hisp.dhis.smscompression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.Consts.SubmissionType;
import org.hisp.dhis.smscompression.models.AttributeValue;
import org.hisp.dhis.smscompression.models.DataValue;
import org.hisp.dhis.smscompression.models.Metadata;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.utils.BitOutputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class SMSSubmissionWriter {
	ByteArrayOutputStream byteStream;
	BitOutputStream outStream;
	Metadata meta;
	
	public SMSSubmissionWriter(Metadata meta) {
		this.meta = meta;
	}
	
	public byte[] compress(SMSSubmission subm) throws Exception {
		this.byteStream = new ByteArrayOutputStream();
		this.outStream = new BitOutputStream(byteStream);
		
		SMSSubmissionHeader header = new SMSSubmissionHeader();
		header.setType(subm.getType());
		header.setVersion(subm.getCurrentVersion());
		header.setLastSyncDate(meta.lastSyncDate);
		header.writeHeader(this);
		
		subm.write(meta, this);
		
		return toByteArray();
	}
	
	public byte[] toByteArray() throws IOException {
		outStream.close();
		byte[] subm = byteStream.toByteArray();
		byte[] crcSubm = writeCRC(subm);
		return crcSubm;
	}
	
	public byte[] writeCRC(byte[] subm) {
		byte crc;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] calcCRC = digest.digest(subm);
			crc = calcCRC[0];
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
				
		byte[] crcSubm = new byte[subm.length + 1];
		System.arraycopy(subm, 0, crcSubm, 1, subm.length - 1);
		crcSubm[0] = crc;
		
		return crcSubm;
	}
	
	public void writeType(SubmissionType type) throws IOException {
		outStream.write(type.ordinal(), Consts.SUBM_TYPE_BITLEN);
	}
	
	public void writeVersion(int version) throws IOException {
		outStream.write(version, Consts.VERSION_BITLEN);		
	}
	
	public void writeDate(Date date) throws IOException {
		long epochSecs = date.getTime() / 1000;			
		outStream.write((int)epochSecs, Consts.EPOCH_DATE_BITLEN);		
	}
	
	public void writeNewID(String id) throws Exception {
		IDUtil.writeNewID(id, outStream);
	}
	
	public void writeAttributeValues(List<AttributeValue> values) throws IOException {
		ValueUtil.writeAttributeValues(values, meta, outStream);		
	}
	
	public void writeDataValues(List<DataValue> values) throws IOException {
		ValueUtil.writeDataValues(values, meta, outStream);		
	}	
}
