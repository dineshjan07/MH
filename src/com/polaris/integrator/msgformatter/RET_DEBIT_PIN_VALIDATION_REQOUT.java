package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polaris.integrator.webtools.properties.ConfigProperties;


public class RET_DEBIT_PIN_VALIDATION_REQOUT extends UserRule {

	private static Logger log = LoggerFactory
			.getLogger(RET_DEBIT_PIN_VALIDATION_REQOUT.class);

	public int Process(HashMap orderedhashtable, String pIPMsg, String s,
			HashMap orderedhashtable1, ErrorHandler erHandler) {

		log.debug("Inside RET_DEBIT_PIN_VALIDATION_REQOUT  --->>>" + pIPMsg);
		if(pIPMsg.contains("<xsd:request>")){
			pIPMsg=pIPMsg.replace("<xsd:request>","<xsd:request><![CDATA[");
			pIPMsg=pIPMsg.replace("</xsd:request>","]]></xsd:request>");
		}
		if(pIPMsg.contains("<ProxyNumber>")&&pIPMsg.contains("<EPinBlock>")){
			String cardNumber=pIPMsg.substring(pIPMsg.indexOf("<ProxyNumber>")+13, pIPMsg.indexOf("</ProxyNumber>"));
			String pin=pIPMsg.substring(pIPMsg.indexOf("<EPinBlock>")+11, pIPMsg.indexOf("</EPinBlock>"));
			log.debug("Card Number"+cardNumber);
			log.debug("Pin number"+pin);
			try {
			String encryptedPin=DF_PinBlockEncryptionUtil.encryptPinBlock(cardNumber, pin, 168);
			pIPMsg=pIPMsg.replace(pin, encryptedPin);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.debug("Final String"+pIPMsg);
		_cresultMessage = new String[1];
				_iresultCode = 0;
				_cresultMessage[0] = pIPMsg;

				log.debug("Response Message from the NCB_CHQ_BK_STATUS_INQ_REQOUT  is ==>"
						+ _cresultMessage[0]);
				return _iresultCode;
		
		
	}
}