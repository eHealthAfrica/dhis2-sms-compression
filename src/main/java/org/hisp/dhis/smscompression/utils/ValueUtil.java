package org.hisp.dhis.smscompression.utils;

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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;

public class ValueUtil
{

    public static void writeAttributeValues( List<SMSAttributeValue> values, SMSMetadata meta,
        BitOutputStream outStream )
        throws IOException
    {
        int attributeBitLen = IDUtil.getBitLengthForList( meta.getTrackedEntityAttributes() );
        outStream.write( attributeBitLen, SMSConsts.VARLEN_BITLEN );
        for ( SMSAttributeValue val : values )
        {
            int idHash = BinaryUtils.hash( val.getAttribute(), attributeBitLen );
            outStream.write( idHash, attributeBitLen );
            writeString( val.getValue(), outStream );
        }
    }

    public static List<SMSAttributeValue> readAttributeValues( SMSMetadata meta, BitInputStream inStream )
        throws IOException
    {
        ArrayList<SMSAttributeValue> values = new ArrayList<>();
        int attributeBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        Map<Integer, String> idLookup = IDUtil.getIDLookup( meta.getTrackedEntityAttributes(), attributeBitLen );

        while ( true )
        {
            try
            {
                int idHash = inStream.read( attributeBitLen );
                String id = idLookup.get( idHash );
                String val = readString( inStream );
                values.add( new SMSAttributeValue( id, val ) );
            }
            catch ( EOFException e )
            {
                break;
            }
        }
        return values;
    }

    public static void writeString( String s, BitOutputStream outStream )
        throws IOException
    {
        for ( char c : s.toCharArray() )
        {
            outStream.write( c, SMSConsts.CHAR_BITLEN );
        }
        // Null terminator
        outStream.write( 0, SMSConsts.CHAR_BITLEN );
    }

    public static String readString( BitInputStream inStream )
        throws IOException
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

    public static void writeDate( Date d, BitOutputStream outStream )
        throws IOException
    {
        long epochSecs = d.getTime() / 1000;
        outStream.write( (int) epochSecs, SMSConsts.EPOCH_DATE_BITLEN );
    }

    public static Date readDate( BitInputStream inStream )
        throws IOException
    {
        long epochSecs = inStream.read( SMSConsts.EPOCH_DATE_BITLEN );
        Date dateVal = new Date( epochSecs * 1000 );
        return dateVal;
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

    // TODO: No error handling for missing IDs at the moment
    public static void writeDataValues( List<SMSDataValue> values, SMSMetadata meta, BitOutputStream outStream )
        throws IOException
    {
        int catOptionComboBitLen = IDUtil.getBitLengthForList( meta.getCategoryOptionCombos() );
        outStream.write( catOptionComboBitLen, SMSConsts.VARLEN_BITLEN );
        int dataElementBitLen = IDUtil.getBitLengthForList( meta.getDataElements() );
        outStream.write( dataElementBitLen, SMSConsts.VARLEN_BITLEN );

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
                writeString( val.getValue(), outStream );

                int separator = valIter.hasNext() ? 1 : 0;
                outStream.write( separator, 1 );
            }

            int separator = keyIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public static List<SMSDataValue> readDataValues( SMSMetadata meta, BitInputStream inStream )
        throws IOException
    {
        int catOptionComboBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        int dataElementBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        Map<Integer, String> cocIDLookup = IDUtil.getIDLookup( meta.getCategoryOptionCombos(), catOptionComboBitLen );
        Map<Integer, String> deIDLookup = IDUtil.getIDLookup( meta.getDataElements(), dataElementBitLen );
        ArrayList<SMSDataValue> values = new ArrayList<>();

        while ( true )
        {
            try
            {
                for ( int cocSep = 1; cocSep == 1; cocSep = inStream.read( 1 ) )
                {
                    int catOptionComboHash = inStream.read( catOptionComboBitLen );
                    String catOptionCombo = cocIDLookup.get( catOptionComboHash );
                    for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
                    {
                        int dataElementHash = inStream.read( dataElementBitLen );
                        String dataElement = deIDLookup.get( dataElementHash );
                        String value = readString( inStream );
                        values.add( new SMSDataValue( catOptionCombo, dataElement, value ) );
                    }
                }
            }
            catch ( EOFException e )
            {
                break;
            }
        }

        return values;
    }
}
