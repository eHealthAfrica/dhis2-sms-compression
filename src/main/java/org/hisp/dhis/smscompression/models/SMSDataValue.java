package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;

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

public class SMSDataValue
{
    protected UID categoryOptionCombo;

    protected UID dataElement;

    protected String value;

    protected SMSValue<?> smsValue;

    public SMSDataValue( String categoryOptionCombo, String dataElement, String value )
    {
        this.categoryOptionCombo = new UID( categoryOptionCombo, MetadataType.CATEGORY_OPTION_COMBO );
        this.dataElement = new UID( dataElement, MetadataType.DATA_ELEMENT );
        this.value = value;
        this.smsValue = SMSValue.asSMSValue( value );
    }

    public SMSDataValue( UID categoryOptionCombo, UID dataElement, SMSValue<?> smsValue )
    {
        this.categoryOptionCombo = categoryOptionCombo;
        this.dataElement = dataElement;
        this.smsValue = smsValue;
        // TODO: We probably need better handling than just toString() here
        this.value = smsValue.getValue().toString();
    }

    public UID getCategoryOptionCombo()
    {
        return categoryOptionCombo;
    }

    public UID getDataElement()
    {
        return dataElement;
    }

    public String getValue()
    {
        return value;
    }

    public SMSValue<?> getSMSValue()
    {
        return smsValue;
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
        SMSDataValue dv = (SMSDataValue) o;

        return categoryOptionCombo.equals( dv.categoryOptionCombo ) && dataElement.equals( dv.dataElement )
            && value.equals( dv.value );
    }

    @Override
    public int hashCode()
    {
        return 0;
    }
}
