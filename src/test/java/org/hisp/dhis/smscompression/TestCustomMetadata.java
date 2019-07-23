package org.hisp.dhis.smscompression;

import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SMSSubmissionHeader;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

public class TestCustomMetadata
{

    SMSMetadata meta;

    SMSSubmissionWriter writer;

    SMSSubmissionReader reader;

    public String compressSubm( SMSSubmission subm )
        throws Exception
    {
        byte[] compressSubm = writer.compress( subm );
        String comp64 = TestUtils.encBase64( compressSubm );
        TestUtils.printBase64Subm( comp64, subm.getClass() );
        return comp64;
    }

    public SMSSubmission decompressSubm( String comp64 )
        throws Exception
    {
        byte[] decSubmBytes = TestUtils.decBase64( comp64 );
        SMSSubmissionHeader header = reader.readHeader( decSubmBytes );
        Assert.assertNotNull( header );
        return reader.readSubmission( decSubmBytes, meta );
    }

    @Test
    public void testNullMetadata()
    {
        try
        {
            meta = null;
            writer = new SMSSubmissionWriter( meta );
            reader = new SMSSubmissionReader();

            AggregateDatasetSMSSubmission origSubm = TestUtils.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSMSSubmission decSubm = (AggregateDatasetSMSSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyMetadata()
    {
        try
        {
            meta = new SMSMetadata();
            writer = new SMSSubmissionWriter( meta );
            reader = new SMSSubmissionReader();

            AggregateDatasetSMSSubmission origSubm = TestUtils.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSMSSubmission decSubm = (AggregateDatasetSMSSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyDataElementsMetadata()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SMSMetadata.class );
            meta.dataElements = null;
            writer = new SMSSubmissionWriter( meta );
            reader = new SMSSubmissionReader();

            AggregateDatasetSMSSubmission origSubm = TestUtils.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSMSSubmission decSubm = (AggregateDatasetSMSSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyAttributesMetadata()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SMSMetadata.class );
            meta.trackedEntityAttributes = null;
            writer = new SMSSubmissionWriter( meta );
            reader = new SMSSubmissionReader();

            EnrollmentSMSSubmission origSubm = TestUtils.createEnrollmentSubmission();
            String comp64 = compressSubm( origSubm );
            EnrollmentSMSSubmission decSubm = (EnrollmentSMSSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testHashingDisabled()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SMSMetadata.class );
            writer = new SMSSubmissionWriter( meta );
            writer.setHashingEnabled( false );
            reader = new SMSSubmissionReader();

            AggregateDatasetSMSSubmission origSubm = TestUtils.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSMSSubmission decSubm = (AggregateDatasetSMSSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

}
