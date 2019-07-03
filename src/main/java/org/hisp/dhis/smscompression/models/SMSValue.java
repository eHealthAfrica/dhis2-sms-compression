package org.hisp.dhis.smscompression.models;

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
}
