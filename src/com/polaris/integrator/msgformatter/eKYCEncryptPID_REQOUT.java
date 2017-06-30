
package com.polaris.integrator.msgformatter;

import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class eKYCEncryptPID_REQOUT
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( eKYCEncryptPID_REQOUT.class );

    public eKYCEncryptPID_REQOUT()
    {
    }

    public int Process( HashMap argParsedFldHM, String argInMsg, String argFldId, HashMap argFldFmtHM,
                        ErrorHandler argErrHandler )
    {
        try
        {
        	String s = argInMsg;
        	log.debug("argInMsg:"+argInMsg);
        	log.debug("s:"+s);
        	argInMsg=argInMsg.replaceAll("data_type", "type").replaceAll("bio_type", "type").replaceAll("auth_ver", "ver");
        	s=argInMsg;
        	log.debug("after replacing attributes : "+argInMsg);
        	String stan=s.substring(s.indexOf("<Stan>")+6,s.indexOf("</Stan>"));
        	log.debug("STAN value "+stan);
        	String transDate=s.substring(s.indexOf("<Transm_Date_time>")+18,s.indexOf("</Transm_Date_time>"));
        	log.debug("STAN value "+transDate);
        	Date today=new Date();
        	String terminalrrn=getLastDigitOfYear(today)+getJulianDate(today)+getHoursFromDateTime(transDate)+stan;
        	argInMsg=argInMsg.replace("<RRN></RRN>","<RRN>"+terminalrrn+"</RRN>");
        	int fromIndex=s.indexOf("<Pid");
        	int endIndex=s.indexOf("</Pid",fromIndex);
        	String pidXML=s.substring(fromIndex,endIndex+6);
        	log.debug("pidXML value "+pidXML);
			// 1. Generate AES session key
			byte[] aesKey;

			aesKey = UIDAIEncryptionUtility.getRandomAESSKey();

			// 2. Get public key
			PublicKey pk = UIDAIEncryptionUtility.getPublicKey();

			// 3. Encrypt session id
			String Skey = Base64.encodeBase64String(UIDAIEncryptionUtility.encryptAESSKey(aesKey, pk));
			log.debug("Encrypted AES Skey: " + Skey);

			// 4. Generate PID Block
		

			// 5Encrypt PID xml
			String encryptPIDXML = Base64.encodeBase64String(UIDAIEncryptionUtility.encrypt(pidXML.getBytes(), aesKey));
			log.debug("Encrypte PID XML: " + encryptPIDXML);

			// 6. Generate Hmac value
//			String base64EncodedPidXMLSha256Hash = Base64.encodeBase64String(UIDAIEncryptionUtility.getHmacValue(pidXML, aesKey));
			String base64EncodedPidXMLSha256Hash = Base64.encodeBase64String(UIDAIEncryptionUtility.generateSha256Hash(pidXML.getBytes(), aesKey));
			log.debug("Hmac Value: " + base64EncodedPidXMLSha256Hash);
			
			argInMsg=argInMsg.replace(pidXML, encryptPIDXML).replace("<Hmac></Hmac>", "<Hmac>"+base64EncodedPidXMLSha256Hash+"</Hmac>").
					replace("</Skey>", Skey+"</Skey>");
			log.debug("arg In msg :"+argInMsg);
			
            super._cresultMessage = new String[1];
           
            String Input = argInMsg;
            
            log.debug("Input message "+Input);
            super._bypassMF = false;
			super._cresultMessage[0] = Input;
            super._iresultCode = 0;
            log.debug( "\n Inside UserExit : coutputStr : " + Input );
            log.debug( "\n Inside UserExit returns : _iresultCode : " + _iresultCode );
            log.debug( "\n Leaving UserExit TruncateHeader" );
            return super._iresultCode;
        }
        catch ( Exception exception )
        {
            super._iresultCode = 1000;
            log.error( "Exception in TruncateHeader : " , exception );
        }
        return super._iresultCode;
    }
    
    public static String getLastDigitOfYear(Date date){
		return Integer.toString(Calendar.getInstance().get(Calendar.YEAR)).substring(3);
	}
	public static String getJulianDate(Date date) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return String.format("%03d", calendar.get(Calendar.DAY_OF_YEAR));
	}
	public static String getHoursFromDateTime(String dateTime){
	
		return dateTime.substring(4, 6);
	}
  /*  

    
	public static void main(String[] args) { HashMap hm = new HashMap();
	eKYCEncryptPID_REQOUT test = new eKYCEncryptPID_REQOUT();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"
	, "", new HashMap(), new ErrorHandler());
	}
   */
    
}