package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EximCards_COMMON_RESIN
  extends UserRule
{
  private static Logger log = LoggerFactory.getLogger(EximCards_COMMON_RESIN.class);
  
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
      byte[] ptext = pIPMsg.getBytes();
      String inputMsg = new String(ptext, "UTF-8");
      
      inputMsg = inputMsg.replaceAll("&lt;", "<");
      inputMsg = inputMsg.replaceAll("&gt;", ">");
      inputMsg = removeXmlStringNamespaceAndPreamble(inputMsg);
      log.debug("inputMsg after removing Namespace ==>" + inputMsg);
      inputMsg = inputMsg.replaceAll("</return>", "").replaceAll("<Header/>", "").replaceAll("<return>", "").replaceAll("</Body>", "").replaceAll("</Envelope>", "").replaceAll("<Envelope>", "");
	  inputMsg = inputMsg.replaceAll("<Body>", "").replaceAll("<serviceCallResponse>", "").replaceAll("</serviceCallResponse>", "");
      StringBuffer coutputStr = new StringBuffer(inputMsg);
      this._iresultCode = 0;
      this._cresultMessage[0] = coutputStr.toString();
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

