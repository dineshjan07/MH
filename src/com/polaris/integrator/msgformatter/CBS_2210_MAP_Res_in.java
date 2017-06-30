package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBS_2210_MAP_Res_in
    extends UserRule
{
    private static Logger log = LoggerFactory.getLogger( CBS_2210_MAP_Res_in.class );

    public CBS_2210_MAP_Res_in()
    {

    }

    public int Process( HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,
                        ErrorHandler erHandler )
    {
        _cresultMessage = new String[1];
        super._bypassMF = false;
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
            String	rCode = "res_Result_Code=00000~*";
    			String sCap = "res_No_Of_Seg=~*";

                log.debug( "Input to CBS_2210_MAP_Res_in ISO8583 -0810- message--->>>" + pIPMsg );

                String resHex = convertStringToHex( pIPMsg );
                log.debug( "Response Hexadecimal ....>>" + resHex );
                // String lenHex = resHex.substring( 0, 4 );
                String mtiHex = resHex.substring( 8, 16 );
                String mti = convertHexToString( mtiHex );
                String bitmapHexOriginal = resHex.substring( 16, 48 );
                String bitmapHex = null;
                String[] hexTemp = new String[16];
                String bitmap = "";
                for ( int i = 0; i < 16; i++ )
                {
                    for ( int j = 0; j < 2; )
                    {
                        hexTemp[i] = bitmapHexOriginal.substring( j, j + 2 );
                        j = j + 2;
                        bitmapHex = bitmapHexOriginal.substring( j, bitmapHexOriginal.length() );
                    }
                    if ( hexTemp[i].startsWith( "0" ) )
                    {
                        hexTemp[i] = hexTemp[i].substring( 1 );
                    }
                    bitmap = bitmap.concat( (String) hexTemp[i] );
                }

                String dataHex = resHex.substring( 48, resHex.length() );
                String data = convertHexToString( dataHex );

                String iso = mti + bitmapHexOriginal + data;

                //iso=iso.substring(2, iso.length());
                log.debug( "ISO 8583 - 0810 message sent to Integartor ....>" + iso );
                
                
            	String ResNVP = convertISOtoNVPMsg( iso, erHandler );
            	log.debug("ResNVP..."+ResNVP);
            	String responseMsg="";
            	String STAN =getValue(ResNVP, "res_systemTraceAuditNumber=");
            	String res_responseCode =getValue(ResNVP, "res_responseCode=");
            	log.debug("Response code..."+res_responseCode);
            	 if(res_responseCode.equals("0000")){
             		responseMsg="req_Ref_No="+STAN+"~*ActCode=0~*ActDescription=Success~*res_Status_Code=00000~*error_cd=0~*res_Err_Desc=SUCCESS~*error_txt=SUCCESS~*";
             	} 
             	else{responseMsg="req_Ref_No="+STAN+"~*ActCode=1~*res_Status_Code="+res_responseCode+"~*error_cd="+res_responseCode+"~*res_Err_Desc=FAILURE~*error_txt=FAILURE~*";}
            	ResNVP=rCode+sCap+responseMsg;

                _iresultCode = 0;
                _cresultMessage[0] = ResNVP;

                return _iresultCode;
            }
            catch ( Exception e )
            {
                _iresultCode = 2;
                _cresultMessage[0] = "Exception in CBS_2210_MAP_Res_in " + e;
                return _iresultCode;
            }
        }
    }

    public static String convertStringToHex( String str )
    {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for ( int i = 0; i < chars.length; i++ )
        {
            String temp = Long.toHexString( (long) chars[i] );

            StringBuilder sb = new StringBuilder( temp );
            if ( sb.length() < 2 )
            {
                sb.insert( 0, '0' ); // pad with leading zero if needed
            }
            hex.append( sb.toString() );
        }

        return hex.toString();
    }

    public static String convertHexToString( String hex )
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
        // System.out.println( "length decimal  .." + temp );
        return sb.toString();
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

    public String getValue(String input, String key)
	  {


	    String reqValue = input.substring(input.indexOf(key) + key.length(), input.length());
	     String value = reqValue.substring(0, reqValue.indexOf("~*"));

	    return value;
		 
		  
	    
	    
	  }
}
