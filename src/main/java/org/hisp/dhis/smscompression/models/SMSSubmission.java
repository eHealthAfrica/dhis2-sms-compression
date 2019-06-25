package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;

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

import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public abstract class SMSSubmission
{
    protected SMSSubmissionHeader header;

    protected String userID;

    protected String orgUnit;

    public abstract int getCurrentVersion();

    public abstract SubmissionType getType();

    public abstract void writeSubm( SMSMetadata meta, SMSSubmissionWriter writer )
        throws Exception;

    public abstract void readSubm( SMSMetadata meta, SMSSubmissionReader reader )
        throws Exception;

    public SMSSubmission()
    {
        this.header = new SMSSubmissionHeader();
        header.setType( this.getType() );
        header.setVersion( this.getCurrentVersion() );
        // Initialise the submission ID so we know if it's been set correctly
        header.setSubmissionID( -1 );
    }

    public void setSubmissionID( int submissionID )
        throws Exception
    {
        if ( submissionID < 0 || submissionID > 255 )
        {
            throw new Exception( "submission ID must be in the range 0 <= submission_id <= 255" );
        }
        header.setSubmissionID( submissionID );
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID( String userID )
    {
        this.userID = userID;
    }

    public String getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = orgUnit;
    }

    public void validateSubmission()
        throws Exception
    {
        header.validateHeaer();
        // TODO: do better validation here
        if ( userID.isEmpty() || orgUnit.isEmpty() )
        {
            throw new Exception( "Ensure the UserID and OrgUnitID is set in the submission" );
        }
    }

    public void write( SMSMetadata meta, SMSSubmissionWriter writer )
        throws Exception
    {
        // Ensure we set the lastSyncDate in the subm header
        header.setLastSyncDate( meta.lastSyncDate );

        validateSubmission();
        header.writeHeader( writer );
        writer.writeNewID( userID );
        writer.writeNewID( orgUnit );
        writeSubm( meta, writer );
    }

    public void read( SMSMetadata meta, SMSSubmissionReader reader, SMSSubmissionHeader header )
        throws Exception
    {
        this.header = header;
        this.userID = reader.readNewID();
        this.orgUnit = reader.readNewID();
        readSubm( meta, reader );
    }
}