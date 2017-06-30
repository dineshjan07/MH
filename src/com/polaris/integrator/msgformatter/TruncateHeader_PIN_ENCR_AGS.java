
package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TruncateHeader_PIN_ENCR_AGS
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( TruncateHeader_PIN_ENCR_AGS.class );

    public TruncateHeader_PIN_ENCR_AGS()
    {
    }

    public int Process( HashMap argParsedFldHM, String argInMsg, String argFldId, HashMap argFldFmtHM,
                        ErrorHandler argErrHandler )
    {
        try
        {
        	String str = argInMsg;
        	int indexNar = str.indexOf("PIN=");
    		int indexNextDelimiter = str.indexOf("~*",indexNar);
    		String pin = str.substring(indexNar+4, indexNextDelimiter);
    		System.out.println("pin : "+pin);
    		int indexNar1 = str.indexOf("CardNum=");
    		int indexNextDelimiter1 = str.indexOf("~*",indexNar1);
    		String splitcardno = str.substring(indexNar1+8, indexNextDelimiter1);
    		String cardno = splitcardno.substring(0, 16);
    		System.out.println("Card No : "+cardno);
    		 String key = "0C8D307E88A9A54B931B8D224BE2651D";
    		 String req_sign = "";
    		 req_sign = encryptPinBlock(cardno, pin, key, 168);
    		 
    		 System.out.println("Encrypted Pin Block : "+req_sign);
            argInMsg = argInMsg.concat("req_sign="+req_sign+"~*");
            System.out.println("Request After Encryption : "+argInMsg);
        	
        	log.debug( "\n After Manipulation "+argInMsg );
    	

        	//hdr_Tran_Id=SENDSMS~*hdr_Status=NULL~*WS_01#mobno=23456~*message=test~*
            log.debug( "\n Entring UserExit TruncateHeader" );
            super._cresultMessage = new String[1];
            String Input = argInMsg.substring( argInMsg.indexOf( "NULL~*" ) + "NULL~*".length(), argInMsg.length() );
            super._bypassMF = false;
			super._cresultMessage[0] = Input;
            super._iresultCode = 0;
            log.debug( "\n Inside UserExit : coutputStr : " + Input );
            log.debug( "\n Inside UserExit returns : _iresultCode : " + _iresultCode );
            log.debug( "\n Leaving UserExit TruncateHeader" );
            return super._iresultCode;
        }
        catch ( Exception exception )
        {
            super._iresultCode = 1000;
            log.error( "Exception in TruncateHeader : " , exception );
        }
        return super._iresultCode;
    }
    
    /**
     * Encrypts the security pin for a card and gives the Hex representation of the encrypted pin block.
     * @param cardNumber Card number for which the Pin is encrypted
     * @param pin Pin to be encrypted
     * @param key Clear Key to be used for encryption
     * @param keySize Key straight
     * @return The Hex representation of the encrypted pin block bytes
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
    * @throws UnsupportedEncodingException 
     */
    public static String encryptPinBlock(String cardNumber, String pin,
      String key, int keySize) throws NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    byte[] keyBytes = getEncryptionKey(key, keySize);
     byte[] pinBlock = getPinBlock(cardNumber, pin);

     SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");

     Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
     cipher.init(Cipher.ENCRYPT_MODE, secretKey);
     byte[] encryptedPinBlock = cipher.doFinal(pinBlock);

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
    	  System.out.println("block");
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
     
     System.out.println("Pin Block1 : " + pinBlockString);
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
     
     System.out.println( "AccNo : "+acctNumber);
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
    
    
	public static void main(String[] args) { HashMap hm = new HashMap();
	TruncateHeader_PIN_ENCR_AGS test = new TruncateHeader_PIN_ENCR_AGS();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	/*"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"*/
	, "", new HashMap(), new ErrorHandler());
	}
   
    
}