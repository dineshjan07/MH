package com.polaris.integrator.msgformatter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DF_PinBlockEncryptionUtil {

 
 public static void main(String[] args) throws InvalidKeyException,
   NoSuchAlgorithmException, NoSuchPaddingException,
   IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
  String key = "15EA4CA20131C2FD2315208C9110AD40"; //- exim
//	 String key="0C8D307E88A9A54B931B8D224BE2651D";
  System.out.println("REsult:"+encryptPinBlock("5162130000008605", "1234", 168));

 // getPinBlock("6BA489C1D6BCC16E31D6765D29195E9B","6729B9BF5E156425A2CDFB7F62FB3B86");
 }

 /**
  * Encrypts the security pin for a card and gives the Hex representation of the encrypted pin block.
  * @param cardNumber Card number for which the Pin is encrypted
  * @param pin Pin to be encrypted
  * @param key Clear Key to be used for encryption
  * @param keySize Key strnght
  * @return The Hex representation of the encrypted pin block bytes
  * @throws NoSuchAlgorithmException
  * @throws NoSuchPaddingException
  * @throws InvalidKeyException
  * @throws IllegalBlockSizeException
  * @throws BadPaddingException
 * @throws UnsupportedEncodingException 
  */
 public static String encryptPinBlock(String cardNumber, String pin,
    int keySize) throws NoSuchAlgorithmException,
   NoSuchPaddingException, InvalidKeyException,
   IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
String key="15EA4CA20131C2FD2315208C9110AD40";
	 byte[] keyBytes = getEncryptionKey(key, keySize);
  byte[] pinBlock = getPinBlock(cardNumber, pin);

  SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");

  Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
  cipher.init(Cipher.ENCRYPT_MODE, secretKey);
  byte[] encryptedPinBlock = cipher.doFinal(pinBlock);
