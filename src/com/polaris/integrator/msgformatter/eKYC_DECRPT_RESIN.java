package com.polaris.integrator.msgformatter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.polaris.integrator.webtools.properties.ConfigProperties;
public class eKYC_DECRPT_RESIN
  extends UserRule
{
  private static Logger log = LoggerFactory.getLogger(eKYC_DECRPT_RESIN.class);
  
  public int Process(HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1, ErrorHandler erHandler)
  {
    log.debug("Inside CommonResIn  --->>>" + pIPMsg);
    this._cresultMessage = new String[1];
    if (pIPMsg == null)
    {
      this._iresultCode = 1;
      this._cresultMessage[0] = "Data not found in InputHash ";
      return this._iresultCode;
    }
    try
    {
    	String decryptedMsg="";
    	PublicKey pk = UIDAIEncryptionUtility.getPublicKey();
      byte[] ptext = pIPMsg.getBytes();
      String inputMsg = new String(ptext, "UTF-8");
      
      inputMsg = inputMsg.replaceAll("&lt;", "<");
      inputMsg = inputMsg.replaceAll("&gt;", ">");
     // inputMsg =inputMsg.substring(inputMsg.indexOf("<return", 0));
      inputMsg = removeXmlStringNamespaceAndPreamble(inputMsg);
      log.debug("inputMsg after removing Namespace ==>" + inputMsg);
      inputMsg = inputMsg.replaceAll("</return>", "").replaceAll("<Header/>", "").replaceAll("<return>", "").replaceAll("</Body>", "").replaceAll("</Envelope>", "").replaceAll("<Envelope>", "");
	inputMsg = inputMsg.replaceAll("<Body>", "").replaceAll("<EKYC_NPCI_Biometric_RequestResponse>", "").replaceAll("</EKYC_NPCI_Biometric_RequestResponse>", "").replaceAll("<EKYC_NPCI_Biometric_RequestResult>", "").replaceAll("</EKYC_NPCI_Biometric_RequestResult>", "");
      StringBuffer coutputStr = new StringBuffer(inputMsg);
      if(inputMsg.contains("<Resp")){
    	  String status;
    		status=inputMsg.substring(inputMsg.indexOf("<Resp status"),inputMsg.indexOf("ko")).replace("<Resp status=", "");
    		String encryptedMsg="";
        if(status.contains("0")){
    			log.debug("Decryption Required");
    			encryptedMsg=inputMsg.substring(inputMsg.indexOf("<Resp"),inputMsg.lastIndexOf(">"));
    			encryptedMsg=encryptedMsg.substring(encryptedMsg.indexOf(">"),encryptedMsg.lastIndexOf("</Resp")).replace(">", "");
    			log.debug("encryptedMsg..."+encryptedMsg);
			 decryptedMsg=UIDAIEncryptionUtility.decrypt(encryptedMsg,pk);
			 inputMsg=inputMsg.replace(encryptedMsg, decryptedMsg);
    		}
    		else{
    			log.debug("Decryption not Required");
    		}
        
      }
      
      this._iresultCode = 0;
      this._cresultMessage[0] = inputMsg.toString();
      log.debug("Response Message from the DF_RESPONSE_COMP_IN  is ==>" + this._cresultMessage[0]);
      return this._iresultCode;
    }
    catch (Exception e)
    {
      this._iresultCode = 2;
      this._cresultMessage[0] = ("Exception DF_RESPONSE_COMP_IN user exit " + e);
      log.error("Exception in DF_RESPONSE_COMP_IN : ", e);
    }
    return this._iresultCode;
  }
  
  public static String removeXmlStringNamespaceAndPreamble(String xmlString)
  {
    return 
    

      xmlString.replaceAll("(<\\?[^<]*\\?>)?", "").replaceAll(" xmlns.*?(\"|').*?(\"|')", "").replaceAll("(<)(\\w+:)(.*?>)", "$1$3").replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
  }

 
}

