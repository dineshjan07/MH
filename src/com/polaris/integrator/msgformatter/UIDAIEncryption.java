package com.polaris.integrator.msgformatter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 
 */

/**
 * @author venugopal.m
 *
 */
public class UIDAIEncryption {

	public static void main(String arg[]) throws FileNotFoundException,
			CertificateException, NoSuchAlgorithmException {

		byte[] aesKey;

		
		// Create a random AES session key
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		SecretKey key = kgen.generateKey();
		aesKey = key.getEncoded();
		

		// Get public key & expiry date
		FileInputStream fin = new FileInputStream("uidai_auth_prod.cer");
		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) f
				.generateCertificate(fin);
		PublicKey pk = certificate.getPublicKey();

		SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyyMMdd");
		String expiryDate = sdfDestination.format(certificate.getNotAfter());
		System.out.println("expiry date: " + expiryDate);

		// Encrypt session id
		Cipher _Cipher;
		try {

			_Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

			try {
				_Cipher.init(Cipher.ENCRYPT_MODE, pk);
				try {
					String Skey = Base64.encodeBase64String(_Cipher
							.doFinal(aesKey));

					System.out.println("Encrypted AES Skey: " + Skey);

				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		String currentTimeStamp = formatter.format(new Date());
		System.out.println("currentTimeStamp: " + currentTimeStamp);
		

		// Generate PID Block
		String pidXML = "<Pid ts="
				+ currentTimeStamp
				+ "\" ver=\"1.0\"><Bios><Bio type=\"FMR\" posh=\"\">Rk] SACAyMAAAAAECAAABLAGQAMUAXQ EAAAAAJOASAthAEBgADLqAIBQADvrAEBBAEBpAEALAEXOAIAgAE92AE82AHZpAEA3AIB4AECOAIZUAEBDAJ R4AEC1ALNxAICNAMRyAEENAM/4AEDyANf4AEEBANp4AEDaANzOAEESAOS] AEASAPGUAEC7APTXAEBWAPd4AI EGAP95AEDEAQZZAIAVAQgUAEBfARj+AEBkATqAAICxAUR1 AEEMAUZ4AECBAUI7AEBMAU4NAIDAAV95AEAzAXChAI BuAXGNAEEDAXFQAEBSAXIVAEEDAXZAAI BnAYMUAEDBAYb7AIC1AYh8AAAA</Bio></Bios></Pid>";
		System.out.println("pidXML as plain text: " + pidXML);
		

		// Encrypt PID xml
		try {
			try {
				String encryptPIDXML = null;
				try {
					encryptPIDXML = encrypt(pidXML, aesKey);
					System.out.println("encryptPIDXML:" + encryptPIDXML);
				} catch (NoSuchProviderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (InvalidKeyException e) {
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
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Generate Hmac value
		
		String pidXMLSHA256Hash = getSha256(pidXML);
		System.out.println("pidXMLSHA256Hash: " + pidXMLSHA256Hash);

		try {
			String base64EncodedPidXMLSha256Hash = encrypt(pidXMLSHA256Hash,
					aesKey);

			System.out.println("base64EncodedPidXMLSha256Hash: "
					+ base64EncodedPidXMLSha256Hash);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String encrypt(String strToEncrypt, byte[] aesKey)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {

		Key SecretKey = new SecretKeySpec(aesKey, "AES");
		Cipher _Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		_Cipher.init(Cipher.ENCRYPT_MODE, SecretKey);

		return Base64.encodeBase64String(_Cipher.doFinal(strToEncrypt
				.getBytes()));

	}

	/**
	 * Provides SHA-256 hash of PID xml string
	 * 
	 * @param value
	 * @return
	 */
	public static String getSha256(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(value.getBytes());
			return getHexString(md.digest());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Takes a byte array as input and provides a Hex String reporesentation
	 * 
	 * @param input
	 * @return
	 */
	private static String getHexString(byte[] input) {
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

}
