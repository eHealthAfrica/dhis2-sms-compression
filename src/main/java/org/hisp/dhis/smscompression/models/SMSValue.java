package org.hisp.dhis.smscompression.models;

import java.text.ParseException;
import java.util.Date;

import org.hisp.dhis.smscompression.SMSConsts;
import org.hisp.dhis.smscompression.SMSConsts.ValueType;

public class SMSValue<T>
{
    T value;

    ValueType type;

    public SMSValue( T value, ValueType type )
    {
        this.value = value;
        this.type = type;
    }

    public T getValue()
    {
        return this.value;

    }

    public ValueType getType()
    {
        return this.type;
    }

    public static SMSValue<?> asSMSValue( String value )
    {
        // BOOL
        if ( value.equals( "true" ) || value.equals( "false" ) )
        {
            Boolean valBool = value.equals( "true" ) ? true : false;
            return new SMSValue<Boolean>( valBool, ValueType.BOOL );
        }

        // DATE
        try
        {
            Date valDate = SMSConsts.SIMPLE_DATE_FORMAT.parse( value );
            return new SMSValue<Date>( valDate, ValueType.DATE );
        }
        catch ( ParseException e )
        {
            // not a date
        }

        // INT
        try
        {
            Integer valInt = Integer.parseInt( value );
            return new SMSValue<Integer>( valInt, ValueType.INT );
        }
        catch ( NumberFormatException e )
        {
            // not an integer
        }

        // FLOAT
        try
        {
            Float valFloat = Float.parseFloat( value );
            return new SMSValue<Float>( valFloat, ValueType.FLOAT );
        }
        catch ( NumberFormatException e )
        {
            // not a float
        }

        // If all else fails we can use String
        return new SMSValue<String>( value, ValueType.STRING );
    }
}
