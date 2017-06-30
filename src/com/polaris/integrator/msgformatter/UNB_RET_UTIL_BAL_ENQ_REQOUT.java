package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNB_RET_UTIL_BAL_ENQ_REQOUT extends UserRule{
	private static Logger log = LoggerFactory
	.getLogger(UNB_RET_UTIL_BAL_ENQ_REQOUT.class);

public int Process(HashMap orderedhashtable, String pIPMsg, String s,
	HashMap orderedhashtable1, ErrorHandler erHandler) {

log.debug("Inside UNB_RET_UTIL_BAL_ENQ_REQOUT  --->>>" + pIPMsg);
_cresultMessage = new String[1];
if (pIPMsg == null) {
	_iresultCode = 1;
	_cresultMessage[0] = "Data not found in InputHash ";
	return _iresultCode;
} else {
	try {
		
		
		String inputStr = pIPMsg;
		inputStr = inputStr.replaceAll("<xml><RET_UTIL_BAL_ENQ_REQ_COMP_OUT>","");
		inputStr = inputStr.replaceAll("</RET_UTIL_BAL_ENQ_REQ_COMP_OUT></xml>","");
		
		
		String[] inputDetails={"<BalanceEnquiry_IN>","</BalanceEnquiry_IN>","<RET_UTIL_BAL_ENQ_REQ_BODY_OUT>","</RET_UTIL_BAL_ENQ_REQ_BODY_OUT>"};
		String[] outputDetails={"<BalanceEnquiry>","</BalanceEnquiry>","",""};
		for(int i=0;i<inputDetails.length;i++){

			if(inputStr.indexOf(inputDetails[i])!=-1){

				inputStr = inputStr.replaceAll(inputDetails[i],outputDetails[i]);
			}
		}
		
        StringBuffer coutputStr = new StringBuffer(inputStr);
        
		// StringBuffer headerstarttag= new
		// StringBuffer("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://corp.alahli.com/middlewareservices/header/1.0/\" xmlns:oas=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:ns1=\"http://corp.alahli.com/middlewareservices/account/1.0/\">");

		StringBuffer headerstarttag = new StringBuffer(
				//"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://ws.apache.org/axis2\" xmlns:oas=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:ns1=\"http://ws.apache.org/axis2\">");
				//"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://ws.apache.org/axis2\" xmlns:ns1=\"http://ws.apache.org/axis2/xsd\">");
			
		//correct for axis		
	///////tam	"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope\" xmlns:unb=\"http://www.webmethods.com/UNB_S030_CorporateOnline.GeneralReportsAndInquiry.Common.ws.provider:CorporateOnlineGeneralReportsAndInquiryWS\"><soap:Body>");
	///////"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope\" xmlns:unb=\"http://www.webmethods.com/UNB_S030_CorporateOnline.GeneralReportsAndInquiry.Common.ws.provider:CorporateOnlineGeneralReportsAndInquiryWS\">");
		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns=\"http://ws.apache.org/axis2\"><soap:Body>");
		//"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		/*"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://corp.alahli.com/middlewareservices/header/1.0/\" xmlns:oas=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:ns1=\"http://corp.alahli.com/middlewareservices/account/1.0/\">");
		StringBuffer headerendtag = new StringBuffer("</soapenv:Envelope>");
		*/
		
		StringBuffer headerendtag = new StringBuffer("</soap:Body></soap:Envelope>");
	//	StringBuffer headerendtag = new StringBuffer("</soap:Envelope>");
		
		
		StringBuffer finalContent = headerstarttag.append(coutputStr).append(headerendtag);
		//StringBuffer finalContent = coutputStr;
				

		_iresultCode = 0;
		_cresultMessage[0] = finalContent.toString();

		log.debug("Response Message from the UNB_RET_UTIL_BAL_ENQ_REQOUT  is ==>"
				+ _cresultMessage[0]);
		return _iresultCode;
	} catch (Exception e) {
		_iresultCode = 2;
		_cresultMessage[0] = "Exception in In_Data_UserExit UNB_RET_UTIL_BAL_ENQ_REQOUT"
				+ e;
		log.error("Exception in UNB_RET_UTIL_BAL_ENQ_REQOUT : ", e);
		return _iresultCode;
	}
}
}

}
