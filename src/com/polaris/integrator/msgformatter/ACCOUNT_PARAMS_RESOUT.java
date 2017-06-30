package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACCOUNT_PARAMS_RESOUT
  extends UserRule
{
  private static Logger log = LoggerFactory.getLogger(ACCOUNT_PARAMS_RESOUT.class);
  
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
      
      StringBuffer coutputStr=null ;
      String inputStr=pIPMsg;
  	  String subString=pIPMsg;
  	int start=-1;
	int end=-1;
String startText="res_No_Of_Seg=";
while(inputStr.contains(startText)){
	if(inputStr.contains(startText)){
		start=inputStr.indexOf(startText,start);
		end=inputStr.indexOf(startText,start+1);
	}
	if(start>0&&end>0){
	String stringToReplace=inputStr.substring(start, end);
	String newString=changeParams(stringToReplace);
	inputStr=inputStr.replace(stringToReplace, newString);
	}
	else{
		String stringToReplace=inputStr.substring(start);
		String newString=changeParams(stringToReplace);
		inputStr=inputStr.replace(stringToReplace, newString);
		break;
	}
	 start=end;

	 subString=inputStr.substring(end);
}
	
	log.info("Converted NVP: "+inputStr);
      this._iresultCode = 0;
      this._cresultMessage[0] = inputStr.toString();
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
   String changeLAParams(String nvpMsg){
		nvpMsg=nvpMsg.replace("AccountNumber=", "accloan=");
		nvpMsg=nvpMsg.replace("AccountCCY=", "loantype_code=");
		nvpMsg=nvpMsg.replace("AccountTypeDesc=", "loantype_desc=");
		nvpMsg=nvpMsg.replace("AccountCCY=", "loan_curr=");
		nvpMsg=nvpMsg.replace("AccountBalance=", "os_amt=");
		nvpMsg=nvpMsg.replace("AccountStatus=", "status=");
		String currency=getValue(nvpMsg, "loantype_code=");
		nvpMsg=nvpMsg+"curr="+currency+"~*loan_curr="+currency+"~*ProductCode=5001~*res_product=LOAN~*loanno=~*loan_amt=~*inst_amt=~*total_amt=~*no_of_inst=~*loan=~*";
		return nvpMsg;
	}
	 String changeDAParams(String nvpMsg){
		nvpMsg=nvpMsg.replace("AccountNumber=", "AccountNum=");
		nvpMsg=nvpMsg.replace("AccountCCY=", "AccTypeCode=");
		nvpMsg=nvpMsg.replace("AccountTypeDesc=", "AccTypeDesc=");
		nvpMsg=nvpMsg.replace("AccountBalance=", "PrincipalDep=");
		nvpMsg=nvpMsg.replace("AccountStatus=", "status=");
		String currency=getValue(nvpMsg, "AccTypeCode=");
		nvpMsg=nvpMsg+"curr="+currency+"~*AcctCurrCode="+currency+"~*ProductCode=2001~*res_product=DEPOSIT~*AcctCurrDesc=~*IncreaseDecrease=~*ShortName=~*RefNo=~*Branch=~*DealTypeCode=~*InterestRate=~*StartDate=~*MaturityDate=~*Period=~*MaturityAmt=~*Action=~*RolloverDate=~*AdjustmentAmt=~*SettlementAccount=~*SettlementCustomerName=~*SettlementAccCurrency=~*SettlementAccBalance=~*ChangeDate=~*MovementSeq=~*FundAcBranch=~*FundAcBase=~*FundAcSuffix=~*deposit=~*";
		return nvpMsg;
	}
	 String changeAccountParams(String nvpMsg){
		nvpMsg=nvpMsg.replace("AccountNumber=", "acccode=");
		nvpMsg=nvpMsg.replace("AccountCCY=", "acctypecode=");
		nvpMsg=nvpMsg.replace("AccountTypeDesc=", "AccTypeDesc=");
		nvpMsg=nvpMsg.replace("AccountBalance=", "accbal=");
		nvpMsg=nvpMsg.replace("AccountStatus=", "status=");
		String currency=getValue(nvpMsg, "acctypecode=");
		nvpMsg=nvpMsg+"curr="+currency+"~*hold_amt=~*amt_under_clear=~*account=~*res_product=ACCOUNT~*status=Active~*~*ProductCode=202~*custtypecode=SB-Retail Customer-Standard Savings~*acctype=SB-Retail Customer-Standard Savings~*";
		return nvpMsg;
	}
	 String changeParams(String nvpMsg){
		String accType=getValue(nvpMsg, "AccountType=");
		if (accType.equals("LA")){
			nvpMsg=changeLAParams(nvpMsg);
		}
		if (accType.equals("DA")){
			nvpMsg=changeDAParams(nvpMsg);
		}
		if (accType.equals("CA")||accType.equals("SA")){
			nvpMsg=changeAccountParams(nvpMsg);
		}
		return nvpMsg;
	}

	public  String getValue(String input, String key) {
		String value = "";
		if (input.contains(key)) {

			String reqValue = input.substring(
					input.indexOf(key) + key.length(), input.length());
			value = reqValue.substring(0, reqValue.indexOf("~*"));
		}
		return value;

	}

}

