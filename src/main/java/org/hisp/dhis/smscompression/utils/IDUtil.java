package org.hisp.dhis.smscompression.utils;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.smscompression.SMSCompressionException;
import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.models.SMSMetadata;

public class IDUtil
{

    public static int getBitLengthForList( List<String> ids, MetadataType type )
        throws SMSCompressionException
    {
        String typeMsg = String.format( "Error Hashing MetadataType: %s. ", type );
        checkIDList( ids, typeMsg );

        // Start with the shortest length that will fit all IDs
        int len = BinaryUtils.log2( ids.size() );

        // Track whether we have a hash collision (duplicate) for this length
        boolean collision = false;
        do
        {
            collision = false;
            ArrayList<Integer> idList = new ArrayList<>();
            for ( String id : ids )
            {
                int newHash = BinaryUtils.hash( id, len );
                if ( idList.contains( newHash ) )
                {
                    len++;
                    collision = true;
                    break;
                }
                else
                {
                    idList.add( newHash );
                }
            }
            // This is the max bit length we can support if we still
            // have a collision we can't support this UID list
            if ( len > (Math.pow( 2, SMSConsts.VARLEN_BITLEN )) )
                throw new SMSCompressionException( typeMsg + "Group too large to support" );
        }
        while ( collision );
        return len;
    }

    public static boolean checkIDList( List<String> ids, String typeMsg )
        throws SMSCompressionException
    {
        if ( ids.isEmpty() )
            throw new SMSCompressionException( typeMsg + "Empty list given" );

        HashSet<String> set = new HashSet<>();
        for ( String id : ids )
        {
            if ( !set.add( id ) )
                throw new SMSCompressionException( typeMsg + "List of UIDs in Metadata contains duplicate: " + id );
            if ( !validID( id ) )
                throw new SMSCompressionException( typeMsg + "Invalid format UID found in Metadata UID List: " + id );
        }

        return true;
    }

    public static boolean validID( String id )
    {
        return id.matches( "^[A-z0-9]{" + SMSConsts.ID_LEN + "}$" );
    }

    public static int convertIDCharToInt( char c )
    {
        int i = c;

        if ( c >= '0' && c <= '9' )
        {
            i -= '0';
        }
        else if ( c >= 'A' && c <= 'Z' )
        {
            i -= '0' + ('A' - '9' - 1);
        }
        else if ( c >= 'a' && c <= 'z' )
        {
            i -= '0' + ('A' - '9' - 1) + ('a' - 'Z' - 1);
        }

        return i;
    }

    public static char convertIDIntToChar( int i )
    {
        char c = (char) i;

        c += '0';
        if ( c >= '0' && c <= '9' )
            return c;

        c += ('A' - '9' - 1);
        if ( c >= 'A' && c <= 'Z' )
            return c;

        c += ('a' - 'Z' - 1);
        if ( c >= 'a' && c <= 'z' )
            return c;

        return 0;
    }

    // Used to write new IDs where we have to express the full ID.
    // Must only include chars in the range a-Z0-9, as we encode each
    // char to 6 bits (64 vals)
    public static void writeNewID( String id, BitOutputStream outStream )
        throws SMSCompressionException
    {
        if ( !validID( id ) )
            throw new SMSCompressionException( "Attempting to write out ID with invalid format: " + id );
        for ( char c : id.toCharArray() )
        {
            outStream.write( convertIDCharToInt( c ), 6 );
        }
    }

    // Used to read new IDs where we have to express the full ID.
    // Must only include chars in the range a-Z0-9, as we encode each
    // char to 6 bits (64 vals)
    public static String readNewID( BitInputStream inStream )
        throws SMSCompressionException
    {
        String id = "";
        while ( id.length() < SMSConsts.ID_LEN )
        {
            int i = inStream.read( 6 );
            id += convertIDIntToChar( i );
        }
        return id;
    }

    public static String readID( MetadataType type, SMSMetadata meta, BitInputStream inStream )
        throws SMSCompressionException
    {
        int typeBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
        Map<Integer, String> idLookup = IDUtil.getIDLookup( meta.getType( type ), typeBitLen );
        int idHash = inStream.read( typeBitLen );
        return idLookup.get( idHash );
    }

    public static Map<Integer, String> getIDLookup( List<String> idList, int hashLen )
    {
        HashMap<Integer, String> idLookup = new HashMap<>();
        for ( String id : idList )
        {
            int hash = BinaryUtils.hash( id, hashLen );
            idLookup.put( hash, id );
        }
        return idLookup;
    }

    public static void writeID( String id, MetadataType type, SMSMetadata meta, BitOutputStream outStream )
        throws SMSCompressionException
    {
        if ( !validID( id ) )
            throw new SMSCompressionException( "Attempting to write out ID with invalid format: " + id );

        // TODO: Check id is in metadata list

        int typeBitLen = IDUtil.getBitLengthForList( meta.getType( type ), type );
        outStream.write( typeBitLen, SMSConsts.VARLEN_BITLEN );
        int idHash = BinaryUtils.hash( id, typeBitLen );
        outStream.write( idHash, typeBitLen );
    }
}
