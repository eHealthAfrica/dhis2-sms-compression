package org.hisp.dhis.smscompression.models;

public class UID
{
    public String uid;

    int hash;

    public UID( String uid )
    {
        this.uid = uid;
    }

    public UID( String uid, int hash )
    {
        this.uid = uid;
        this.hash = hash;
    }

    public String getHash()
    {
        return String.format( "#%d", hash );
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
        return uid.equals( u.uid );
    }
}
