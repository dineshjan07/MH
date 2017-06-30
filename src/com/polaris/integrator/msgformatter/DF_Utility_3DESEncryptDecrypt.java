package com.polaris.integrator.msgformatter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DF_Utility_3DESEncryptDecrypt {

	public static String secretKey = "AGSINDIA123";

	public static String encrypt(String strToEncrypt)
			throws UnsupportedEncodingException {
		try {

			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] key_Array = md.digest(secretKey.getBytes("UTF-8"));

			byte[] keyBytes = Arrays.copyOf(key_Array, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}

			Key SecretKey = new SecretKeySpec(keyBytes, "DESede");
			Cipher _Cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			_Cipher.init(Cipher.ENCRYPT_MODE, SecretKey);

			return Base64.encodeBase64String(_Cipher.doFinal(strToEncrypt
					.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Exception]:" + e.getMessage());
		}
		return null;
	}

	public static String decrypt(String EncryptedMessage) {
		try {

			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] key_Array = md.digest(secretKey.getBytes("UTF-8"));

			byte[] keyBytes = Arrays.copyOf(key_Array, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}

			Key SecretKey = new SecretKeySpec(keyBytes, "DESede");
			Cipher _Cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			_Cipher.init(Cipher.DECRYPT_MODE, SecretKey);

			byte DecodedMessage[] = Base64.decodeBase64(EncryptedMessage);
			return new String(_Cipher.doFinal(DecodedMessage));

		} catch (Exception e) {
			System.out.println("[Exception]:" + e.getMessage());

		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		String outputOfEncrypt = encrypt("210101213146000");
		System.out.println("[Encrypt]:" + outputOfEncrypt);

		String outputOfDecrypt = decrypt("HU2X9ySss8B18CifacZSbxR9RBBtI4ZV");
		System.out.println("[Decrypt]:" + outputOfDecrypt);
	}

}