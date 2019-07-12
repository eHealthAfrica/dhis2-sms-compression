package org.hisp.dhis.smscompression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSValue;
import org.hisp.dhis.smscompression.utils.BinaryUtils;
import org.hisp.dhis.smscompression.utils.BitOutputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class ValueWriter
{
    BitOutputStream outStream;

    SMSMetadata meta;

    boolean hashingEnabled;

    public ValueWriter( BitOutputStream outStream, SMSMetadata meta, boolean hashingEnabled )
    {
        this.outStream = outStream;
        this.meta = meta;
        this.hashingEnabled = meta == null ? false : hashingEnabled;
    }

    public int writeHashBitLen( MetadataType type )
        throws SMSCompressionException
    {
        if ( !hashingEnabled )
            return 0;

        int hashBitLen = IDUtil.getBitLengthForList( meta.getType( type ) );
        outStream.write( hashBitLen, SMSConsts.VARLEN_BITLEN );
        return hashBitLen;
    }

    public void writeValID( String id, int bitLen )
        throws SMSCompressionException
    {
        if ( !hashingEnabled )
            ValueUtil.writeString( id, outStream );

        int idHash = BinaryUtils.hash( id, bitLen );
        outStream.write( idHash, bitLen );
    }

    public void writeAttributeValues( List<SMSAttributeValue> values )
        throws SMSCompressionException
    {
        ValueUtil.writeBool( hashingEnabled, outStream );
        int attributeBitLen = writeHashBitLen( MetadataType.TRACKED_ENTITY_ATTRIBUTE );

        List<SMSValue<?>> smsVals = values.stream().map( v -> v.getSMSValue() ).collect( Collectors.toList() );
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        for ( Iterator<SMSAttributeValue> valIter = values.iterator(); valIter.hasNext(); )
        {
            SMSAttributeValue val = valIter.next();
            writeValID( val.getAttribute().uid, attributeBitLen );
            ValueUtil.writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

            int separator = valIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public Map<String, List<SMSDataValue>> groupDataValues( List<SMSDataValue> values )
    {
        HashMap<String, List<SMSDataValue>> map = new HashMap<>();
        for ( SMSDataValue val : values )
        {
            String catOptionCombo = val.getCategoryOptionCombo().uid;
            if ( !map.containsKey( catOptionCombo ) )
            {
                ArrayList<SMSDataValue> list = new ArrayList<>();
                map.put( catOptionCombo, list );
            }
            List<SMSDataValue> list = map.get( catOptionCombo );
            list.add( val );
        }
        return map;
    }

    public void writeDataValues( List<SMSDataValue> values )
        throws SMSCompressionException
    {
        ValueUtil.writeBool( hashingEnabled, outStream );
        int catOptionComboBitLen = writeHashBitLen( MetadataType.CATEGORY_OPTION_COMBO );
        int dataElementBitLen = writeHashBitLen( MetadataType.DATA_ELEMENT );

        List<SMSValue<?>> smsVals = values.stream().map( v -> v.getSMSValue() ).collect( Collectors.toList() );
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        Map<String, List<SMSDataValue>> valMap = groupDataValues( values );

        for ( Iterator<String> keyIter = valMap.keySet().iterator(); keyIter.hasNext(); )
        {
            String catOptionCombo = keyIter.next();
            writeValID( catOptionCombo, catOptionComboBitLen );
            List<SMSDataValue> vals = valMap.get( catOptionCombo );

            for ( Iterator<SMSDataValue> valIter = vals.iterator(); valIter.hasNext(); )
            {
                SMSDataValue val = valIter.next();
                writeValID( val.getDataElement().uid, dataElementBitLen );
                ValueUtil.writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

                int separator = valIter.hasNext() ? 1 : 0;
                outStream.write( separator, 1 );
            }

            int separator = keyIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }
}
