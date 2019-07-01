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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSMetadata
{
    public static class ID
    {
        String id;

        public ID( String id )
        {
            this.id = id;
        }
    }

    public Date lastSyncDate;

    public List<ID> users;

    public List<ID> trackedEntityTypes;

    public List<ID> trackedEntityAttributes;

    public List<ID> programs;

    public List<ID> organisationUnits;

    public List<ID> dataElements;

    public List<ID> categoryOptionCombos;

    public List<ID> dataSets;

    public List<ID> programStages;

    public List<String> getType( MetadataType type )
    {
        switch ( type )
        {
        case USER:
            return getIDs( users );
        case TRACKED_ENTITY_TYPE:
            return getIDs( trackedEntityTypes );
        case TRACKED_ENTITY_ATTRIBUTE:
            return getIDs( trackedEntityAttributes );
        case PROGRAM:
            return getIDs( programs );
        case ORGANISATION_UNIT:
            return getIDs( organisationUnits );
        case DATA_ELEMENT:
            return getIDs( dataElements );
        case CATEGORY_OPTION_COMBO:
            return getIDs( categoryOptionCombos );
        case DATASET:
            return getIDs( dataSets );
        case PROGRAM_STAGE:
            return getIDs( programStages );
        default:
            return null;
        }
    }

    private List<String> getIDs( List<ID> ids )
    {
        if ( ids == null )
            return null;
        ArrayList<String> idList = new ArrayList<>();
        for ( ID id : ids )
        {
            idList.add( id.id );
        }
        return idList;
    }
}
