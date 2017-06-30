package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polaris.integrator.webtools.properties.ConfigProperties;

public class ISOAddResultCode_MAPFUNDTRANSFER extends UserRule {
	private static Logger log = LoggerFactory.getLogger(ISOAddResultCode_MAPFUNDTRANSFER.class);

	public int Process(HashMap hashmap, String inMsg, String argFldId, HashMap hashmap1,
			ErrorHandler errorhandler) {
		try {
			String finalProfileRes = "", finalResponse = "";
			String rCode;
			log.debug("Inside AddResultCode_Bybass  --->>>" + inMsg);
			rCode = "res_Result_Code=00000~*";
			String sCap = "res_No_Of_Seg=~*";
			_cresultMessage = new String[1];
			super._bypassMF = false;
			if (inMsg == null) {
				_iresultCode = 1;
				_cresultMessage[0] = "Input message is null.";
				return _iresultCode;
			}
		/*	String defaultFields = ConfigProperties.get("CBS", "FUNDTRANSFER_RESPONSE");
			log.debug("defaultFields values  --->>>" + defaultFields);*/
			inMsg = convertISOtoNVPMsg( inMsg, errorhandler );
			log.debug("ISO to NVP..."+inMsg);
			String spliting = inMsg.substring(inMsg.indexOf("res_systemTraceAuditNumber"));
			log.debug(spliting);
			String Stan = spliting
			.substring(spliting.indexOf("res_systemTraceAuditNumber"),
					spliting.indexOf("res_Time"))
			.replace("res_systemTraceAuditNumber=", "").replace("~*", "");
			
			log.debug("Stan  --->>>" + Stan );
			String res_responseCode = inMsg
            .substring(inMsg.indexOf("res_responseCode"), inMsg.indexOf("res_cardAptTerminalId")).
            replace("res_responseCode=", "").replace("~*","");
			
			String keys;
			if (inMsg.contains("res_responseCode=0000~*")) {

				keys = "ActCode=0~*ActDescription=Success~*res_Status_Code=00000~*error_cd=0~*res_Err_Desc=SUCCESS~*error_txt=SUCCESS~*";
			}
			else {
				keys = "ActCode=1~*res_Status_Code="+res_responseCode+"~*error_cd="+res_responseCode+"~*res_Err_Desc=FAILURE~*error_txt=FAILURE~*";
			}
			String nvp = "hdr_Tran_Id=REMITTANCE~*" + keys + "res_ref_no="+Stan+"~*req_Ref_No="+Stan+"~*req_Ref_No="+Stan+"~*req_Txn_Ref_No="+Stan+"~*req_Host_Ref_No="+Stan+"~*";

			String res_Result = rCode + sCap + nvp;

			// String res_Result = sCap + nvp;
			_cresultMessage[0] = res_Result;
			return _iresultCode;
		}
		catch (Exception exception) {
			this._iresultCode = 2;
			this._cresultMessage[0] = ("Exception in ISOAddResultCode_MAPFUNDTRANSFER " + exception);
			log.error("Exception in ISOAddResultCode_MAPFUNDTRANSFER: ", exception);
		}
		return this._iresultCode;
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