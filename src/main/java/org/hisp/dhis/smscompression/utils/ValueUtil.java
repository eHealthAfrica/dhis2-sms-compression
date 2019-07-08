package org.hisp.dhis.smscompression.utils;

import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.ValueType;
import org.hisp.dhis.smscompression.models.SMSValue;

public class ValueUtil
{
    // TODO: Find a better way to pass down fixedIntBitLen
    public static void writeSMSValue( SMSValue<?> val, int fixedIntBitlen, BitOutputStream outStream )
        throws SMSCompressionException
    {
        // First write out the value type
        outStream.write( val.getType().ordinal(), SMSConsts.VALTYPE_BITLEN );

        // Now write out the actual value
        switch ( val.getType() )
        {
        case BOOL:
            writeBool( (Boolean) val.getValue(), outStream );
            break;
        case DATE:
            writeDate( (Date) val.getValue(), outStream );
            break;
        case INT:
            writeInt( (Integer) val.getValue(), fixedIntBitlen, outStream );
            break;
        case FLOAT:
            writeFloat( (Float) val.getValue(), outStream );
            break;
        // STRING is the default case
        default:
            writeString( (String) val.getValue(), outStream );
        }
    }

    // TODO: Find a better way to pass down fixedIntBitLen
    public static SMSValue<?> readSMSValue( int fixedIntBitLen, BitInputStream inStream )
        throws SMSCompressionException
    {
        // First read the value type
        int typeNum = inStream.read( SMSConsts.VALTYPE_BITLEN );
        ValueType type = ValueType.values()[typeNum];

        // Now read the actual value
        switch ( type )
        {
        case BOOL:
            return new SMSValue<Boolean>( readBool( inStream ), type );
        case DATE:
            return new SMSValue<Date>( readDate( inStream ), type );
        case INT:
            return new SMSValue<Integer>( readInt( fixedIntBitLen, inStream ), type );
        case FLOAT:
            return new SMSValue<Float>( readFloat( inStream ), type );
        case STRING:
            return new SMSValue<String>( readString( inStream ), type );
        default:
            throw new SMSCompressionException( "Unknown ValueType: " + type );
        }
    }

    public static void writeBool( boolean val, BitOutputStream outStream )
        throws SMSCompressionException
    {
        int intVal = val ? 1 : 0;
        outStream.write( intVal, 1 );
    }

    public static boolean readBool( BitInputStream inStream )
        throws SMSCompressionException
    {
        int intVal = inStream.read( 1 );
        return intVal == 1;
    }

    public static void writeDate( Date d, BitOutputStream outStream )
        throws SMSCompressionException
    {
        long epochSecs = d.getTime() / 1000;
        outStream.write( (int) epochSecs, SMSConsts.EPOCH_DATE_BITLEN );
    }

    public static Date readDate( BitInputStream inStream )
        throws SMSCompressionException
    {
        long epochSecs = inStream.read( SMSConsts.EPOCH_DATE_BITLEN );
        Date dateVal = new Date( epochSecs * 1000 );
        return dateVal;
    }

    public static void writeInt( int val, int fixedIntBitlen, BitOutputStream outStream )
        throws SMSCompressionException
    {
        // We can't write negative ints so we handle sign separately
        int isNegative = val < 0 ? 1 : 0;
        outStream.write( isNegative, 1 );
        val = Math.abs( val );

        // If it's bigger than the fixedInt size we need to give its size
        int isVariable = val > SMSConsts.MAX_FIXED_NUM ? 1 : 0;
        outStream.write( isVariable, 1 );
        int intBitlen = BinaryUtils.bitlenNeeded( val );

        if ( isVariable == 1 )
            outStream.write( intBitlen, SMSConsts.VARLEN_BITLEN );

        int bitLen = isVariable == 1 ? intBitlen : fixedIntBitlen;

        outStream.write( val, bitLen );
    }

    public static int readInt( int fixedIntBitlen, BitInputStream inStream )
        throws SMSCompressionException
    {
        // Is this int negative?
        int setNegative = inStream.read( 1 ) == 1 ? -1 : 1;

        // Is this int fixed or variable size?
        int isVariable = inStream.read( 1 );

        int bitLen = isVariable == 1 ? inStream.read( SMSConsts.VARLEN_BITLEN ) : fixedIntBitlen;
        return setNegative * inStream.read( bitLen );
    }

    public static void writeFloat( float val, BitOutputStream outStream )
        throws SMSCompressionException
    {
        // TODO: We need to handle floats better
        writeString( Float.toString( val ), outStream );
    }

    public static float readFloat( BitInputStream inStream )
        throws SMSCompressionException
    {
        // TODO: We need to handle floats better
        return Float.parseFloat( readString( inStream ) );
    }

    public static void writeString( String s, BitOutputStream outStream )
        throws SMSCompressionException
    {
        for ( char c : s.toCharArray() )
        {
            outStream.write( c, SMSConsts.CHAR_BITLEN );
        }
        // Null terminator
        outStream.write( 0, SMSConsts.CHAR_BITLEN );
    }

    public static String readString( BitInputStream inStream )
        throws SMSCompressionException
    {
        String s = "";
        do
        {
            int i = inStream.read( SMSConsts.CHAR_BITLEN );
            if ( i == 0 )
                break;
            s += (char) i;
        }
        while ( true );
        return s;
    }

    public static int getBitlenLargestInt( List<SMSValue<?>> values )
    {
        int maxInt = 0;
        for ( SMSValue<?> val : values )
        {
            if ( val.getType() == ValueType.INT )
            {
                int intVal = Math.abs( (Integer) val.getValue() );
                if ( intVal > SMSConsts.MAX_FIXED_NUM )
                    continue;
                maxInt = intVal > maxInt ? intVal : maxInt;
            }
        }
        return BinaryUtils.bitlenNeeded( maxInt );
    }
}
