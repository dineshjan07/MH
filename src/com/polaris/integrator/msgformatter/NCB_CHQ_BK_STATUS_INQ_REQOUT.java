package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.polaris.integrator.webtools.properties.ConfigProperties;


public class NCB_CHQ_BK_STATUS_INQ_REQOUT extends UserRule {

	private static Logger log = LoggerFactory
			.getLogger(NCB_CHQ_BK_STATUS_INQ_REQOUT.class);

	public int Process(HashMap orderedhashtable, String pIPMsg, String s,
			HashMap orderedhashtable1, ErrorHandler erHandler) {

		log.debug("Inside NCB_CHQ_BK_STATUS_INQ_REQOUT  --->>>" + pIPMsg);
		_cresultMessage = new String[1];
		if (pIPMsg == null) {
			_iresultCode = 1;
			_cresultMessage[0] = "Data not found in InputHash ";
			return _iresultCode;
		} else {
			try {


				String soapEnvelope_BEGIN = ConfigProperties.get("ncb","CHQBOOK_SOAPENV_BEGIN");
				String soapEnvelope_END = ConfigProperties.get("ncb","SOAPENV_END");
				String soapHeader = ConfigProperties.get("ncb","SOAPHEADER");
				String soapServiceHeader = ConfigProperties.get("ncb","SOAP_SERVICE_HEADER");
				String soapSecurityHeader = ConfigProperties.get("ncb","COMMON_SECURITY_HEADER");
				String soapUserNameToken = ConfigProperties.get("ncb","COMMON_USERNAME_TOKEN");
				String soapEnvelopeBody = ConfigProperties.get("ncb","SOAPENV_BODY");
				String soapRequest = ConfigProperties.get("ncb","CHQBOOKINQ_DETAILS_REQUEST");
				String inputStr = pIPMsg;
				inputStr = inputStr.replaceAll("<xml><CHQ_BK_STATUS_INQ_REQ_COMP_OUT>",
						"");
				inputStr = inputStr.replaceAll(
						"</CHQ_BK_STATUS_INQ_REQ_COMP_OUT></xml>", "");
				inputStr = inputStr.replaceAll("<xml><CHQ_BK_STATUS_INQ__REQ_COMP_OUT>","");
				inputStr = inputStr.replaceAll("CHQ_BK_STATUS_INQ_soap:Header",
				soapHeader);
				inputStr = inputStr.replaceAll("CHQBOOK_COMMON_ns:ServiceHeader",
				soapServiceHeader);
				inputStr = inputStr.replaceAll("COMMON_oas:Security",
				soapSecurityHeader);
				inputStr = inputStr.replaceAll("COMMON_oas:UsernameToken",
				soapUserNameToken);
				inputStr = inputStr.replaceAll("CHQ_BK_STATUS_INQ_soapenv:Body",
				soapEnvelopeBody);
				inputStr = inputStr.replaceAll("CHQ_BK_STATUS_INQ_REQ_COMP_ChequeBookStatusInquiryRequest_OUT",
				soapRequest);
				
				inputStr = inputStr.replaceAll("ns:", ""); 	  	 
                inputStr = inputStr.replaceAll("ns1:", ""); 	  	 
                inputStr = inputStr.replaceAll("oas:", "");
				
				StringBuffer coutputStr = new StringBuffer(inputStr);

				StringBuffer headerstarttag = new StringBuffer(soapEnvelope_BEGIN);
				StringBuffer headerendtag = new StringBuffer(soapEnvelope_END);
				StringBuffer finalContent = headerstarttag.append(coutputStr).append(headerendtag);
				_iresultCode = 0;
				_cresultMessage[0] = finalContent.toString();

				log.debug("Response Message from the NCB_CHQ_BK_STATUS_INQ_REQOUT  is ==>"
						+ _cresultMessage[0]);
				return _iresultCode;
			} catch (Exception e) {
				_iresultCode = 2;
				_cresultMessage[0] = "Exception in In_Data_UserExit NCB_CHQ_BK_STATUS_INQ_REQOUT"
						+ e;
				log.error("Exception in NCB_CHQ_BK_STATUS_INQ_REQOUT : ", e);
				return _iresultCode;
			}
		}
	}
}