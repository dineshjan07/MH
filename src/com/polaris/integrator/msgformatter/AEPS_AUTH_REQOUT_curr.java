package com.polaris.integrator.msgformatter;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AEPS_AUTH_REQOUT_curr extends UserRule {

	private static Logger log = LoggerFactory.getLogger(AEPS_AUTH_REQOUT_curr.class);

	public int Process(HashMap argParsedFldHM, String argInMsg,
			String argFldId, HashMap argFldFmtHM, ErrorHandler argErrHandler) {

		_cresultMessage = new String[1];

		if (argInMsg == null) {
			_iresultCode = 1;
			_cresultMessage[0] = "Data not found in InputHash ";
			return _iresultCode;
		} else {
			try {
				String Input = argInMsg;
				String subStr1[];

				String Bioxml = "", posh = "", Bio = "", pidXML = "";
				String delimiter = "\\~\\*";

				log.debug("AEPS_AUTH_REQOUT UE - Input Message:" + argInMsg);

				// 1. Generate AES session key
				byte[] aesKey;

				aesKey = UIDAIEncryptionUtility.getRandomAESSKey();

				// 2. Get public key
				PublicKey pk = UIDAIEncryptionUtility.getPublicKey();

				// 3. Encrypt session id
				byte[] Skey = UIDAIEncryptionUtility.encryptAESSKey(aesKey, pk);
//				Skey=Skey.replaceAll("\r", "");
//				Skey=Skey.replaceAll("\n", "");
				log.debug("Encrypted AES Skey: " + Skey);

				// 4. Generate PID Block
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				String currentTimeStamp = formatter.format(new Date());
				String ts = getValue(Input, "ts@@");
				String type = getValue(Input, "type@@");
				String ver = getValue(Input, "ver@@");

				/* Bio and Posh Value Iteration */
				subStr1 = Input.split(delimiter);

				for (int i = 0; i < subStr1.length; i++) {
					if (subStr1[i].contains("posh")) {
						String poshstr = subStr1[i];
						posh = poshstr.substring(poshstr.indexOf("@@"))
								.replace("@@", "");

					}
					if (subStr1[i].contains("Bio_VALUE")) {
						String Biostr = subStr1[i];
						Bio = Biostr.substring(Biostr.indexOf("@@")).replace(
								"@@", "");
						Bioxml += "<Bio type=\"" + "FMR" + "\" posh=\"" + posh
								+ "\">" + Bio + "</Bio>";
						log.debug(Bioxml);
					}

				}
				/* Bio and Posh Value Iteration */

				pidXML = "<Pid ts=" + ts + "\" ver=" + ver + "\"><Bios>"
						+ Bioxml + "</Bios></Pid>";
				log.debug("pidXML as plain text: " + pidXML);

				// 5Encrypt PID xml
				byte[] encryptPIDXML = UIDAIEncryptionUtility.encrypt(pidXML.getBytes(),
						aesKey);
//				encryptPIDXML=encryptPIDXML.replace("\r", "");
//				encryptPIDXML=encryptPIDXML.replace("\n", "");
				log.debug("Encrypte PID XML: " + encryptPIDXML);
				
				// 6. Generate Hmac value
				byte[] base64EncodedPidXMLSha256Hash = UIDAIEncryptionUtility
						.getHmacValue(pidXML, aesKey);
//				base64EncodedPidXMLSha256Hash=base64EncodedPidXMLSha256Hash.replaceAll("\r", "");
//				base64EncodedPidXMLSha256Hash=base64EncodedPidXMLSha256Hash.replaceAll("\n", "");
				log.debug("Hmac Value: " + base64EncodedPidXMLSha256Hash);

				String ci = getValue(Input, "ci@@");
				String ac = getValue(Input, "ac@@");
				String sa = getValue(Input, "sa@@");
				String lk = getValue(Input, "lk@@");
				String tag001 = null, tag002, tag003 = null, tag004, tag005, tag006 = null, tag007 = null;
				
				String newSkey=new String(Skey);
				String newbase64EncodedPidXMLSha256Hash=new String(base64EncodedPidXMLSha256Hash);
				String	newencryptPIDXML=new String(encryptPIDXML);
				tag001 = "001" + String.format("%03d", newSkey.length()) + newSkey;
				tag002 = "002" + String.format("%03d", ci.length()) + ci;
				tag003 = "003"
						+ String.format("%03d",
								newbase64EncodedPidXMLSha256Hash.length())
						+ newbase64EncodedPidXMLSha256Hash;
				tag004 = "004" + String.format("%03d", ac.length()) + ac;
				tag005 = "005" + String.format("%03d", sa.length()) + sa;
				tag006 = "006" + String.format("%03d", lk.length()) + lk;
				tag007 = newencryptPIDXML;
				
				
				String field12722 = tag001 + tag002 + tag003 + tag004 + tag005
						+ tag006 + tag007;
				

				log.debug("field12722 ..." + field12722);
				String field12722Length=String.format("%05d", field12722.length());

				log.debug("field12722 length..."
						+field12722Length);
				
				field12722 = field12722Length+ field12722;


				String field127 = String.format("%06d",
						(Integer.parseInt(field12722Length)+5+ 12));
				
				log.debug("field127 length..." + field127);

				String reqString = "AdditionalTags@@" + field127 + "~*";
				log.debug("after reqString added..." + reqString);
				Input = Input + reqString;

				log.debug("after 127 added..." + Input);

				Input = getISOMessage(Input, argErrHandler);

				log.debug("String upto 127 " + Input);

				log.debug("hexa upto 127 " + Input);

				log.debug("String  field12722 " + field12722);

				String filed127BitHex = "0000040080000000";
				String field1272233 = field12722 + "9117";

				String isoaciiStr = converttoAscci(Input, filed127BitHex,
						field1272233);
				
				log.debug("Final msg Hexa:"+convertStringToHex(isoaciiStr));
				_iresultCode = 0;

				_cresultMessage[0] = isoaciiStr;
				return _iresultCode;
			} catch (Exception exception) {

				_iresultCode = 2;
				_cresultMessage[0] = "Exception in AEPS_AUTH_REQOUT"
						+ exception;
				return _iresultCode;
			}

		}
	}

	private String converttoAscciChar(String input) {
		// TODO Auto-generated method stub
		String ascii = "";
		for (char c : input.toCharArray()) {
			ascii += (int) c;
		}
		return ascii;
	}

	private String getISOMessage(String input, ErrorHandler erHandler)
			throws Exception {
		String isoType = "ISO8583";
		OiAbsFmtrInterface pObject = null;
		HashMap isoHM = new HashMap();
		HashMap iphash = null;
		String output = null;
		int bResult = 1;

		LogicInfo logicInfo = new LogicInfo();
		logicInfo.SetFieldDelimiter("@@");

		isoType = isoType.substring(isoType.indexOf("ISO") + 3,
				isoType.length());
		isoHM.put("isoType", isoType);

		pObject = new OiNVPISOFormatter(erHandler, logicInfo);
		bResult = pObject.Parse(null, input, isoHM);
		if (bResult != ErrorHandler.ERROR_STATUS_OK) {
			throw new Exception("Exception caught in OiNVISOPFormatter.Parse");
		}
		iphash = pObject.GetParsedInMsg();

		if (iphash.containsKey("ISOConvrtMsg")) {
			output = (String) iphash.get("ISOConvrtMsg");
			log.debug("ISO MESSAGE:" + output);
		} else {
			log.error("Converted message hashmap is null");
			throw new Exception("Exception caught in OiNVISOPFormatter.Parse");
		}
		return output;

	}

	public String ConvertHexa(String Input) {
		String[] hexTemp = new String[16];
		String bitmap = "";
		String mti = Input.substring(0, 4);
		log.debug("mti" + mti);
		String messageTypeHex = convertStringToHex(mti);
		String psHex = Input.substring(4, 36);
		log.debug("psHex" + psHex);
		String actualHex = psHex;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 2;) {
				hexTemp[i] = psHex.substring(j, j + 2);
				j = j + 2;
				psHex = psHex.substring(j, psHex.length());
			}
			if (hexTemp[i].startsWith("0")) {
				hexTemp[i] = hexTemp[i].substring(1);
			}
			bitmap = bitmap.concat((String) hexTemp[i]);
		}

		String data = Input.substring(36, Input.length());
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
		// String hex = Integer.toHexString( i );
		// log.debug( "ISOhex" + hex );

		// String length=Integer.toString(i);

		// String isoaciiStr = convertHexToString( isoHexStr ) ;
		//
		//
		// isoHexStr=length.concat(isoHexStr);

		log.debug("ISO 8583 -0800 Hexadecimal String :" + (isoHexStr));
		// log.debug( "ISO ASCII String :" + isoaciiStr );
		// log.debug( "ISO ASCII String length:" + isoaciiStr.length() );
		// log.debug(
		// "Output from UNB_0800_Req_out ISO8583 -0800- message--->>>" +
		// isoaciiStr );

		return isoHexStr;
	}

	public String ConvertHexa12722(String Input) {

		String data = Input;
		log.debug("data" + data);
		String datahex = convertStringToHex(data);
		log.debug("datahex" + datahex);

		log.debug("datahex--->>>" + datahex);

		return datahex;
	}

	public String converttoAscci(String input, String field127BitHex,
			String field1272233) throws Exception {

		log.debug("Input as hexadecimal--->>>" + input);

		String[] hexTemp = new String[16];
		String bitmap = "";
		String mti = input.substring(0, 4);
		log.debug("mti" + mti);
		String messageTypeHex = convertStringToHex(mti);

		String psHex = input.substring(4, 36);

		log.debug("psHex" + psHex);
		String actualHex = psHex;

		String data = input.substring(36, input.length());
		log.debug("data" + data);

		String datahex = convertStringToHex(data);
		log.debug("datahex" + datahex);
		// String isotempmessage = mti.concat(actualHex).concat(data)
		// .concat(field127BitHex).concat(field1272233);

		log.debug("mti length  : " + mti.length());
		log.debug("bitmap length  : " + bitmap.length());
		log.debug("data length  : " + data.length());
		// log.debug("isotempmessage" + isotempmessage);

		String isoHexStr = messageTypeHex.concat(actualHex).concat(datahex)
				.concat(field127BitHex);
				//.concat(convertStringToHex(field1272233));
	

		String isoaciiStr = convertHexToString(isoHexStr);
		isoaciiStr=isoaciiStr.concat(field1272233);
		log.debug("After Ascii conversion:" + isoaciiStr);

		int i = isoaciiStr.length();
		String length = String.format("%4s", Integer.toHexString(i)).replace(
				" ", "0");
		log.debug("Length in BCD HEX:" + length);
		length = convertHexToString(length);
		log.debug("Length in Hex format" + convertStringToHex(length));

		isoaciiStr = length.concat(isoaciiStr);

		log.debug("ISO 8583 -0800 Hexadecimal String :" + (isoHexStr));
		log.debug("ISO ASCII String :" + isoaciiStr);
		log.debug("ISO ASCII String length:" + isoaciiStr.length());
		log.debug("Output from UNB_0800_Req_out ISO8583 -0800- message--->>>"
				+ isoaciiStr);

		log.debug("**********************************");
		String hexReq = convertStringToHex(isoaciiStr);

		log.debug("Hexa val : " + hexReq);

		log.debug("Hexa val : " + convertHexToString(hexReq));
		return isoaciiStr;

	}

	public String convertStringToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}

	public String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}
		log.debug(" iso Decimal String : " + temp.toString());

		return sb.toString();
	}

	public String getValue(String input, String key) {
		String value = "";
		if (input.contains(key)) {

			String reqValue = input.substring(
					input.indexOf(key) + key.length(), input.length());
			value = reqValue.substring(0, reqValue.indexOf("~*"));
		}
		return value;

	}

}