package com.polaris.integrator.msgformatter;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polaris.integrator.webtools.properties.ConfigProperties;

public class AEPS_AUTH_REQOUT extends UserRule {

	private static Logger log = LoggerFactory.getLogger(AEPS_AUTH_REQOUT.class);

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
				
//				Encrypter encryptor = new Encrypter(ConfigProperties.get("NPCI", "UIDAI_PublicKeyFile"));

				// 1. Generate AES session key
				byte[] aesKey;

				aesKey = UIDAIEncryptionUtility.getRandomAESSKey();
//				aesKey=encryptor.generateSessionKey();

				// 2. Get public key
				PublicKey pk = UIDAIEncryptionUtility.getPublicKey();

				// 3. Encrypt session id
				byte[] Skey = UIDAIEncryptionUtility.encryptAESSKey(aesKey, pk);
//				byte[] Skey=	encryptor.encryptUsingPublicKey(aesKey);

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

				pidXML = "<Pid ts=\"" + ts + "\" ver=\"" + ver + "\"><Bios>"
						+ Bioxml + "</Bios></Pid>";
				log.debug("pidXML as plain text: " + pidXML);

				// 5Encrypt PID xml
				byte[] encryptPIDXML = UIDAIEncryptionUtility.encrypt(pidXML.getBytes(),
						aesKey);
//				byte[] encryptPIDXML =	encryptor.encryptUsingSessionKey(aesKey,pidXML.getBytes());

				log.debug("Encrypte PID XML: " + encryptPIDXML);
				
				// 6. Generate Hmac value
//				byte[] base64EncodedPidXMLSha256Hash = UIDAIEncryptionUtility
//						.getHmacValue(pidXML, aesKey);
				byte[] base64EncodedPidXMLSha256Hash = UIDAIEncryptionUtility.generateSha256Hash(pidXML.getBytes(),aesKey);
				
//				byte[] pidXmlHash=new HashGenerator().generateSha256Hash(pidXML.getBytes());
//				byte[] base64EncodedPidXMLSha256Hash=encryptor.encryptUsingSessionKey(aesKey,pidXmlHash);
				log.debug("Hmac Value: " + base64EncodedPidXMLSha256Hash);

				String ci = getValue(Input, "ci@@");
				String ac = getValue(Input, "ac@@");
				String sa = getValue(Input, "sa@@");
				String lk = getValue(Input, "lk@@");
				String tag001 = null, tag002, tag003 = null, tag004, tag005, tag006 = null, tag007 = null;
				
				//Convert all fields into HEX values
				String newSkey=UIDAIEncryptionUtility.getHexString(Skey);
				String newbase64EncodedPidXMLSha256Hash=UIDAIEncryptionUtility.getHexString(base64EncodedPidXMLSha256Hash);
				String	newencryptPIDXML=UIDAIEncryptionUtility.getHexString(encryptPIDXML);
				String hexCi=convertStringToHex(ci);
				String hexac=convertStringToHex(ac);
				String hexsa=convertStringToHex(sa);
				String hexlk=convertStringToHex(lk);
				// Tag Number+ASCII data length in 3 digit and in HEX format(It always half of Hex data length)+Hex converted values
				tag001 = convertStringToHex("001") + convertStringToHex(String.format("%03d", newSkey.length()/2)) +newSkey;
				tag002 = convertStringToHex("002") + convertStringToHex(String.format("%03d", hexCi.length()/2)) + hexCi;
				tag003 = convertStringToHex("003")
						+convertStringToHex(String.format("%03d",newbase64EncodedPidXMLSha256Hash.length()/2))
						+ newbase64EncodedPidXMLSha256Hash;
				tag004 = convertStringToHex("004") + convertStringToHex(String.format("%03d", hexac.length()/2))+ hexac;
				tag005 = convertStringToHex("005") + convertStringToHex(String.format("%03d", hexsa.length()/2)) + hexsa;
				tag006 =convertStringToHex("006") + convertStringToHex(String.format("%03d", hexlk.length()/2)) + hexlk;
				tag007 = newencryptPIDXML;
				
				
				String field12722 = tag001 + tag002 + tag003 + tag004 + tag005
						+ tag006 + tag007;
				

				log.debug("field12722 ..." + field12722);

