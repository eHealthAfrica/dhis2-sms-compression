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

import java.util.List;

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.SubmissionType;
import org.hisp.dhis.smscompression.SMSSubmissionReader;
import org.hisp.dhis.smscompression.SMSSubmissionWriter;

public class AggregateDatasetSMSSubmission
    extends
    SMSSubmission
{
    protected String orgUnit;

    protected String dataSet;

    protected boolean complete;

    protected String attributeOptionCombo;

    protected String period;

    protected List<SMSDataValue> values;

    /* Getters and Setters */

    public String getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = orgUnit;
    }

    public String getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( String dataSet )
    {
        this.dataSet = dataSet;
    }

    public boolean isComplete()
    {
        return complete;
    }

    public void setComplete( boolean complete )
    {
        this.complete = complete;
    }

    public String getAttributeOptionCombo()
    {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        this.attributeOptionCombo = attributeOptionCombo;
    }

    public String getPeriod()
    {
        return period;
    }

    public void setPeriod( String period )
    {
        this.period = period;
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
        AggregateDatasetSMSSubmission subm = (AggregateDatasetSMSSubmission) o;

        values.equals( subm.values );

        return orgUnit.equals( subm.orgUnit ) && dataSet.equals( subm.dataSet ) && complete == subm.complete
            && attributeOptionCombo.equals( subm.attributeOptionCombo ) && period.equals( subm.period )
            && values.equals( subm.values );
    }

    /* Implementation of abstract methods */

    public void writeSubm( SMSMetadata meta, SMSSubmissionWriter writer )
        throws Exception
    {
        writer.writeID( orgUnit, MetadataType.ORGANISATION_UNIT );
        writer.writeID( dataSet, MetadataType.DATASET );
        writer.writeBool( complete );
        writer.writeID( attributeOptionCombo, MetadataType.CATEGORY_OPTION_COMBO );
        writer.writePeriod( period );
        writer.writeDataValues( values );
    }

    public void readSubm( SMSMetadata meta, SMSSubmissionReader reader )
        throws Exception
    {
        this.orgUnit = reader.readID( MetadataType.ORGANISATION_UNIT, meta );
        this.dataSet = reader.readID( MetadataType.DATASET, meta );
        this.complete = reader.readBool();
        this.attributeOptionCombo = reader.readID( MetadataType.CATEGORY_OPTION_COMBO, meta );
        this.period = reader.readPeriod();
        this.values = reader.readDataValues( meta );
    }

    public int getCurrentVersion()
    {
        return 1;
    }

    public SubmissionType getType()
    {
        return SMSConsts.SubmissionType.AGGREGATE_DATASET;
    }
}
