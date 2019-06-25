package org.hisp.dhis.smscompression;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.utils.BitOutputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class SMSSubmissionWriter
{
    ByteArrayOutputStream byteStream;

    BitOutputStream outStream;

    SMSMetadata meta;

    public SMSSubmissionWriter( SMSMetadata meta )
    {
        // TODO: Check metadata looks valid here
        this.meta = meta;
    }

    public void setMetadata( SMSMetadata meta )
    {
        // TODO: Check metadata looks valid here
        this.meta = meta;
    }

    public byte[] compress( SMSSubmission subm )
        throws Exception
    {
        this.byteStream = new ByteArrayOutputStream();
        this.outStream = new BitOutputStream( byteStream );

        subm.write( meta, this );

        return toByteArray();
    }

    public byte[] toByteArray()
        throws IOException
    {
        outStream.close();
        byte[] subm = byteStream.toByteArray();
        byte[] crcSubm = writeCRC( subm );
        return crcSubm;
    }

    public byte[] writeCRC( byte[] subm )
    {
        byte crc;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] calcCRC = digest.digest( subm );
            crc = calcCRC[0];
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
            return null;
        }

        byte[] crcSubm = new byte[subm.length + 1];
        System.arraycopy( subm, 0, crcSubm, 1, subm.length );
        crcSubm[0] = crc;

        return crcSubm;
    }

    public void writeType( SubmissionType type )
        throws IOException
    {
        outStream.write( type.ordinal(), SMSConsts.SUBM_TYPE_BITLEN );
    }

    public void writeVersion( int version )
        throws IOException
    {
        outStream.write( version, SMSConsts.VERSION_BITLEN );
    }

    public void writeDate( Date date )
        throws IOException
    {
        long epochSecs = date.getTime() / 1000;
        outStream.write( (int) epochSecs, SMSConsts.EPOCH_DATE_BITLEN );
    }

    public void writeNewID( String id )
        throws Exception
    {
        IDUtil.writeNewID( id, outStream );
    }

    public void writeAttributeValues( List<SMSAttributeValue> values )
        throws IOException
    {
        ValueUtil.writeAttributeValues( values, meta, outStream );
    }

    public void writeDataValues( List<SMSDataValue> values )
        throws IOException
    {
        ValueUtil.writeDataValues( values, meta, outStream );
    }

    public void writeBool( boolean val )
        throws IOException
    {
        int intVal = val ? 1 : 0;
        outStream.write( intVal, 1 );
    }

    // TODO: We should consider a better implementation for period than just
    // String
    public void writePeriod( String period )
        throws IOException
    {
        ValueUtil.writeString( period, outStream );
    }

    public void writeSubmissionID( int submissionID )
        throws IOException
    {
        outStream.write( submissionID, SMSConsts.SUBM_ID_BITLEN );
    }
}
