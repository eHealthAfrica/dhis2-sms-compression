package org.hisp.dhis.smscompression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSValue;
import org.hisp.dhis.smscompression.models.UID;
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
        this.hashingEnabled = hashingEnabled;
    }

    public int writeHashBitLen( MetadataType type, boolean useHashing )
        throws SMSCompressionException
    {
        if ( !useHashing )
            return 0;

        int hashBitLen = IDUtil.getBitLengthForList( meta.getType( type ) );
        outStream.write( hashBitLen, SMSConsts.VARLEN_BITLEN );
        return hashBitLen;
    }

    public void writeValID( UID uid, int bitLen, boolean useHashing )
        throws SMSCompressionException
    {
        if ( !useHashing )
        {
            ValueUtil.writeString( uid.uid, outStream );
            return;
        }

        // We have a non-empty list of UIDs in our metadata with hashing
        // enabled, we expect to find all UIDs in the metadata for this type
        if ( !meta.getType( uid.type ).contains( uid.uid ) )
            throw new SMSCompressionException(
                String.format( "Error hashing UID [%s] not found in [%s]", uid.uid, uid.type ) );

        int idHash = BinaryUtils.hash( uid.uid, bitLen );
        outStream.write( idHash, bitLen );
    }

    public void writeAttributeValues( List<SMSAttributeValue> values )
        throws SMSCompressionException
    {
        MetadataType attrType = MetadataType.TRACKED_ENTITY_ATTRIBUTE;
        boolean useHashing = hashingEnabled && meta != null && meta.getType( attrType ) != null
            && !meta.getType( attrType ).isEmpty();
        ValueUtil.writeBool( useHashing, outStream );
        int attributeBitLen = writeHashBitLen( attrType, useHashing );

        List<SMSValue<?>> smsVals = new ArrayList<>();
        for ( SMSAttributeValue val : values )
        {
            smsVals.add( val.getSMSValue() );
        }
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        for ( Iterator<SMSAttributeValue> valIter = values.iterator(); valIter.hasNext(); )
        {
            SMSAttributeValue val = valIter.next();
            writeValID( val.getAttribute(), attributeBitLen, useHashing );
            ValueUtil.writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

            int separator = valIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public Map<UID, List<SMSDataValue>> groupDataValues( List<SMSDataValue> values )
    {
        HashMap<UID, List<SMSDataValue>> map = new HashMap<>();
        for ( SMSDataValue val : values )
        {
            UID catOptionCombo = val.getCategoryOptionCombo();
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
        MetadataType cocType = MetadataType.CATEGORY_OPTION_COMBO;
        MetadataType deType = MetadataType.DATA_ELEMENT;
        boolean useHashing = hashingEnabled && meta != null && meta.getType( cocType ) != null
            && !meta.getType( cocType ).isEmpty() && meta.getType( deType ) != null
            && !meta.getType( deType ).isEmpty();

        ValueUtil.writeBool( useHashing, outStream );
        int catOptionComboBitLen = writeHashBitLen( cocType, useHashing );
        int dataElementBitLen = writeHashBitLen( deType, useHashing );

        List<SMSValue<?>> smsVals = new ArrayList<>();
        for ( SMSDataValue val : values )
        {
            smsVals.add( val.getSMSValue() );
        }
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SMSConsts.FIXED_INT_BITLEN );

        Map<UID, List<SMSDataValue>> valMap = groupDataValues( values );

        for ( Iterator<UID> keyIter = valMap.keySet().iterator(); keyIter.hasNext(); )
        {
            UID catOptionCombo = keyIter.next();
            writeValID( catOptionCombo, catOptionComboBitLen, useHashing );
            List<SMSDataValue> vals = valMap.get( catOptionCombo );

            for ( Iterator<SMSDataValue> valIter = vals.iterator(); valIter.hasNext(); )
            {
                SMSDataValue val = valIter.next();
                writeValID( val.getDataElement(), dataElementBitLen, useHashing );
                ValueUtil.writeSMSValue( val.getSMSValue(), fixedIntBitLen, outStream );

                int separator = valIter.hasNext() ? 1 : 0;
                outStream.write( separator, 1 );
            }

            int separator = keyIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }
}
