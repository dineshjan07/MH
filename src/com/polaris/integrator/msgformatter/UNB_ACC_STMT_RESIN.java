package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNB_ACC_STMT_RESIN extends UserRule{
	private static Logger log = LoggerFactory.getLogger( UNB_ACC_SUMM_RESIN.class );
	public int Process(HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1,ErrorHandler erHandler){

		log.debug( "Inside UNB_ACC_STMT_RESIN  --->>>" + pIPMsg );
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
				inputMsg =inputMsg.substring(inputMsg.indexOf("<soapenv:Body", 0));
				log.debug("inputMsg after removing header ==>"+ inputMsg);
				inputMsg=removeXmlStringNamespaceAndPreamble(inputMsg);
				log.debug("inputMsg after removing Namespace ==>"+ inputMsg);
				
				inputMsg=inputMsg.replaceAll("</return>","").replaceAll("<return>","").replaceAll("<GetAccountStatementResponse>","").replaceAll("</GetAccountStatementResponse>","").replaceAll("</Body>","").replaceAll("</Envelope>","").replaceAll("<Envelope >","");
				inputMsg=inputMsg.concat("</Body>");
				/*String[] inputDetails={"<soapenv:Header>","<Header>","<Header >","</Header>","</soapenv:Header>","<header>","<header >","</header>","<BPM>","</BPM>","<soapenv:Body>","<Body>","</Body>","</soapenv:Body>","<AccountDetailsResponse>","<AccountDetailsResponse >","</AccountDetailsResponse>","<account>","</account>","<fault>","<fault >","</fault>","<accountChequeBook>","</accountChequeBook>","</ns:AccountDetailsResponse>","</ns:GetAccountDetailsResponse>","</soapenv:Body>","</Envelope>"};
				String[] outputDetails={"<ACC_SUMM_Header_RESIN>","<ACC_SUMM_Header_RESIN>","<ACC_SUMM_Header_RESIN>","</ACC_SUMM_Header_RESIN>","</ACC_SUMM_Header_RESIN>","<ACC_SUMM_header_RESIN>","<ACC_SUMM_header_RESIN>","</ACC_SUMM_header_RESIN>","<ACC_SUMM_BPM_RESIN>","</ACC_SUMM_BPM_RESIN>","<ACC_SUMM_Body_RESIN>","<ACC_SUMM_Body_RESIN>","</ACC_SUMM_Body_RESIN>","</ACC_SUMM_Body_RESIN>","<AccountDetailsResponse_RESIN>","<AccountDetailsResponse_RESIN>","</AccountDetailsResponse_RESIN>","<ACC_SUMM_account_RESIN>","</ACC_SUMM_account_RESIN>","<ACC_SUMM_fault_RESIN>","<ACC_SUMM_fault_RESIN>","</ACC_SUMM_fault_RESIN>","<ACC_SUMM_chequeBookType_RESIN>","</ACC_SUMM_chequeBookType_RESIN>","","","",""};
				for(int i=0;i<inputDetails.length;i++){

					if(inputMsg.indexOf(inputDetails[i])!=-1){

						inputMsg = inputMsg.replaceAll(inputDetails[i],outputDetails[i]);
					}
				}*/
				
				String[] inputDetails={"<Body>","</Body>"};
				String[] outputDetails={"<ACC_STMT_RES_BODY_IN>","</ACC_STMT_RES_BODY_IN>"};
				for(int i=0;i<inputDetails.length;i++){

					if(inputMsg.indexOf(inputDetails[i])!=-1){

						inputMsg = inputMsg.replaceAll(inputDetails[i],outputDetails[i]);
					}
				}

				StringBuffer coutputStr =new StringBuffer( inputMsg);
				_iresultCode=0;
				_cresultMessage[0]="<ACC_STMT_RES_COMP_IN>"+coutputStr.toString()+"</ACC_STMT_RES_COMP_IN>";

				log.debug("Response Message from the UNB_ACC_STMT_RESIN  is ==>"+ _cresultMessage[0]);
				return _iresultCode;
			}catch(Exception e){
				_iresultCode = 2;
				_cresultMessage[0]="Exception in UNB_ACC_STMT_RESIN user exit "+e;
				log.error( "Exception in UNB_ACC_STMT_RESIN : " , e );
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
