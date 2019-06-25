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
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
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
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

public class TestEncodeDecode
{

    public void printDecoded( String subm64, SMSMetadata meta )
        throws Exception
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SMSSubmissionReader reader = new SMSSubmissionReader();
        byte[] smsBytes = Base64.getDecoder().decode( subm64 );
        SMSSubmissionHeader header = reader.readHeader( smsBytes );
        System.out.println( "Decoded header is: " + gson.toJson( header ) );

        SMSSubmission decodedSubm = reader.readSubmission( smsBytes, meta );
        System.out.println( "Decoded submission is: " + gson.toJson( decodedSubm ) );
    }

    @Test
    public void testEncodeDelete()
    {
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            DeleteSMSSubmission subm = new DeleteSMSSubmission();

            // Tom Wakiki (system)
            subm.setUserID( "GOLswS44mh8" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // Generated UID of test event
            subm.setUid( "Jpr20TLJ7Z1" );
            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Delete submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeRelationship()
    {
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            RelationshipSMSSubmission subm = new RelationshipSMSSubmission();

            // Tom Wakiki (system)
            subm.setUserID( "GOLswS44mh8" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // Sibling_a-to-b_(Person-Person)
            subm.setRelationshipType( "XdP5nraLPZ0" );
            // Generated UID for new relationship
            subm.setRelationship( "uf3svrmpzOj" );
            // Gloria Murray (Person)
            subm.setFrom( "qv0j4JBXQX0" );
            // Jerald Wilson (Person)
            subm.setTo( "LSEjy8nA3kY" );
            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Relationship submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncodeSimpleEvent()
    {
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            SimpleEventSMSSubmission subm = new SimpleEventSMSSubmission();

            // Tom Wakiki (system)
            subm.setUserID( "GOLswS44mh8" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // Antenatal Care Visit
            subm.setEventProgram( "lxAQ7Zs9VYR" );
            // Default catOptionCombo
            subm.setAttributeOptionCombo( "HllvX50cXC0" );
            // New UID
            subm.setEvent( "l7M1gUFK37v" );
            subm.setTimestamp( meta.lastSyncDate );
            ArrayList<SMSDataValue> values = new ArrayList<>();
            // WHOMCH Smoking
            values.add( new SMSDataValue( "HllvX50cXC0", "sWoqcoByYmD", "true" ) );
            // WHOMCH Smoking cessation counselling provided
            values.add( new SMSDataValue( "HllvX50cXC0", "Ok9OQpitjQr", "false" ) );
            // WHOMCH Hemoglobin value
            values.add( new SMSDataValue( "HllvX50cXC0", "vANAXwtLwcT", "14" ) );
            subm.setValues( values );
            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Simple Event submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
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
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            AggregateDatasetSMSSubmission subm = new AggregateDatasetSMSSubmission();

            // Tom Wakiki (system)
            subm.setUserID( "GOLswS44mh8" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // IDSR Weekly
            subm.setDataSet( "Nyh6laLdBEJ" );
            subm.setComplete( true );
            subm.setAttributeOptionCombo( "HllvX50cXC0" );
            subm.setPeriod( "2019W16" );
            ArrayList<SMSDataValue> values = new ArrayList<>();
            // Yellow Fever
            values.add( new SMSDataValue( "HllvX50cXC0", "noIzB569hTM", "11" ) );
            // Malaria
            values.add( new SMSDataValue( "HllvX50cXC0", "vq2qO3eTrNi", "24" ) );
            // Plague
            values.add( new SMSDataValue( "HllvX50cXC0", "HS9zqaBdOQ4", "99" ) );
            // Measles
            values.add( new SMSDataValue( "HllvX50cXC0", "YazgqXbizv1", "3" ) );
            // Cholera
            values.add( new SMSDataValue( "HllvX50cXC0", "UsSUX0cpKsH", "1" ) );
            // Fake Data Element, doesn't exist server side
            values.add( new SMSDataValue( "HllvX50cXC0", "xxxxxxxxxxx", "1" ) );

            subm.setValues( values );
            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Aggregate Dataset submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
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
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            EnrollmentSMSSubmission subm = new EnrollmentSMSSubmission();

            // Tom Wakiki (system)
            subm.setUserID( "GOLswS44mh8" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // Child Programme
            subm.setTrackerProgram( "IpHINAT79UW" );
            // Person
            subm.setTrackedEntityType( "nEenWmSyUEp" );
            // Newly generated UID
            subm.setTrackedEntityInstance( "T2bRuLEGoVN" );
            // Newly generated UID
            subm.setEnrollment( "p7M1gUFK37W" );
            subm.setTimestamp( meta.lastSyncDate );

            ArrayList<SMSAttributeValue> values = new ArrayList<>();
            // First Name
            values.add( new SMSAttributeValue( "w75KJ2mc4zz", "Harold" ) );
            // Last Name
            values.add( new SMSAttributeValue( "zDhUuAYrxNC", "Smith" ) );
            // City
            values.add( new SMSAttributeValue( "FO4sWYJ64LQ", "Sydney" ) );
            // Address
            values.add( new SMSAttributeValue( "VqEFza8wbwA", "The Opera House" ) );
            // Unique ID
            values.add( new SMSAttributeValue( "lZGmxYbs97q", "987123" ) );
            subm.setValues( values );

            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Enrollment submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
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
        Gson gson = new Gson();
        try
        {
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            SMSMetadata meta = gson.fromJson( metadataJson, SMSMetadata.class );
            TrackerEventSMSSubmission subm = new TrackerEventSMSSubmission();

            // Jasper Timm
            subm.setUserID( "V3qn98bKsr6" );
            // Ngelehun CHC
            subm.setOrgUnit( "DiszpKrYNg8" );
            // Birth
            subm.setProgramStage( "A03MvHHogjR" );
            // Default catOptionCombo
            subm.setAttributeOptionCombo( "HllvX50cXC0" );
            // Test Person
            subm.setTrackedEntityInstance( "DacGG5vK1K6" );
            // New UID
            subm.setEvent( "r7M1gUFK37v" );
            subm.setTimestamp( meta.lastSyncDate );
            ArrayList<SMSDataValue> values = new ArrayList<>();
            // Apgar score
            values.add( new SMSDataValue( "HllvX50cXC0", "a3kGcGDCuk6", "10" ) );
            // Weight (g)
            values.add( new SMSDataValue( "HllvX50cXC0", "UXz7xuGCEhU", "500" ) );
            // ARV at birth"
            values.add( new SMSDataValue( "HllvX50cXC0", "wQLfBvPrXqq", "Others" ) );
            // Infant feeding
            values.add( new SMSDataValue( "HllvX50cXC0", "X8zyunlgUfM", "Exclusive" ) );
            subm.setValues( values );
            subm.setSubmissionID( 1 );

            SMSSubmissionWriter writer = new SMSSubmissionWriter( meta );
            byte[] compressSubm = writer.compress( subm );
            String subm64 = Base64.getEncoder().encodeToString( compressSubm );
            System.out.println( "Tracker Event submission in Base64 is: " + subm64 );

            printDecoded( subm64, meta );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

}
