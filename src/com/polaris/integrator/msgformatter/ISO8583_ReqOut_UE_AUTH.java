package com.polaris.integrator.msgformatter;

import java.io.PrintStream;
import java.util.HashMap;
import com.polaris.integrator.msgformatter.LogicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ISO8583_ReqOut_UE_AUTH
    extends UserRule
{
    private static Logger log = LoggerFactory.getLogger( ISO8583_ReqOut_UE_AUTH.class );

    public int Process( HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,
                        ErrorHandler erHandler )
    {
        this._cresultMessage = new String[1];

        if ( pIPMsg == null )
        {
            this._iresultCode = 1;
            this._cresultMessage[0] = "Data not found in InputHash ";
            return this._iresultCode;
        }

        try
        {

            log.debug( "Input to ISO8583_ReqOut_UE_AUTH --->>>" + pIPMsg );
            
    
            String Input = pIPMsg;
            log.debug("NVP Request...."+Input);
            Input = converNVPtoISOMsg( Input, erHandler );
            log.debug("ISO Request...."+Input);

         //   pIPMsg = converNVPtoISOMsg( pIPMsg, erHandler );

            if ( pIPMsg == null )
            {
                _iresultCode = 2;
                _cresultMessage[0] = "Exception caught in OiNVISOPFormatter.Parse";
                return _iresultCode;
            }

            String[] hexTemp = new String[16];
            String bitmap = "";
            String mti = pIPMsg.substring( 0, 4 );
            log.debug( "mti" + mti );
            String messageTypeHex = convertStringToHex( mti );
            String psHex = pIPMsg.substring( 4, 36 );
            log.debug( "psHex" + psHex );
            String actualHex = psHex;
            for ( int i = 0; i < 16; ++i )
            {
                for ( int j = 0; j < 2; )
                {
                    hexTemp[i] = psHex.substring( j, j + 2 );
                    j += 2;
                    psHex = psHex.substring( j, psHex.length() );
                }
                if ( hexTemp[i].startsWith( "0" ) )
                {
                    hexTemp[i] = hexTemp[i].substring( 1 );
                }
                bitmap = bitmap.concat( hexTemp[i] );
            }
            String data = pIPMsg.substring( 36, pIPMsg.length() );
            log.debug( "data" + data );
            String datahex = convertStringToHex( data );
            log.debug( "datahex" + datahex );
            String isotempmessage = mti.concat( bitmap ).concat( data );

            log.debug( "mti length  : " + mti.length() );
            log.debug( "bitmap length  : " + bitmap.length() );
            log.debug( "data length  : " + data.length() );
            log.debug( "isotempmessage" + isotempmessage );
            int i = isotempmessage.length();

            log.debug( "isotempmessage  length" + i );
            String isoHexStr = messageTypeHex.concat( actualHex ).concat( datahex );

            String length = Integer.toString( i );
            log.debug( "isoHexStr        " + isoHexStr );
            String isoaciiStr = convertHexToString( isoHexStr );
            log.debug( "isoaciiStr        " + isoaciiStr );
            if ( i > 99 )
            {
                isoaciiStr = "0".concat( length ).concat( isoaciiStr );
            }
            else if ( i < 99 )
            {
                isoaciiStr = "00".concat( length ).concat( isoaciiStr );
            }
            log.debug( "ISO 8583 -0800 Hexadecimal String :" + isoHexStr );
            log.debug( "ISO ASCII String :" + isoaciiStr );
            log.debug( "ISO ASCII String length:" + isoaciiStr.length() );
            log.debug( "Output from UNB_0800_Req_out ISO8583 -0800- message--->>>" + isoaciiStr );
            this._iresultCode = 0;
            this._cresultMessage[0] = isoaciiStr;
            return this._iresultCode;
        }
        catch ( Exception e )
        {
            this._iresultCode = 2;
            this._cresultMessage[0] = "Exception in UNB_0800_Req_out" + e;
        }
        return this._iresultCode;
    }

    public String convertStringToHex( String str )
    {
        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for ( int i = 0; i < chars.length; ++i )
        {
            hex.append( Integer.toHexString( chars[i] ) );
        }

        return hex.toString();
    }

    public String convertHexToString( String hex )
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for ( int i = 0; i < hex.length() - 1; i += 2 )
        {
            String output = hex.substring( i, i + 2 );
            int decimal = Integer.parseInt( output, 16 );
            sb.append( (char) decimal );
            temp.append( decimal );
        }
        System.out.println( " iso Decimal String : " + temp.toString() );

        return sb.toString();
    }

    private String converNVPtoISOMsg( String input, ErrorHandler erHandler )
        throws Exception
    {
        String isoType = "ISO8583";
        OiAbsFmtrInterface pObject = null;
        HashMap isoHM = new HashMap();
        HashMap iphash = null;
        String output = null;
        int bResult = 1;

        LogicInfo logicInfo = new LogicInfo();
        logicInfo.SetFieldDelimiter( "=" );

        isoType = isoType.substring( isoType.indexOf( "ISO" ) + 3, isoType.length() );
        isoHM.put( "isoType", isoType );

        pObject = new OiNVPISOFormatter( erHandler, logicInfo );
        bResult = pObject.Parse( null, input, isoHM );
        if ( bResult != ErrorHandler.ERROR_STATUS_OK )
        {
            throw new Exception( "Exception caught in OiNVISOPFormatter.Parse" );
        }
        iphash = pObject.GetParsedInMsg();

        if ( iphash.containsKey( "ISOConvrtMsg" ) )
        {
            output = (String) iphash.get( "ISOConvrtMsg" );
            log.debug( "ISO MESSAGE:" + output );
        }
        else
        {
            log.error( "Converted message hashmap is null" );
            throw new Exception( "Exception caught in OiNVISOPFormatter.Parse" );
        }
        return output;

    }

    private String convertISOtoNVPMsg( String input, ErrorHandler erHandler )
        throws Exception
    {
        String isoType = "ISO8583";
        OiAbsFmtrInterface pObject = null;
        HashMap isoHM = new HashMap();
        HashMap iphash = null;
        String output = null;
        int bResult = 1;

        isoType = isoType.substring( isoType.indexOf( "ISO" ) + 3, isoType.length() );
        isoHM.put( "isoType", isoType );

        pObject = new OiISONVPFormatter( erHandler, null );
        bResult = pObject.Parse( null, input, isoHM );

        if ( bResult != ErrorHandler.ERROR_STATUS_OK )
        {
            throw new Exception( "Exception caught in OiISONVPFormatter.Parse" );
        }

        iphash = pObject.GetParsedInMsg();

        if ( iphash.containsKey( "ISOConvrtMsg" ) )
        {
            output = (String) iphash.get( "ISOConvrtMsg" );
        }
        else
        {
            log.error( "Converted message hashmap is null" );
            throw new Exception( "Exception caught in OiISONVPFormatter.Parse" );
        }
        return output;

    }
}