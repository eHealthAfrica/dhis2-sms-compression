package org.hisp.dhis.smscompression.models;

import java.util.Date;

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

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public abstract class SMSSubmission
{
    protected SMSSubmissionHeader header;

    protected UID userID;

    public abstract int getCurrentVersion();

    public abstract SubmissionType getType();

    public abstract void writeSubm( SMSSubmissionWriter writer )
        throws SMSCompressionException;

    public abstract void readSubm( SMSSubmissionReader reader, int version )
        throws SMSCompressionException;

    public SMSSubmission()
    {
        this.header = new SMSSubmissionHeader();
        header.setType( this.getType() );
        header.setVersion( this.getCurrentVersion() );
        // Initialise the submission ID so we know if it's been set correctly
        header.setSubmissionID( -1 );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        SMSSubmission subm = (SMSSubmission) o;
        return userID.equals( subm.userID ) && header.equals( subm.header );
    }

    public void setSubmissionID( int submissionID )
    {
        header.setSubmissionID( submissionID );
    }

    public UID getUserID()
    {
        return userID;
    }

    public void setUserID( String userID )
    {
        this.userID = new UID( userID, MetadataType.USER );
    }

    public void validateSubmission()
        throws SMSCompressionException
    {
        header.validateHeaer();
        if ( userID.uid.isEmpty() )
        {
            throw new SMSCompressionException( "Ensure the UserID is set in the submission" );
        }
        // TODO: We should run validations on each submission here
    }

    public void write( SMSMetadata meta, SMSSubmissionWriter writer )
        throws SMSCompressionException
    {
        // Ensure we set the lastSyncDate in the subm header
        Date lastSyncDate = meta != null && meta.lastSyncDate != null ? meta.lastSyncDate : new Date( 0 );
        header.setLastSyncDate( lastSyncDate );

        validateSubmission();
        header.writeHeader( writer );
        writer.writeID( userID );
        writeSubm( writer );
    }

    public void read( SMSSubmissionReader reader, SMSSubmissionHeader header )
        throws SMSCompressionException
    {
        this.header = header;
        this.userID = reader.readID( MetadataType.USER );
        readSubm( reader, this.header.getVersion() );
    }
}