
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

public class BFDEncryptRBD_REQOUT
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( BFDEncryptRBD_REQOUT.class );

    public BFDEncryptRBD_REQOUT()
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
			// 1. Generate AES session key
			byte[] aesKey;

			aesKey = UIDAIEncryptionUtility.getRandomAESSKey();

			// 2. Get public key
			PublicKey pk = UIDAIEncryptionUtility.getPublicKey();

			// 3. Encrypt session id
			String Skey = Base64.encodeBase64String(UIDAIEncryptionUtility.encryptAESSKey(aesKey, pk));
			log.debug("Encrypted AES Skey: " + Skey);
			
			argInMsg=argInMsg.replace("</tem:Skey_ki>", Skey+"</tem:Skey_ki>");
			log.debug("After replacing Skey value: "+argInMsg);

			// 4. Generate PID Block
		

			// 5Encrypt PID xml
			String encryptPIDXML =Base64.encodeBase64String(UIDAIEncryptionUtility.encrypt(rbdXML.getBytes(), aesKey));
			log.debug("Encrypte RBD XML: " + encryptPIDXML);
//			String encryptPIDXML=rbdXML;

			// 6. Generate Hmac value
			String base64EncodedPidXMLSha256Hash =Base64.encodeBase64String(UIDAIEncryptionUtility.generateSha256Hash(rbdXML.getBytes(), aesKey));
			log.debug("Hmac Value: " + base64EncodedPidXMLSha256Hash);
			
			argInMsg=argInMsg.replace("</tem:Hmac>", base64EncodedPidXMLSha256Hash+"</tem:Hmac>");
			log.debug("After replacing Hmac value: "+argInMsg);
			
			byte[] encodedXml=encryptPIDXML.getBytes();
			int length=encodedXml.length;
			int stringLength=Integer.toString(length).length();
			int total_length=length+stringLength+3;
			int no_of_iteration=1;
			 int BYTESPERTAG=999;
			if(total_length>BYTESPERTAG){
				no_of_iteration=total_length/BYTESPERTAG;
				if(total_length%BYTESPERTAG!=0){
					no_of_iteration++;
				}
			}
			if(no_of_iteration<=1){
				BYTESPERTAG=total_length;
			}
			boolean firstIteration=true;
			int start=0;
			int end=BYTESPERTAG;
			log.debug("BYTESPERTAG : "+BYTESPERTAG);
			log.debug("length : "+length);
			log.debug("stringLength : "+stringLength);
			log.debug("total_length : "+total_length);
			log.debug("no_of_iteration : "+no_of_iteration);
			List<String> data=new ArrayList<String>();
			while(no_of_iteration>0){
				if(firstIteration){
					end=end-(stringLength+3);
					data.add("BFD"+length+encryptPIDXML.substring(start,end));
					firstIteration=false;
				}
				else{
					data.add(encryptPIDXML.substring(start, end));
				}
				start=end;
				end=end+BYTESPERTAG;
				if(end>length){
					end=length;
				}
				no_of_iteration--;
			}
			String finalTagValue=new String();
			for(int i=0;i<data.size();i++){
				switch(i){
				case 0:
					finalTagValue="<tem:DE112>"+data.get(i)+"</tem:DE112>";
					break;
				case 1:
					finalTagValue=finalTagValue+"<tem:DE113>"+data.get(i)+"</tem:DE113>";
					break;
				case 2:
					finalTagValue=finalTagValue+"<tem:DE114>"+data.get(i)+"</tem:DE114>";
					break;
				case 3:
					finalTagValue=finalTagValue+"<tem:DE115>"+data.get(i)+"</tem:DE115>";
					break;
				case 4:
					finalTagValue=finalTagValue+"<tem:DE116>"+data.get(i)+"</tem:DE116>";
					break;
				case 5:
					finalTagValue=finalTagValue+"<tem:DE117>"+data.get(i)+"</tem:DE117>";
					break;
				}
			}
			log.debug("Final tag value :"+finalTagValue);
			argInMsg=argInMsg.replace(rbdXML, finalTagValue);
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
    

    
	public static void main(String[] args) { HashMap hm = new HashMap();
	BFDEncryptRBD_REQOUT test = new BFDEncryptRBD_REQOUT();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	/*"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"*/
	, "", new HashMap(), new ErrorHandler());
	}
   
    
}