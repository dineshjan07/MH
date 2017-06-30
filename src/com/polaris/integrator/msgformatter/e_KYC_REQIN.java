package com.polaris.integrator.msgformatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class e_KYC_REQIN extends UserRule{
	private static Logger log = LoggerFactory
	.getLogger(e_KYC_REQIN.class);

public int Process(HashMap orderedhashtable, String pIPMsg, String s,
	HashMap orderedhashtable1, ErrorHandler erHandler) {

log.debug("Inside e_KYC_REQIN  --->>>" + pIPMsg);
_cresultMessage = new String[1];
if (pIPMsg == null) {
	_iresultCode = 1;
	_cresultMessage[0] = "Data not found in InputHash ";
	return _iresultCode;
} else {
	try {
		/*Local_Trans_Time ,Local_date */
		String Transm_Date_time="1012081102";
		String Local_Trans_Time=Transm_Date_time.substring(4,Transm_Date_time.length());
		String Local_date=Transm_Date_time.substring(0, 4);		
		log.debug("Local_Trans_Time:"+Local_Trans_Time);
		 log.debug("Local_date:"+Local_date);
		/*String inputStr = pIPMsg;
		GregorianCalendar date = new GregorianCalendar();
        int month = date.get(Calendar.MONTH);
    	String Time="req_Time="+date.get(Calendar.YEAR)+"/"+(month+1)+"/"+date.get(Calendar.DAY_OF_MONTH)+":"+date.get(Calendar.HOUR)+":"+date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND)+"~*";
    	Time=Time.replace("/", "").replace(":", "");
    	log.debug(Time);
    	String captureDate="req_captureDate="+date.get(Calendar.YEAR)+"/"+(month+1)+"/"+date.get(Calendar.DAY_OF_MONTH)+"~*";
    	captureDate=captureDate.replace("/", "");
    	log.debug(captureDate);
		inputStr=inputStr.replace("req_Time=~*", Time).replace("req_captureDate=~*", captureDate);
		
	      String sytemNo="req_systemTraceAuditNumber=100"+Math.round(Math.random() * 1000000000)+"~*";
	      
		String inputStrs="hdr_Tran_Id=RET_VALIDATE_FUNDS~*hdr_Status=NULL~*MTI=2200~*req_cardNumber=1263800061321728~*req_processingCode=310000~*req_amount=0000000000550000~*req_tranmisssionAmount=0412115559~*req_systemTraceAuditNumber=100921002001~*req_Time=20100921180921~*req_captureDate=20100921~*req_functinCode=200~*req_transmissionFee=000000001000~*req_aquiringInstIdentificationCode=12354587865~*req_forwardingInstIdentificationCode=1263800061321728~*req_retrievalRefNumber=405635144380~*req_cardAptTerminalId=0002123456756789~*req_cardAcceptorIdCode=CLAB~*req_cardAcceptorLocation=ABCDEFGH BANK DIEBOLD D ATM 2 XYZXYZX IN~*req_transcationCurrencyCode=356~*req_accountID1=006200010523~*req_deliveryChannelControlName=SWT~*req_deliveryChannelNameID=001~*";
		
		inputStrs=inputStrs.replace("req_systemTraceAuditNumber=~*", sytemNo);
		_iresultCode = 0;
		_cresultMessage[0] = inputStrs;
		
		log.debug("Response Message from the e_KYC_REQIN  is ==>"
				+ _cresultMessage[0]);
*/		return _iresultCode;
	} catch (Exception e) {
		_iresultCode = 2;
		_cresultMessage[0] = "Exception in In_Data_UserExit e_KYC_REQIN"
				+ e;
		log.error("Exception in e_KYC_REQIN : ", e);
		return _iresultCode;
	}
}
}

public String RRN(String stan)
{
	String rrn;
	Date today=new Date();
	Calendar calendar=Calendar.getInstance();
		calendar.setTime(today);
		String LastDigitOfYear= Integer.toString(Calendar.getInstance().get(Calendar.YEAR)).substring(3);
		String JulianDate= String.format("%03d", calendar.get(Calendar.DAY_OF_YEAR));
		String Hours= String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY));
		log.debug("Year Value Y:"+LastDigitOfYear);
        log.debug("Julian Day DDD:"+JulianDate);
        log.debug("Hour HH:"+Hours);
        String StanNo=stan;
        rrn=LastDigitOfYear+JulianDate+Hours+StanNo;
        log.debug("rrn..:"+rrn);
	return rrn;
	
}

}