System.out.println("Encrypted code:"+getHexString(encryptedPinBlock));
  
  Cipher cipher1=Cipher.getInstance("DESede/ECB/NoPadding");
  cipher1.init(Cipher.DECRYPT_MODE, secretKey);
  byte[] decryptedPinBlock=cipher1.doFinal(encryptedPinBlock);
  System.out.println("Decrpted;"+getHexString(decryptedPinBlock));
  
  return getHexString(encryptedPinBlock);

 }

 /**
  * Takes the Card number and Pin as input and generates the Pin Block Out of it.
  * First get the card padded (16 Char) which when converted to Hex gives an array of 8
  * Get the Pin Padded (16 Char) which when converted to Hex gives an array of 8
  * XOR the resulting arrays to get the pin block 
  * @param cardNumber
  * @param pin
  * @return
  * @throws IllegalBlockSizeException
  */
 private static byte[] getPinBlock(String cardNumber, String pin)
   throws IllegalBlockSizeException {
  int[] paddedPin = padPin(pin);
  int[] paddedCard = padCard(cardNumber);

  byte[] pinBlock = new byte[8];
  
  
  for (int cnt = 0; cnt < 8; cnt++) {
	  System.out.println("block:"+cnt);
   pinBlock[cnt] = (byte) (paddedPin[cnt] ^ paddedCard[cnt]);
  }
 
  System.out.println("PIN BLOCK: "+getHexString(pinBlock));
  
  return pinBlock;
 }

 private static final String PIN_PAD = "FFFFFFFFFFFFFF";

 /**
  * Generates a 16 digit block, with following Components
  * Two digit pin length (left padded with zero if length less than 10)
  * Pin Number 
  * Right padded with F to make it 16 char long.
  * FOr example for a 5 digit Pin 12345 the outout would be 
  * 0512 345F FFFF FFFF
  * @param pin
  * @return
  * @throws IllegalBlockSizeException
  */
 private static int[] padPin(String pin) throws IllegalBlockSizeException {
  String pinBlockString = "0" + pin.length() + pin + PIN_PAD;
  pinBlockString = pinBlockString.substring(0, 16);
  
  System.out.println(pinBlockString);
  return getHexIntArray(pinBlockString);

 }

 private static final String ZERO_PAD = "0000000000000000";

 /**
  * Using the Card Number it generates a 16-digit block with 4 zeroes and and
  * the 12 right most digits of the card number, excluding the check digit
  * (which is the last digit of the card number. 
  * For Example for a Card 5259 5134 8115 5074
  * 4 Will be the check digit
  * Right most 12 digits would be 951348115507
  * Hence the output would be 0000 9513 4811 5507
  * @param cardNumber
  * @return
  * @throws IllegalBlockSizeException
  */
 private static int[] padCard(String cardNumber)
   throws IllegalBlockSizeException {
  cardNumber = ZERO_PAD + cardNumber;
  int cardNumberLength = cardNumber.length();
  int beginIndex = cardNumberLength - 13;
  String acctNumber = "0000"
    + cardNumber.substring(beginIndex, cardNumberLength - 1);
  
  System.out.println("acc:"+acctNumber);
  return getHexIntArray(acctNumber);
 }

 /**
  * Takes Hex representation of the key, validates the length and returns the
  * equivallent bytes
  * 
  * @param keyString
  *            Hex representation of the key. THe allowed length of the
  *            string are 16 (56 bit), 32 (112 bit), 32 or 48 (for 168 bit).
  *            If the key Strength is 168 bit and the key length is 32 the
  *            first 16 chars are repeated.
  * @param keySize
  *            Valid values are 56, 112, 168
  * @return
  * @throws IllegalBlockSizeException
  * @throws InvalidKeyException
  * 
  */
 private static byte[] getEncryptionKey(String keyString, int keySize)
   throws IllegalBlockSizeException, InvalidKeyException {
  int keyLength = keyString.length();
  switch (keySize) {
  case 56:
   if (keyLength != 16)
    throw new InvalidKeyException(
      "Hex Key length should be 16 for a 56 Bit Encryption, found ["
        + keyLength + "]");
   break;
  case 112:
   if (keyLength != 32)
    throw new InvalidKeyException(
      "Hex Key length should be 32 for a 112 Bit Encryption, found["
        + keyLength + "]");
   break;
  case 168:
   if (keyLength != 32 && keyLength != 48)	
    throw new InvalidKeyException(
      "Hex Key length should be 32 or 48 for a 168 Bit Encryption, found["
        + keyLength + "]");
   if (keyLength == 32) {
    keyString = keyString + keyString.substring(0, 16);
   }
   break;
  default:
   throw new InvalidKeyException(
     "Invalid Key Size, expected one of [56, 112, 168], found["
       + keySize + "]");
  }

  System.out.println("key string: "+keyString);
  byte[] keyBytes = getHexByteArray(keyString);
  
  System.out.println("KEYBYTES:"+keyBytes.length);
  return keyBytes;

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

 /**
  * Converts a Hex string representation to an int array
  * 
  * @param input
  *            Every two character of the string is assumed to be
  * @return int array containing the Hex String input
  * @throws IllegalBlockSizeException
  */
 private static int[] getHexIntArray(String input)
   throws IllegalBlockSizeException {
  if (input.length() % 2 != 0) {
   throw new IllegalBlockSizeException(
     "Invalid Hex String, Hex representation length is not a multiple of 2");
  }
  int[] resultHex = new int[input.length() / 2];
  for (int iCnt1 = 0; iCnt1 < input.length(); iCnt1++) {
   String byteString = input.substring(iCnt1, ++iCnt1 + 1);
   int hexOut = Integer.parseInt(byteString, 16);
   resultHex[iCnt1 / 2] = (hexOut & 0x000000ff);
  }
  return resultHex;
 }

 /**
  * Converts a Hex string representation to an byte array
  * 
  * @param input
  *            Every two character of the string is assumed to be
  * @return byte array containing the Hex String input
  * @throws IllegalBlockSizeException
  */
 private static byte[] getHexByteArray(String input)
   throws IllegalBlockSizeException {

  int[] resultHex = getHexIntArray(input);
  byte[] returnBytes = new byte[resultHex.length];
  for (int cnt = 0; cnt < resultHex.length; cnt++) {
   returnBytes[cnt] = (byte) resultHex[cnt];
  }
  return returnBytes;
 }

}
