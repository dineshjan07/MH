package com.polaris.integrator.msgformatter;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.polaris.integrator.webtools.properties.ConfigProperties;

/**
 * 
 */

/**
 * @author venugopal.m
 *
 */
public class UIDAIEncryptionUtility {

	public static void main(String arg[]) throws FileNotFoundException,
			CertificateException, NoSuchAlgorithmException {

		try {

			// 1. Generate AES session key
			byte[] aesKey;

			aesKey = getRandomAESSKey();

			// 2. Get public key
			PublicKey pk = new UIDAIEncryptionUtility().getPublicKey();

			// 3. Encrypt session id
			byte[] Skey = encryptAESSKey(aesKey, pk);
			
			System.out.println("1;"+Skey.toString());
			
			System.out.println(new String(Skey,"UTF-8"));
			
			System.out.println("2;"+Base64.encodeBase64String(Skey).getBytes());
			System.out.println("3;"+Base64.encodeBase64String(Base64.encodeBase64String(Skey).getBytes()));
			
			System.out.println("Encrypted AES Skey: " + Skey);
			String strSKey=Skey.toString();
			
			System.out.println("Encrypted AES Skey: " + strSKey);
			
			System.out.println("dd;"+Base64.encodeBase64String("[B@148bd3".getBytes()));
			
			
			
			
			// 4. Generate PID Block
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			String currentTimeStamp = formatter.format(new Date());

			String pidXML = "<Pid ts="
					+ currentTimeStamp
					+ "\" ver=\"1.0\"><Bios><Bio type=\"FMR\" posh=\"\">Rk] SACAyMAAAAAECAAABLAGQAMUAXQ EAAAAAJOASAthAEBgADLqAIBQADvrAEBBAEBpAEALAEXOAIAgAE92AE82AHZpAEA3AIB4AECOAIZUAEBDAJ R4AEC1ALNxAICNAMRyAEENAM/4AEDyANf4AEEBANp4AEDaANzOAEESAOS] AEASAPGUAEC7APTXAEBWAPd4AI EGAP95AEDEAQZZAIAVAQgUAEBfARj+AEBkATqAAICxAUR1 AEEMAUZ4AECBAUI7AEBMAU4NAIDAAV95AEAzAXChAI BuAXGNAEEDAXFQAEBSAXIVAEEDAXZAAI BnAYMUAEDBAYb7AIC1AYh8AAAA</Bio></Bios></Pid>";
			System.out.println("pidXML as plain text: " + pidXML);

			// 5Encrypt PID xml
			byte[] encryptPIDXML = encrypt(pidXML.getBytes(), aesKey);
			System.out.println("Encrypte PID XML: " + encryptPIDXML);

			// 6. Generate Hmac value
			byte[] base64EncodedPidXMLSha256Hash = getHmacValue(pidXML, aesKey);
			System.out.println("Hmac Value: " + base64EncodedPidXMLSha256Hash);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Generate random AES session key
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getRandomAESSKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		SecretKey key = kgen.generateKey();
		return key.getEncoded();

	}

	/**
	 * Get public key & expiry date
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 */
	public static PublicKey getPublicKey() throws NoSuchAlgorithmException,
			FileNotFoundException, CertificateException {
		// Get public key & expiry date
//		InputStream file=new FileInputStream("D:/MH_HOME/Resources/uidai_auth_encrypt_preprod.cer");
		InputStream file=new FileInputStream(ConfigProperties.get("NPCI", "UIDAI_PublicKeyFile"));
		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) f
				.generateCertificate(file);
		PublicKey pk = certificate.getPublicKey();

		SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyyMMdd");
		String expiryDate = sdfDestination.format(certificate.getNotAfter());
		System.out.println("expiry date: " + expiryDate);

		return pk;

	}

	/**
	 * Encrypt AESsession key
	 * 
	 * @param aesKey
	 * @param pk
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] encryptAESSKey(byte[] aesKey, PublicKey pk)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher _Cipher = null;

		_Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		_Cipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] encSessionKey=_Cipher.doFinal(aesKey);

		return encSessionKey;

	}

	public static byte[] encrypt(byte[] strToEncrypt, byte[] aesKey)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {

		Key SecretKey = new SecretKeySpec(aesKey, "AES");
		Cipher _Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		_Cipher.init(Cipher.ENCRYPT_MODE, SecretKey);

		return _Cipher.doFinal(strToEncrypt);

	}

	/**
	 * Generate Hmac value Provides SHA-256 hash of PID xml string
	 * 
	 * @param value
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static byte[] getHmacValue(String xmlStr, byte[] aesKey)
			throws NoSuchAlgorithmException, InvalidKeyException,
			UnsupportedEncodingException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(xmlStr.getBytes());

		String pidXMLSHA256Hash = getHexString(md.digest());
		return encrypt(pidXMLSHA256Hash.getBytes(), aesKey);

	}
	
	public static byte[] generateSha256Hash(byte[] message,byte[] aesKey) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		String algorithm = "SHA-256";

		byte[] hash = null;

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
			digest.reset();
			hash = digest.digest(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encrypt(hash, aesKey);
	}

	/**
	 * Takes a byte array as input and provides a Hex String reporesentation
	 * 
	 * @param input
	 * @return
	 */
	public static String getHexString(byte[] input) {
		StringBuilder strBuilder = new StringBuilder();
		for (byte hexByte : input) {
			int res = 0xFF & hexByte;
			String hexString = Integer.toHexString(res);
			if (hexString.length() == 1) {
				strBuilder.append(0);
			}
			strBuilder.append(hexString);

		}

		return strBuilder.toString().toUpperCase();
	}
	
	public static String decrypt(String encryptedMsg, PublicKey pk)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher _Cipher = null;

		_Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		_Cipher.init(Cipher.DECRYPT_MODE, pk);

		byte DecodedMessage[] = Base64.decodeBase64(encryptedMsg);
		return new String(_Cipher.doFinal(DecodedMessage));

	}


}


