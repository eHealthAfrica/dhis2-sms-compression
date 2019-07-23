package org.hisp.dhis.smscompression.models;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.utils.IDUtil;

public class UID
{
    public String uid;

    public int hash;

    public MetadataType type;

    public UID( String uid, MetadataType type )
    {
        this.uid = uid;
        this.type = type;
    }

    public UID( String uid, int hash, MetadataType type )
    {
        this.uid = uid;
        this.hash = hash;
        this.type = type;
    }

    public String getHash()
    {
        return ("#" + IDUtil.hashAsBase64( this ));
    }

    @Override
    public String toString()
    {
        if ( uid == null )
            return getHash();
        return uid;
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
        UID u = (UID) o;
        return uid.equals( u.uid ) && (type == u.type);
    }

    @Override
    public int hashCode()
    {
        return uid.hashCode();
    }

}
