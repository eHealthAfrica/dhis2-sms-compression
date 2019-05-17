package org.hisp.dhis.smscompression;

public class Consts {
	public static int VARLEN_BITLEN = 5;
	public static int CHAR_BITLEN = 8;
	public static int EPOCH_DATE_BITLEN = 32;
	public static int SUBM_TYPE_BITLEN = 4;
	public static int VERSION_BITLEN = 4;
	public static int CRC_BITLEN = 8;
	public static int SUBM_ID_BITLEN = 8;
	
	public enum SubmissionType {
		AGGREGATE_DATASET,
		DELETE,
		ENROLLMENT,
		RELATIONSHIP,
		SIMPLE_EVENT,
		TRACKER_EVENT
	}
}
