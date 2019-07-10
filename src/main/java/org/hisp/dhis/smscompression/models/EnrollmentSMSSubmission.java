package org.hisp.dhis.smscompression.models;

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

import java.util.Date;
import java.util.List;

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public class EnrollmentSMSSubmission
    extends
    SMSSubmission
{
    protected UID orgUnit;

    protected UID trackerProgram;

    protected UID trackedEntityType;

    protected UID trackedEntityInstance;

    protected UID enrollment;

    protected Date timestamp;

    protected List<SMSAttributeValue> values;

    public UID getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = new UID( orgUnit, MetadataType.ORGANISATION_UNIT );
    }

    public UID getTrackerProgram()
    {
        return trackerProgram;
    }

    public void setTrackerProgram( String trackerProgram )
    {
        this.trackerProgram = new UID( trackerProgram, MetadataType.PROGRAM );
    }

    public UID getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( String trackedEntityType )
    {
        this.trackedEntityType = new UID( trackedEntityType, MetadataType.TRACKED_ENTITY_TYPE );
    }

    public UID getTrackedEntityInstance()
    {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance( String trackedEntityInstance )
    {
        this.trackedEntityInstance = new UID( trackedEntityInstance, MetadataType.TRACKED_ENTITY_INSTANCE );
    }

    public UID getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment( String enrollment )
    {
        this.enrollment = new UID( enrollment, MetadataType.ENROLLMENT );
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public List<SMSAttributeValue> getValues()
    {
        return values;
    }

    public void setValues( List<SMSAttributeValue> values )
    {
        this.values = values;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        EnrollmentSMSSubmission subm = (EnrollmentSMSSubmission) o;

        return orgUnit.equals( subm.orgUnit ) && trackerProgram.equals( subm.trackerProgram )
            && trackedEntityType.equals( subm.trackedEntityType )
            && trackedEntityInstance.equals( subm.trackedEntityInstance ) && enrollment.equals( subm.enrollment )
            && timestamp.equals( subm.timestamp ) && values.equals( subm.values );
    }

    @Override
    public void writeSubm( SMSSubmissionWriter writer )
        throws SMSCompressionException
    {
        writer.writeID( orgUnit );
        writer.writeID( trackerProgram );
        writer.writeID( trackedEntityType );
        writer.writeID( trackedEntityInstance );
        writer.writeID( enrollment );
        writer.writeDate( timestamp );
        writer.writeAttributeValues( values );
    }

    @Override
    public void readSubm( SMSSubmissionReader reader )
        throws SMSCompressionException
    {
        this.orgUnit = reader.readID( MetadataType.ORGANISATION_UNIT );
        this.trackerProgram = reader.readID( MetadataType.PROGRAM );
        this.trackedEntityType = reader.readID( MetadataType.TRACKED_ENTITY_TYPE );
        this.trackedEntityInstance = reader.readID( MetadataType.TRACKED_ENTITY_INSTANCE );
        this.enrollment = reader.readID( MetadataType.ENROLLMENT );
        this.timestamp = reader.readDate();
        this.values = reader.readAttributeValues();
    }

    @Override
    public int getCurrentVersion()
    {
        return 1;
    }

    @Override
    public SubmissionType getType()
    {
        return SMSConsts.SubmissionType.ENROLLMENT;
    }

}
