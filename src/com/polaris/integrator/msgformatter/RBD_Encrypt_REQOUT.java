
package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBD_Encrypt_REQOUT
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( RBD_Encrypt_REQOUT.class );

    public RBD_Encrypt_REQOUT()
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
        	int fromIndex=s.indexOf("<Rbd");
        	int endIndex=s.indexOf("</Rbd",fromIndex);
        	String rbdXML=s.substring(fromIndex,endIndex+6);
        	log.debug("rbdXML value "+rbdXML);
        	argInMsg=argInMsg.replace(rbdXML, "");
			// 1. Generate AES session key
			byte[] aesKey;

			aesKey = UIDAIEncryptionUtility.getRandomAESSKey();

			// 2. Get public key
			PublicKey pk =  UIDAIEncryptionUtility.getPublicKey();

			// 3. Encrypt session id
			String Skey =Base64.encodeBase64String(UIDAIEncryptionUtility.encryptAESSKey(aesKey, pk));
			log.debug("Encrypted AES Skey: " + Skey);
				
			argInMsg=argInMsg.replace("</Skey>",Skey+"</Skey>");
			// 4. Generate PID Block
			log.debug("After adding Skey :"+argInMsg);

			// 5Encrypt PID xml
			String encryptPIDXML = Base64.encodeBase64String(UIDAIEncryptionUtility.encrypt(rbdXML.getBytes(), aesKey));
			log.debug("Encrypte RBD XML: " + encryptPIDXML);
			argInMsg=argInMsg.replace("</Data>",encryptPIDXML+"</Data>");
			log.debug("After adding Data :"+argInMsg);

			// 6. Generate Hmac value
			String base64EncodedPidXMLSha256Hash = Base64.encodeBase64String(UIDAIEncryptionUtility.generateSha256Hash(rbdXML.getBytes(), aesKey));
			log.debug("Hmac Value: " + base64EncodedPidXMLSha256Hash);
			argInMsg=argInMsg.replace("</Hmac>",base64EncodedPidXMLSha256Hash+"</Hmac>");
			log.debug("After adding Hmac :"+argInMsg);
			log.debug("Final arg In msg :"+argInMsg);
			
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
    

    
	public static void main(String[] args) { HashMap hm = new HashMap();
	RBD_Encrypt_REQOUT test = new RBD_Encrypt_REQOUT();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	/*"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"*/
	, "", new HashMap(), new ErrorHandler());
	}
   
    
}