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

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public class RelationshipSMSSubmission
    extends
    SMSSubmission
{
    protected UID relationshipType;

    protected UID relationship;

    protected UID from;

    protected UID to;

    /* Getters and Setters */

    public UID getRelationshipType()
    {
        return relationshipType;
    }

    public void setRelationshipType( String relationshipType )
    {
        this.relationshipType = new UID( relationshipType, MetadataType.RELATIONSHIP_TYPE );
    }

    public UID getRelationship()
    {
        return relationship;
    }

    public void setRelationship( String relationship )
    {
        this.relationship = new UID( relationship, MetadataType.RELATIONSHIP );
    }

    public UID getFrom()
    {
        return from;
    }

    public void setFrom( String from )
    {
        this.from = new UID( from, null );
    }

    public UID getTo()
    {
        return to;
    }

    public void setTo( String to )
    {
        this.to = new UID( to, null );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        RelationshipSMSSubmission subm = (RelationshipSMSSubmission) o;

        return relationshipType.equals( subm.relationshipType ) && relationship.equals( subm.relationship )
            && from.equals( subm.from ) && to.equals( subm.to );
    }

    /* Implementation of abstract methods */

    @Override
    public void writeSubm( SMSSubmissionWriter writer )
        throws SMSCompressionException
    {
        writer.writeID( relationshipType );
        writer.writeID( relationship );
        writer.writeNewID( from.uid );
        writer.writeNewID( to.uid );
    }

    @Override
    public void readSubm( SMSSubmissionReader reader, int version )
        throws SMSCompressionException
    {
        this.relationshipType = reader.readID( MetadataType.RELATIONSHIP_TYPE );
        this.relationship = reader.readID( MetadataType.RELATIONSHIP );
        this.from = new UID( reader.readNewID(), null );
        this.to = new UID( reader.readNewID(), null );
    }

    @Override
    public int getCurrentVersion()
    {
        return 1;
    }

    @Override
    public SubmissionType getType()
    {
        return SMSConsts.SubmissionType.RELATIONSHIP;
    }
}
