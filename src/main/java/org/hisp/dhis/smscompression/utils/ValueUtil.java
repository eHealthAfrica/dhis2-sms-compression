package org.hisp.dhis.smscompression.utils;

import java.text.ParseException;

/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.ValueType;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSValue;

public class ValueUtil
{
    // Complex types

    // TODO: Add handling to support New UIDs if the id isn't in Metadata
    public static void writeAttributeValues( List<SMSAttributeValue> values, SMSMetadata meta,
        BitOutputStream outStream )
        throws SMSCompressionException
    {
        int attributeBitLen = IDUtil.getBitLengthForList( meta.getType( MetadataType.TRACKED_ENTITY_ATTRIBUTE ),
            MetadataType.TRACKED_ENTITY_ATTRIBUTE );
        outStream.write( attributeBitLen, SMSConsts.VARLEN_BITLEN );

        List<SMSValue<?>> smsVals = values.stream().map( v -> v.getSMSValue() ).collect( Collectors.toList() );
        int fixedIntBitLen = getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        for ( Iterator<SMSAttributeValue> valIter = values.iterator(); valIter.hasNext(); )
        {
            SMSAttributeValue val = valIter.next();
            int idHash = BinaryUtils.hash( val.getAttribute(), attributeBitLen );
            outStream.write( idHash, attributeBitLen );
            writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

            int separator = valIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public static List<SMSAttributeValue> readAttributeValues( SMSMetadata meta, BitInputStream inStream )
        throws SMSCompressionException
    {
        ArrayList<SMSAttributeValue> values = new ArrayList<>();
        int attributeBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );

        int fixedIntBitLen = inStream.read( SMSConsts.FIXED_INT_BITLEN ) + 1;

        Map<Integer, String> idLookup = IDUtil.getIDLookup( meta.getType( MetadataType.TRACKED_ENTITY_ATTRIBUTE ),
            attributeBitLen );
        for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
        {
            int idHash = inStream.read( attributeBitLen );
            String id = idLookup.get( idHash );
            // TODO: Should we be warning and is this the best way to do it?
            if ( id == null )
                System.out.println(
                    "WARNING: SMSCompression(readAttributeValues) - Cannot find UID in submission for attribute value" );

            SMSValue<?> smsValue = readSMSValue( fixedIntBitLen, inStream );
            values.add( new SMSAttributeValue( id, smsValue ) );
        }

        return values;
    }

    public static Map<String, List<SMSDataValue>> groupDataValues( List<SMSDataValue> values )
    {
        HashMap<String, List<SMSDataValue>> map = new HashMap<>();
        for ( SMSDataValue val : values )
        {
            String catOptionCombo = val.getCategoryOptionCombo();
            if ( !map.containsKey( catOptionCombo ) )
            {
                ArrayList<SMSDataValue> list = new ArrayList<>();
                map.put( catOptionCombo, list );
            }
            List<SMSDataValue> list = map.get( catOptionCombo );
            list.add( val );
        }
        return map;
    }

    // TODO: Add handling to support New UIDs if the id isn't in Metadata
    public static void writeDataValues( List<SMSDataValue> values, SMSMetadata meta, BitOutputStream outStream )
        throws SMSCompressionException
    {
        int catOptionComboBitLen = IDUtil.getBitLengthForList( meta.getType( MetadataType.CATEGORY_OPTION_COMBO ),
            MetadataType.CATEGORY_OPTION_COMBO );
        outStream.write( catOptionComboBitLen, SMSConsts.VARLEN_BITLEN );

        int dataElementBitLen = IDUtil.getBitLengthForList( meta.getType( MetadataType.DATA_ELEMENT ),
            MetadataType.DATA_ELEMENT );
        outStream.write( dataElementBitLen, SMSConsts.VARLEN_BITLEN );

        List<SMSValue<?>> smsVals = values.stream().map( v -> v.getSMSValue() ).collect( Collectors.toList() );
        int fixedIntBitLen = getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        Map<String, List<SMSDataValue>> valMap = groupDataValues( values );

        for ( Iterator<String> keyIter = valMap.keySet().iterator(); keyIter.hasNext(); )
        {
            String catOptionCombo = keyIter.next();
            int catOptionComboHash = BinaryUtils.hash( catOptionCombo, catOptionComboBitLen );
            outStream.write( catOptionComboHash, catOptionComboBitLen );
            List<SMSDataValue> vals = valMap.get( catOptionCombo );

            for ( Iterator<SMSDataValue> valIter = vals.iterator(); valIter.hasNext(); )
            {
                SMSDataValue val = valIter.next();
                int deHash = BinaryUtils.hash( val.getDataElement(), dataElementBitLen );
                outStream.write( deHash, dataElementBitLen );
                writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

                int separator = valIter.hasNext() ? 1 : 0;
                outStream.write( separator, 1 );
            }

            int separator = keyIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public static List<SMSDataValue> readDataValues( SMSMetadata meta, BitInputStream inStream )
        throws SMSCompressionException
    {
        int catOptionComboBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        int dataElementBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        int fixedIntBitLen = inStream.read( SMSConsts.FIXED_INT_BITLEN ) + 1;
        Map<Integer, String> cocIDLookup = IDUtil.getIDLookup( meta.getType( MetadataType.CATEGORY_OPTION_COMBO ),
            catOptionComboBitLen );
        Map<Integer, String> deIDLookup = IDUtil.getIDLookup( meta.getType( MetadataType.DATA_ELEMENT ),
            dataElementBitLen );
        ArrayList<SMSDataValue> values = new ArrayList<>();

        for ( int cocSep = 1; cocSep == 1; cocSep = inStream.read( 1 ) )
        {
            int catOptionComboHash = inStream.read( catOptionComboBitLen );
            String catOptionCombo = cocIDLookup.get( catOptionComboHash );
            // TODO: Should we be warning and is this the best way to do it?
            if ( catOptionCombo == null )
                System.out.println(
                    "WARNING: SMSCompression(readDataValues) - Cannot find UID in submission for category option combo" );

            for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
            {
                int dataElementHash = inStream.read( dataElementBitLen );
                String dataElement = deIDLookup.get( dataElementHash );
                // TODO: Should we be warning and is this the best way to do it?
                if ( dataElement == null )
                    System.out.println(
                        "WARNING: SMSCompression(readDataValues) - Cannot find UID in submission for data element" );

                SMSValue<?> smsValue = readSMSValue( fixedIntBitLen, inStream );
                values.add( new SMSDataValue( catOptionCombo, dataElement, smsValue ) );
            }
        }

        return values;
    }

    // Simple types

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

    public static SMSValue<?> asSMSValue( String value )
    {
        // BOOL
        if ( value.equals( "true" ) || value.equals( "false" ) )
        {
            Boolean valBool = value.equals( "true" ) ? true : false;
            return new SMSValue<Boolean>( valBool, ValueType.BOOL );
        }

        // DATE
        try
        {
            Date valDate = SMSConsts.SIMPLE_DATE_FORMAT.parse( value );
            return new SMSValue<Date>( valDate, ValueType.DATE );
        }
        catch ( ParseException e )
        {
            // not a date
        }

        // INT
        try
        {
            Integer valInt = Integer.parseInt( value );
            return new SMSValue<Integer>( valInt, ValueType.INT );
        }
        catch ( NumberFormatException e )
        {
            // not an integer
        }

        // FLOAT
        try
        {
            Float valFloat = Float.parseFloat( value );
            return new SMSValue<Float>( valFloat, ValueType.FLOAT );
        }
        catch ( NumberFormatException e )
        {
            // not a float
        }

        // If all else fails we can use String
        return new SMSValue<String>( value, ValueType.STRING );
    }

    public static int getBitlenLargestInt( List<SMSValue<?>> values )
    {
        int maxInt = 0;
        for ( SMSValue<?> val : values )
        {
            if ( val.getType() == ValueType.INT )
            {
                int intVal = Math.abs( (int) val.getValue() );
                if ( intVal > SMSConsts.MAX_FIXED_NUM )
                    continue;
                maxInt = intVal > maxInt ? intVal : maxInt;
            }
        }
        return BinaryUtils.bitlenNeeded( maxInt );
    }
}
