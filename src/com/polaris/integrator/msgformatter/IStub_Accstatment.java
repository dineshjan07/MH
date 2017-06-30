package com.polaris.integrator.msgformatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polaris.integrator.webtools.properties.ConfigProperties;

public class IStub_Accstatment extends UserRule {

	private static Logger log = LoggerFactory.getLogger(IStub_Accstatment.class);

	public int Process(HashMap hashmap, String inMsg, String argFldId, HashMap hashmap1,
			ErrorHandler errorhandler) {
		try {
			

			log.debug("Inside IStub_Accstatment  --->>>" + inMsg);
	

			this._cresultMessage = new String[1];
			this._bypassMF = false;
			if (inMsg == null) {
				this._iresultCode = 1;
				this._cresultMessage[0] = "Input message is null.";
				return this._iresultCode;
			}
			String finalResponse = "";
			String subStr1[];
			if(inMsg.contains("res_Seg_No")){
			String frontvalue=inMsg.substring(0,inMsg.indexOf("res_Seg_No"));
			System.out.println("frontvalue.."+frontvalue);
			String delimiter = "res_Seg_No";
			subStr1 = inMsg.split(delimiter);
			if (subStr1.length > 1) {
				System.out.println("subStr1.length.."+subStr1.length);
				for (int j = 1; j < subStr1.length; j++) {
					String record=subStr1[j];
					String flag =getValue(record, "Drcr=");	
					String DAmount =getValue(record, "Db_Amount=");
					String CAmount =getValue(record, "Cr_Amount=");
					
					if(flag.equals("D")){
						subStr1[j]=subStr1[j].replace("Cr_Amount="+CAmount, "Cr_Amount=0");
					}
					else{
						subStr1[j]=subStr1[j].replace("Db_Amount="+DAmount, "Db_Amount=0");
					}
					
					finalResponse+="res_Seg_No"+subStr1[j];
				}
			}

			finalResponse=frontvalue+finalResponse;
			}
			else{
				finalResponse=inMsg;
			}
			
			System.out.println("Final response  --->>>" + finalResponse);
			
			log.debug("Final response  --->>>" + finalResponse);

			String res_Result = finalResponse;
			this._cresultMessage[0] = res_Result;
			return this._iresultCode;

		}
		catch (Exception exception) {
			this._iresultCode = 2;
			this._cresultMessage[0] = ("Exception in IStub_Accstatment " + exception);
			log.error("Exception in IStub_Accstatment: ", exception);
		}
		return this._iresultCode;

	}



	public String getValue(String input, String key) {
		String reqValue = input.substring(input.indexOf(key) + (key).length(), input.length());
		String value = reqValue.substring(0, reqValue.indexOf("~*"));
		return value;

	}

}
