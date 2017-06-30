package com.polaris.integrator.msgformatter;

import com.polaris.integrator.webtools.properties.ConfigProperties;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class CBS_ISO_MAPREQOUT
  extends UserRule
{
  private static Logger log = LoggerFactory.getLogger(CBS_ISO_MAPREQOUT.class);
  
  public int Process(HashMap orderedhashtable, String pIPMsg, String s, HashMap orderedhashtable1, ErrorHandler erHandler)
  {
    log.debug("Inside CBS_ISO_MAPREQOUT  --->>>" + pIPMsg);
    this._cresultMessage = new String[1];
    if (pIPMsg == null)
    {
      this._iresultCode = 1;
      this._cresultMessage[0] = "Data not found in InputHash ";
      return this._iresultCode;
    }
    try
    {
      long startTime = System.currentTimeMillis();
      
      log.debug("Start processing time in millseconds............." + startTime + "ms");
      String inputStr = pIPMsg;
      log.debug("inputStr : " + inputStr);
      String tranID = "";
      tranID = getValue(inputStr, "TranID=");
      log.debug("tranID...." + tranID);
      log.debug("tranID.length()...." + tranID.length());
      log.debug("tranID.trim().length()...." + tranID.trim().length());
      if (inputStr.contains("STAN="))
      {
        inputStr = inputStr.replace("STAN=", "req_systemTraceAuditNumber=");
        
        log.debug("inputStr......" + inputStr);
      }
      else
      {
        log.debug("Ref no to stan no");
        inputStr = inputStr.replace("req_Ref_No=", "req_systemTraceAuditNumber=");
        log.debug("inputStr...inside ref no" + inputStr);
      }
      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      Date date = new Date();
      String Time = dateFormat.format(date);

      DateFormat field7dateFormat = new SimpleDateFormat("MMddHHmmss");
      Date field7date = new Date();
      String F7date = field7dateFormat.format(field7date);

      log.debug("field 7 date.."+F7date);
      DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
      Date date1 = new Date();
      String captureDate = dateFormat1.format(date1);
      inputStr = inputStr.replace("req_Time=null~*", "req_Time=" + Time + "~*").replace(
      "req_captureDate=null~*", "req_captureDate=" + captureDate + "~*").replace("req_tranmisssionAmount=null~*", "req_tranmisssionAmount=" + F7date + "~*");
      if ((tranID.equals("REMITTANCE")) || (tranID.equals("INTERNATIONAL")))
      {
        log.debug("Log-Aug20-2016-Bug analysis after If.." + inputStr);
        String remarks = getValue(inputStr, "req_Ext_Remarks=");
        String stanno = getValue(inputStr, "req_systemTraceAuditNumber=");
        log.debug("refno....." + stanno);
        String inputCCY = getValue(inputStr, "req_Input_CCY=");
        String inputcurrCode = ConfigProperties.get("CBS", inputCCY);
        String tranCCY = getValue(inputStr, "req_Txn_CCY=");
        String trancurrCode = ConfigProperties.get("CBS", tranCCY);
        String tranMode = getValue(inputStr, "TransactionMode=");
        
        log.debug("trancurrCode...." + trancurrCode);
        
        log.debug("inputcurrCode.." + inputcurrCode);
        log.debug("transactionMode...." + tranMode);
        String req_Ref_No = inputStr.substring(
          inputStr.indexOf("req_Ref_No=") + "req_Ref_No=".length(), inputStr.length());
        
        log.debug("inputStr.." + inputStr);
        String addressloction = "req_cardAcceptorLocation=" + remarks + "-" + stanno + "~*";
        String amount = getValue(inputStr, "req_Amt=");
        String ifsccode = getValue(inputStr, "ifscCode=");
        String Ben_Name = getValue(inputStr, "req_Ben_Name=");
       /* String amount = inputStr
          .substring(inputStr.indexOf("req_Amt"), inputStr.indexOf("req_systemTraceAuditNumber"))
          .replace("req_Amt=", "").replace("~*", "");
        String ifsccode = inputStr
          .substring(inputStr.indexOf("ifscCode"), inputStr.indexOf("req_Ben_Bank"))
          .replace("ifscCode=", "").replace("~*", "");
        String Ben_Name = inputStr
          .substring(inputStr.indexOf("req_Ben_Name"), inputStr.indexOf("req_Msg_Type"))
          .replace("req_Ben_Name=", "").replace("~*", "");*/
        
        inputStr = inputStr.replace("req_Credit_Acc_No", "req_Cr_Acc_No");
        
        log.debug("ifsccode.." + ifsccode);
        log.debug("Ben_Name.." + Ben_Name);
        
        log.debug("inputStr.." + inputStr);
        
        String req_currencys = ifsccode + "|" + Ben_Name;
        
        log.debug("req_currencys.." + req_currencys);
        System.out.println("AMT : " + amount);
        String amounts = amount.replace(".", "");
        
        BigInteger amt = new BigInteger(amounts);
        String formattedNumber = String.format("%016d", new Object[] { amt });
        log.debug("16 digit formated amount.." + formattedNumber);
        inputStr = inputStr.replace("req_Amt=" + amount, "req_Amt=" + formattedNumber);
        log.debug("Final amount.." + inputStr);
        
        log.debug("Final amount.." + inputStr);
        
        String field126 = "";
        String dealType = getValue(inputStr, "DEAL_TYPE=");
        log.debug("dealType....." + dealType);
        if ((dealType.equals("SXB")) || (dealType.equals("SCB"))) {
          field126 = "0|0|0|P";
        } else {
          field126 = "0|0|0|S";
        }
        if (tranID.equals("REMITTANCE"))
        {
          if (tranMode.equals("TISS"))
          {
            inputStr = inputStr + "req_processingCode=403000~*req_currencys=" + req_currencys + "~*";
          }
          else if (tranMode.equals("NEFT"))
          {
            inputStr = inputStr + "req_processingCode=401000~*req_currencys=" + req_currencys + "~*";
          }
          else if (tranMode.equals("RTGS"))
          {
            inputStr = inputStr + "req_processingCode=402000~*req_currencys=" + req_currencys + "~*";
          }
          else if (tranMode.equals("IMPSOUT"))
          {
            inputStr = inputStr + "req_processingCode=420000~*req_currencys=" + req_currencys + "~*";
         
            String TransactionType = getValue(inputStr, "TransactionType=");
            String RemmiterMobileNo = getValue(inputStr, "RemmiterMobileNo=");
       	    String RemmiterMMID = getValue(inputStr, "RemmiterMMID=");
            String BeniIFSCCode = getValue(inputStr, "BeniIFSCCode=");
            String BeniMMID = getValue(inputStr, "BeniMMID=");
            String BeniAccountNo = getValue(inputStr, "BeniAccountNo=");
            String BeniNBINo = getValue(inputStr, "BeniNBINo=");
            String BeniAadharNo = getValue(inputStr, "BeniAadharNo=");
            String BeniMobileNo = getValue(inputStr, "BeniMobileNo=");
            String field125,TransactionCode="";
            if(TransactionType.equals("P2P")){
            	 TransactionCode="45";
            }
            else if(TransactionType.equals("P2A")){
            	TransactionCode="48";
            }
            field125=TransactionCode+"|"+RemmiterMobileNo+"|"+RemmiterMMID+"|"+BeniMobileNo+"|"+BeniNBINo+"|"+BeniAccountNo+"|"+Ben_Name+"|"+ifsccode+"|"+BeniMMID+"|"+BeniAadharNo+"~*";
            inputStr=inputStr+"req_accountbalance="+field125+"~*";
            /*String BeniNBINo = getValue(inputStr, "BeniNBINo=");
            String BeniAadharNo = getValue(inputStr, "BeniAadharNo=");*/
            
            
          }
             else if (tranMode.equals("IMPSIN"))
          {
            inputStr = inputStr + "req_processingCode=410000~*req_currencys=" + req_currencys + "~*";
          }
          else if (tranMode.equals("CEFT"))
          {
            String bankCode = ifsccode.substring(0, 4);
            String branchCode = ifsccode.substring(4);
            
            String beneName = getValue(inputStr, "req_Ben_Name=");
            String particulars = getValue(inputStr, "req_Ext_Remarks=");
            String reference = getValue(inputStr, "req_Ref_No=");
            
            inputStr = inputStr + "req_processingCode=407000~*req_currencys=" + bankCode + "|" + branchCode + "|" + beneName + "|" + particulars + "|" + reference + "|~*";
          }
          inputStr = inputStr + addressloction + "~*req_transcationCurrencyCode=" + trancurrCode + "~*" + "req_currency=" + inputcurrCode + "~*" + "req_currency1=" + trancurrCode + "~*" ;
          log.debug("Final amount after adding 61 same field.." + inputStr);
        }
        else if (tranID.equals("INTERNATIONAL"))
        {
          inputStr = inputStr + addressloction + "~*req_transcationCurrencyCode=" + trancurrCode + "~*" + "req_currency=" + inputcurrCode + "~*" + "req_currencys=" + req_currencys + "~*req_processingCode=404000~*" + "req_accountTypeDescs=" + field126 + "~*";
          log.debug("Final amount after adding 61 same field.." + inputStr);
        }
        else
        {
          inputStr = inputStr + addressloction + "~*req_transcationCurrencyCode=" + inputcurrCode + "~*" + "req_currency=" + trancurrCode + "~*req_processingCode=430000~*";
          log.debug("Final amount after adding 61 multi field.." + inputStr);
        }
      }
      if (tranID.equals("RET_MMID_CREATION")) {

 	     inputStr = inputStr.replace("MobileNumber", "res_availableBalance").replace("AccountNumber", "accNo");
 	   }
      inputStr = getISOMessage(inputStr, erHandler);
      log.debug("ISO Request...." + inputStr);
      
      String isoaciiStr = converttoAscci(inputStr);
      log.debug("Input message as ascii " + isoaciiStr);
      
      this._iresultCode = 0;
      this._cresultMessage[0] = isoaciiStr;
      
      log.debug("Response Message from the CBS_ISO_MAPREQOUT  is ==>" + this._cresultMessage[0]);
      return this._iresultCode;
    }
    catch (Exception e)
    {
      this._iresultCode = 2;
      this._cresultMessage[0] = ("Exception in In_Data_UserExit CBS_ISO_MAPREQOUT" + e);
      log.error("Exception in CBS_ISO_MAPREQOUT : ", e);
    }
    return this._iresultCode;
  }
  
  public String getValue(String input, String key)
  {
    String reqValue = input.substring(input.indexOf(key) + key.length(), input.length());
    String value = reqValue.substring(0, reqValue.indexOf("~*"));
    return value;
  }
  
  public String formatingAmount(String amount)
  {
    String reqAmt = "";
    if (amount.contains("."))
    {
      String[] reqAmtArr = amount.split("\\.");
      if (reqAmtArr[1].length() > 2) {
        reqAmtArr[1] = reqAmtArr[1].substring(0, 1);
      }
      reqAmt = reqAmtArr[0] + reqAmtArr[1];
    }
    else
    {
      reqAmt = amount + "00";
    }
    BigInteger amt = new BigInteger(reqAmt);
    
    String formattedNumber = String.format("%016d", new Object[] { amt });
    
    return formattedNumber;
  }
  
  private String getISOMessage(String input, ErrorHandler erHandler)
    throws Exception
  {
    String isoType = "ISO8583";
    OiAbsFmtrInterface pObject = null;
    HashMap isoHM = new HashMap();
    HashMap iphash = null;
    String output = null;
    int bResult = 1;
    
    LogicInfo logicInfo = new LogicInfo();
    logicInfo.SetFieldDelimiter("=");
    
    isoType = isoType.substring(isoType.indexOf("ISO") + 3, 
      isoType.length());
    isoHM.put("isoType", isoType);
    
    pObject = new OiNVPISOFormatter(erHandler, logicInfo);
    bResult = pObject.Parse(null, input, isoHM);
    if (bResult != 0) {
      throw new Exception("Exception caught in OiNVISOPFormatter.Parse");
    }
    iphash = pObject.GetParsedInMsg();
    if (iphash.containsKey("ISOConvrtMsg"))
    {
      output = (String)iphash.get("ISOConvrtMsg");
      log.debug("ISO MESSAGE:" + output);
    }
    else
    {
      log.error("Converted message hashmap is null");
      throw new Exception("Exception caught in OiNVISOPFormatter.Parse");
    }
    return output;
  }
  
  private String converttoAscci(String input)
    throws Exception
  {
    log.debug("Input as hexadecimal--->>>" + input);
    
    String[] hexTemp = new String[16];
    String bitmap = "";
    String mti = input.substring(0, 4);
    log.debug("mti" + mti);
    String messageTypeHex = convertStringToHex(mti);
    
    String psHex = input.substring(4, 36);
    
    log.debug("psHex" + psHex);
    String actualHex = psHex;
    for (int i = 0; i < 16; i++)
    {
      for (int j = 0; j < 2;)
      {
        hexTemp[i] = psHex.substring(j, j + 2);
        j += 2;
        psHex = psHex.substring(j, psHex.length());
      }
      if (hexTemp[i].startsWith("0")) {
        hexTemp[i] = hexTemp[i].substring(1);
      }
      bitmap = bitmap.concat(hexTemp[i]);
    }
    String data = input.substring(36, input.length());
    log.debug("data" + data);
    String datahex = convertStringToHex(data);
    log.debug("datahex" + datahex);
    String isotempmessage = mti.concat(bitmap).concat(data);
    
    log.debug("mti length  : " + mti.length());
    log.debug("bitmap length  : " + bitmap.length());
    log.debug("data length  : " + data.length());
    log.debug("isotempmessage" + isotempmessage);
    int i = isotempmessage.length();
    
    log.debug("isotempmessage  length" + i);
    String isoHexStr = messageTypeHex.concat(actualHex).concat(datahex);
    
    String length = Integer.toString(i);
    
    String isoaciiStr = convertHexToString(isoHexStr);
    
    isoaciiStr = "0".concat(length).concat(isoaciiStr);
    
    log.debug("ISO 8583 -0800 Hexadecimal String :" + isoHexStr);
    log.debug("ISO ASCII String :" + isoaciiStr);
    log.debug("ISO ASCII String length:" + isoaciiStr.length());
    log.debug("Output from UNB_0800_Req_out ISO8583 -0800- message--->>>" + 
      isoaciiStr);
    
    log.debug("**********************************");
    String hexReq = convertStringToHex(isoaciiStr);
    
    log.debug("Hexa val : " + hexReq);
    
    log.debug("Hexa val : " + convertHexToString(hexReq));
    return isoaciiStr;
  }
  
  public String convertStringToHex(String str)
  {
    char[] chars = str.toCharArray();
    
    StringBuffer hex = new StringBuffer();
    for (int i = 0; i < chars.length; i++) {
      hex.append(Integer.toHexString(chars[i]));
    }
    return hex.toString();
  }
  
  public String convertHexToString(String hex)
  {
    StringBuilder sb = new StringBuilder();
    StringBuilder temp = new StringBuilder();
    for (int i = 0; i < hex.length() - 1; i += 2)
    {
      String output = hex.substring(i, i + 2);
      int decimal = Integer.parseInt(output, 16);
      sb.append((char)decimal);
      temp.append(decimal);
    }
    log.debug(" iso Decimal String : " + temp.toString());
    
    return sb.toString();
  }
}
