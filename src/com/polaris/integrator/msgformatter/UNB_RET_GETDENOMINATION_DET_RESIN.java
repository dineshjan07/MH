package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNB_RET_GETDENOMINATION_DET_RESIN extends UserRule{
	private static Logger log = LoggerFactory.getLogger( UNB_RET_GETDENOMINATION_DET_RESIN.class );
	public int Process(HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,ErrorHandler erHandler){
		
		log.debug( "Inside UNB_RET_GETDENOMINATION_DET_RESIN  --->>>" + pIPMsg );
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
				inputMsg = inputMsg.replaceAll("<ns:getDenominationResponse xmlns:ns=\"http://ws.apache.org/axis2\">", "");
				inputMsg = inputMsg.replaceAll("</ns:getDenominationResponse>", "");
				inputMsg =inputMsg.substring(inputMsg.indexOf("<soapenv:Header", 0));
				log.debug("inputMsg after removing header ==>"+ inputMsg);
				inputMsg=removeXmlStringNamespaceAndPreamble(inputMsg);
				log.debug("inputMsg after removing Namespace ==>"+ inputMsg);
				
				inputMsg=inputMsg.replaceAll("</return>","").replaceAll("<return>","").replaceAll("</Body>","").replaceAll("</Envelope>","").replaceAll("<Envelope >","");
				inputMsg=inputMsg.concat("</Body>");
				/*String[] inputDetails={"<soapenv:Header>","<Header>","<Header >","</Header>","</soapenv:Header>","<header>","<header >","</header>","<BPM>","</BPM>","<soapenv:Body>","<Body>","</Body>","</soapenv:Body>","<AccountDetailsResponse>","<AccountDetailsResponse >","</AccountDetailsResponse>","<account>","</account>","<fault>","<fault >","</fault>","<accountChequeBook>","</accountChequeBook>","</ns:AccountDetailsResponse>","</ns:GetAccountDetailsResponse>","</soapenv:Body>","</Envelope>"};
				String[] outputDetails={"<ACC_SUMM_Header_RESIN>","<ACC_SUMM_Header_RESIN>","<ACC_SUMM_Header_RESIN>","</ACC_SUMM_Header_RESIN>","</ACC_SUMM_Header_RESIN>","<ACC_SUMM_header_RESIN>","<ACC_SUMM_header_RESIN>","</ACC_SUMM_header_RESIN>","<ACC_SUMM_BPM_RESIN>","</ACC_SUMM_BPM_RESIN>","<ACC_SUMM_Body_RESIN>","<ACC_SUMM_Body_RESIN>","</ACC_SUMM_Body_RESIN>","</ACC_SUMM_Body_RESIN>","<AccountDetailsResponse_RESIN>","<AccountDetailsResponse_RESIN>","</AccountDetailsResponse_RESIN>","<ACC_SUMM_account_RESIN>","</ACC_SUMM_account_RESIN>","<ACC_SUMM_fault_RESIN>","<ACC_SUMM_fault_RESIN>","</ACC_SUMM_fault_RESIN>","<ACC_SUMM_chequeBookType_RESIN>","</ACC_SUMM_chequeBookType_RESIN>","","","",""};
				for(int i=0;i<inputDetails.length;i++){

					if(inputMsg.indexOf(inputDetails[i])!=-1){

						inputMsg = inputMsg.replaceAll(inputDetails[i],outputDetails[i]);
					}
				}*/
				/*
				 * 
				 */
				String[] inputDetails={"<getDenominationResponse>","</getDenominationResponse>","<getDenominationOutput>","</getDenominationOutput>","<T24WSResponseParams>","</T24WSResponseParams>","<DenominationDetails>","</DenominationDetails>","<Body>","</Body>","<Envelope>","<Header/>"};
				String[] outputDetails={"<getDenominationResponse_IN>","</getDenominationResponse_IN>","<getDenominationOutput_IN>","</getDenominationOutput_IN>","<T24WSResponseParams_IN>","</T24WSResponseParams_IN>","<DenominationDetails_IN>","</DenominationDetails_IN>","<RET_GETDENOMINATION_DET_RES_BODY_IN>","</RET_GETDENOMINATION_DET_RES_BODY_IN>","",""};
				for(int i=0;i<inputDetails.length;i++){

					if(inputMsg.indexOf(inputDetails[i])!=-1){

						inputMsg = inputMsg.replaceAll(inputDetails[i],outputDetails[i]);
					}
				}

				StringBuffer coutputStr =new StringBuffer( inputMsg);
				_iresultCode=0;
				_cresultMessage[0]="<RET_GETDENOMINATION_DET_RES_COMP_IN>"+coutputStr.toString()+"</RET_GETDENOMINATION_DET_RES_COMP_IN>";

				log.debug("Response Message from the UNB_RET_GETDENOMINATION_DET_RESIN  is ==>"+ _cresultMessage[0]);
				return _iresultCode;
			}catch(Exception e){
				_iresultCode = 2;
				_cresultMessage[0]="Exception in UNB_RET_GETDENOMINATION_DET_RESIN user exit "+e;
				log.error( "Exception in UNB_RET_GETDENOMINATION_DET_RESIN : " , e );
				return _iresultCode;
			}
		}
	}
	public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
		  return xmlString.replaceAll("(<\\?[^<]*\\?>)?", ""). /* remove preamble */
		  replaceAll(" xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
		  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
		  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
		}

}
