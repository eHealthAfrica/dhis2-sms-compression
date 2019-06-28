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

import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class TestEncodeDecode
{
    SMSMetadata meta;

    SMSSubmissionWriter writer;

    SMSSubmissionReader reader;

    @Before
    public void init()
        throws Exception
    {
        Gson gson = new Gson();
        String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
        meta = gson.fromJson( metadataJson, SMSMetadata.class );
        writer = new SMSSubmissionWriter( meta );
        reader = new SMSSubmissionReader();
    }

    @After
    public void cleanup()
    {

    }

    @Test
    public void testEncodeDecodeRelationship()
    {
        try
        {
            // Encode
            RelationshipSMSSubmission origSubm = TestUtils.createRelationshipSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Relationship submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            RelationshipSMSSubmission decSubm = (RelationshipSMSSubmission) reader.readSubmission( decSubmBytes, meta );

            origSubm.setFrom( "" );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeDecodeDelete()
    {
        try
        {
            // Encode
            DeleteSMSSubmission origSubm = TestUtils.createDeleteSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Delete submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            DeleteSMSSubmission decSubm = (DeleteSMSSubmission) reader.readSubmission( decSubmBytes, meta );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeDecodeSimpleEvent()
    {
        try
        {
            // Encode
            SimpleEventSMSSubmission origSubm = TestUtils.createSimpleEventSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Simple Event submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            SimpleEventSMSSubmission decSubm = (SimpleEventSMSSubmission) reader.readSubmission( decSubmBytes, meta );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeAggregateDataset()
    {
        try
        {
            // Encode
            AggregateDatasetSMSSubmission origSubm = TestUtils.createAggregateDatasetSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Aggregate Dataset submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            AggregateDatasetSMSSubmission decSubm = (AggregateDatasetSMSSubmission) reader.readSubmission( decSubmBytes,
                meta );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeEnrollment()
    {
        try
        {
            // Encode
            EnrollmentSMSSubmission origSubm = TestUtils.createEnrollmentSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Enrollment submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            EnrollmentSMSSubmission decSubm = (EnrollmentSMSSubmission) reader.readSubmission( decSubmBytes, meta );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeTrackerEvent()
    {
        try
        {
            // Encode
            TrackerEventSMSSubmission origSubm = TestUtils.createTrackerEventSubmission();
            Assert.assertNotNull( origSubm );
            byte[] compressSubm = writer.compress( origSubm );
            String comp64 = TestUtils.encBase64( compressSubm );
            System.out.println( "Tracker Event submission in Base64 is: " + comp64 );

            // Decode
            byte[] decSubmBytes = TestUtils.decBase64( comp64 );
            SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
            Assert.assertNotNull( header );
            TrackerEventSMSSubmission decSubm = (TrackerEventSMSSubmission) reader.readSubmission( decSubmBytes, meta );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

}
