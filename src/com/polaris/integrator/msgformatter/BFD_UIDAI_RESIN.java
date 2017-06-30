package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polaris.integrator.util.JSONConverter;

public class BFD_UIDAI_RESIN
  extends UserRule
{
  private static Logger log = LoggerFactory.getLogger(BFD_UIDAI_RESIN.class);
  
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
      
      /*inputMsg = inputMsg.replaceAll("&lt;", "<");
      inputMsg = inputMsg.replaceAll("&gt;", ">");*/
//      inputMsg =inputMsg.substring(inputMsg.indexOf("<soap:Body", 0));
		log.debug("inputMsg after removing header once again for stub only==>"+ inputMsg);
      inputMsg = removeXmlStringNamespaceAndPreamble(inputMsg);
      log.debug("inputMsg after removing Namespace ==>" + inputMsg);
      
      inputMsg = inputMsg.replaceAll("</return>", "").replaceAll("<Header/>", "").replaceAll("</Body>", "").replaceAll("</Envelope>", "").replaceAll("<Envelope>", "");
      inputMsg = inputMsg.replaceAll("<Body>", "");
     
      log.debug("JSON.... ==>" + inputMsg);
    String jSONString=  inputMsg.substring(inputMsg.indexOf("{\"",0),inputMsg.indexOf("\"}")+2);
      log.debug("JSON String:"+jSONString);
      String ranksXml="";
      if(jSONString.contains("<Ranks>")){
       ranksXml=jSONString.substring(jSONString.indexOf("<Ranks>"),jSONString.indexOf("\"}"));
      }
     log.debug("Json string after removing Ranks xml"+jSONString.replace(ranksXml,"")); 
  String jsonToXML = JSONConverter.getXMLFromJSon(jSONString.replace(ranksXml,""));
           /* log.debug("TEST.... ==>" + inputMsg);  */
  jsonToXML = jsonToXML.replace(":\"",":").replace("}\"}", "}}").replaceAll("\\\\","");
            /*log.debug("TEST1.... ==>" + inputMsg);
            inputMsg = JSONConverter.getXMLFromJSon(inputMsg);*/
            
            /*inputMsg = inputMsg.replace("\":\"","\":").replace("\"}\"", "\"}");*/
        //    log.debug("XML.... ==>" + inputMsg);
  if(jsonToXML.contains("<uidai_authentication_data>")){
  	String code=jsonToXML.substring(jsonToXML.indexOf("<uidai_authentication_data>")+27,jsonToXML.indexOf("</uidai_authentication_data>"));
  	log.debug("uidai code : "+code);
  	jsonToXML=jsonToXML.replace(code,"<code>"+code+"</code>").replace("</code>", "</code>"+ranksXml);
  }
  	log.debug("JSON to XML :"+jsonToXML);
  	inputMsg=inputMsg.replace(jSONString, jsonToXML).replaceAll("<process_BFDResponse>", "").replaceAll("</process_BFDResponse>","");
  	log.debug("Final response : "+inputMsg);
  	StringBuffer coutputStr = new StringBuffer(inputMsg);
      this._iresultCode = 0;
      this._cresultMessage[0] = coutputStr.toString();
      log.debug("Response Message from the AGS_RESPONSE_COMP_IN  is ==>" + this._cresultMessage[0]);
      return this._iresultCode;
    }
    catch (Exception e)
    {
      this._iresultCode = 2;
      this._cresultMessage[0] = ("Exception in AGS_RESPONSE_COMP_IN user exit " + e);
      log.error("Exception in AGS_RESPONSE_COMP_IN : ", e);
    }
    return this._iresultCode;
  }
  
  public static String removeXmlStringNamespaceAndPreamble(String xmlString)
  {
    return 
    

      xmlString.replaceAll("(<\\?[^<]*\\?>)?", "").replaceAll(" xmlns.*?(\"|').*?(\"|')", "").replaceAll("(<)(\\w+:)(.*?>)", "$1$3").replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
  }
  
 public static void main(String[] args)
 {
   HashMap hm = new HashMap();
   String input="<process_BFDResponse><process_BFDResult>{\"response_code\":\"00\",\"stan\":\"142323\",\"rsp_time\":\"153913\",\"rsp_date\":\"0306\",\"rrn\":null,\"auth_code\":null,\"err_desc\":\"TransactionSuccessful.\",\"uidai_authentication_data\":\"172001032a0e3c5c0a6ab48e9a8206d5a8930ca7d002128<Ranks><Rank pos=\"LEFT_MIDDLE\" val=\"5\"/><Rank pos=\"LEFT_THUMB\" val=\"4\"/><Rank pos=\"RIGHT_MIDDLE\"val=\"2\"/><Rank pos=\"LEFT_INDEX\" val=\"1\"/><Rank pos=\"RIGHT_INDEX\" val=\"8\"/><Rank pos=\"LEFT_RING\" val=\"6\"/><Rank pos=\"RIGHT_RING\" val=\"-1\"/><Rank pos=\"LEFT_LITTLE\" val=\"7\"/><Rank pos=\"RIGHT_THUMB\" val=\"3\"/><Rank pos=\"RIGHT_LITTLE\" val=\"9\"/></Ranks>\"}</process_BFDResult></process_BFDResponse>";
   BFD_UIDAI_RESIN test = new BFD_UIDAI_RESIN();
   test.Process(
     new HashMap(), 
     
     input, 
     "", new HashMap(), new ErrorHandler());
 }
}
