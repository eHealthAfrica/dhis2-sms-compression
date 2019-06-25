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

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public class EnrollmentSMSSubmission
    extends
    SMSSubmission
{
    protected String orgUnit;

    protected String trackerProgram;

    protected String trackedEntityType;

    protected String trackedEntityInstance;

    protected String enrollment;

    protected Date timestamp;

    protected List<SMSAttributeValue> values;

    public String getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = orgUnit;
    }

    public String getTrackerProgram()
    {
        return trackerProgram;
    }

    public void setTrackerProgram( String trackerProgram )
    {
        this.trackerProgram = trackerProgram;
    }

    public String getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( String trackedEntityType )
    {
        this.trackedEntityType = trackedEntityType;
    }

    public String getTrackedEntityInstance()
    {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance( String trackedEntityInstance )
    {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public String getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment( String enrollment )
    {
        this.enrollment = enrollment;
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

    public void writeSubm( SMSMetadata meta, SMSSubmissionWriter writer )
        throws Exception
    {
        writer.writeNewID( orgUnit );
        writer.writeNewID( trackerProgram );
        writer.writeNewID( trackedEntityType );
        writer.writeNewID( trackedEntityInstance );
        writer.writeNewID( enrollment );
        writer.writeDate( timestamp );
        writer.writeAttributeValues( values );
    }

    public void readSubm( SMSMetadata meta, SMSSubmissionReader reader )
        throws Exception
    {
        this.orgUnit = reader.readNewID();
        this.trackerProgram = reader.readNewID();
        this.trackedEntityType = reader.readNewID();
        this.trackedEntityInstance = reader.readNewID();
        this.enrollment = reader.readNewID();
        this.timestamp = reader.readDate();
        this.values = reader.readAttributeValues( meta );
    }

    public int getCurrentVersion()
    {
        return 1;
    }

    public SubmissionType getType()
    {
        return SMSConsts.SubmissionType.ENROLLMENT;
    }

}
