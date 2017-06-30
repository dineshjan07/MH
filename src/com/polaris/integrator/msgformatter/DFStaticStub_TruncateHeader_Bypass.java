/*****************************************************************************************************
 *  File Name        :  TruncateHeader_Bypass.java
 *  Author           :  Intellect Integrator Team
 *  Created Date     :  
 *  Product Name     :  Intellect Integrator
 *  Module Name      :  MsgFormatter
 *  Purpose          :  This class Truncate the Header and bypass the message    
 *  CopyRight        :  Copyright  2005 Polaris Software Lab. All rights reserved.
 **************************************************************************/

package com.polaris.integrator.msgformatter;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DFStaticStub_TruncateHeader_Bypass extends UserRule {

	private static Logger log = LoggerFactory.getLogger(DFStaticStub_TruncateHeader_Bypass.class);

	public DFStaticStub_TruncateHeader_Bypass() {
	}

	public int Process(HashMap argParsedFldHM, String argInMsg, String argFldId,
			HashMap argFldFmtHM, ErrorHandler argErrHandler) {
		try {

			super._cresultMessage = new String[1];
			String Input = argInMsg;
			if (log.isDebugEnabled())
				log.debug("DFStaticStub_TruncateHeader_Bypass : InputMessage --->>>" + Input);
			super._bypassMF = true;
			super._cresultMessage[0] = Input;
			super._iresultCode = 0;

			if (log.isDebugEnabled())
				log.debug("\n Inside UserExit : coutputStr : " + Input);
			if (log.isDebugEnabled())
				log.debug("\n Inside UserExit returns : _iresultCode : " + _iresultCode);
			if (log.isDebugEnabled())
				log.debug("\n Leaving UserExit DFStaticStub_TruncateHeader_Bypass");
			return super._iresultCode;
		}
		catch (Exception exception) {
			super._iresultCode = 1000;
			log.error("Exception in DFStaticStub_TruncateHeader_Bypass : ", exception);
		}
		return super._iresultCode;
	}
}