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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.hisp.dhis.smscompression.utils.BitInputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class SMSSubmissionReader
{
    protected BitInputStream inStream;

    public SMSSubmissionHeader readHeader( byte[] smsBytes )
        throws Exception
    {
        if ( !checkCRC( smsBytes ) )
            throw new Exception( "Invalid CRC" );

        ByteArrayInputStream byteStream = new ByteArrayInputStream( smsBytes );
        this.inStream = new BitInputStream( byteStream );
        inStream.read( SMSConsts.CRC_BITLEN ); // skip CRC

        SMSSubmissionHeader header = new SMSSubmissionHeader();
        header.readHeader( this );

        return header;
    }

    public SMSSubmission readSubmission( byte[] smsBytes, SMSMetadata meta )
        throws Exception
    {
        SMSSubmissionHeader header = readHeader( smsBytes );
        SMSSubmission subm = null;

        switch ( header.getType() )
        {
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
            throw new Exception( "Unknown SMS Submission Type" );
        }

        subm.read( meta, this, header );
        inStream.close();
        return subm;
    }

    private boolean checkCRC( byte[] smsBytes )
    {
        byte crc = smsBytes[0];
        byte[] submBytes = Arrays.copyOfRange( smsBytes, 1, smsBytes.length );

        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] calcCRC = digest.digest( submBytes );
            return (calcCRC[0] == crc);
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    public SubmissionType readType()
        throws IOException
    {
        int submType = inStream.read( SMSConsts.SUBM_TYPE_BITLEN );
        return SMSConsts.SubmissionType.values()[submType];
    }

    public int readVersion()
        throws IOException
    {
        return inStream.read( SMSConsts.VERSION_BITLEN );
    }

    public Date readDate()
        throws IOException
    {
        long epochSecs = inStream.read( SMSConsts.EPOCH_DATE_BITLEN );
        Date dateVal = new Date( epochSecs * 1000 );
        return dateVal;
    }

    public String readNewID()
        throws Exception
    {
        return IDUtil.readNewID( inStream );
    }

    public String readID( MetadataType type, SMSMetadata meta )
        throws Exception
    {
        return IDUtil.readID( type, meta, inStream );
    }

    public List<SMSAttributeValue> readAttributeValues( SMSMetadata meta )
        throws IOException
    {
        return ValueUtil.readAttributeValues( meta, inStream );
    }

    public List<SMSDataValue> readDataValues( SMSMetadata meta )
        throws IOException
    {
        return ValueUtil.readDataValues( meta, inStream );
    }

    public boolean readBool()
        throws IOException
    {
        int intVal = inStream.read( 1 );
        return intVal == 1;
    }

    // TODO: Update this once we have a better impl of period
    public String readPeriod()
        throws IOException
    {
        return ValueUtil.readString( inStream );
    }

    public int readSubmissionID()
        throws IOException
    {
        return inStream.read( SMSConsts.SUBM_ID_BITLEN );
    }
}
