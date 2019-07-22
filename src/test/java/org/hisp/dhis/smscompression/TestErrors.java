package org.hisp.dhis.smscompression;

import java.util.ArrayList;

import org.hisp.dhis.smscompression.SMSConsts.MetadataType;
import org.hisp.dhis.smscompression.models.UID;
import org.junit.Assert;
import org.junit.Test;

public class TestErrors
{

    @Test
    public void testPrintSMSResponse()
    {
        UID ouid = new UID( "ooooooooooo", MetadataType.ORGANISATION_UNIT );
        UID programid = new UID( "ppppppppppp", MetadataType.PROGRAM );
        String errorMsg = SMSResponse.OU_NOTIN_PROGRAM.set( ouid, programid ).toString();
        String expectedMsg = "302:ooooooooooo,ppppppppppp:Organisation unit [ooooooooooo] is not assigned to program [ppppppppppp]";
        Assert.assertEquals( expectedMsg, errorMsg );
    }

    @Test
    public void testPrintSMSResponseList()
    {
        UID ouid = new UID( "ooooooooooo", MetadataType.ORGANISATION_UNIT );
        UID programid = new UID( "ppppppppppp", MetadataType.PROGRAM );
        ArrayList<Object> uidList = new ArrayList<>();
        uidList.add( ouid );
        uidList.add( programid );
        String errorMsg = SMSResponse.OU_NOTIN_PROGRAM.setList( uidList ).toString();
        String expectedMsg = "302:ooooooooooo,ppppppppppp:Organisation unit [ooooooooooo] is not assigned to program [ppppppppppp]";
        Assert.assertEquals( expectedMsg, errorMsg );
    }

}
