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
import org.hisp.dhis.smscompression.SMSConsts.SMSEventStatus;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public class SimpleEventSMSSubmission
    extends
    SMSSubmission
{
    protected UID orgUnit;

    protected UID eventProgram;

    protected SMSEventStatus eventStatus;

    protected UID attributeOptionCombo;

    protected UID event;

    protected Date timestamp;

    protected List<SMSDataValue> values;

    /* Getters and Setters */

    public UID getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = new UID( orgUnit, MetadataType.ORGANISATION_UNIT );
    }

    public UID getEventProgram()
    {
        return eventProgram;
    }

    public void setEventProgram( String eventProgram )
    {
        this.eventProgram = new UID( eventProgram, MetadataType.PROGRAM );
    }

    public SMSEventStatus getEventStatus()
    {
        return eventStatus;
    }

    public void setEventStatus( SMSEventStatus eventStatus )
    {
        this.eventStatus = eventStatus;
    }

    public UID getAttributeOptionCombo()
    {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        this.attributeOptionCombo = new UID( attributeOptionCombo, MetadataType.CATEGORY_OPTION_COMBO );
    }

    public UID getEvent()
    {
        return event;
    }

    public void setEvent( String event )
    {
        this.event = new UID( event, MetadataType.EVENT );
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public List<SMSDataValue> getValues()
    {
        return values;
    }

    public void setValues( List<SMSDataValue> values )
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
        SimpleEventSMSSubmission subm = (SimpleEventSMSSubmission) o;

        return orgUnit.equals( subm.orgUnit ) && eventProgram.equals( subm.eventProgram )
            && eventStatus == subm.eventStatus && attributeOptionCombo.equals( subm.attributeOptionCombo )
            && event.equals( subm.event ) && timestamp.equals( subm.timestamp ) && values.equals( subm.values );
    }

    /* Implementation of abstract methods */

    @Override
    public void writeSubm( SMSSubmissionWriter writer )
        throws SMSCompressionException
    {
        writer.writeID( orgUnit );
        writer.writeID( eventProgram );
        writer.writeEventStatus( eventStatus );
        writer.writeID( attributeOptionCombo );
        writer.writeID( event );
        writer.writeDate( timestamp );
        writer.writeDataValues( values );
    }

    @Override
    public void readSubm( SMSSubmissionReader reader, int version )
        throws SMSCompressionException
    {
        this.orgUnit = reader.readID( MetadataType.ORGANISATION_UNIT );
        this.eventProgram = reader.readID( MetadataType.PROGRAM );
        this.eventStatus = reader.readEventStatus();
        this.attributeOptionCombo = reader.readID( MetadataType.CATEGORY_OPTION_COMBO );
        this.event = reader.readID( MetadataType.EVENT );
        this.timestamp = reader.readDate();
        this.values = reader.readDataValues();
    }

    @Override
    public int getCurrentVersion()
    {
        return 1;
    }

    @Override
    public SubmissionType getType()
    {
        return SMSConsts.SubmissionType.SIMPLE_EVENT;
    }
}
