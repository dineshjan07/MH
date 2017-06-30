package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNB_RET_UAEPGS_PAYMENT_RESIN extends UserRule {
		private static Logger log = LoggerFactory.getLogger( UNB_RET_UAEPGS_PAYMENT_RESIN.class );
	public int Process(HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,ErrorHandler erHandler){
		
		log.debug( "Inside UNB_RET_UAEPGS_PAYMENT_RESIN  --->>>" + pIPMsg );
		_cresultMessage = new String[1];
		if (pIPMsg==null){
			_iresultCode = 1;
			_cresultMessage[0] ="Data not found in InputHash ";
			return _iresultCode;
		}else{
			try{

				String inputMsg=pIPMsg;
				inputMsg=inputMsg.replaceAll("&lt;","<");
				inputMsg=inputMsg.replaceAll("&gt;",">");
				
				inputMsg = inputMsg.replaceAll("ser-root:", "");
				inputMsg =inputMsg.substring(inputMsg.indexOf("<soapenv:Body",0));
				log.debug("inputMsg after removing header ==>"+ inputMsg);
				inputMsg=removeXmlStringNamespaceAndPreamble(inputMsg);
				log.debug("inputMsg after removing Namespace ==>"+ inputMsg);
				
				inputMsg=inputMsg.replaceAll("</return>","").replaceAll("<return>","").replaceAll("<Header>","").replaceAll("</Header>","").replaceAll("<Body>","").replaceAll("</Body>","").replaceAll("</Envelope>","").replaceAll("<Envelope >","");
			
			
				_iresultCode=0;
				_cresultMessage[0]=inputMsg;

				log.debug("Response Message from the UNB_RET_UAEPGS_PAYMENT_RESIN  is ==>"+ _cresultMessage[0]);
				return _iresultCode;
			}catch(Exception e){
				_iresultCode = 2;
				_cresultMessage[0]="Exception in UNB_RET_UAEPGS_PAYMENT_RESIN user exit "+e;
				log.error( "Exception in UNB_RET_UAEPGS_PAYMENT_RESIN : " , e );
				return _iresultCode;
			}
		}
	}
	public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
		return xmlString.replaceAll("(<\\?[^<]*\\?>)?", ""). /* remove preamble */
		  replaceAll(" xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
		  .replaceAll(" ser-root.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
		  .replaceAll(" xsi.*?(\"|\').*?(\"|\')", "") /* remove xsi declaration */
		  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
		  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
		}

}
