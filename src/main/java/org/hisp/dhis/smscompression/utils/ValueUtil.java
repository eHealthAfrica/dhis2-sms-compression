package org.hisp.dhis.smscompression.utils;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.smscompression.Consts;
import org.hisp.dhis.smscompression.models.AttributeValue;
import org.hisp.dhis.smscompression.models.Metadata;

public class ValueUtil {

	public static void writeAttributeValues(List<AttributeValue> values, Metadata meta, BitOutputStream outStream) throws IOException {
		int attributeBitLen = IDUtil.getBitLengthForList(meta.getTrackedEntityAttributes());
		outStream.write(attributeBitLen, Consts.VALUEID_BITLEN);
		for (AttributeValue val : values) {
			int idHash = BinaryUtils.hash(val.getAttribute(), attributeBitLen);
			outStream.write(idHash, attributeBitLen);
			writeString(val.getValue(), outStream);
		}
	}
		
	public static void writeString(String s, BitOutputStream outStream) throws IOException {
		for (char c : s.toCharArray()) {
			outStream.write(c, Consts.CHAR_BITLEN);
		}
		// Null terminator
		outStream.write(0, Consts.CHAR_BITLEN);
	}
	
	public static String readString(BitInputStream inStream) throws IOException {
		String s = "";
		do {
			int i = inStream.read(Consts.CHAR_BITLEN);
			if (i == 0) break;
			s += (char) i;
		} while(true);
		return s;
	}
	
	public static void writeDate(Date d, BitOutputStream outStream) throws IOException {
		long epochSecs = d.getTime() / 1000;			
		outStream.write((int)epochSecs, Consts.EPOCH_DATE_BITLEN);
	}
	
	public static Date readDate(BitInputStream inStream) throws IOException {
		long epochSecs = inStream.read(Consts.EPOCH_DATE_BITLEN);
		Date dateVal = new Date(epochSecs * 1000);
		return dateVal;
	}
	
	public static List<AttributeValue> readAttributeValues(Metadata meta, BitInputStream inStream) throws IOException {
		ArrayList<AttributeValue> values = new ArrayList<>();		
		int attributeBitLen = inStream.read(Consts.VALUEID_BITLEN);
		Map<Integer, String> idLookup = IDUtil.getIDLookup(meta.getTrackedEntityAttributes(), attributeBitLen);

		while(true) {
			try {
				int idHash = inStream.read(attributeBitLen);
				String id = idLookup.get(idHash);
				String val = readString(inStream);
				values.add(new AttributeValue(id, val));
			} catch (EOFException e) {
				break;				
			}
		}
		return values;
	}
}
