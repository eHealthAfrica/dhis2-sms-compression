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

public class SMSConsts
{
    public static int VARLEN_BITLEN = 6;

    public static int CHAR_BITLEN = 8;

    public static int EPOCH_DATE_BITLEN = 32;

    public static int SUBM_TYPE_BITLEN = 4;

    public static int VERSION_BITLEN = 4;

    public static int CRC_BITLEN = 8;

    public static int SUBM_ID_BITLEN = 8;

    public enum SubmissionType
    {
        AGGREGATE_DATASET, DELETE, ENROLLMENT, RELATIONSHIP, SIMPLE_EVENT, TRACKER_EVENT,

        ;

    }

    public enum MetadataType
    {
        USER,
        TRACKED_ENTITY_TYPE,
        TRACKED_ENTITY_ATTRIBUTE,
        PROGRAM,
        ORGANISATION_UNIT,
        DATA_ELEMENT,
        CATEGORY_OPTION_COMBO,
        DATASET,
        PROGRAM_STAGE,

        ;
    }
}
