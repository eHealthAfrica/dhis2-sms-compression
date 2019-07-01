package org.hisp.dhis.smscompression;

public class SMSCompressionException
    extends
    Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SMSCompressionException( String message )
    {
        super( message );
    }

    public SMSCompressionException( String message, Throwable error )
    {
        super( message, error );
    }

    public SMSCompressionException( Throwable error )
    {
        super( error );
    }
}
