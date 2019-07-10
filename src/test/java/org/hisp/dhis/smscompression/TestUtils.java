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

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.hisp.dhis.smscompression.SMSConsts.SMSEventStatus;
import org.hisp.dhis.smscompression.models.AggregateDatasetSMSSubmission;
import org.hisp.dhis.smscompression.models.DeleteSMSSubmission;
import org.hisp.dhis.smscompression.models.EnrollmentSMSSubmission;
import org.hisp.dhis.smscompression.models.RelationshipSMSSubmission;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSSubmission;
import org.hisp.dhis.smscompression.models.SimpleEventSMSSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSMSSubmission;
import org.junit.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestUtils
{

    public static Date getNowWithoutMillis()
    {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.MILLISECOND, 0 );
        return cal.getTime();
    }

    public static DeleteSMSSubmission createDeleteSubmission()
    {
        DeleteSMSSubmission subm = new DeleteSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setEvent( "Jpr20TLJ7Z1" ); // Generated UID of test event
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static RelationshipSMSSubmission createRelationshipSubmission()
    {
        RelationshipSMSSubmission subm = new RelationshipSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setRelationshipType( "XdP5nraLPZ0" ); // Sibling_a-to-b_(Person-Person)
        subm.setRelationship( "uf3svrmpzOj" ); // Generated UID for new
                                               // relationship
        subm.setFrom( "qv0j4JBXQX0" ); // Gloria Murray (Person)
        subm.setTo( "LSEjy8nA3kY" ); // Jerald Wilson (Person)
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static SimpleEventSMSSubmission createSimpleEventSubmission()
    {
        SimpleEventSMSSubmission subm = new SimpleEventSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setEventProgram( "lxAQ7Zs9VYR" ); // Antenatal Care Visit
        subm.setAttributeOptionCombo( "HllvX50cXC0" ); // Default catOptionCombo
        subm.setEvent( "l7M1gUFK37v" ); // New UID
        subm.setEventStatus( SMSEventStatus.COMPLETED );
        subm.setTimestamp( getNowWithoutMillis() );
        ArrayList<SMSDataValue> values = new ArrayList<>();
        values.add( new SMSDataValue( "HllvX50cXC0", "sWoqcoByYmD", "true" ) ); // WHOMCH
                                                                                // Smoking
        values.add( new SMSDataValue( "HllvX50cXC0", "Ok9OQpitjQr", "false" ) ); // WHOMCH
                                                                                 // Smoking
                                                                                 // cessation
                                                                                 // counselling
                                                                                 // provided
        values.add( new SMSDataValue( "HllvX50cXC0", "vANAXwtLwcT", "14" ) ); // WHOMCH
                                                                              // Hemoglobin
                                                                              // value
        subm.setValues( values );
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static AggregateDatasetSMSSubmission createAggregateDatasetSubmission()
    {
        AggregateDatasetSMSSubmission subm = new AggregateDatasetSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setDataSet( "Nyh6laLdBEJ" ); // IDSR Weekly
        subm.setComplete( true );
        subm.setAttributeOptionCombo( "HllvX50cXC0" );
        subm.setPeriod( "2019W16" );
        ArrayList<SMSDataValue> values = new ArrayList<>();
        values.add( new SMSDataValue( "HllvX50cXC0", "noIzB569hTM", "12345678" ) ); // Yellow
        // Fever
        values.add( new SMSDataValue( "HllvX50cXC0", "vq2qO3eTrNi", "-24.5" ) ); // Malaria
        values.add( new SMSDataValue( "HllvX50cXC0", "HS9zqaBdOQ4", "-65535" ) ); // Plague
        values.add( new SMSDataValue( "HllvX50cXC0", "YazgqXbizv1", "0.12345678" ) ); // Measles
        values.add( new SMSDataValue( "HllvX50cXC0", "UsSUX0cpKsH", "0" ) ); // Cholera
        subm.setValues( values );
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static EnrollmentSMSSubmission createEnrollmentSubmission()
    {
        EnrollmentSMSSubmission subm = new EnrollmentSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setTrackerProgram( "IpHINAT79UW" ); // Child Programme
        subm.setTrackedEntityType( "nEenWmSyUEp" ); // Person
        subm.setTrackedEntityInstance( "T2bRuLEGoVN" ); // Newly generated UID
        subm.setEnrollment( "p7M1gUFK37W" ); // Newly generated UID
        subm.setTimestamp( getNowWithoutMillis() );
        ArrayList<SMSAttributeValue> values = new ArrayList<>();
        values.add( new SMSAttributeValue( "w75KJ2mc4zz", "Harold" ) ); // First
                                                                        // Name
        values.add( new SMSAttributeValue( "zDhUuAYrxNC", "Smith" ) ); // Last
                                                                       // Name
        values.add( new SMSAttributeValue( "FO4sWYJ64LQ", "Sydney" ) ); // City
        values.add( new SMSAttributeValue( "VqEFza8wbwA", "The Opera House" ) ); // Address
        values.add( new SMSAttributeValue( "lZGmxYbs97q", "987123" ) ); // Unique
                                                                        // ID
        subm.setValues( values );
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static TrackerEventSMSSubmission createTrackerEventSubmission()
    {
        TrackerEventSMSSubmission subm = new TrackerEventSMSSubmission();

        subm.setUserID( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setProgramStage( "A03MvHHogjR" ); // Birth
        subm.setAttributeOptionCombo( "HllvX50cXC0" ); // Default catOptionCombo
        subm.setEnrollment( "DacGG5vK1K6" ); // Test Person
        subm.setEvent( "r7M1gUFK37v" ); // New UID
        subm.setEventStatus( SMSEventStatus.COMPLETED );
        subm.setTimestamp( getNowWithoutMillis() );
        ArrayList<SMSDataValue> values = new ArrayList<>();
        values.add( new SMSDataValue( "HllvX50cXC0", "a3kGcGDCuk6", "10" ) ); // Apgar
                                                                              // score
        values.add( new SMSDataValue( "HllvX50cXC0", "UXz7xuGCEhU", "500" ) ); // Weight
                                                                               // (g)
        values.add( new SMSDataValue( "HllvX50cXC0", "wQLfBvPrXqq", "Others" ) ); // ARV
                                                                                  // at
                                                                                  // birth
        values.add( new SMSDataValue( "HllvX50cXC0", "X8zyunlgUfM", "Exclusive" ) ); // Infant
                                                                                     // feeding
        subm.setValues( values );
        subm.setSubmissionID( 1 );

        return subm;
    }

    public static void printSubm( SMSSubmission subm )
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println( gson.toJson( subm ) );
    }

    public static String encBase64( byte[] subm )
    {
        return Base64.getEncoder().encodeToString( subm );
    }

    public static byte[] decBase64( String subm )
    {
        return Base64.getDecoder().decode( subm );
    }

    public static void printBase64Subm( String subm, Class<?> submType )
    {
        System.out.println( submType );
        System.out.println( "Base64 encoding is: " + subm );
        System.out.println( "Char length: " + subm.length() );
        System.out.println( "Num SMS: " + ((subm.length() / 160) + 1) );
        System.out.println( "************************" );
    }

    public static void checkSubmissionsAreEqual( SMSSubmission origSubm, SMSSubmission decSubm )
    {
        if ( !origSubm.equals( decSubm ) )
        {
            System.out.println( "Submissions are not equal!" );
            System.out.println( "Original submission: " );
            printSubm( origSubm );
            System.out.println( "Decoded submission: " );
            printSubm( decSubm );
            Assert.fail();
        }
    }
}
