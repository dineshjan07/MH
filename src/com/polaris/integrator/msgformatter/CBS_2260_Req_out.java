package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBS_2260_Req_out
    extends UserRule
{
    private static Logger log = LoggerFactory.getLogger( CBS_2260_Req_out.class );

    public CBS_2260_Req_out()
    {
    }

    @Override
    public int Process( HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,
                        ErrorHandler erHandler )
    {
        _cresultMessage = new String[1];

        if ( pIPMsg == null )
        {
            _iresultCode = 1;
            _cresultMessage[0] = "Data not found in InputHash ";
            return _iresultCode;
        }
        else
        {
            try
            {
                log.debug( "Input to UNB_0800_Req_out ISO8583 -0800- message--->>>" + pIPMsg );

                String[] hexTemp = new String[16];
                String bitmap = "";
                String mti = pIPMsg.substring( 0, 4 );
                log.debug( "mti" + mti );
                String messageTypeHex = convertStringToHex( mti );
                String psHex = pIPMsg.substring( 4, 36 );
                log.debug( "psHex" + psHex );
                String actualHex = psHex;
                for ( int i = 0; i < 16; i++ )
                {
                    for ( int j = 0; j < 2; )
                    {
                        hexTemp[i] = psHex.substring( j, j + 2 );
                        j = j + 2;
                        psHex = psHex.substring( j, psHex.length() );
                    }
                    if ( hexTemp[i].startsWith( "0" ) )
                    {
                        hexTemp[i] = hexTemp[i].substring( 1 );
                    }
                    bitmap = bitmap.concat( (String) hexTemp[i] );
                }
                String data = pIPMsg.substring( 36, pIPMsg.length() );
                log.debug( "data" + data );
                String datahex = convertStringToHex( data );
                log.debug( "datahex" + datahex );
                String isotempmessage = mti.concat( bitmap ).concat( data );
                
                log.debug("mti length  : "+mti.length());
                log.debug("bitmap length  : "+bitmap.length());
                log.debug("data length  : "+data.length());
                log.debug( "isotempmessage" + isotempmessage );
                int i = isotempmessage.length();
                
                log.debug( "isotempmessage  length" + i );
                String isoHexStr = messageTypeHex.concat( actualHex ).concat( datahex );
                //String hex = Integer.toHexString( i );
              //  log.debug( "ISOhex" + hex );
                
                String length=Integer.toString(i);
                
                String isoaciiStr = convertHexToString( isoHexStr ) ;
              
              
            	   isoaciiStr="0".concat(length).concat(isoaciiStr);
              
                log.debug( "ISO 8583 -0800 Hexadecimal String :" +( isoHexStr ) );
                log.debug( "ISO ASCII String :" + isoaciiStr );
                log.debug( "ISO ASCII String length:" + isoaciiStr.length() );
                log.debug( "Output from UNB_0800_Req_out ISO8583 -0800- message--->>>" + isoaciiStr );
                _iresultCode = 0;
                _cresultMessage[0] = isoaciiStr;
                return _iresultCode;
            }
            catch ( Exception e )
            {
                _iresultCode = 2;
                _cresultMessage[0] = "Exception in UNB_0800_Req_out" + e;
                return _iresultCode;
            }
        }
    }

    public String convertStringToHex( String str )
    {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for ( int i = 0; i < chars.length; i++ )
        {
            hex.append( Integer.toHexString( (int) chars[i] ) );
        }

        return hex.toString();
    }

    public String convertHexToString( String hex )
    {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for ( int i = 0; i < hex.length() - 1; i += 2 )
        {
            String output = hex.substring( i, ( i + 2 ) );
            int decimal = Integer.parseInt( output, 16 );
            sb.append( (char) decimal );
            temp.append( decimal );
        }
        System.out.println( " iso Decimal String : " + temp.toString() );

        return sb.toString();
    }
}
