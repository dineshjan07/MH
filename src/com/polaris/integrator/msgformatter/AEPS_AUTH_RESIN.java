package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AEPS_AUTH_RESIN extends UserRule {
	private static Logger log = LoggerFactory.getLogger(AEPS_AUTH_RESIN.class);

	
	
	
	public int Process(HashMap hashmap, String pInMsg, String argFldId, HashMap hashmap1,
			ErrorHandler errorhandler) {
		log.debug("Input message in  AEPS_AUTH_RESIN User_Exit :  " + pInMsg);
		this._cresultMessage = new String[1];

		if (pInMsg == null) {
			this._iresultCode = 1;
			this._cresultMessage[0] = "Data not found in InputHash ";
			return this._iresultCode;
		}
		else{
		try {
			String inputMsg = pInMsg;
			log.debug("Input message in  AEPS_AUTH_RESIN User_Exit :  " + inputMsg);
            log.debug( "Input to CBS_2210_MAP_Res_in ISO8583 -0810- message--->>>" + inputMsg );
            String resHex = convertStringToHex( inputMsg );
            log.debug( "Response Hexadecimal ....>>" + resHex );
            // String lenHex = resHex.substring( 0, 4 );
            String mtiHex = resHex.substring( 4, 12);
            String mti = convertHexToString( mtiHex );
            String bitmapHexOriginal = resHex.substring( 12, 44 );
            String bitmapHex = null;
            String[] hexTemp = new String[16];
            String bitmap = "";
            /*for ( int i = 0; i < 16; i++ )
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

*/            String dataHex = resHex.substring( 44, resHex.length() );
            String data = convertHexToString( dataHex );

            String iso = mti + bitmapHexOriginal + data;

            //iso=iso.substring(2, iso.length());
            log.debug( "ISO 8583 - 0210 message sent to Integartor ....>" + iso );
			
            String ResNVP  = convertISOtoNVPMsg( iso, errorhandler );
			System.out.println("inputMsg  ISO TO NVP: " + ResNVP+"~*");
			this._iresultCode = 0;
			this._cresultMessage[0] = ResNVP+"~*";

			log.debug("Response Message from the AEPS_AUTH_RESIN is ==>"
					+ this._cresultMessage[0]);
			return this._iresultCode;
		}
		catch (Exception e) {
			this._iresultCode = 2;
			this._cresultMessage[0] = "Exception in AEPS_AUTH_RESIN user exit " + e;
			log.error("Exception in AEPS_AUTH_RESIN : ", e);
		}
		return this._iresultCode;
	}
	}
	public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
		return xmlString.replaceAll("(<\\?[^<]*\\?>)?", "")
				.replaceAll(" xmlns.*?(\"|').*?(\"|')", "")
				.replaceAll(" ser-root.*?(\"|').*?(\"|')", "")
				.replaceAll(" xsi.*?(\"|').*?(\"|')", "").replaceAll("(<)(\\w+:)(.*?>)", "$1$3")
				.replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
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
 
   
    
	public static void main(String[] args) {
		System.out.println("main Method called");
		AEPS_AUTH_RESIN testObj = new AEPS_AUTH_RESIN();
		HashMap hm = new HashMap();
		testObj.Process(hm, "<ResponseMessage>" + "<Assets>" + "<SubRecord>"
				+ "<Assets_cell0> </Assets_cell0>" + "<Assets_cell1>0</Assets_cell1>"
				+ "<Assets_cell2>0</Assets_cell2>" + "<Assets_cell3>TRY</Assets_cell3>"
				+ "<Assets_cell4>12</Assets_cell4>" + "<Assets_cell5>12</Assets_cell5>"
				+ "</SubRecord>" + "</Assets>" + "</ResponseMessage>", "", null, null);
	}
}