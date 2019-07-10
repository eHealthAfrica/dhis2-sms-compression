package org.hisp.dhis.smscompression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.models.SMSAttributeValue;
import org.hisp.dhis.smscompression.models.SMSDataValue;
import org.hisp.dhis.smscompression.models.SMSMetadata;
import org.hisp.dhis.smscompression.models.SMSValue;
import org.hisp.dhis.smscompression.models.UID;
import org.hisp.dhis.smscompression.utils.BitInputStream;
import org.hisp.dhis.smscompression.utils.IDUtil;
import org.hisp.dhis.smscompression.utils.ValueUtil;

public class ValueReader
{
    BitInputStream inStream;

    SMSMetadata meta;

    boolean hashingEnabled;

    public ValueReader( BitInputStream inStream, SMSMetadata meta )
    {
        this.inStream = inStream;
        this.meta = meta;
    }

    public UID readValID( int bitLen, Map<Integer, String> idLookup, MetadataType type )
        throws SMSCompressionException
    {
        if ( !hashingEnabled )
            return new UID( ValueUtil.readString( inStream ), type );

        String id;
        int idHash = inStream.read( bitLen );
        id = idLookup.get( idHash );
        return new UID( id, idHash, type );
    }

    public List<SMSAttributeValue> readAttributeValues()
        throws SMSCompressionException
    {
        ArrayList<SMSAttributeValue> values = new ArrayList<>();
        MetadataType type = MetadataType.TRACKED_ENTITY_ATTRIBUTE;
        int attributeBitLen = 0;
        Map<Integer, String> idLookup = null;

        this.hashingEnabled = ValueUtil.readBool( inStream );
        if ( hashingEnabled )
        {
            attributeBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
            idLookup = IDUtil.getIDLookup( meta.getType( type ), attributeBitLen );
        }
        int fixedIntBitLen = inStream.read( SMSConsts.FIXED_INT_BITLEN ) + 1;

        for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
        {
            UID id = readValID( attributeBitLen, idLookup, type );
            SMSValue<?> smsValue = ValueUtil.readSMSValue( fixedIntBitLen, inStream );
            values.add( new SMSAttributeValue( id, smsValue ) );
        }

        return values;
    }

    public List<SMSDataValue> readDataValues()
        throws SMSCompressionException
    {
        int catOptionComboBitLen = 0;
        int dataElementBitLen = 0;
        Map<Integer, String> cocIDLookup = null;
        Map<Integer, String> deIDLookup = null;
        MetadataType cocType = MetadataType.CATEGORY_OPTION_COMBO;
        MetadataType deType = MetadataType.DATA_ELEMENT;

        this.hashingEnabled = ValueUtil.readBool( inStream );
        if ( hashingEnabled )
        {
            catOptionComboBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
            cocIDLookup = IDUtil.getIDLookup( meta.getType( cocType ), catOptionComboBitLen );

            dataElementBitLen = inStream.read( SMSConsts.VARLEN_BITLEN );
            deIDLookup = IDUtil.getIDLookup( meta.getType( deType ), dataElementBitLen );
        }
        int fixedIntBitLen = inStream.read( SMSConsts.FIXED_INT_BITLEN ) + 1;
        ArrayList<SMSDataValue> values = new ArrayList<>();

        for ( int cocSep = 1; cocSep == 1; cocSep = inStream.read( 1 ) )
        {
            UID catOptionCombo = readValID( catOptionComboBitLen, cocIDLookup, cocType );

            for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
            {
                UID dataElement = readValID( dataElementBitLen, deIDLookup, deType );
                SMSValue<?> smsValue = ValueUtil.readSMSValue( fixedIntBitLen, inStream );
                values.add( new SMSDataValue( catOptionCombo, dataElement, smsValue ) );
            }
        }

        return values;
    }
}
