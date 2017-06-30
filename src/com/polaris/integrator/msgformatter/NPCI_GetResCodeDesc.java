package com.polaris.integrator.msgformatter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.polaris.integrator.webtools.properties.ConfigProperties;

public class NPCI_GetResCodeDesc extends UserRule {
	private static Logger log = LoggerFactory
			.getLogger(NPCI_GetResCodeDesc.class);

	public int Process(HashMap hashmap, String pInMsg, String argFldId,
			HashMap hashmap1, ErrorHandler errorhandler) {
		log.debug("Input message in  NPCI_GetResCodeDesc User_Exit :  " + pInMsg);
		this._cresultMessage = new String[1];

		if (pInMsg == null) {
			this._iresultCode = 1;
			this._cresultMessage[0] = "Data not found in InputHash ";
			return this._iresultCode;
		}

		try {
			log.debug("Input message in  NPCI_GetResCodeDesc User_Exit :  " + pInMsg);

			pInMsg = ConfigProperties.get("NPCI", pInMsg);

			System.out.println("Error Description..." + pInMsg);
			this._iresultCode = 0;
			this._cresultMessage[0] = pInMsg;

			log.debug("Response Message from the NPCI_GetResCodeDesc is ==>"
					+ this._cresultMessage[0]);
			
			return this._iresultCode;
		} catch (Exception e) {
			this._iresultCode = 2;
			this._cresultMessage[0] = "Exception in NPCI_GetResCodeDesc user exit "
					+ e;
			log.error("Exception in NPCI_GetResCodeDesc : ", e);
		}
		return this._iresultCode;
	}

}