				//calculate 127.22 field length in 5 digits 
				String field12722Length=String.format("%05d", field12722.length());

				log.debug("field12722 length..."
						+field12722Length);
				
				field12722 = field12722Length+ field12722;
			
				//calculate 127 field length includes Length of 127.22's length(5)+
				//Bitmap indicating 127 field values 0000040080000000, since it is Hex value we should take half of length(16/2=8)+
				//Length of 127.22 field+Length of 127.33 field(4).
				String field127 = String.format("%06d",
						(Integer.parseInt(field12722Length)+5+8+4));
				
				log.debug("field127 length..." + field127);

				String reqString = "AdditionalTags@@" + field127 + "~*";
				log.debug("after reqString added..." + reqString);
				Input = Input + reqString;

				log.debug("after 127 added..." + Input);
				
				String publicKey=getValue(Input, "mc@@");
				
/*				Example: 
					Let’s assume registered device public key certificate data is 2000 bytes. 

					Sample structure:
					DE#108- 999mc2000<Encrypted registered device public key certificate of length 993> 
					DE#109 till DE#110- 999 <Encrypted registered device public key certificate length 999>*/
				
				int charPerTag=999;
				int keyLength=publicKey.length();
				publicKey="mc"+keyLength+publicKey;
				keyLength=publicKey.length();
				log.debug("Public Key value: "+publicKey);
				int iterateCount=keyLength/charPerTag;
				int extraChars=keyLength%charPerTag;
				if(extraChars!=0){
					iterateCount++;
				}
				int startIndex=0;
				int endIndex=charPerTag;
				for(int i=0;i<iterateCount;i++){
					if(!(publicKey.length()>endIndex)){
						endIndex=publicKey.length();
					}
				String keySubtring=publicKey.substring(startIndex, endIndex);
				log.debug("Count "+i+" Splitted public Key SubString :"+keySubtring);
				if(i==0){
					Input=Input+"mc_1@@"+keySubtring+ "~*";
				}
				if(i==1){
					Input=Input+"mc_2@@"+keySubtring+ "~*";
				}
				if(i==2){
					Input=Input+"mc_3@@"+keySubtring+ "~*";
				}
					startIndex=endIndex;
					endIndex+=charPerTag;
				}
				

				String pi=getValue(Input, "pi@@");
				String pa=getValue(Input, "pa@@");
				String pfa=getValue(Input, "pfa@@");
				String bio=getValue(Input, "bio@@");
				String Bt=getValue(Input, "Bt@@");
				String pin=getValue(Input, "pin@@");
				String Otp=getValue(Input, "Otp@@");
				
				String pidType=getValue(Input, "type@@");
				String udc=getValue(Input, "udc@@");
				String dpId=getValue(Input, "dpId@@");
				String rdsId=getValue(Input, "rdsId@@");
				String rdsVer=getValue(Input, "rdsVer@@");
				String dc=getValue(Input, "dc@@");
				String mi=getValue(Input, "mi@@");
				String bav=getValue(Input, "bav@@");
				
				String Uses=pi+pa+pfa+bio+Bt+pin+Otp;
				String t001="001"+String.format("%03d",Uses.length())+Uses;
				String t008="008"+String.format("%03d",pidType.length())+pidType;
				String t009="009"+String.format("%03d",udc.length())+udc;
				String t010="010"+String.format("%03d",dpId.length())+dpId;
				String t011="011"+String.format("%03d",rdsId.length())+rdsId;
				String t012="012"+String.format("%03d",rdsVer.length())+rdsVer;
				String t013="013"+String.format("%03d",dc.length())+dc;
				String t014="014"+String.format("%03d",mi.length())+mi;
				String t015="015"+String.format("%03d",bav.length())+bav;
				String AdditionalData=t001+t008+t009+t010+t011+t012+t013+t014+t015;
				Input=Input+"AdditionalData@@"+AdditionalData+"~*";
				
				Input = getISOMessage(Input, argErrHandler);

				log.debug("String upto 127 " + Input);

				log.debug("String  field12722 " + field12722);

				String filed127BitHex = "0000040080000000";
				String field1272233 = field12722 + "9117";
//				String field1272233=field12722 + "39313137";
			//Append 127 field Bitmap value in ASCII format before 127 field.
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