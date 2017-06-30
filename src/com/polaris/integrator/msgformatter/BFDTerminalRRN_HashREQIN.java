
package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BFDTerminalRRN_HashREQIN
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( BFDTerminalRRN_HashREQIN.class );

    public BFDTerminalRRN_HashREQIN()
    {
    }

    public int Process( HashMap argParsedFldHM, String argInMsg, String argFldId, HashMap argFldFmtHM,
                        ErrorHandler argErrHandler )
    {
        try
        {
        	String s = argInMsg;
        	log.debug("argInMsg: "+argInMsg);
        	log.debug("argParsedFldHM: "+argParsedFldHM);
        	log.debug("argFldId: "+argFldId);
        	log.debug("argFldFmtHM: "+argFldFmtHM);
        	String transDate=new String();
        	String stan=new String();
        	if(argParsedFldHM!=null){
//        		HashMap	Request_bfd=(HashMap)argParsedFldHM.get("Request_bfd");
//        		HashMap	TransactionInfo=(HashMap)Request_bfd.get("TransactionInfo");
        		transDate=(String) argParsedFldHM.get("Transm_Date_time");
        		stan=(String) argParsedFldHM.get("Stan");
        	}
        	log.debug("Stan value "+stan);
        	log.debug("transDate value "+transDate);
            super._cresultMessage = new String[1];
            Date today=new Date();
            String terminalrrn=getLastDigitOfYear(today)+getJulianDate(today)+getHoursFromDateTime(transDate)+stan;
//          String Input = argInMsg.substring( argInMsg.indexOf( "NULL~*" ) + "NULL~*".length(), argInMsg.length() );
//            String Input = argInMsg+"terminal_rrn="+terminalrrn+"~*~*"; 
            
//            super._bypassMF = false;
			//super._cresultMessage[0] = Input;
//            argParsedFldHM.put("RRN", terminalrrn);
            argParsedFldHM.put("terminal_rrn", terminalrrn);
            log.debug("argParsedFldHM: "+argParsedFldHM);
            super._cresultMessageHM=argParsedFldHM;
            super._iresultCode = 0;
//            log.debug( "\n Inside UserExit : coutputStr : " + Input );
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
    
      
    
	public static void main(String[] args) { HashMap hm = new HashMap();
	BFDTerminalRRN_HashREQIN test = new BFDTerminalRRN_HashREQIN();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	/*"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"*/
	, "", new HashMap(), new ErrorHandler());
	}
   
    
}